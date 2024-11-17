package com.homeautomation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.FlameSensorDAO;
import com.homeautomation.model.FlameSensor;

@RestController
@RequestMapping("/flamesensor")
public class FlameSensorAPI {
	@GetMapping("/{id}")
	public FlameSensor getFlameSensor(@PathVariable Integer id) {
		FlameSensorDAO flameSensorDAO = new FlameSensorDAO();
		return flameSensorDAO.getFlameSensorById(id);
	}
}
