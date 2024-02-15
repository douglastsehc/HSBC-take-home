package org.hsbc.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.util.internal.StringUtil;
import org.hsbc.entitiy.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;


public class BookingService {

    public boolean isCorrectTimeFormat(String time) {
        if (StringUtil.isNullOrEmpty(time)) {
            return false;
        }
        try {
            LocalTime bookingTime = LocalTime.parse(time);
            if(bookingTime.isBefore(LocalTime.of(6,0)) || bookingTime.isAfter(LocalTime.of(22,0))){
                return false;
            }
        } catch (DateTimeParseException | NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isCorrectDateFormat(String date) {
        if (StringUtil.isNullOrEmpty(date)) {
            return false;
        }
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException | NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void addNewBooking(Map<String, List<Booking>> bookingListMap, Booking newBooking) {
        bookingListMap.computeIfAbsent(newBooking.getDate(), k -> new ArrayList<>()).add(newBooking);
        newBooking.setOrderNumber(bookingListMap.get(newBooking.getDate()).size());
    }

    public boolean checkIsHavingConflictWithOtherBooking(List<Booking> bookingList, Booking newBooking) {
        LocalTime newBookingTime = LocalTime.parse(newBooking.getTime());
        for (Booking booking : bookingList) {
            LocalTime bookingTime = LocalTime.parse(booking.getTime());
            if (bookingTime.equals(newBookingTime) || (bookingTime.isBefore(newBookingTime) && newBookingTime.isBefore(bookingTime.plusHours(2)))) {
                return false;
            }
        }
        return true;
    }

    public Map<String, String> validateJsonNode(JsonNode jsonNode) {
        Map<String, String> errorsList = new HashMap<>();

        if (jsonNode.get("customerName") == null || StringUtil.isNullOrEmpty(jsonNode.get("customerName").asText())) {
            errorsList.put("customerName", "Customer Name is invalid");
        }
        if (jsonNode.get("tableSize") == null || !jsonNode.get("tableSize").isInt() || jsonNode.get("tableSize").asInt() <= 0) {
            errorsList.put("tableSize", "Table Size is invalid");
        }
        if (jsonNode.get("date") == null || !isCorrectDateFormat(jsonNode.get("date").asText())) {
            errorsList.put("date", "Date is invalid");
        }
        if (jsonNode.get("time") == null || !isCorrectTimeFormat(jsonNode.get("time").asText())) {
            errorsList.put("time", "Time is invalid");
        }
        return errorsList;
    }

    public Booking convertJsonNodeToBooking(JsonNode jsonNode) {
        return new Booking(jsonNode.get("customerName").asText(),
                jsonNode.get("tableSize").asInt(), jsonNode.get("date").asText(), jsonNode.get("time").asText());
    }
}
