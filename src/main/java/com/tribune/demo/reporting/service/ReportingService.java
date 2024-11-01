package com.tribune.demo.reporting.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import com.tribune.demo.reporting.error.UnsupportedItemException;
import com.tribune.demo.reporting.model.ReportExportType;
import com.tribune.demo.reporting.model.ReportRequest;


import java.io.IOException;

public interface ReportingService {


    void generateFromModel(ReportRequest request, ReportExportType accept,
                           HttpServletResponse response)
            throws JRException, IOException, UnsupportedItemException;

    void generateDirect(ObjectNode reportRequest, String reportTitle, String language,
                        ReportExportType accept, HttpServletResponse servletResponse)
            throws JRException, IOException;
}
