package com.tribune.demo.reporting.util.processor;

import com.tribune.demo.reporting.model.ReportExportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarException;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DynamicOutputProcessor {

    private final Map<ReportExportType, OutputProcessor> processorMap;

    @Autowired
    public DynamicOutputProcessor(List<OutputProcessor> processors) {
        processorMap = processors.stream()
                .collect(Collectors.toMap(OutputProcessor::getExportType, Function.identity()));
    }

    public void export(ReportExportType exportType, JasperPrint jasperPrint, OutputStream outputStream) throws JarException, JRException {
        OutputProcessor service = processorMap.get(exportType);

        log.info("Exporting via: {}", service.getExportType());
        service.export(jasperPrint, outputStream);
    }


    public OutputProcessor as(ReportExportType exportType){
        return processorMap.get(exportType);
    }


    public int getSize(){
        return processorMap.size();
    }
}
