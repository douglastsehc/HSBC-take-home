package org.hsbc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import org.hsbc.entitiy.Booking;
import org.hsbc.service.BookingService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/booking")
public class BookingApi {

    BookingService bookingService = new BookingService();
    Map<String, List<Booking>> bookingListMap = new ConcurrentHashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingByDate(@PathParam("date") String date, @HeaderParam("restaurant") String owner) throws JsonProcessingException {
        if (StringUtil.isNullOrEmpty(owner) || !owner.equals("owner")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("You are not authorize").build();
        }
        if (!bookingService.isCorrectDateFormat(date)) {
            if (date == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Date cannot be empty").build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong date format: " + date).build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        if (bookingListMap.get(date) != null) {
            return Response.ok(objectMapper.writeValueAsString(bookingListMap.get(date))).build();
        }
        return Response.ok(objectMapper.writeValueAsString(List.of())).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(String requestBody) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        Map<String, String> errorList = bookingService.validateJsonNode(jsonNode);
        if(!errorList.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).entity(objectMapper.writeValueAsString(errorList)).type(MediaType.APPLICATION_JSON).build();
        }
        Booking booking = bookingService.convertJsonNodeToBooking(jsonNode);
        if (bookingListMap.get(booking.getDate()) != null && !bookingService.checkIsHavingConflictWithOtherBooking(bookingListMap.get(booking.getDate()), booking)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("The booking time is having conflict with other bookings").build();
        }
        bookingService.addNewBooking(bookingListMap, booking);
        return Response.accepted(objectMapper.writeValueAsString(booking)).type(MediaType.APPLICATION_JSON).build();
    }
}
