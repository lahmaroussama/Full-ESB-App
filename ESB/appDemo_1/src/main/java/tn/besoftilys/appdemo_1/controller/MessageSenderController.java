package tn.besoftilys.appdemo_1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.kafka.clients.producer.*;



import java.util.Properties;

@RestController
@RequestMapping("/appDemo_1")
public class MessageSenderController {

    private static final String TOPIC_NAME = "stock-message";
    private final Producer<String, String> producer;

    public MessageSenderController() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }
    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        try {
            String key = "app_2"; // Message key
            String contentType = getContentType(message);
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, key, message);
            record.headers().add("Content-Type", contentType.getBytes());
            producer.send(record).get(); // Synchronous sending for simplicity
            return "Message sent successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send message.";
        }
    }

    private String getContentType(String messageBody) {
        messageBody = messageBody.trim(); // Remove leading/trailing whitespace

        // Check for JSON structure (without parsing)
        if (messageBody.startsWith("{") && messageBody.endsWith("}") &&
                messageBody.indexOf(':') != -1 && messageBody.indexOf(',') != -1) {
            return "application/json";
        }

        // Check for JavaScript function definition
        if (messageBody.startsWith("(") && messageBody.endsWith(")") &&
                (messageBody.contains("function ") || messageBody.matches("^[^\\s]*\\([^\\)]*\\)$"))) {
            return "application/javascript";
        }

        // Check for HTML structure
        if (messageBody.startsWith("<") && messageBody.endsWith(">") &&
                messageBody.toLowerCase().contains("<html")) {
            return "text/html";
        }

        // Check for XML structure (basic check)
        if (messageBody.startsWith("<") && messageBody.endsWith(">")) {
            return "application/xml";
        }

        // Default for unknown content type
        return "text/plain";
    }
}