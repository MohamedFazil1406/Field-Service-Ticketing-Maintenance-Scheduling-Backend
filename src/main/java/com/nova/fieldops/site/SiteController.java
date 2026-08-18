package com.nova.fieldops.site;

import com.nova.fieldops.site.dto.CreateSiteRequest;
import com.nova.fieldops.site.dto.SiteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
    public ResponseEntity<SiteResponse> createSite(
            @Valid @RequestBody CreateSiteRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(siteService.createSite(request));
    }
}