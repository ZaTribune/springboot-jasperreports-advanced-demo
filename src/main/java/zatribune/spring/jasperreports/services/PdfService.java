package zatribune.spring.jasperreports.services;

import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PdfService {

    ResponseEntity<?> generatePdf(String json, HttpServletResponse response) throws JRException, IOException;
}
