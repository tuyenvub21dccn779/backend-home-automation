package com.homeautomation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.FanDAO;
import com.homeautomation.model.Fan;

@RestController
@RequestMapping("/fan")
public class FanAPI {
	@GetMapping("/{id}")
	public Fan getFan(@PathVariable Integer id) {
		FanDAO fanDAO = new FanDAO();
		return fanDAO.getFanById(id);
	}
	
	@PutMapping("")
	public void updateFan(@RequestBody Fan fan) {
		FanDAO fanDAO = new FanDAO();
		fanDAO.updateFan(fan);
	}
}
