package com.homeautomation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.BulbDAO;
import com.homeautomation.model.Bulb;

@RestController
@RequestMapping("/bulb")
public class BulbAPI {
	@GetMapping("/{id}")
	public Bulb getBulb(@PathVariable Integer id) {
		BulbDAO bulbDAO = new BulbDAO();
		return bulbDAO.getBulbById(id);
	}
	
	@PutMapping("")
	public void updateBulb(@RequestBody Bulb bulb) {
		BulbDAO bulbDAO = new BulbDAO();
		bulbDAO.updateBulb(bulb);
		
	}
}
