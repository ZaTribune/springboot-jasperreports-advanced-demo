#!/bin/bash

# Path to your JaCoCo XML report
report="target/site/jacoco/jacoco.xml"  # For Maven
# report="build/reports/jacoco/test/jacocoTestReport.xml"  # For Gradle

# Extract coverage percentage from JaCoCo report
coverage=$(grep -oP '(?<=linecoverage=")[0-9]+(\.[0-9]+)?' "$report")

# Create a badge Markdown text
badge="[![Coverage](https://img.shields.io/badge/coverage-${coverage}%25-brightgreen.svg)](https://github.com/zatribune/springboot-jasperreports-advanced-demo)"

# Print the badge Markdown
echo "$badge"
