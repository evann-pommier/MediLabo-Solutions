package com.openclassrooms.MediLaboSolutions.gateway;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayRoutingTest {

    private static final int PATIENT_SERVICE_PORT = 8081;

    static MockWebServer mockPatientService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void startMockServer() throws IOException {
        mockPatientService = new MockWebServer();

        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setBody("[]");
        response.addHeader("Content-Type", "application/json");
        mockPatientService.enqueue(response);

        mockPatientService.start(PATIENT_SERVICE_PORT);
    }

    @AfterEach
    void shutdown() throws IOException {
        mockPatientService.shutdown();
    }

    @Test
    void shouldRoutePatientsRequestAndInjectAuthHeader() throws InterruptedException {
        webTestClient.get()
                .uri("/patients")
                .exchange()
                .expectStatus().isOk();

        RecordedRequest recordedRequest = mockPatientService.takeRequest();

        assertThat(recordedRequest.getPath()).isEqualTo("/patients");
        assertThat(recordedRequest.getHeader("Authorization"))
                .isEqualTo("Basic Z2F0ZXdheTpnYXRld2F5LXB3ZA==");
    }
}