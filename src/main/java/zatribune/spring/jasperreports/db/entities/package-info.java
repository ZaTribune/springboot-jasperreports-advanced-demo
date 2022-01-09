/**
 * This package is used to make a standard modeling for all reports
 * so each {@link zatribune.spring.jasperreports.db.entities.Report} will have a set of :
 * (1) {@link zatribune.spring.jasperreports.db.entities.ReportField} which are first level fields,
 *      served one time for the report and always has a fixed value.
 * (2) {@link zatribune.spring.jasperreports.db.entities.ReportList} a collection data source provided to
 *      the famously used {@link net.sf.jasperreports.engine.data.JRBeanCollectionDataSource}.
 * (3) {@link zatribune.spring.jasperreports.db.entities.ReportListField} fields within each list in (2).
 * (4) {@link zatribune.spring.jasperreports.db.entities.ReportImage} 1st level images that should be rendered within the report;
 *     also served one time like (1).
 *
 * */
package zatribune.spring.jasperreports.db.entities;