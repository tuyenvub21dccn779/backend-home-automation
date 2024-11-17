package com.homeautomation.api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewAPI {
	
	@GetMapping("/test")
	public String testApi() {
		return "success";
	}
}
