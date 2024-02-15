# HSBC Java Dev Coding Quiz

## Prerequisite

- Java 21
- Maven 3.9.6
- Libraries in pom.xml

## Requirements

- Create a simple restaurant booking application in Java and implement the two user stories below.

- User Story 1: As a customer I want to be able to request a booking at this restaurant

- User Story 2: As a restaurant owner I want to be able to see all bookings for a particular day.

- Assume each booking has a customer name, table size, date and time. Assume time slots are for 2 hours.

- The only technical requirements are to use Java and to use https://muserver.io/ to implement a RESTful API.


## Assumption

- Try to finish the task within 1-2 hours
- No need to handle any server security
- only have 1 table in the restaurant
- the table size will never exceed the maximum of an integer amount and will greater than zero
- restaurant will only available from 06:00-22:00
- it is always using 24-hour format for the time
- every booking timeslot will be two hours only
- do not need to handle any multithreading problems


## Implementation
### Entities:

- Booking: Represents a booking made by a customer. It has properties such as customerName, tableSize, date, time, and orderNumber. An order number is set after adding the booking to the system.
### Services:

- BookingService: Provides functionalities to handle bookings. It includes methods for validating time and date formats, adding new bookings, checking for conflicts with existing bookings, validating JSON input, and converting JSON data to Booking objects.
### Controller:

- BookingApi: Defines RESTful endpoints for managing bookings. It includes methods for retrieving bookings for a specific date and creating new bookings. It uses the BookingService to perform booking-related operations.
### Server Configuration:

- Server: Configures and starts the server using MuServer. It defines a port number and initializes the BookingApi to handle incoming requests.
### Functionality:

- GET /booking/{date}: Retrieves bookings for a specific date. It validates the date format and owner authorization before returning the bookings.
- POST /booking: Creates a new booking based on the JSON request body. It validates the input JSON, checks for conflicts with existing bookings, adds the new booking, and returns the created booking or appropriate error messages.
### Dependencies:

- The application uses the MuServer framework for handling HTTP requests and responses.
It also uses Jackson for JSON serialization and deserialization.
Deployment:

- The server is configured to run on port 9090.
- To deploy the application, you can compile the code and run the Server class.

## Performance

### Time Complexity:
```isCorrectTimeFormat(String time):```
- This method parses the input time string using LocalTime.parse(time), which has a time complexity of O(n), where n is the length of the time string.
Thus, the time complexity of this method is O(n).

``` isCorrectDateFormat(String date):``` 
- Similar to isCorrectTimeFormat, this method parses the input date string using LocalDate.parse(date), which also has a time complexity of O(n), where n is the length of the date string.
Therefore, the time complexity of this method is O(n).

```addNewBooking(Map<String, List<Booking>> bookingListMap, Booking newBooking):```
- This method involves adding a new booking to the bookingListMap.
The time complexity of computeIfAbsent operation in Java's Map interface is O(1) on average, but it can be up to O(n) in the worst case, where n is the number of elements in the map.
Setting the order number (newBooking.setOrderNumber()) and adding the booking to the list also have a time complexity of O(1).
Therefore, the overall time complexity of this method is O(1) on average, but it can be up to O(n) in the worst case.

```checkIsHavingConflictWithOtherBooking(List<Booking> bookingList, Booking newBooking):```
- This method checks if there is a time conflict between the new booking and existing bookings in the list.
It iterates through the list of existing bookings, and for each booking, it parses the time strings using LocalTime.parse(booking.getTime()).
Parsing each time string has a time complexity of O(n), where n is the length of the time string.
Therefore, the overall time complexity of this method is O(m * n), where m is the number of existing bookings and n is the average length of the time strings.

```validateJsonNode(JsonNode jsonNode):```
- This method checks the validity of various fields in the JSON object (jsonNode).
Each field validation involves accessing the field from the JSON node, which has a time complexity of O(1).
Therefore, the overall time complexity of this method is O(1).


``` convertJsonNodeToBooking(JsonNode jsonNode):```
- This method creates a new Booking object using data from the JSON node.
Accessing each field from the JSON node and creating the Booking object have a time complexity of O(1).
Therefore, the overall time complexity of this method is O(1).

### Space Complexity:
```isCorrectTimeFormat(String time)``` and ```isCorrectDateFormat(String date):```

- These methods do not use additional space that grows with the input size. They only use constant space.
Therefore, the space complexity of these methods is O(1).


```addNewBooking(Map<String, List<Booking>> bookingListMap, Booking newBooking):```
- This method adds a new booking to the bookingListMap.
The space complexity depends on the size of the map and the number of bookings.
Therefore, the space complexity of this method is O(n), where n is the total number of bookings stored in the map.


```checkIsHavingConflictWithOtherBooking(List<Booking> bookingList, Booking newBooking):```
- This method does not use additional space that grows with the input size. It only uses constant space.
Therefore, the space complexity of this method is O(1).

```validateJsonNode(JsonNode jsonNode)``` and ```convertJsonNodeToBooking(JsonNode jsonNode):```
- These methods do not use additional space that grows with the input size. They only use constant space.
Therefore, the space complexity of these methods is O(1).


## Alternative Solution or Improvement

- handle if there is more tables
- add more extra server security
- handle async approach
- Should spend more time to study on the MuServer and follow their recommend approach

## Build, Run Tests

### To build the project use a build tool like Maven. Execute the following commands:

```
mvn clean install
```

### Test the testcase

```
mvn test
```

### Start it with intellij

can run the application in intellij and use postman to test