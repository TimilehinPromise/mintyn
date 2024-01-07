# **Mintyn Card Verification Service**

### Overview

This application provides a card verification service using a variety of design principles and patterns for clean and efficient software development. It includes features like card scheme verification and statistics gathering.

### **Key Features**

* Card Scheme Verification: Determine the details of a card, such as its validity, scheme (VISA, MasterCard, etc.), and issuing bank.
* Statistics Gathering: Track and display the number of verifications made for each card.

### **Technologies**

* Spring Boot: For creating the web application.
* WebClient: For reactive web requests.
* Caffeine: For efficient in-memory caching.
* PostgreSQL: As the database system.
* Docker: For containerizing and managing the application environment.
* JUnit 5: For unit testing the application.
* Mockito: For mocking objects in tests.


### **Running the Application**

#### Prerequisites

* Docker
* Docker Compose


## Design Principles and Patterns in Our Application
1. Strategy Pattern

   What It Does: Allows us to choose between different algorithms or processes.

   Our Use Case: We use different strategies (like BinListProvider) for processing card information, easily switchable based on our needs.


2. Factory Pattern

   What It Does: Helps in creating objects without specifying the exact class to be created.

   Our Use Case: CardProcessorFactory determines which card processor to use, simplifying object creation in our system.


3. Dependency Injection

   What It Does: Manages how objects obtain their dependencies.
   
   Our Use Case: We use Spring to inject dependencies like WebClient, ensuring flexibility and easing testing.


4. Template Method Pattern

   What It Does: Outlines an algorithm's structure but lets subclasses modify specific steps.

   Our Use Case: If our AbstractCardProcessor defines a general card processing workflow, allowing subclasses to tailor specific parts.


5. Reactive Programming

   What It Is: A programming style based on data flows and change propagation.

   Our Use Case: We utilize reactive types like Mono for building responsive and non-blocking components.


6. Single Responsibility Principle

   What It Implies: Each class should have one purpose, one reason to change.

   Our Use Case: Classes like BinListProvider and CardServiceImpl are focused on a single functionality, making our code more coherent and easier to maintain.


7. Open/Closed Principle

   What It Advocates: Code entities should be open for extension but closed for modification.

   Our Use Case: Our use of interfaces and abstraction allows for adding new features without altering existing, stable code.



## Steps to Run

#### Clone the Repository: 

git clone https://github.com/TimilehinPromise/mintyn.git


#### Navigate to Project Directory:
cd mintyn


##### Start the Application: Use Docker Compose to start the application along with the PostgreSQL database.
docker-compose up


The service will be available on http://localhost:9060.

##### Accessing the Application:

https://documenter.getpostman.com/view/15243512/2s9YsJAryf


Use the postman collection


##### Stopping the Application

To stop the application, run the following command:

docker-compose down
