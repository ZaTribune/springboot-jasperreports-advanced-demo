<div align="center" style="margin-top: 20px">
  <img src="samples/spring.svg" width="100px" height="100px" alt="spring"/>
  <br/>
  <br/>
  <img src="samples/plus.svg" width="5%" alt="spring"/>
  <br/>
  <img src="samples/jasper-reports.png" width="417" alt="jasperreports"/>
  <h2>SpringBoot Reporting Service</h2>
  <p>A powerful demo project for generating dynamic reports using JasperReports Libraries & Spring Boot.</p>

<div align="center" style="margin: 20px 0; border: 2px solid; border-radius: 10px; background-color: transparent; max-width: 600px;">
  <h3 style="margin: 0; font-size: 1.5em;">📊 Code Coverage</h3>
  <div style="display: flex; flex-wrap: wrap; gap: 10px; justify-content: center;">

   ![Instruction Coverage](https://img.shields.io/badge/Instruction_Coverage-15.32%25-red)
   ![Line Coverage](https://img.shields.io/badge/Line_Coverage-12.94%25-red)
   ![Branch Coverage](https://img.shields.io/badge/Branch_Coverage-33.33%25-red)
   ![Complexity Coverage](https://img.shields.io/badge/Complexity_Coverage-12.9%25-red)
   ![Method Coverage](https://img.shields.io/badge/Method_Coverage-11.49%25-red)
   ![Class Coverage](https://img.shields.io/badge/Class_Coverage-15.0%25-red)
   ![Overall Coverage](https://img.shields.io/badge/Overall_Coverage-14.74%25-red)

  </div>
</div>

</div>

## Built with

- Java (JDK 17 or later).
- Jasperreports.
- TIBCO Jaspersoft Studio (Community Edition).
- **[LibreTranslate]("https://github.com/LibreTranslate/LibreTranslate")** : Free and Open Source Machine Translation
  API.

## Features
- **Two Report Generation Models**:
  - **Pre-Modeled Reports**: Strict validation and mapping for structured data.
  - **Direct Filling Reports (v2)**: Supports translation capabilities for dynamic data.
- **Performance**: Tested with JMeter for up to 1000 requests, averaging 45 seconds for 1000 PDFs.
- **Multilingual Support**: Handles both `LTR` (Left-to-Right) and `RTL` (Right-to-Left) languages like English and Arabic.
- **Output Formats**: Supports multiple formats, including `PDF`, `XLS`, `CSV`, and `HTML`.
- **Documentation**: Includes "how-to" notes for different workflows (in progress).
- **Code Coverage**: This project uses JaCoCo for code coverage and GitHub Actions to generate and display the coverage percentage directly in this README.
## Steps to deploy
- Load this project on your preferred IDE, and Maven will load all dependencies
  and configure directories.
- Every Jasperreports workflow starts with the creation of a template, exported to a `.jrxml` file.
  This file will be compiled later (during runtime) to a `.jasper` file.
- To create these templates easily, you will need to use the [Jaspersoft Studio](https://community.jaspersoft.com/download-jaspersoft/download-jaspersoft/).
- After exporting your templates from the editor, you can insert them [here](src/main/resources/static/templates).
- To support multiple languages/locales per record, you will have to create a template for each reportLocale.
- (Optional) You can use the `LibreTranslate API` - [docs](https://github.com/LibreTranslate/LibreTranslate/blob/main/README.md).
  - Install locally [here](local).

### First Example
- On the first example, I've provided two templates {`invoice_en`, `invoice_ar`} representing the two reportLocale
  implementations of a report called invoice.
- You will have to follow a certain structure/hierarchy for data input.
  Adapting this structure helps on
  injecting and validating report data.  
  See the following figure:,
  
  <div align="center">
  <img src="samples/overview.svg" height="300" alt="overview"/>
  </div>

### Second Example
- The second example `v2` is for **direct filling** with **field translation**; 
- Also, two samples {`receipt_en`, `receipt_ar`} are provided.

## Testing

- Provided with two testing templates for both types of models, You can use Postman for testing
  by importing this [collections file](samples/jasperreports.postman_collection.json).
- Also, you can use [swagger-ui]("http://localhost:8083/swagger-ui/index.html) for reference.
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

## License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Special thanks to the JasperReports community for providing a powerful reporting library.
- Thanks to the LibreTranslate team for providing a reliable translation service.
- Gratitude to the Spring Boot team for making it easy to integrate with Spring applications.

## Authors

[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white&label=Muhammad%20Ali)](https://linkedin.com/in/zatribune)


 
 
