package com.tribune.demo.reporting.util.processor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tribune.demo.reporting.model.ReportExportType;

import java.io.OutputStream;
import java.util.jar.JarException;

@Service
public class BeanFactoryDynamicAutowireService {
    private static final String SERVICE_NAME_SUFFIX = "processor";
    private final BeanFactory beanFactory;

    @Autowired
    public BeanFactoryDynamicAutowireService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void export(ReportExportType exportType, JasperPrint jasperPrint, OutputStream outputStream) throws JarException, JRException {
        OutputProcessor service = beanFactory.getBean(getWorkerName(exportType),
                OutputProcessor.class);

        service.export(jasperPrint, outputStream);
    }

    private String getWorkerName(ReportExportType exportType) {
        return exportType + SERVICE_NAME_SUFFIX;
    }
}
