package com.masanori;

import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;

public class Message extends DoFn<String, PubsubMessage> {

    @StartBundle
    public void startBundle(StartBundleContext c){

    }
}
