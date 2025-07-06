package tn.besoftilys.messages.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.besoftilys.messages.entity.Message;
import tn.besoftilys.messages.service.IMessage;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    IMessage iMessage;

    private static final String UPLOAD_DIR2 = "C:/Users/rbencheikh/Desktop/Output";

    @GetMapping("/count-by-content-type")
    public ResponseEntity<Map<String, Long>> countByContentType() {
        Map<String, Long> counts = iMessage.countMessagesByContentType();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/getAllMessages")
    ResponseEntity<List<Message>>getAllMessages(){
        return iMessage.getAllMessages();
    }

    @GetMapping("/countAllMessages")
    public ResponseEntity<?> countAllMessages() {
        return iMessage.countAllMessages();
    }


    @PostMapping("/uploadFile")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("uploadDir") String uploadDir) {
        return iMessage.uploadFile(file, uploadDir);
    }

    @GetMapping("/stats")
    public Map<String, Integer> getMessageStats() {
        return iMessage.getMessageStats();
    }

    @PostMapping("/uploadFile1")
    public ResponseEntity<Object> uploadFile1(@RequestParam("file") MultipartFile file) {
        return iMessage.uploadFile1(file);
    }



}
