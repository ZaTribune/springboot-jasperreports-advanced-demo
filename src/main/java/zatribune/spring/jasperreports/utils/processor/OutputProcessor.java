package zatribune.spring.jasperreports.utils.processor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import zatribune.spring.jasperreports.model.ReportExportType;

import java.io.OutputStream;
import java.util.jar.JarException;

public interface OutputProcessor {

    void export(JasperPrint jasperPrint, OutputStream outputStream) throws JarException, JRException;


    ReportExportType getExportType();
}
