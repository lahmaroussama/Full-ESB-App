package tn.besoftilys.messages.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.besoftilys.messages.entity.Message;
import tn.besoftilys.messages.repository.MessageRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MessageService implements IMessage{
    @Autowired
    MessageRepository messageRepository;

    private static final String UPLOAD_DIR = "C:/Users/rbencheikh/Desktop/Output";

    @Override
    public Message saveMessage(Message message) {
       return messageRepository.save(message);
    }

    @Override
    public Map<String, Long> countMessagesByContentType() {
        List<String> contentTypes = Arrays.asList("application/json", "application/javascript", "application/xml", "text/html","text/plain");
        Map<String, Long> counts = new HashMap<>();

        for (String contentType : contentTypes) {
            Long count = messageRepository.countByContentType(contentType);
            counts.put(contentType, count);
        }

        return counts;
    }

    @Override
    public String getContentType(String messageBody) {
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

    @Override
    public ResponseEntity<List<Message>> getAllMessages() {
        try{
            List<Message> messages = messageRepository.findAll();
            if (messages.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>(messages,HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> countAllMessages() {
       long counter = messageRepository.count();
       return new ResponseEntity<>(counter,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> uploadFile(MultipartFile file, String uploadDir) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Create the upload directory if it doesn't exist
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // Save the file to the upload directory
            Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> uploadFile1(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Create the upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file to the upload directory
            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }
    public Map<String, Integer> getMessageStats() {
        List<Message> messages = messageRepository.findAll();
        int total = messages.size();
        int json = 0, xml = 0, text = 0;
        for (Message m : messages) {
            if ("application/json".equals(m.getContentType())) json++;
            else if ("application/xml".equals(m.getContentType())) xml++;
            else if ("text/plain".equals(m.getContentType())) text++;
        }
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("json", json);
        stats.put("xml", xml);
        stats.put("text", text);
        return stats;
    }

}

