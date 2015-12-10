package draw_it.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import draw_it.data.message.ChatMessage;
import draw_it.data.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service(value = "messageUtils")
public final class MessageUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public Class<?> getClassFromJsonMessage(String jsonMessage) throws IOException, ClassNotFoundException {
        JsonNode tree = objectMapper.readTree(jsonMessage);
        String className = tree.get("@class").asText();
        Class clazz = Class.forName("draw_it.data.message." + className);
        return clazz;
    }

    public String formJsonListMessage(Message... messages) throws JsonProcessingException {

        StringBuilder result = new StringBuilder();
        result.append("[");

        for (Message message : messages) {
            String jsonMessage = objectMapper.writeValueAsString(message);
            result.append(jsonMessage);
            result.append(", ");
        }

        result.delete(result.length()-2, result.length());
        result.append("]");
        return result.toString();
    }


    public Message readMessage(String message, Class<? extends Message> messageClass) throws IOException {
        return objectMapper.readValue(message, messageClass);
    }
}
