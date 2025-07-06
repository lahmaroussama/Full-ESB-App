package tn.besoftilys.appdemo_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.besoftilys.appdemo_2.service.ConsumeMessageService;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class AppDemo2Application {

    public static void main(String[] args) {

        SpringApplication.run(AppDemo2Application.class, args);
        String bootstrapServers = "localhost:29092";
        String groupId = "b725f72-3292-4cec-a55b-bb7cb80dcc44";

        // Create a set of desired keys
        Set<String> desiredKeys = new HashSet<>();
        desiredKeys.add("app_2");
        desiredKeys.add("xml");

        // Define the target URL for the microservice
        String targetUrl = "http://localhost:8092/api/messages";

        // Pass the set of keys and the target URL to the ConsumeMessage constructor
        ConsumeMessageService consumeMessage = new ConsumeMessageService(bootstrapServers, groupId, desiredKeys, targetUrl);

        // Send the second desired key to the microservice and wait for acknowledgment
        if (consumeMessage.sendDesiredKey(desiredKeys)) {
            // Start consuming messages
            consumeMessage.startConsuming(1);       
        } else {
            System.out.println("Failed to send the desired key to the microservice.");

        }
    }

}
