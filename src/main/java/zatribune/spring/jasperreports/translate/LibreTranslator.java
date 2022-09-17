package zatribune.spring.jasperreports.translate;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.regex.Pattern;
/**
 * Visit this webpage for more info:
 * <a href="https://github.com/LibreTranslate/LibreTranslate">LibreTranslate</a>
 **/
@Slf4j
@Component
public class LibreTranslator implements Translator {
    //a pattern to check weather a word wasn't translated from translator server
    private final Pattern p = Pattern.compile(".[a-zA-Z]");
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class TranslateRequest {
        private String q;
        private String source;
        private String target;
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
    public String translate(String textToTranslate, String from, String to) {
        TranslateResponse response = WebClient.builder().build()
                .post()
                .uri("https://translate.argosopentech.com/translate")
                .bodyValue(TranslateRequest.builder()
                        .q("\t" + textToTranslate)//to fix the first word issue when not translated
                        .source(from)
                        .target(to)
                        .build())
                .exchangeToMono(clientResponse ->
                        clientResponse.bodyToMono(TranslateResponse.class)
                )
                .timeout(Duration.ofSeconds(1))
                .onErrorResume(t -> Mono.just(TranslateResponse.builder()
                        .translatedText(textToTranslate)
                        .build()))
                .block();
        if (response != null) {
            if (p.matcher(response.translatedText).find())
                return ": ".concat(response.getTranslatedText());
            else
                return response.getTranslatedText().concat(" :");
        }
        return "";
    }
}
