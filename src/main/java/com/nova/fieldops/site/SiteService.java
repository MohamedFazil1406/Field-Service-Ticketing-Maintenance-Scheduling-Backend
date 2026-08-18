package com.nova.fieldops.site;

import com.nova.fieldops.site.dto.CreateSiteRequest;
import com.nova.fieldops.site.dto.SiteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteResponse createSite(CreateSiteRequest request) {

        Site site = Site.builder()
                .name(request.name())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .createdAt(LocalDateTime.now())
                .build();

        Site savedSite = siteRepository.save(site);

        return new SiteResponse(
                savedSite.getId(),
                savedSite.getName(),
                savedSite.getAddress(),
                savedSite.getLatitude(),
                savedSite.getLongitude()
        );
    }
}