/**
 * This package is used to make a standard modeling for all reports
 * so each {@link com.tribune.demo.reporting.db.entity.Report} will have a set of :
 * (1) {@link com.tribune.demo.reporting.db.entity.ReportField} which are first level fields,
 *      served one time for the report and always has a fixed value.
 * (2) {@link com.tribune.demo.reporting.db.entity.ReportTable} a collection data source provided to
 *      the famously used {@link net.sf.jasperreports.engine.data.JRBeanCollectionDataSource}.
 * (3) {@link com.tribune.demo.reporting.db.entity.ReportTableColumn} fields within each list in (2).
 * (4) {@link com.tribune.demo.reporting.db.entity.ReportImage} 1st level images that should be rendered within the report;
 *     also served one time like (1).
 *
 * */
package com.tribune.demo.reporting.db.entity;