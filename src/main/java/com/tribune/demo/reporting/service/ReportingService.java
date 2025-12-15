package com.tribune.demo.reporting.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Map;

public interface ReportingService {


    StreamingResponseBody generateFromModel(ReportRequest request, ReportExportType accept)
            throws JRException, UnsupportedItemException;

    StreamingResponseBody generateDirect(Map<String,Object> reportRequest, String reportTitle, String language,
                                         ReportExportType accept)
            throws JRException;
}
