package fr.esgi.cleancode.service;

public class SocialSecurityNumberValidatorService {

        public boolean isValid(String socialSecurityNumber) {
                return socialSecurityNumber != null
                        && !socialSecurityNumber.isEmpty()
                        && !socialSecurityNumber.isBlank()
                        && socialSecurityNumber.matches("[0-9]+")
                        && socialSecurityNumber.length() == 15;
        }
}
