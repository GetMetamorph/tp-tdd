package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DrivingLicencePointsWithdrawServiceTest {
    private static final UUID DRIVING_LICENCE_ID = UUID.randomUUID();
    private static final int AVAILABLE_POINTS = 5;
    private static final int POINTS_TO_WITHDRAW = 2;
    private static final String SOCIAL_SECURITY_NUMBER = "012345678912345";

    private DrivingLicenceFinderService finderService = Mockito.mock(DrivingLicenceFinderService.class);
    @Mock
    private InMemoryDatabase database = Mockito.mock(InMemoryDatabase.class);
    private DrivingLicencePointsWithdrawService service = new DrivingLicencePointsWithdrawService(database, finderService);

    @Test
    void should_throw_exception_when_driving_licence_not_found() {
        Mockito.when(finderService.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.withdrawPoints(DRIVING_LICENCE_ID, POINTS_TO_WITHDRAW));

        assertEquals("Driving licence not found", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_withdrawing_more_points_than_available() {
        DrivingLicence drivingLicence = DrivingLicence.builder()
                .id(DRIVING_LICENCE_ID)
                .driverSocialSecurityNumber(SOCIAL_SECURITY_NUMBER)
                .availablePoints(AVAILABLE_POINTS)
                .build();
        Mockito.when(finderService.findById(any())).thenReturn(Optional.of(drivingLicence));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.withdrawPoints(DRIVING_LICENCE_ID, AVAILABLE_POINTS + 1));

        assertEquals("Cannot withdraw more points than available", exception.getMessage());
    }

    @Test
    void should_withdraw_points() {
        DrivingLicence drivingLicence = DrivingLicence.builder()
                .id(DRIVING_LICENCE_ID)
                .driverSocialSecurityNumber(SOCIAL_SECURITY_NUMBER)
                .availablePoints(AVAILABLE_POINTS)
                .build();
        Mockito.when(finderService.findById(any())).thenReturn(Optional.of(drivingLicence));

        DrivingLicence actual = service.withdrawPoints(DRIVING_LICENCE_ID, POINTS_TO_WITHDRAW);

        assertEquals(DRIVING_LICENCE_ID, actual.getId());
        assertEquals(SOCIAL_SECURITY_NUMBER, actual.getDriverSocialSecurityNumber());
        assertEquals(AVAILABLE_POINTS - POINTS_TO_WITHDRAW, actual.getAvailablePoints());
    }

    @Test
    void should_update_driving_licence_with_correct_number_of_available_points() throws ResourceNotFoundException {
        DrivingLicence drivingLicence = DrivingLicence.builder()
                .id(DRIVING_LICENCE_ID)
                .driverSocialSecurityNumber(SOCIAL_SECURITY_NUMBER)
                .availablePoints(AVAILABLE_POINTS)
                .build();
        Mockito.when(finderService.findById(any())).thenReturn(Optional.of(drivingLicence));

        DrivingLicence updatedDrivingLicence = service.withdrawPoints(DRIVING_LICENCE_ID, POINTS_TO_WITHDRAW);

        assertEquals(AVAILABLE_POINTS - POINTS_TO_WITHDRAW, updatedDrivingLicence.getAvailablePoints());
    }

    @Test
    void should_return_updated_driving_licence() throws ResourceNotFoundException {
        DrivingLicence drivingLicence = DrivingLicence.builder()
                .id(DRIVING_LICENCE_ID)
                .driverSocialSecurityNumber(SOCIAL_SECURITY_NUMBER)
                .availablePoints(AVAILABLE_POINTS)
                .build();
        Mockito.when(finderService.findById(any())).thenReturn(Optional.of(drivingLicence));

        DrivingLicence updatedDrivingLicence = service.withdrawPoints(DRIVING_LICENCE_ID, POINTS_TO_WITHDRAW);

        assertEquals(drivingLicence.getId(), updatedDrivingLicence.getId());
        assertEquals(drivingLicence.getDriverSocialSecurityNumber(), updatedDrivingLicence.getDriverSocialSecurityNumber());
        assertEquals(AVAILABLE_POINTS - POINTS_TO_WITHDRAW, updatedDrivingLicence.getAvailablePoints());
    }

    @Test
    void should_save_updated_driving_licence_to_database() throws ResourceNotFoundException {
        service = new DrivingLicencePointsWithdrawService(database, finderService);

        DrivingLicence drivingLicence = DrivingLicence.builder()
                .id(DRIVING_LICENCE_ID)
                .driverSocialSecurityNumber(SOCIAL_SECURITY_NUMBER)
                .availablePoints(AVAILABLE_POINTS)
                .build();
        Mockito.when(finderService.findById(any())).thenReturn(Optional.of(drivingLicence));

        service.withdrawPoints(DRIVING_LICENCE_ID, POINTS_TO_WITHDRAW);

        assertEquals(Optional.of(drivingLicence), finderService.findById(DRIVING_LICENCE_ID));
    }

}

