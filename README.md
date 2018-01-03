![Practera](http://intersective-wordpress-media.s3.amazonaws.com/wp-content/uploads/2014/04/Practera_Logo.png)

## Overview
* An Eclipse Project
* Personal Edge and AppV1 test cases, test cases organise as different folders within the **testfile** directory.
* Integrate Selenium with **testNG**, **log4j2** for logging, build-in java **sqlite**, **java 1.8**.
* Emailable test report customised

## Setup
### Windows
* Download JDK8 for windows via [Oracle official website](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Choose the JDK for your operating system, e.g., "Windows x64" (for 64-bit Windows OS) or "Windows x86" (for 32-bit Windows OS)
* Run the downloaded installer
* Define JAVA\_HOME environment variable which points to the JDK installed directory and adds JAVA\_HOME\bin into the PATH system environment variable
* Verify the setup by typing java -version at the command prompt

### Mac OS
* Download JDK8 for windows via [Oracle official website](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Choose macOS JDK version
* Run the installer
* Double-click the downloaded Disk Image (DMG) file. Follow the screen instructions to install JDK
* Eject the DMG file
* Verify the setup by typing java -version at the terminal

### Download Eclipse IDE via [Eclipse official website](http://www.eclipse.org/downloads/) and install the IDE

### Download the chrome driver via [Google sites](https://sites.google.com/a/chromium.org/chromedriver/)

### Project Structure Setup
* Create **db** and **logs** directories
* Manually put necessary jar files of **Selenium**, **log4j2** , java **sqlite** , **ooxml** and **json-simple** into the **lib** folder
* Install **testNG** for Eclipse via Eclipse Market place
* **user.properties** for environment specific values
* **data** folder contains the test data JSON files
* **upload_file** folder contains the example files for file uploading test cases
* Modify **webDriverPath** to point to your chrome driver file path
* Modify **evidenceFolder** to point to your **upload_file** path

## Note
* **test.properties** for wait time values
* **log4j2.xml** for log4j settings
* **testsuit.xml** and other files with same name pattern within the project root directory are for running TestNG test suites

## Some Handy Developer Links
* [Selenium](http://www.seleniumhq.org/docs/) - Automates web-based testing
* [TestNG](http://testng.org/doc/documentation-main.html) - Functional, End-to-End, Integration test framework
* [Eclipse](http://www.eclipse.org/) - Integrated Development Environment