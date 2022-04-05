# Reporting [Spring Boot + Jasperreports]
  
A Demo project for generating PDF reports in Spring Boot using Jasper Reports.  

<p align="center">
  <table>
    <tr>
      <td><img src="src/main/resources/static/images/spring.svg" width="200" height="100"/></td>
      <td><img src="src/main/resources/static/images/jaspersoft-tibco.png" width="200"/></td>
      <td><img src="src/main/resources/static/images/jasper-reports.png" width="200"/></td>
    </tr>
  </table>
</p>  

## Main Technologies  
- Java
- Jasperreports
- TIBCO Jaspersoft Studio (Community Edition)  

## Features
- Tested with JMeter against 1000 requests.  
- Support for LTR & RTL languages.
- Notes for different workflows created as "**how to**" in order to help users.

## Steps to deploy
- Load this project on your preferred IDE e.g. Intellij or Netbeans ...etc and Maven will load all dependencies
  and configure directories.    
- Every Jasperreports workflow starts with the creation of a template, exported to a jrxml file.
  This file will be compiled later to a .jasper file later.   
- To create these templates easily, you'll need to use the Jaspersoft Studio.
- After exporting your templates from the editor, you can insert them under 
  "resources/static/templates".    
## Testing
- Provided with a testing template, You can use Postman for testing.  
- Import the collections file "resources/static/test.postman_collection.json" and enjoy.
 
 
