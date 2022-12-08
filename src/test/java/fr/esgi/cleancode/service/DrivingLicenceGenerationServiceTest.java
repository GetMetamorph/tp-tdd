package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DrivingLicenceGenerationServiceTest {

    @InjectMocks
    private DrivingLicenceGenerationService service;

    @Mock
    private InMemoryDatabase database;

    @Test
    void should_generate() {
        final var drivingLicence = service.generateNewDrivingLicence().get();
        assertThat(drivingLicence.getAvailablePoints()).isEqualTo(12);
        assertThat(drivingLicence.getDriverSocialSecurityNumber()).isNotNull();
        verify(database).save(drivingLicence.getId(), drivingLicence);
        verifyNoMoreInteractions(database);
    }
}
