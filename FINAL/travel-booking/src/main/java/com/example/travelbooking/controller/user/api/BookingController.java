package com.example.travelbooking.controller.user.api;

import com.example.travelbooking.entity.Booking;
import com.example.travelbooking.model.request.user.CreateBookingRequest;
import com.example.travelbooking.model.response.partner.RoomResponse;
import com.example.travelbooking.model.response.user.BookingResponse;
import com.example.travelbooking.service.partner.RoomService;
import com.example.travelbooking.service.user.BookingService;
import com.example.travelbooking.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BookingController {

    BookingService bookingService;
    RoomService roomService;
    UserService userService;

    @PostMapping("/bookings/create-booking")
    public ResponseEntity<?> creatBooking(@ModelAttribute CreateBookingRequest request) {

        if (!accountIsBlocked(request.getUserId())) {
            BookingResponse booking = bookingService.createBooking(request);
            return ResponseEntity.ok(booking);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account was blocked");
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBookingDetail(@PathVariable Long id) {
        BookingResponse bookingResponse = bookingService.getBookingById(id);

        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getRoomDetail(@RequestParam String roomId) {
        RoomResponse roomResponse = roomService.getRoomById(Long.parseLong(roomId));

        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }

    private boolean accountIsBlocked(Long userId) {
        return userService.accountIsBlocked(userId);
    }
}
