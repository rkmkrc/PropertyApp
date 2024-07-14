package org.erkam.propertylistingreviewservice.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertylistingreviewservice.constants.LogMessage;
import org.erkam.propertylistingreviewservice.constants.enums.MessageStatus;
import org.erkam.propertylistingreviewservice.consumer.dto.ListingDto;
import org.erkam.propertylistingreviewservice.service.ListingReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListingReviewConsumer {
    private final ListingReviewService listingReviewService;

    @RabbitListener(queues = "listing.review.queue")
    public void consumeListingReview(ListingDto listingDto) {
        log.info(LogMessage.generate(MessageStatus.POS, "Listing in review to change status:", listingDto.toString()));
        if (listingDto.getId() == null) {
            log.error("Listing id is null for listing review.");
            throw new IllegalArgumentException("Listing id cannot be null");
        }
        listingReviewService.handleListingReview(listingDto);
    }
}
