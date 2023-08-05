package com.data_conversion.resume_to_excel;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class NameExtraction {

    public static void main(String[] args) {
        String resumeText = "John Doe is an experienced software developer with a bachelor's degree in Computer Science.";

    // Initialize Stanford NLP pipeline
    Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    // Create an Annotation object
    Annotation document = new Annotation(resumeText);

    // Process the text through the pipeline
        pipeline.annotate(document);

    // Extract names from the NER output
    List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
        for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
            String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            if (ner.equals("PERSON")) {
                System.out.println("Name: " + token.originalText());
            }
        }
    }
}
}
