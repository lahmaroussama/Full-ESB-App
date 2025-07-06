package tn.besoftilys.messages.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.besoftilys.messages.entity.Message;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IMessage {
    Message saveMessage(Message message);
    Map<String, Long> countMessagesByContentType();
    String getContentType(String messageBody);
    Map<String, Integer> getMessageStats();

    ResponseEntity<List<Message>>getAllMessages();
    ResponseEntity<?>countAllMessages();
    ResponseEntity<Object> uploadFile(MultipartFile file, String uploadDir);
    ResponseEntity<Object> uploadFile1(MultipartFile file);

}
