# Food delivery management system

## About :thought_balloon:
The task was to design an order management system for processing client orders for a warehouse for a catering company. Three types
of users are supported: admin (who edits products), employee (is notified of the products) and client (orders products). 
The project explores many different language features, detailed in the [implementation](#notable-implementation-details) section below.

## Features :white_check_mark:
The food delivery management system has the following features:
 - Allows the registration of the user with login and password
 - Allows user to filter the products by criteria and search using keywords
 - Allows the admin to add, edit and delete products and add composite products (menu items formed of multiple products)
 - The admin can generate different types of reports:
      - Orders betweeen given hours, minutes and seconds.
      - Products that have been ordered a minimum number of times
      - Clients that have ordered a minimum number of times
      - Orders on a specific date
  - The employee is notified of any new orders
  - All the data is saved upon application exit

## Implementation and GUI :computer:

### Notable implementation details
- **Composite Design Pattern** (General Heirarchy): the menu is structured in a way that supports the composite design pattern. Basic menu items are leaf nodes and complex menu items are formed of multiple basic items, their attributes being calculated recursively.
- **Singleton Design Pattern**: the food delivery service makes use of the singleton design pattern in order to keep data integrity.
- **Observer Design Pattern**: The employee interface is notified of every new order using the observer design pattern, with the singleton delivery service being the observable class.
- **Design by Contract**: preconditions, postconditions and invariants are defined in the IDeliveryServiceInterface which are then checked by the implementation.
- **Serialization**: application data is serialized into a file for safekeeping, allowing for easy storage and retrieval.
- **Streams and lmabda functions**: Functional programming elements like streams and lambda functions are used for processing data for the reports and populating the data from the CSV.

### Main menu
<img src="/ss/main_menu.PNG" width="700" >

### Admin View
<img src="/ss/admin.PNG" width="700" >

### Client Login
<img src="/ss/client_login.PNG" width="700" >

### Client View
<img src="/ss/client_view.PNG" width="700" >

### Employee View
<img src="/ss/employee.PNG" width="700" >

### Report 1 View 
<img src="/ss/report1.PNG" width="700" >

### Report 2 View 
<img src="/ss/report2.PNG" width="700" >

### Report 3 View 
<img src="/ss/report3.PNG" width="700" >

### Report 4 View 
<img src="/ss/report4.PNG" width="700" >
