package pl.malyszko.jerzy.pizzabms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PizzaBMSRestEndpoint {

	@GetMapping("/beep")
	public String beep() {
		return "BEEEEP!";
	}

}
