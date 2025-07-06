package tn.besoftilys.transformation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.besoftilys.transformation.dto.MessageDto;

import java.util.Map;
import java.util.Set;

public interface IReceivedMessage {

    String transformMessage(MessageDto messageDto, String key)throws JsonProcessingException;
    Set<String> processData(String data, String contentType)throws Exception;

}
