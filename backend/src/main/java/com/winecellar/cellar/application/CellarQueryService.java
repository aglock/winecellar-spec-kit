package com.winecellar.cellar.application;

import com.winecellar.cellar.domain.Cellar;
import com.winecellar.cellar.domain.CellarMembership;
import com.winecellar.cellar.repo.CellarMembershipRepository;
import com.winecellar.cellar.repo.CellarRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class CellarQueryService {

    private final CellarMembershipRepository membershipRepository;
    private final CellarRepository cellarRepository;

    public CellarQueryService(CellarMembershipRepository membershipRepository, CellarRepository cellarRepository) {
        this.membershipRepository = membershipRepository;
        this.cellarRepository = cellarRepository;
    }

    public List<CellarSummaryView> listForUser(String userId) {
        List<CellarMembership> memberships = membershipRepository.findByUserId(userId);
        if (memberships.isEmpty()) {
            return List.of();
        }
        Map<String, Cellar> cellarsById = cellarRepository.findByIdIn(
                        memberships.stream().map(CellarMembership::cellarId).toList())
                .stream()
                .collect(java.util.stream.Collectors.toMap(Cellar::id, Function.identity()));

        return memberships.stream()
                .map(membership -> {
                    Cellar cellar = cellarsById.get(membership.cellarId());
                    return cellar == null ? null : new CellarSummaryView(
                            cellar.id(),
                            cellar.name(),
                            cellar.location(),
                            cellar.description(),
                            membership.role().name(),
                            null,
                            null);
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(CellarSummaryView::name))
                .toList();
    }

    public record CellarSummaryView(
            String cellarId,
            String name,
            String location,
            String description,
            String memberRole,
            Integer bottleCount,
            String recentActivityAt) {
    }
}
