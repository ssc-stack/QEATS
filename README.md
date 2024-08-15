
# QEats: Food Ordering App Backend

**QEats** is a popular food ordering app that allows users to browse and order their favorite dishes from nearby restaurants. This project involves the development and optimization of the backend, which is a Spring Boot application.

## Navigation

- [Overview](#overview)
- [Retrieve Restaurant Data](#retrieve-restaurant-data-for-a-given-user-location)
- [Resolve Production Issues](#resolve-production-issues-using-scientific-debugging)
- [Replicate Performance Issues and Solve Them](#replicate-performance-issues-and-solve-them-using-caching-strategies)
- [Perform Search Operations](#perform-search-operations-using-custom-attributes)

## Overview

During the course of this project:

- Built different parts of the QEats backend as a Spring Boot application.
- Implemented several REST API endpoints to query restaurant information and place food orders.
- Investigated production issues using Scientific Debugging methods.
- Improved app performance under large load scenarios and added an advanced search feature.

<img src="https://example.com/path/to/your/image.png" alt="QEats Overview" />

## Retrieve Restaurant Data for a Given User Location

### Scope of Work

- Implemented `GET /API/v1/restaurants` and the corresponding request handler and response methods.
- Used Mockito to enable the development of the relevant MVCS layers independently.
- Retrieved a list of restaurants from MongoDB based on a user location.

### Skills Used

- Spring Boot
- Spring Data
- REST API
- Jackson
- Mockito
- JUnit
- MongoDB

<img src="https://example.com/path/to/your/image.png" alt="Retrieve Restaurant Data" />

## Resolve Production Issues Using Scientific Debugging

### Scope of Work

- Debugged QEats app crashes from the backend leveraging log messages and structured debugging techniques.
- Utilized IDE features (breakpoints) and assert statements to identify the root cause.

### Skills Used

- Scientific Debugging

<img src="https://example.com/path/to/your/image.png" alt="Scientific Debugging" />

## Replicate Performance Issues and Solve Them Using Caching Strategies

### Scope of Work

- Employed JMeter or load testing to expose performance issues.
- Identified DB queries contributing to performance degradation.
- Used Redis cache to alleviate read performance issues.

### Skills Used

- Redis
- JMeter

<img src="https://example.com/path/to/your/image.png" alt="Caching Strategies" />

## Perform Search Operations Using Custom Attributes

### Scope of Work

- Used MongoDB queries to enable users to search for restaurants using attributes like name, cuisine, dish, and price.
- Implemented multithreading to increase the number of concurrent searches.

### Skills Used

- MongoDB Querying
- Multithreading

<img src="https://example.com/path/to/your/image.png" alt="Search Operations" />

## Installation and Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/qeats-backend.git
