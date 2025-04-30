# Beauty Salon Management System

## Version
v1.0 – March 27, 2023

## Overview
This project is a Java-based application developed for managing operations in a beauty salon. It was created as part of the OOP1 course for the 2022/2023 academic year and demonstrates the use of object-oriented programming principles with either a console or graphical (Swing) user interface.

## Description
The system provides an information management solution for a beauty salon, supporting multiple user roles and covering core functionalities such as user management, appointment scheduling, treatment handling, financial tracking, and loyalty management.

## Technologies
- **Language:** Java 1.8
- **UI:** Java Swing (GUI)
- **Data Storage:** Text-based (CSV)
- **External Libraries:** [XChart](https://knowm.org/open-source/xchart/) for graphing in GUI version

			   [MigLayout](https://search.maven.org/) for designing complex user interfaces

## System Users
- **Manager**: Full access to the system including CRUD operations on all entities, managing employees, pricing, loyalty thresholds, and financial reports.
- **Beautician**: Views their own schedule and assigned cosmetic treatments.
- **Receptionist**: Manages appointment bookings and cancellations; can schedule treatments on behalf of clients.
- **Client**: Registers, views their treatment history and statuses, books appointments, and tracks loyalty benefits.

## Key Features
- **User Registration & Login:** Clients self-register; employees are registered by managers.
- **Role-Based Access:** Each role sees and interacts with a different subset of the system.
- **Treatment Scheduling:** Booked by clients or receptionists with automatic/manual beautician assignment based on qualifications.
- **Treatment States:** Includes “SCHEDULED”, “COMPLETED”, “CANCELED_BY_CLIENT”, “CANCELED_BY_SALON”, and “NO_SHOW”.
- **Loyalty Program:** Clients earning above a defined spending threshold receive a 10% discount on future services.
- **Dynamic Pricing:** Treatments are priced at the time of booking and unaffected by later price changes.
- **Income & Reports:** Managers can view financial statistics, treatment history, and generate client loyalty eligibility lists.

## Reports Required
- Beautician performance: number of treatments and income over a selected date range.
- Treatment statistics: number of confirmed and canceled treatments by reason.
- Service analytics: usage and income per treatment type.
- Loyalty eligibility: list of clients who exceeded the manager-defined spending threshold.

## Data Management
- All entities such as Employees, Clients, Treatments, Service Types, Scheduled Appointments, and Pricing are persistently stored and editable through the UI.

## Tests
A few functionalities are tested what is visible in folder testovi. The one example of test classes is MenadzerKlijentTest that tests MenadzerKlijent class. Using JUnit 5, it performs the following types of tests:

- File reading and formatting (procitaj)

- Client data updating (azuriraj)

- Client deletion from internal map and file (izbrisi)

- Writing client data to a file (upisiUFajl)

- Loyalty card eligibility based on spending (traziKarticuLojalnosti)

