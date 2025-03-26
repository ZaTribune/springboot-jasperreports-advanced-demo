package com.tribune.demo.reporting.util.translator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;


/**
 * Visit this webpage for more info:
 * <a href="https://github.com/LibreTranslate/LibreTranslate">LibreTranslate</a>
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class LibreTranslator implements Translator {

    @Value("${api.libre-translate}")
    private String url;

    private final WebClient webClient;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class TranslateRequest {
        private String q;
        private String source;
        private String target;
        private String format;
        @JsonProperty("api_key")
        private String apiKey;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class TranslateResponse {
        private String translatedText;
    }

    //todo: add a redo to fix the issue when first word can't be translated
    //todo: don't perform requests when translation server is unreachable
    @Override
    public String translate(String input, String fromLang, String toLang) {
        log.info("Translating text [{}] to [{}]", input, toLang);
        final String text = breakCamel(input);
        TranslateRequest request = TranslateRequest.builder()
                .q("\t" + text)//to fix the first word issue when not translated
                .source(fromLang.toLowerCase())
                .target(toLang.toLowerCase())
                .build();

        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TranslateResponse.class)
                .timeout(Duration.ofSeconds(3))
                .doOnError(t -> log.error("Error occurred while translating text [{}] from [{}] to [{}]", text, fromLang, toLang, t))
                .onErrorResume(this::handleTranslationError)
                .mapNotNull(TranslateResponse::getTranslatedText) // Extract the translated text
                .blockOptional() // to avoid null checks
                .orElse(text); // Fallback to sam input string if no response is available
    }

    // Method to handle translation errors
    private Mono<TranslateResponse> handleTranslationError(Throwable throwable) {
        log.error("Translation failed due to: {}", throwable.getMessage(), throwable);
        return Mono.just(TranslateResponse.builder()
                .build());
    }
}
