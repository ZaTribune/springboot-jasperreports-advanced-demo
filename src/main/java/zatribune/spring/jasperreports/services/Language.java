package zatribune.spring.jasperreports.services;

public enum Language {
    ENGLISH("en"), ARABIC("ar");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
