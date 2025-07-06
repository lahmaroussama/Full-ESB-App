package tn.besoftilys.messages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.besoftilys.messages.entity.Message;

import java.util.Date;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Long countByContentType(String type);

}
