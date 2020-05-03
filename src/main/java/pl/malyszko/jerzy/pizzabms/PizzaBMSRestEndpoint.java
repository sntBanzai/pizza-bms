package pl.malyszko.jerzy.pizzabms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.service.OrderService;
import pl.malyszko.jerzy.pizzabms.service.WishService;

@RestController
public class PizzaBMSRestEndpoint {

	@Autowired
	private WishService wishService;

	@Autowired
	private OrderService orderService;

	@GetMapping("/beep")
	public String beep() {
		return "BEEEEP!";
	}

	@PostMapping(path = "doWish")
	public ResponseEntity<Wish> newWish(@RequestBody Wish aWish) {
		wishService.makeAWish(aWish);
		orderService.makeAnOrder(aWish);
		return  ResponseEntity.ok(aWish);
	}

}
