package com.mfarion.carregistry.dtos;

import com.mfarion.carregistry.controllers.dtos.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestTest {

    private SignUpRequest signUpRequest;

    @BeforeEach
    public void setUp() {
        signUpRequest = new SignUpRequest();
    }

    @Test
    void testSignUpRequestConstructor() {
        signUpRequest = new SignUpRequest("John", "Doe", "john.doe@example.com", "password");
        assertThat(signUpRequest).isNotNull();
        assertThat(signUpRequest.getName()).isEqualTo("John");
        assertThat(signUpRequest.getSurname()).isEqualTo("Doe");
        assertThat(signUpRequest.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(signUpRequest.getPassword()).isEqualTo("password");
    }

    @Test
    void testSignUpRequestGettersAndSetters() {
        signUpRequest.setName("Jane");
        signUpRequest.setSurname("Smith");
        signUpRequest.setEmail("jane.smith@example.com");
        signUpRequest.setPassword("newPassword");

        assertThat(signUpRequest.getName()).isEqualTo("Jane");
        assertThat(signUpRequest.getSurname()).isEqualTo("Smith");
        assertThat(signUpRequest.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(signUpRequest.getPassword()).isEqualTo("newPassword");
    }

    @Test
    void testSignUpRequestEqualsAndHashCode() {
        SignUpRequest signUpRequest1 = new SignUpRequest("John", "Doe", "john.doe@example.com", "password");
        SignUpRequest signUpRequest2 = new SignUpRequest("John", "Doe", "john.doe@example.com", "password");
        SignUpRequest signUpRequest3 = new SignUpRequest("Jane", "Smith", "jane.smith@example.com", "password");

        assertThat(signUpRequest1).isEqualTo(signUpRequest2);
        assertThat(signUpRequest1.hashCode()).hasSameHashCodeAs(signUpRequest2.hashCode());

        assertThat(signUpRequest1).isNotEqualTo(signUpRequest3);
        assertThat(signUpRequest1.hashCode()).isNotEqualTo(signUpRequest3.hashCode());
    }

    @Test
    void testSignUpRequestToString() {
        signUpRequest = new SignUpRequest("John", "Doe", "john.doe@example.com", "password");
        String expectedToString = "SignUpRequest(name=John, surname=Doe, email=john.doe@example.com, password=password)";
        assertThat(signUpRequest.toString()).hasSameHashCodeAs(expectedToString);
    }
}