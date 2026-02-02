package com.bit.app.controller;

import com.bit.app.dto.request.LinkRequestDto;
import com.bit.app.dto.response.DefaultApiResponse;
import com.bit.app.dto.response.LinkResponseDto;
import com.bit.app.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Link Management", description = "APIs for creating and managing short links")
public class LinkController {
    private final LinkService linkService;

    @Operation(summary = "Create a short link", description = "Generates a short code or uses a custom alias for a given long URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Link created successfully", content = @Content(schema = @Schema(implementation = LinkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or alias already exists", content = @Content)
    })
    @PostMapping("/shorten")
    public ResponseEntity<DefaultApiResponse<LinkResponseDto>> encode(
            @RequestBody @Valid LinkRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(linkService.encode(
                        request.getLinkUrl(),
                        request.getAlias(),
                        request.getExpiresAt()));
    }
}
