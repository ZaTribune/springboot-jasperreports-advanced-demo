package zatribune.spring.jasperreports.services;

import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.GenericResponse;
import zatribune.spring.jasperreports.model.ReportExportType;
import zatribune.spring.jasperreports.model.ReportRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportingService {

    ResponseEntity<GenericResponse> generateReport(ReportRequest request, ReportExportType accept, HttpServletResponse response)
            throws JRException, IOException, UnsupportedItemException;
}
