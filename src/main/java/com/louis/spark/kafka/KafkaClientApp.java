package com.louis.spark.kafka;

public class KafkaClientApp {
    public static void main(String[] args) {

        new KafkaConsumer(KafkaProperties.TOPIC).start();
        new KafkaProducer(KafkaProperties.TOPIC).start();

    }
}
