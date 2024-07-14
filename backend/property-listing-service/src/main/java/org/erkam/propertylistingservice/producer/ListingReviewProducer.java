package org.erkam.propertylistingservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertylistingservice.config.RabbitConfig;
import org.erkam.propertylistingservice.constants.LogMessage;
import org.erkam.propertylistingservice.constants.enums.MessageStatus;
import org.erkam.propertylistingservice.producer.dto.ListingDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
@Slf4j
public class ListingReviewProducer {
    private final AmqpTemplate rabbitTemplate;
    private final RabbitConfig rabbitConfig;

    public void sendNotification(ListingDto listingDto) {
        rabbitTemplate.convertAndSend(rabbitConfig.getExchange(), rabbitConfig.getRoutingKey(), listingDto);
        log.info(LogMessage.generate(MessageStatus.POS,
                "Listing with id: "+ listingDto.getId()
                        + " added to the listing review queue, exchange: ", rabbitConfig.getExchange()));
    }
}
