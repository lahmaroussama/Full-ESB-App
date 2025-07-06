package tn.besoftilys.messages.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.besoftilys.messages.entity.Message;
import tn.besoftilys.messages.service.IMessage;
import java.util.Date;

@Component
public class FileMoveRoute extends RouteBuilder {
    final String HTTP_ENDPOINT = "http://localhost:8092/api/messages/processFile";
    @Autowired
    IMessage iMessage;
    @Override
    public void configure() throws Exception {
        // Global error handling
        onException(HttpOperationFailedException.class)
                .maximumRedeliveries(3)
                .redeliveryDelay(2000)
                .handled(true)
                .log("Error occurred: ${exception.message}")
                .to("file://C://Users//rbencheikh//Desktop//Error");

        from("file:///C://Users//rbencheikh//Desktop//Input")

                .process(exchange -> {
                    // Get content type
                    String contentType = iMessage.getContentType(exchange.getIn().getBody(String.class));
                    exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, contentType);
                    System.out.println(contentType);
                    Object body = exchange.getIn().getBody();
                    // Get file size
                    int fileSize = Math.toIntExact(exchange.getIn().getHeader(Exchange.FILE_LENGTH, Long.class));
                    System.out.println("File size: " + fileSize + " bytes");
                    //set current date
                    Date comingDate = new Date();
                    System.out.println(comingDate);
                    //set destination
                    String messageDestination = "Output";
                    //save in database
                    iMessage.saveMessage(new Message(fileSize, contentType, messageDestination, comingDate));
                })
                .log("${headers}")
                .log("${body}")
                .process(exchange -> {
                    // Prepare the body for the HTTP request
                    String contentType = exchange.getIn().getHeader(Exchange.CONTENT_TYPE, String.class);
                    String fileBody = exchange.getIn().getBody(String.class);

                    exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, contentType);
                    exchange.getMessage().setBody(fileBody);
                })
                .log("Sending file to the microservice for processing")
                .to(HTTP_ENDPOINT)
                .process(exchange -> {
                    // Process the response from the microservice
                    String responseBody = exchange.getIn().getBody(String.class);
                    System.out.println("Response from microservice: " + responseBody);

                    // Update the message body with the response from the microservice if needed
                    exchange.getMessage().setBody(responseBody);
                });
               // .to("file:///C://Users//rbencheikh//Desktop//Output");
    }

}