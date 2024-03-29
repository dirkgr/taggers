package org.allenai.taggers.tag;

import java.util.List;
import com.google.common.collect.Lists;
import org.allenai.taggers.SentenceFunctions;
import org.allenai.taggers.StringFunctions;
import org.allenai.nlpstack.typer.Type;
import org.allenai.nlpstack.chunk.ChunkedToken;
import org.allenai.nlpstack.lemmatize.Lemmatized;

/***
 * Search for exact keyword matches, ignoring case.
 * @author schmmd
 *
 */
public class CaseInsensitiveKeywordTagger extends KeywordTagger {
    public CaseInsensitiveKeywordTagger(String name, List<String> keywords) {
        super(name, Lists
                .transform(keywords, StringFunctions.toLowerCase));
    }

    /**
     * Constructor used by reflection.
     * @param name name of the tagger
     * @param args arguments to the tagger
     */
    public CaseInsensitiveKeywordTagger(String name, scala.collection.Seq<String> args) {
        this(name, scala.collection.JavaConversions.asJavaList(args));
    }

    @Override
    public List<Type> findTagsJava(final List<Lemmatized<ChunkedToken>> sentence) {
        return super.findTagsJava(SentenceFunctions.lowercase(sentence));
    }
}
