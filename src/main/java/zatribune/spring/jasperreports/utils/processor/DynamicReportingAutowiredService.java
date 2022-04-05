package zatribune.spring.jasperreports.utils.processor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.jasperreports.model.ReportExportType;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarException;
import java.util.stream.Collectors;

@Service
public class DynamicReportingAutowiredService {
    private final Map<ReportExportType, OutputProcessor> servicesByCountryCode;

    @Autowired
    public DynamicReportingAutowiredService(List<OutputProcessor> regionServices) {
        servicesByCountryCode = regionServices.stream()
                .collect(Collectors.toMap(OutputProcessor::getExportType, Function.identity()));
    }

    public void export(ReportExportType exportType, JasperPrint jasperPrint, OutputStream outputStream) throws JarException, JRException {
        OutputProcessor service = servicesByCountryCode.get(exportType);

        service.export(jasperPrint, outputStream);
    }
}
