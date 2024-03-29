# Please don't plagarise.

# Assignment Name

Tools for Agile Software Development (2021)

## Project Name

Development of an Automated Teller Machine (ATM) software for XYZ Bank

## Group Members

 - Imran Issa
 - Matthew Karko
 - Alexander Marrapese
 - William Qiu
 - Raymond Ton


# Description

The code is written in Java, spread across three files:
 - ATM.java
 - Card.java
 - Admin.java

The above are supplied with a .csv file of the name 'cards.csv' as the database for valid XYZ bank cards. 

ATM.java holds most of the functionality required for a customer's use of the ATM application, namely the processes of depositing, withdrawal and balance checking, as well as the application's main method.

Card.java is responsible for creating Card objects for use against the ATM application, carrying its own attributes resembling an authentic credit card, including but not limited to Account Number, PIN, and expiry date.

Admin.java is designed to handle administrative purposes of the ATM application, particularly for performing routine maintenance and adding cash into the system.
 
The test code of the application is found in the file:
 - TestATM.java

The above test file is supplied with a test .csv database of the name 'test_cards.csv'. It contains unit cases to test each method of the ATM application and verify correct behaviour.

## Software Development Approach and Tools

Our team employed Agile practices during the development of this application. The tools used include GitHub, Gradle, Junit, JaCoCo and Jenkins. Each member was entitled to use any IDE they wished, as long as they were able to contribute code. A single shared Git repository was used between us, allowing us to work and commit from our own branches. Gradle was essential for build automation and JUnit for automated testing. The integration of JaCoCo was intended for understanding test code coverage. Jenkins, via webhooks and ngrok, enabled continuous integration with every commit made and test reports following. 

# Running the Application

The following instructions assumes Gradle 6.7 and Java are installed on your PC.

The ATM application can be run by entering the following in a Linux-supported terminal from the directory of the 'build.gradle' file:

```bash
gradle build
```

If there are errors, please raise the issue immediately with the development team.
Otherwise, commence with the following from within the same directory:

```bash
gradle run
```

For a cleaner output, use this command instead:
```bash
gradle --console plain run
```
When the system runs, Select option 1 at the first prompt to start the ATM in regular running mode.

Simply then follow the instructions prompted on the screen.

# Testing the Application

The following instructions assumes Gradle and Java are installed on your PC.

The ATM application can be tested by entering the following in a Linux-supported terminal from the directory of the 'build.gradle' file:

```bash
gradle test
```

If there are errors, please raise the issue immediately with the development team.
