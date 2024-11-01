package com.tribune.demo.reporting.error;

import java.util.List;

public class UnsupportedLanguageException extends RuntimeException {

    public UnsupportedLanguageException(String entry, List<String> supportedLanguages) {
        super(
                String.format("Unsupported language [%s]. " +
                                "Please, update your [Accept-Language] HTTP header to match one of the following %s.",
                        entry, supportedLanguages
                ));
    }
}
