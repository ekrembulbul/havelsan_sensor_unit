package org.havelsan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        Producer<String, String> producer = createProducer();

        String topic = "sensor-data-topic";
        String key = "sensor-data";

        String jsonData = convertSensorDataToJson(createSensorData());

        // send message
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, jsonData);
        producer.send(record);

        // close producer
        producer.close();
    }

    private static String convertSensorDataToJson(SensorData sensorData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(sensorData);
    }

    private static SensorData createSensorData() {
        Random random = new Random();
        SensorData sensorData = new SensorData("sensor-1",
                              new SensorData.Point(random.nextDouble(-1000d,1000d), random.nextDouble(-1000d, 1000d)),
                              random.nextDouble(360d));
//        SensorData sensorData = new SensorData("sensor-1",
//                                               new SensorData.Point(5d, -1d),
//                                               315d);
//        SensorData sensorData = new SensorData("sensor-2",
//                                               new SensorData.Point(-5d, 1d),
//                                               45d);
        System.out.println(sensorData.toString());
        return sensorData;
    }

    private static Producer<String, String> createProducer() {
        // Kafka server address and port
        String bootstrapServers = "localhost:9092";

        // Kafka properties
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Kafka producer
        return new KafkaProducer<>(properties);
    }
}