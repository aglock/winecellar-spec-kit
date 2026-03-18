package com.winecellar.cellar.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.winecellar.cellar.domain.Cellar;
import com.winecellar.cellar.domain.CellarMembership;
import com.winecellar.cellar.domain.MembershipRole;
import com.winecellar.cellar.repo.CellarMembershipRepository;
import com.winecellar.cellar.repo.CellarRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CellarQueryServiceTest {

    private CellarMembershipRepository membershipRepository;
    private CellarRepository cellarRepository;
    private CellarQueryService service;

    @BeforeEach
    void setUp() {
        membershipRepository = Mockito.mock(CellarMembershipRepository.class);
        cellarRepository = Mockito.mock(CellarRepository.class);
        service = new CellarQueryService(membershipRepository, cellarRepository);
    }

    @Test
    void returnsMembershipBackedCellarSummaries() {
        when(membershipRepository.findByUserId("user-1"))
                .thenReturn(List.of(new CellarMembership("membership-1", "user-1", "cellar-1", MembershipRole.OWNER, Instant.now(), "user-1")));
        when(cellarRepository.findByIdIn(List.of("cellar-1")))
                .thenReturn(List.of(new Cellar("cellar-1", "Reserve", "Bordeaux", "Private cellar", Instant.now())));

        assertThat(service.listForUser("user-1")).hasSize(1);
    }
}
