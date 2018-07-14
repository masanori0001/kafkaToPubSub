package com.masanori;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.*;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaToPubSub {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaToPubSub.class);

    public interface KafkaToPubSubOptions extends PipelineOptions, StreamingOptions{
        @Description("debug mode")
        @Default.Boolean(false)
        Boolean isDebug();
        void setDebug(Boolean value);

        @Description("test mode")
        @Default.Boolean(false)
        Boolean isTest();
        void setTest(Boolean value);
    }

    public static void main(String args[]){
        KafkaToPubSubOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation().as(KafkaToPubSubOptions.class);

        LOG.info("Options: " + options);

        Pipeline p = Pipeline.create(options);
        PCollection<byte[]> input = p.apply(KafkaIO.<String, byte[]>read()
                .withValueDeserializer(ByteArrayDeserializer.class)
                .withKeyDeserializer(StringDeserializer.class)
                .withBootstrapServers(Constants.KAFKA_CONSUMES)
                .withTopic(Constants.KAFKA_TOPIC)
                .withoutMetadata())
                .apply(Values.<byte[]>create());

        PCollection<PubsubMessage> message = input.apply(ParDo.of(new DoFn<byte[], PubsubMessage>() {
            @ProcessElement
            public void processElement(ProcessContext c){
                PubsubMessage m = new PubsubMessage(c.element(), null);
                c.output(m);
            }
        }));

        message.apply(PubsubIO.writeMessages().to(Constants.TOPIC_NAME));

        if (options.isStreaming()) {
            p.run();
        } else {
            p.run().waitUntilFinish();
        }

    }
}
