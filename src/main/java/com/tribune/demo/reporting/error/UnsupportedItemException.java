package com.tribune.demo.reporting.error;


import java.util.List;

public class UnsupportedItemException extends NullPointerException {

    public UnsupportedItemException(String support, String unsupported, List<String> supported) {
        super(
                String.format("Unsupported %s item at '%s'. Supported %s Items are %s.",
                        support,unsupported,support,supported)
        );
    }
}
