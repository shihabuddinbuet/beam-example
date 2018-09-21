package com.su.beam.example.textIO;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Watch;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFileStreaming {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleFileStreaming.class);
    private static final String STREAM_FROM = "/Users/shihab/data/beam/stdin/*";
    private static final String STREAM_OUT = "/Users/shihab/data/beam/stdout/";
    public static void main(String []args) {
        Pipeline pipeline = Pipeline.create();
        PCollection<String> stremIn = pipeline.apply("File streaming from input directory",
                TextIO.read().from(STREAM_FROM)
                .watchForNewFiles(Duration.standardMinutes(2)
                        ,Watch.Growth.never()))
                .apply(Window.<String>into(FixedWindows.of(Duration.standardMinutes(2))));

        stremIn.apply("Writing stream in files",
                TextIO.write()
                .to(STREAM_OUT)
                .withWindowedWrites()
                .withNumShards(1)
                .withSuffix(".stream"));

        pipeline.run();


    }
}
