package com.su.beam.example.textIO;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;

public class SimpleTextIOExample {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleTextIOExample.class);
    private static final String INPUT_PATH = "/Users/shihab/data/beam/input/";
    private static final String OUTPUT_PATH = "/Users/shihab/data/beam/output/test";

    public static void main(String []args) {
        Pipeline pipeline = Pipeline.create();
        LOG.info(format("Reading single file from %s directory", INPUT_PATH));
        PCollection<String> input = pipeline.apply("Reading a file",
                TextIO.read().from(INPUT_PATH + "device2ad_reco_gen_app.py"));
        LOG.info(String.format("Writing single file content to %s directory with .one suffix ", OUTPUT_PATH));
        input.apply("Writing Text",
                TextIO.write().to(OUTPUT_PATH)
                .withNumShards(1)
                .withSuffix(".one"));

        LOG.info(format("Reading *.py files from %s directory", INPUT_PATH));
        PCollection<String> matchedPatternInput  = pipeline.apply("Reading .py files",
                TextIO.read().from(INPUT_PATH +"*.py"));
        LOG.info(format("Writing *.py files content in %s directory with .regex suffix", OUTPUT_PATH));
        matchedPatternInput.apply("Writing multiple files context",
                TextIO.write().to(OUTPUT_PATH).withSuffix(".regex"));

        pipeline.run();
    }


}
