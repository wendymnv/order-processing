package org.example.orderprocessing.model.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * Represents a response from Kafka containing order information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderKafkaResponse {

    private String eventId;
    private String eventTime;
    private String eventType;
    private Event event;

}