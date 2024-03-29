package org.allenai.taggers.tag;

import java.util.ArrayList;
import java.util.List;

import org.allenai.common.immutable.Interval;
import org.allenai.taggers.TypeHelper;
import org.allenai.nlpstack.typer.Type;
import org.allenai.nlpstack.chunk.ChunkedToken;
import org.allenai.nlpstack.lemmatize.Lemmatized;

public class AfterTaggers {
	public static List<Interval> npChunkIntervals(List<Lemmatized<ChunkedToken>> tokens) {
        int start = -1;
        int i = 0;
        List<Interval> intervals = new ArrayList<Interval>();
        for (Lemmatized<ChunkedToken> token : tokens) {
            // end a chunk tag sequence
            if (start != -1 && !token.token().chunk().equalsIgnoreCase("I-NP")) {
                intervals.add(Interval.open(start, i));
                start = -1;
            }

            // start a chunk tag sequence
            if (token.token().chunk().equalsIgnoreCase("B-NP")) {
                start = i;
            }


            i++;
        }

        return intervals;
    }

    public static List<Type> tagChunks(List<Type> initialTags, List<Lemmatized<ChunkedToken>>  sentence) {
        List<Type> tags = new ArrayList<Type>(initialTags.size());
        for (Type tag : initialTags) {
            for (Interval interval : npChunkIntervals(sentence)) {
                if (interval.superset(tag.tokenInterval())) {
                    tags.add(TypeHelper.fromJavaSentence(sentence, tag.name(), tag.source(), interval));
                }
            }
        }

        return tags;
    }

    public static Type tagHeadword(Type tag, List<Lemmatized<ChunkedToken>> tokens) {
        // final Pattern nounPosTag = Pattern.compile("nns?|nnps?", Pattern.CASE_INSENSITIVE);
        for (Interval interval : npChunkIntervals(tokens)) {
            if (interval.superset(tag.tokenInterval())) {
                // look for preposition
                int headwordEndIndex = interval.end() - 1;
                for (int i = tokens.size() - 1; i > 0; i--) {
                    if (tokens.get(i).token().postag().equals("IN") && i > 0) {
                        headwordEndIndex = interval.start() + i - 1;
                    }
                }

                if (tag.tokenInterval().end() == headwordEndIndex + 1) {
                    return TypeHelper.fromJavaSentence(tokens, tag.name(), tag.source(),
                            interval);
                }
            }
        }

        return null;
    }

    public static List<Type> tagHeadword(List<Type> initialTags, List<Lemmatized<ChunkedToken>> tokens) {
        List<Type> tags = new ArrayList<Type>(initialTags.size());
        for (Type tag : initialTags) {
            Type result = tagHeadword(tag, tokens);
            if (result != null) {
                tags.add(result);
            }
        }

        return tags;
    }
}
