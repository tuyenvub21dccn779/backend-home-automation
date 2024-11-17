package com.homeautomation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.TemperatureSensorDAO;
import com.homeautomation.model.TemperatureSensor;

@RestController
@RequestMapping("/temperaturesensor")
public class TemperatureSensorAPI {
	@GetMapping("/{id}")
	public TemperatureSensor getTemperatureSensor(@PathVariable Integer id) {
		TemperatureSensorDAO temperatureSensorDAO = new TemperatureSensorDAO();
		return temperatureSensorDAO.getTemperatureSensorById(id);
	}
}
