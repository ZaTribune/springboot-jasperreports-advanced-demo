package zatribune.spring.jasperreports.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.ReportExportType;
import zatribune.spring.jasperreports.model.ReportRequest;


import java.io.IOException;

public interface ReportingService {


    void generateFromModel(ReportRequest request, ReportExportType accept,
                           HttpServletResponse response)
            throws JRException, IOException, UnsupportedItemException;

    void generateDirect(ObjectNode reportRequest, String reportTitle, String language,
                        ReportExportType accept, HttpServletResponse servletResponse)
            throws JRException, IOException;
}
