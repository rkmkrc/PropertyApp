package org.erkam.propertypaymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.erkam.propertypaymentservice.dto.request.PaymentRequest;
import org.erkam.propertypaymentservice.dto.response.GenericResponse;
import org.erkam.propertypaymentservice.dto.response.PaymentResponse;
import org.erkam.propertypaymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @Operation(
            description = "Post endpoint to receive payment from the user.",
            summary = "Receive payment from the user.",
            responses = {
                    @ApiResponse(
                            description = "Created.",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Forbidden.",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Bad Request.",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<GenericResponse<PaymentResponse>> receivePayment(@RequestBody PaymentRequest request) {
        return new ResponseEntity<>(paymentService.receivePayment(request), HttpStatus.OK);
    }
}
