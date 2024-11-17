package com.homeautomation.api;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.RoomDAO;
import com.homeautomation.model.Room;

@RestController
@RequestMapping("/room")
public class RoomAPI {
	
	@GetMapping()
	public ArrayList<Room> getRooms() {
		RoomDAO roomDAO = new RoomDAO();
		return roomDAO.getAllRoom();
	}
	
	@GetMapping("/{id}")
	public Room getRoom(@PathVariable Integer id) {
		RoomDAO roomDAO = new RoomDAO();
		return roomDAO.getRoomById(id);
	}
}
