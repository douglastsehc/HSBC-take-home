package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hsbc.entitiy.Booking;
import org.hsbc.service.BookingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingServiceTest {

    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingService = new BookingService();
    }

    @Test
    public void testIsCorrectTimeFormat_ValidTime() {
        Assert.assertTrue(bookingService.isCorrectTimeFormat("12:30"));
    }

    @Test
    public void testIsCorrectTimeFormat_InvalidTime() {
        Assert.assertFalse(bookingService.isCorrectTimeFormat("25:00"));
    }

    @Test
    public void testIsCorrectTimeFormat_NullOrEmpty() {
        Assert.assertFalse(bookingService.isCorrectTimeFormat(null));
        Assert.assertFalse(bookingService.isCorrectTimeFormat(""));
    }

    @Test
    public void testIsCorrectDateFormat_ValidDate() {
        Assert.assertTrue(bookingService.isCorrectDateFormat("2024-02-14"));
    }

    @Test
    public void testIsCorrectDateFormat_InvalidDate() {
        Assert.assertFalse(bookingService.isCorrectDateFormat("2024-02-30"));
    }

    @Test
    public void testIsCorrectDateFormat_NullOrEmpty() {
        Assert.assertFalse(bookingService.isCorrectDateFormat(null));
        Assert.assertFalse(bookingService.isCorrectDateFormat(""));
    }

    @Test
    public void testAddNewBooking() {
        Map<String, List<Booking>> bookingListMap = new HashMap<>();
        Booking newBooking = new Booking("John Doe", 4, "2024-02-14", "12:00");

        bookingService.addNewBooking(bookingListMap, newBooking);

        Assert.assertTrue(bookingListMap.containsKey("2024-02-14"));
        Assert.assertEquals(1, bookingListMap.get("2024-02-14").size());
        Assert.assertEquals(newBooking, bookingListMap.get("2024-02-14").get(0));
        Assert.assertEquals(1, newBooking.getOrderNumber());
    }

    @Test
    public void testAddNewBooking2() {
        Map<String, List<Booking>> bookingListMap = new HashMap<>();
        Booking newBooking = new Booking("John Doe", 4, "2024-02-14", "12:00");
        Booking newBooking2 = new Booking("John Doe2", 100, "2024-02-14", "16:00");
        bookingService.addNewBooking(bookingListMap, newBooking);
        bookingService.addNewBooking(bookingListMap, newBooking2);
        Assert.assertTrue(bookingListMap.containsKey("2024-02-14"));
        Assert.assertEquals(2, bookingListMap.get("2024-02-14").size());
        Assert.assertEquals(newBooking, bookingListMap.get("2024-02-14").get(0));
        Assert.assertEquals(newBooking2, bookingListMap.get("2024-02-14").get(1));
        Assert.assertEquals(1, newBooking.getOrderNumber());
        Assert.assertEquals(2, newBooking2.getOrderNumber());
    }

    @Test
    public void testCheckIsHavingConflictWithOtherBooking_NoConflict() {
        Booking newBooking = new Booking("John Doe", 4, "2024-02-14", "12:00");
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(new Booking("Alice Smith", 2, "2024-02-14", "10:00"));
        bookingList.add(new Booking("Bob Johnson", 6, "2024-02-14", "14:00"));
        Assert.assertTrue(bookingService.checkIsHavingConflictWithOtherBooking(bookingList, newBooking));
    }

    @Test
    public void testCheckIsHavingConflictWithOtherBooking_Conflict() {
        Booking newBooking = new Booking("John Doe", 4, "2024-02-14", "12:00");
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(new Booking("Alice Smith", 2, "2024-02-14", "11:00"));
        bookingList.add(new Booking("Bob Johnson", 6, "2024-02-14", "13:00"));

        Assert.assertFalse(bookingService.checkIsHavingConflictWithOtherBooking(bookingList, newBooking));
    }

    @Test
    public void testValidateJsonNode_InvalidCustomerName() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a new empty JsonNode
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Add values to the JsonNode
        jsonNode.put("tableSize", 123);
        jsonNode.put("date", "2024-02-14");
        jsonNode.put("time", "21:59");
        jsonNode.put("customerName", "");
        Map<String, String> errors = bookingService.validateJsonNode(jsonNode);

        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsKey("customerName"));
        Assert.assertEquals("Customer Name is invalid", errors.get("customerName"));
    }

    @Test
    public void testValidateJsonNode_InvalidTableSize() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a new empty JsonNode
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Add values to the JsonNode
        jsonNode.put("customerName", "value1");
        jsonNode.put("date", "2024-02-14");
        jsonNode.put("time", "21:59");
        jsonNode.put("tableSize", "");
        Map<String, String> errors = bookingService.validateJsonNode(jsonNode);

        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsKey("tableSize"));
        Assert.assertEquals("Table Size is invalid", errors.get("tableSize"));
    }

    @Test
    public void testValidateJsonNode_InvalidDate() {

        ObjectMapper objectMapper = new ObjectMapper();

        // Create a new empty JsonNode
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Add values to the JsonNode
        jsonNode.put("customerName", "value1");
        jsonNode.put("tableSize", 123);
        jsonNode.put("time", "21:59");
        jsonNode.put("date", "");
        Map<String, String> errors = bookingService.validateJsonNode(jsonNode);

        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsKey("date"));
        Assert.assertEquals("Date is invalid", errors.get("date"));
    }

    @Test
    public void testValidateJsonNode_InvalidTime() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a new empty JsonNode
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Add values to the JsonNode
        jsonNode.put("customerName", "value1");
        jsonNode.put("tableSize", 123);
        jsonNode.put("date", "2024-02-14");
        jsonNode.put("time", "");

        Map<String, String> errors = bookingService.validateJsonNode(jsonNode);

        Assert.assertEquals(1, errors.size());
        Assert.assertTrue(errors.containsKey("time"));
        Assert.assertEquals("Time is invalid", errors.get("time"));
    }

    @Test
    public void testConvertJsonNodeToBooking() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a new empty JsonNode
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // Add values to the JsonNode
        jsonNode.put("customerName", "value1");
        jsonNode.put("tableSize", 123);
        jsonNode.put("date", "2024-02-14");
        jsonNode.put("time", "23:59");

        Booking booking = bookingService.convertJsonNodeToBooking(jsonNode);

        Assert.assertNotNull(booking);
        Assert.assertEquals(jsonNode.get("customerName").asText(), booking.getCustomerName());
        Assert.assertEquals(jsonNode.get("tableSize").asInt(), booking.getTableSize());
        Assert.assertEquals(jsonNode.get("date").asText(), booking.getDate());
        Assert.assertEquals(jsonNode.get("time").asText(), booking.getTime());
    }
}
