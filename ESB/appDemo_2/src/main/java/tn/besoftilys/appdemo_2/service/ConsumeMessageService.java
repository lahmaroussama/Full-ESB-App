package tn.besoftilys.appdemo_2.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

public class ConsumeMessageService {

    private static final String TOPIC_NAME = "stock-message-2";
    private final KafkaConsumer<String, String> consumer;
    private final Set<String> desiredKeys;
    private final RestTemplate restTemplate;
    private final String targetUrl;

    public ConsumeMessageService(String bootstrapServers, String groupId, Set<String> desiredKeys, String targetUrl) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("session.timeout.ms", "3600000");
        props.put("request.timeout.ms", "3600000");

        this.consumer = new KafkaConsumer<>(props);
        this.desiredKeys = new HashSet<>(desiredKeys);
        this.restTemplate = new RestTemplate();
        this.targetUrl = targetUrl;
    }


    public boolean sendDesiredKey(Set<String> keys) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Convert the set to a list
        List<String> keysList = new ArrayList<>(keys);
        HttpEntity<List<String>> request = new HttpEntity<>(keysList, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl + "/keys2", request, String.class);

        System.out.println("Response from microservice: " + response.getBody());

        return response.getStatusCode().is2xxSuccessful();
    }

    public void startConsuming(int partition) {
        // Get partition info for the topic
        List<PartitionInfo> partitions = consumer.partitionsFor(TOPIC_NAME);
        // Select the specific partition
        TopicPartition topicPartition = new TopicPartition(TOPIC_NAME, partition);
        // Assign the consumer to the specific partition
        consumer.assign(Collections.singletonList(topicPartition));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                if (record.key() != null && desiredKeys.contains(record.key())) {
                    System.out.printf("Received message (key: %s, value: %s)\n", record.key(), record.value());
                    // Process the message as needed
                }
            }

            consumer.commitSync();
        }
    }
}
