package org.allenai.taggers.tag;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import org.allenai.taggers.SentenceFunctions;
import org.allenai.nlpstack.typer.Type;
import org.allenai.nlpstack.chunk.ChunkedToken;
import org.allenai.nlpstack.lemmatize.Lemmatized;
import org.allenai.nlpstack.lemmatize.MorphaStemmer$;

/***
 * Search for normalized keywords against a normalized sentence and tag the
 * match.  Lemmatized is defined by {@see Stemmer.lemmatize()}.
 * @author schmmd
 *
 */
public class LemmatizedKeywordTagger extends KeywordTagger {
    public LemmatizedKeywordTagger(String name, List<String> keywords) {
        super(name, Lists
                .transform(keywords, new Function<String, String>() {
                    public String apply(String string) {
                        return MorphaStemmer$.MODULE$.lemmatize(string);
                    }
                }));
    }

    /**
     * Constructor used by reflection.
     * @param name name of the tagger
     * @param args arguments to the tagger
     */
    public LemmatizedKeywordTagger(String name, scala.collection.Seq<String> args) {
        this(name, scala.collection.JavaConversions.asJavaList(args));
    }

    @Override
    protected List<Type> findTagsJava(final List<Lemmatized<ChunkedToken>> sentence,
            List<String> tokens) {
        return super.findTagsJava(sentence, SentenceFunctions.lemmas(sentence));
    }
}
