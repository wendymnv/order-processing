package org.example.orderprocessing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.orderprocessing.exception.GenericDomainException;
import org.example.orderprocessing.expose.ReceiverOrdersKafka;
import org.example.orderprocessing.model.enums.ErrorCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
/**
 * Repository implementation for managing Order documents in MongoDB.
 */
public final class JsonReaderUtil {

    private static final Logger log = LoggerFactory.getLogger(ReceiverOrdersKafka.class);

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static final String USER_MESSAGE = "Error al convertir objeto a json";
    public static final String USER_MESSAGE_LOG = "Error al convertir objeto a json: {}";

    private JsonReaderUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getObjectFromJson(String payload, Class<T> valueType) {
        try {
            return objectMapper.readValue(payload, valueType);
        } catch (IOException | IllegalArgumentException e) {
            log.warn(USER_MESSAGE_LOG, e.getMessage());
            throw new GenericDomainException(ErrorCategory.UNEXPECTED, USER_MESSAGE);
        }
    }
    public static <T> T getObjectFromJsonFromPath(String path, Class<T> valueType) {
        var resource = new ClassPathResource(path);
        try (var inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, valueType);
        } catch (IOException | IllegalArgumentException e) {
            log.warn(USER_MESSAGE_LOG, e.getMessage());
            throw new GenericDomainException(ErrorCategory.UNEXPECTED, USER_MESSAGE);
        }
    }
    public static <T> T getSingle(String jsonPath, Class<T> clazz) {
        return getObjectFromJsonFromPath(jsonPath, clazz);
    }

}
