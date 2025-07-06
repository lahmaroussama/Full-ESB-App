package tn.besoftilys.messages.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
@Entity
@Table(name = "t_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int messageSize;
    String contentType;
    String messageDestination;
    Date comingDate;

    public Message(int messageSize, String contentType, String messageDestination, Date comingDate) {
        this.messageSize = messageSize;
        this.contentType = contentType;
        this.messageDestination = messageDestination;
        this.comingDate = comingDate;
    }
}
