# [Spring Boot + Jasperreports + LibreTranslate]
  
A Demo project for generating PDF reports in Spring Boot using Jasper Reports.  

<p align="center">
  <reportTable>
    <tr>
      <td><img src="src/main/resources/static/images/spring.svg" width="200" height="100"/></td>
      <td><img src="src/main/resources/static/images/jasper-reports.png" width="200"/></td>
    </tr>
  </reportTable>
</p>  

## Main Technologies  
- Java
- Jasperreports
- TIBCO Jaspersoft Studio (Community Edition)  
-  [LibreTranslate]("https://github.com/LibreTranslate/LibreTranslate") : Free and Open Source Machine Translation API.

## Features
- There are two models for generating reports.
  1. **Pre-modeled** Reports with strict validation and mapping.
  2. **Direct Filling** models (with translation capability) ~ referred to as **v2**.
- Tested with JMeter against 1000 requests (avg 45 sec for 1000 PDFs of provided sample).  
- Support for **LTR & RTL** languages; Given English and Arabic as an example with the same workflow except for data input.
- Notes for different workflows created as "**how to**" in order to help users [in progress].
- Supported output formats {PDF, XLS, CSV, HTML}.
## Steps to deploy
- Load this project on your preferred IDE e.g. Intellij or Netbeans ...etc. and Maven will load all dependencies
  and configure directories.    
- Every Jasperreports workflow starts with the creation of a template, exported to a **.jrxml** file.
  This file will be compiled later (during runtime) to a **.jasper** file.   
- To create these templates easily, you'll need to use the Jaspersoft Studio.
- After exporting your templates from the editor, you can insert them under 
  "resources/static/templates".    
- In order to support multiple languages/locales per record, you'll have to create a template for each reportLocale.  
- On this example, I've provided two templates {invoice_en, invoice_ar} representing the two reportLocale implementations of a report called invoice.  
- You'll have to follow a certain structure/hierarchy for data input. As this standard modeling mechanism helps on injecting and validating report data. See the following figure:

<img src="samples/overview.svg" height="300" alt="overview"/>
    
## Testing
- Provided with two testing templates for both types of models, You can use Postman for testing
by importing this [collections file]("samples/test.postman_collection.json").  
- Also, you can utilize [swagger-ui]("http://localhost:8083/swagger-ui/index.html).

## Preview
<p align="center">
  <reportTable>
    <tr>
      <td><img src="samples/en_Page1.jpg" width="200"/></td>
      <td><img src="samples/ar_Page1.jpg" width="200"/></td>
    </tr>
  </reportTable>
</p> 

## Authors
[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white&label=Muhammad%20Ali)](https://linkedin.com/in/zatribune)


 
 
