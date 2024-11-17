package com.homeautomation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.FlameSensorDAO;
import com.homeautomation.dao.LightSensorDAO;
import com.homeautomation.model.FlameSensor;
import com.homeautomation.model.LightSensor;

@RestController
@RequestMapping("/lightsensor")
public class LightSensorAPI {
	@GetMapping("/{id}")
	public LightSensor getLightSensor(@PathVariable Integer id) {
		LightSensorDAO lightSensorDAO = new LightSensorDAO();
		return lightSensorDAO.getLightSensorById(id);
	}
}
