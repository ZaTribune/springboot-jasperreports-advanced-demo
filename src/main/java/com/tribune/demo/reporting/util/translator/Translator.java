package com.tribune.demo.reporting.util.translator;

public interface Translator {

    String translate(String textToTranslate, String from, String to);

    default String breakCamel(String content) {
        StringBuilder builder = new StringBuilder();
        for (String w : content.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            builder.append(w).append(" ");
        }
        return builder.substring(0, builder.length() - 1);
    }
}