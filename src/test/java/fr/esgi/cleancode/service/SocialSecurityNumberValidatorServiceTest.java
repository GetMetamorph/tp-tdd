package fr.esgi.cleancode.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SocialSecurityNumberValidatorServiceTest {

    SocialSecurityNumberValidatorService service = new SocialSecurityNumberValidatorService();

    @Test
    void should_be_valid() {
        final var socialSecurityNumber = "123456789012345";
        final var actual = service.isValid(socialSecurityNumber);
        assertThat(actual).isTrue();
        }

    @Test
    void should_not_be_valid_because_null() {
        final var actual = service.isValid(null);
        assertThat(actual).isFalse();
        }

    @Test
    void should_not_be_valid_because_empty() {
            final var actual = service.isValid("");
            assertThat(actual).isFalse();
            }

    @Test
    void should_not_be_valid_because_blank() {
            final var actual = service.isValid("               ");
            assertThat(actual).isFalse();
            }

    @Test
    void should_not_be_valid_because_not_a_number() {
            final var actual = service.isValid("NOT_A_NUMBERRRR");
            assertThat(actual).isFalse();
            }

    @Test
    void should_not_be_valid_because_not_15_characters() {
        final var actual = service.isValid("1234");
        assertThat(actual).isFalse();
        }
}

