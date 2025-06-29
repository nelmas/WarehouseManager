# Inventory Management System

## Project Overview

This Inventory Management System was a school project for the Database course and is a Java-based application designed to efficiently manage suppliers, products, and warehouses within a logistics environment. The system replaces manual, error-prone tracking methods with a scalable, secure, and user-friendly solution that ensures accurate inventory control and streamlined operations.

## Features

- Register and manage **Products**, **Suppliers**, and **Warehouses**  
- Associate products with suppliers and allocate stock to warehouses  
- Track inventory levels across multiple warehouse locations  
- Enforce warehouse capacity limits and prevent negative stock values  
- Retrieve and update product, supplier, and warehouse information  
- Display detailed stock information per product and per warehouse  
- Export inventory data and metadata to Microsoft Excel files  
- Metadata queries including table, primary key, and foreign key information  
- Real-time warnings when warehouse stock exceeds configured thresholds  
- Designed with Model-View-Controller (MVC) architecture for modularity  
- Secure handling of database credentials and protection against SQL injection  

## Architecture

The system follows the **Model-View-Controller (MVC)** design pattern, ensuring a clear separation between the user interface, business logic, and data access layers. The backend database is normalized to Third Normal Form (3NF) and hosted on a secure SQL Server instance with restricted permissions to enhance security.

## Security

- SQL Injection prevention via parameterized queries  
- Use of limited-privilege SQL Server login accounts  
- Secure storage of database connection credentials outside of source code  

## Development

- Programming Language: Java  
- Database: SQL Server  
- Libraries: Standard Java libraries and JavaFX for UI (no third-party libraries)  
- Version Control: GitHub  
