package com.tribune.demo.reporting.util.processor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tribune.demo.reporting.model.ReportExportType;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarException;
import java.util.stream.Collectors;

@Service
public class DynamicOutputProcessorService {
    private final Map<ReportExportType, OutputProcessor> processorServices;

    @Autowired
    public DynamicOutputProcessorService(List<OutputProcessor> regionServices) {
        processorServices = regionServices.stream()
                .collect(Collectors.toMap(OutputProcessor::getExportType, Function.identity()));
    }

    public void export(ReportExportType exportType, JasperPrint jasperPrint, OutputStream outputStream) throws JarException, JRException {
        OutputProcessor service = processorServices.get(exportType);
        service.export(jasperPrint, outputStream);
    }
}
