package com.tribune.demo.reporting.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportEntry {
    private String key;
    private String value;
    private boolean translate = false;
}
