package com.tribune.demo.reporting.util.translator;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class TranslatorTest {

    Translator translator = new LibreTranslator();

    static MockWebServer mockWebServer;


    @Test
    void breakCamel() {

        String input = "helloWorld";
        assertEquals("hello World", translator.breakCamel(input));
    }

    @BeforeEach
    void setUp() throws IOException {
        // Initialize MockWebServer
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Initialize the class under test
        translator = new LibreTranslator();

        // Inject the mock server URL
        ReflectionTestUtils.setField(translator, "url", mockWebServer.url("/translate").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Shutdown MockWebServer
        mockWebServer.shutdown();
    }

    @Test
    void translate_shouldReturnTranslatedText() {
        // Arrange
        String textToTranslate = "Hello";
        String from = "en";
        String to = "ar";
        String translatedText = "أهلا";

        MockResponse mockResponse = new MockResponse()
                .setBody("{\"translatedText\":\"" + translatedText + "\"}")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        // Act
        String result = translator.translate(textToTranslate, from, to);

        // Assert
        assertEquals(translatedText, result);
    }

    @Test
    void translate_shouldReturnEmptyStringWhenResponseIsEmpty() {
        // Arrange
        String textToTranslate = "Hello";
        String from = "en";
        String to = "es";

        MockResponse mockResponse = new MockResponse()
                .setBody("") // Empty body
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        // Act
        String result = translator.translate(textToTranslate, from, to);

        // Assert
        assertEquals("", result);
    }

    @Test
    void translate_shouldReturnOriginalWhenTimeout() {
        // Arrange
        String textToTranslate = "Hello";
        String from = "en";
        String to = "ar";

        MockResponse mockResponse = new MockResponse()
                .setBody("{\"translatedText\":\"Hola\"}")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200)
                .setBodyDelay(5, TimeUnit.SECONDS); // Simulate server delay

        mockWebServer.enqueue(mockResponse);

        // Act
        String result = translator.translate(textToTranslate, from, to);

        // Assert
        assertEquals(textToTranslate, result); // Timeout should result in an empty string
    }
}
