package com.tribune.demo.reporting.util.processor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import com.tribune.demo.reporting.model.ReportExportType;

import java.io.OutputStream;
import java.util.jar.JarException;

public interface OutputProcessor {

    void export(JasperPrint jasperPrint, OutputStream outputStream) throws JarException, JRException;


    ReportExportType getExportType();
}
