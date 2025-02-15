package com.example.travelbooking.controller.partner.api;

import com.example.travelbooking.entity.Room;
import com.example.travelbooking.exception.NotFoundException;
import com.example.travelbooking.model.request.partner.CreateRoomRequest;
import com.example.travelbooking.model.request.partner.RoomSearchRequest;
import com.example.travelbooking.model.request.partner.UpdateRoomRequest;
import com.example.travelbooking.model.response.partner.RoomSearchResponse;
import com.example.travelbooking.service.partner.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/partner/rooms")
@AllArgsConstructor
public class RoomController {

    RoomService roomService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> createRoom(@ModelAttribute CreateRoomRequest request) {
        Room room = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @PutMapping(value = "/{roomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId, @ModelAttribute UpdateRoomRequest request) {
        Room room = roomService.updateRoom(roomId, request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        try {
            // Gọi phương thức xóa phòng từ service
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok().build(); // Trả về HTTP status code 200 OK nếu xóa thành công
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build(); // Trả về HTTP status code 404 Not Found nếu không tìm thấy phòng cần xóa
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete room"); // Trả về HTTP status code 500 Internal Server Error nếu xảy ra lỗi trong quá trình xóa
        }
    }

}
