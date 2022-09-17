package zatribune.spring.jasperreports.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.GenericResponse;
import zatribune.spring.jasperreports.model.ReportExportType;
import zatribune.spring.jasperreports.model.ReportRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportingService {

    void generateReport(ReportRequest request, ReportExportType accept,
                                                   HttpServletResponse response)
            throws JRException, IOException, UnsupportedItemException;

    void generateReport(ObjectNode reportRequest, String language,
                        ReportExportType accept, HttpServletResponse servletResponse)
            throws JRException, IOException;
}
