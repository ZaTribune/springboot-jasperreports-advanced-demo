# Overview
### Spring Boot JasperReports PDF Report Generation Demo
This project demonstrates how to generate PDF reports using JasperReports in a Spring Boot web application.

<p align="center">
   <img src="samples/spring.svg" height="100" alt="spring"/>
</p>
<p align="center">
  <img src="samples/jasper-reports.png" width="417" alt="jasperreports"/>
</p>

## Prerequisites
Before running this project, ensure you have the following installed:

- Java Development Kit (JDK) version 22 or higher
- Apache Maven
- Your favorite IDE (e.g., IntelliJ IDEA, Eclipse)

## Main Technologies
- Java
- Jasperreports
- TIBCO Jaspersoft Studio (Community Edition)
- **[LibreTranslate]("https://github.com/LibreTranslate/LibreTranslate")** : Free and Open Source Machine Translation
  API.

## Features
- There are two models for generating reports.
    - **Pre-modeled** Reports (with strict validation and mapping).
    - **Direct Filling** Reports (with translation capability) ~ Also referred to as **v2**.
- Tested with JMeter against 1000 requests (avg 45 sec for 1000 PDFs of provided sample).
- Support for `LTR` & `RTL` languages; Given English and Arabic as an example with the same workflow except for data
  input.
- Notes for different workflows created as "**how to**" to help users [in progress].
- Supported output formats {`PDF`, `XLS`, `CSV`, `HTML`}.

## Steps to deploy
- Load this project on your preferred IDE, e.g., Intellij or Netbeans ...etc. and Maven will load all dependencies
  and configure directories.
- Every Jasperreports workflow starts with the creation of a template, exported to a `.jrxml` file.
  This file will be compiled later (during runtime) to a `.jasper` file.
- To create these templates easily, you'll need to use the Jaspersoft Studio.
- After exporting your templates from the editor, you can insert them [here]("src/main/resources/static/templates").
- To support multiple languages/locales per record, you'll have to create a template for each reportLocale.
- On the first example, I've provided two templates {`invoice_en`, `invoice_ar`} representing the two reportLocale
  implementations of a report called invoice.
- You'll have to follow a certain structure/hierarchy for data input.
  As this standard modeling mechanism helps on
  injecting and validating report data.
  See the following figure:

<img src="samples/overview.svg" height="300" alt="overview"/>

- The second example `v2` is for getting translated reposts; Also two samples {`receipt_en`, `receipt_ar`} were added.
- For translation APIs,
  visit this [url](https://github.com/LibreTranslate/LibreTranslate/tree/main?tab=readme-ov-file#mirrors) 
  to get a free mirror for LibreTranslate.

## License
This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

## Acknowledgments
- Special thanks to the JasperReports community for providing a powerful reporting library.
- Thanks to the LibreTranslate team for providing a reliable translation service.
- Gratitude to the Spring Boot team for making it easy to integrate with Spring applications.

## Testing
- Provided with two testing templates for both types of models, You can use Postman for testing
  by importing this [collections file]("samples/test.postman_collection.json").
- Also, you can utilize [swagger-ui]("http://localhost:8083/swagger-ui/index.html).
- A [console.sql](samples/console.sql) file is added here to validate the db.

## Preview
<p align="center">
  <reportTable>
    <tr>
      <td><img src="samples/en_Page1.jpg" style="width: 200px" alt="report_english"/></td>
      <td><img src="samples/ar_Page1.jpg" style="width: 200px" alt="report_arabic"/></td>
    </tr>
  </reportTable>
</p> 

## Authors

[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white&label=Muhammad%20Ali)](https://linkedin.com/in/zatribune)


 
 
