package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicenceGenerationService {
    private final InMemoryDatabase database;
    private final SocialSecurityNumberValidatorService socialSecurityNumberValidatorService = new SocialSecurityNumberValidatorService();
    private final DrivingLicenceIdGenerationService idGenerationService = new DrivingLicenceIdGenerationService();
    private final DrivingLicenceFinderService finderService = new DrivingLicenceFinderService(InMemoryDatabase.getInstance());

    public Optional<DrivingLicence> generateNewDrivingLicence() {

        UUID id = idGenerationService.generateNewDrivingLicenceId();
        Optional<DrivingLicence> drivingLicence = finderService.findById(id);

        try{
            String driverSocialSecurityNumber = JOptionPane.showInputDialog("Enter your social security number : ");

            if (!socialSecurityNumberValidatorService.isValid(driverSocialSecurityNumber)) {
                throw new InvalidDriverSocialSecurityNumberException("Invalid social security number, must be 15 digits");
            }
            if (drivingLicence.isPresent()) {
                throw new IllegalStateException("Driving licence already exists");
            }
            DrivingLicence newDrivingLicence = DrivingLicence.builder()
                    .id(id)
                    .driverSocialSecurityNumber(driverSocialSecurityNumber)
                    .build();
            database.save(newDrivingLicence.getId(), newDrivingLicence);
            return Optional.of(newDrivingLicence);
        } catch (InvalidDriverSocialSecurityNumberException | IllegalStateException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
