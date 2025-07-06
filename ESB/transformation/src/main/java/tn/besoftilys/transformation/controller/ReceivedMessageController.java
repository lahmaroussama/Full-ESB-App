package tn.besoftilys.transformation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.besoftilys.transformation.dto.MessageDto;
import tn.besoftilys.transformation.service.IReceivedMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/messages")
public class ReceivedMessageController {
@Autowired
    IReceivedMessage iReceivedMessage;
    private List<String> receivedKeys1;
    private List<String> receivedKeys2;
    private List<String> receivedKeys3;
    private final Set<String> processedContent = new HashSet<>();

    @PostMapping("/keys1")
    public ResponseEntity<String> receiveKeys1(@RequestBody List<String> keys) {
        System.out.println("Received Keys: " + keys);
        // Store the keys for later use
        this.receivedKeys1 = keys;
        return new ResponseEntity<>("Keys received: " + keys, HttpStatus.OK);
    }
    @PostMapping("/keys2")
    public ResponseEntity<String> receiveKeys2(@RequestBody List<String> keys) {
        System.out.println("Received Keys: " + keys);
        // Store the keys for later use
        this.receivedKeys2 = keys;
        return new ResponseEntity<>("Keys received: " + keys, HttpStatus.OK);
    }
    @PostMapping("/keys3")
    public ResponseEntity<String> receiveKeys3(@RequestBody List<String> keys) {
        System.out.println("Received Keys: " + keys);
        // Store the keys for later use
        this.receivedKeys3 = keys;
        return new ResponseEntity<>("Keys received: " + keys, HttpStatus.OK);
    }
    @PostMapping("/{process}")
    public ResponseEntity<String> receiveMessage(@PathVariable String process,@RequestBody String body, @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType, @RequestHeader("messageDestination") String messageDestination) throws JsonProcessingException {
        // Print received headers and body
        System.out.println("Received Content-Type: " + contentType);
        System.out.println("Received messageDestination: " + messageDestination);
        System.out.println("Received Body: " + body);

        MessageDto messageDto = new MessageDto();
        messageDto.setBody(body);
        messageDto.setContentType(contentType);

        // Get the transformation type if messageDestination matches any item in the lists
        String transformationType = getMatchingTransformationType(messageDestination, receivedKeys1, receivedKeys2 , receivedKeys3 /*, additional lists */);

        // Transform the message if a matching transformation type is found
        String transformedMessage;
        if (transformationType != null) {
            transformedMessage = iReceivedMessage.transformMessage(messageDto, transformationType);
        } else {
            transformedMessage = "Message destination did not match any key";
        }

        // Return a response
        return new ResponseEntity<>(transformedMessage, HttpStatus.OK);


    }

    private String getMatchingTransformationType(String messageDestination, List<String>... keyLists) {
        for (List<String> keyList : keyLists) {
            if (keyList != null && keyList.size() == 2) {
                if (messageDestination.equals(keyList.get(0))) {
                    System.out.println(keyList.get(1));
                    return keyList.get(1);
                } else if (messageDestination.equals(keyList.get(1))) {

                    return keyList.get(0);
                }
            }
        }
        return null; // Return null if no match is found
    }

    @PostMapping("/processFile")
    public ResponseEntity<String> processFile(@RequestBody String fileBody, @RequestHeader("Content-Type") String contentType) throws Exception {
        // Log received headers and body
        System.out.println("Received Content-Type: " + contentType);
        System.out.println("Received File Body: " + fileBody);

        // Process the file body and content type
       Set<String> newContent = iReceivedMessage.processData(fileBody,contentType);

        // Update the shared processedContent
        synchronized (processedContent) {
            processedContent.clear();
            processedContent.addAll(newContent);
        }

        // Log the processed content
        System.out.println("Processed File Body: " + processedContent);

        return ResponseEntity.ok(processedContent.toString());
    }

    @GetMapping("/getElements")
    public ResponseEntity<Set<String>> getElements() {
        synchronized (processedContent) {
            return ResponseEntity.ok(new HashSet<>(processedContent));
        }
    }

}
