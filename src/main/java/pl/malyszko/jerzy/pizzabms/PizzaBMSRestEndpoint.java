package pl.malyszko.jerzy.pizzabms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
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

	@PostMapping(path = "wish")
	public ResponseEntity<WishDTO> newWish(@RequestBody WishDTO aWish) {
		try {
			Wish wish = wishService.makeAWish(aWish);
			return ResponseEntity.status(HttpStatus.CREATED).body(aWish);
		} catch (Exception e) {
			return ResponseEntity.unprocessableEntity().build();
		}
	}

	@PutMapping(path = "wish")
	public ResponseEntity<WishDTO> changeWish(@RequestBody WishDTO aWish) {
		wishService.deleteExistingWish(aWish.getNick());
		Wish wish = wishService.makeAWish(aWish);
		return ResponseEntity.ok(aWish);
	}

	@GetMapping(path = "wish")
	public ResponseEntity<WishDTO> readWish(
			@RequestParam("eater") String eater) {
		WishDTO currWish = wishService.getCurrentWish(eater);
		if (currWish == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(currWish);
	}

	@DeleteMapping(path = "wish")
	public ResponseEntity<WishDTO> deleteWish(
			@RequestParam("eater") String eater) {
		WishDTO wishDTO = wishService.deleteExistingWish(eater);
		if (wishDTO == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(wishDTO);
	}

	@GetMapping(path = "order")
	public ResponseEntity<OrderDTO> getCurrentOrder() {
		AnOrder currentOrder = orderService.getCurrentOrder();
		if (currentOrder == null)
			return ResponseEntity.notFound().build();
		OrderDTO dto = OrderDTO.wrap(currentOrder);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(path = "orders")
	public ResponseEntity<List<OrderDTO>> getOrders() {
		return ResponseEntity.ok(orderService.listOrders());
	}

}
