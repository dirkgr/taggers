package org.allenai.taggers

import org.allenai.repr.sentence
import org.allenai.repr.sentence.Sentence
import org.allenai.taggers.constraint.VerbPhraseConstraint
import org.allenai.taggers.tag.ConstrainedTagger
import org.allenai.taggers.tag.KeywordTagger
import org.allenai.nlpstack.chunk.OpenNlpChunker
import org.allenai.nlpstack.lemmatize.MorphaStemmer
import org.scalatest.FlatSpec
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.seqAsJavaListConverter
import org.allenai.taggers.tag.LemmatizedKeywordTagger

class LemmatizedTaggerSpec extends FlatSpec {
  val chunker = new OpenNlpChunker();
  val morpha = new MorphaStemmer();

  val runTagger = new LemmatizedKeywordTagger("JamesTagger", Seq("james"))

  "LemmatizedKeywordTagger" should "match 'james' in a sentence." in {
    val sentenceText = "Jack enjoyed a beer with James."
    val opennlpChunker = new OpenNlpChunker
    val s = new Sentence(sentenceText) with sentence.Chunker with sentence.Lemmatizer with Consume {
      override val chunker = new OpenNlpChunker
      override val lemmatizer = MorphaStemmer
    }

    val types = runTagger.apply(s)

    assert(types.size === 1)
  }
}
