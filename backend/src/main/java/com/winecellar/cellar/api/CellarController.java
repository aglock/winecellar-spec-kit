package com.winecellar.cellar.api;

import com.winecellar.cellar.application.CellarQueryService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cellars")
public class CellarController {

    private final CellarQueryService cellarQueryService;

    public CellarController(CellarQueryService cellarQueryService) {
        this.cellarQueryService = cellarQueryService;
    }

    @GetMapping
    CellarListResponse list(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return new CellarListResponse(cellarQueryService.listForUser(userId));
    }

    public record CellarListResponse(List<CellarQueryService.CellarSummaryView> items) {
    }
}
