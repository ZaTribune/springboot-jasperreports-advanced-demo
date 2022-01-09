package zatribune.spring.jasperreports.services;

import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import zatribune.spring.jasperreports.errors.UnsupportedItemException;
import zatribune.spring.jasperreports.model.ReportRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportingService {

    ResponseEntity<?> generateReport(ReportRequest request, HttpServletResponse response) throws JRException, IOException, UnsupportedItemException;
}
