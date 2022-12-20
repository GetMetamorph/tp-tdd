package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicencePointsWithdrawService {
    private final InMemoryDatabase database;
    private final DrivingLicenceFinderService drivingLicenceFinderService;

    public DrivingLicence withdrawPoints(UUID drivingLicenceId, int points) throws ResourceNotFoundException {
        Optional<DrivingLicence> optionalDrivingLicence = drivingLicenceFinderService.findById(drivingLicenceId);

        if (!optionalDrivingLicence.isPresent()) {
            throw new ResourceNotFoundException("Driving licence not found");
        }

        DrivingLicence drivingLicence = optionalDrivingLicence.get();
        int newPoints = drivingLicence.getAvailablePoints() - points;
        if (newPoints < 0) {
            throw new IllegalArgumentException("Cannot withdraw more points than available");
        }

        drivingLicence = DrivingLicence.builder()

                .id(drivingLicence.getId())
                .driverSocialSecurityNumber(drivingLicence.getDriverSocialSecurityNumber())
                .availablePoints(newPoints)
                .build();

        database.save(drivingLicenceId, drivingLicence);
        return drivingLicence;
    }
}
