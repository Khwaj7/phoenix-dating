package com.phoenix.dating.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Status", description = "Application monitoring")
public class StatusController {

    @Operation(summary = "Check application health")
    @ApiResponse(responseCode = "200", description = "Application is up and running")
    @GetMapping("/status")
    public String status() {
        return "OK";
    }
}
