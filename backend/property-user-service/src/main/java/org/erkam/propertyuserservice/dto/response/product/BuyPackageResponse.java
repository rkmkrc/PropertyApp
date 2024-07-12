package org.erkam.propertyuserservice.dto.response.product;

import lombok.*;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyPackageResponse {
    private String responseMessage;
    public static BuyPackageResponse of(BuyPackageRequest request) {
        return BuyPackageResponse.builder()
                .responseMessage(
                        "Package purchased successfully."
                                + " Type: " + request.getType())
                .build();
    }
}
