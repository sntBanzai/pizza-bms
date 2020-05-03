package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;

import pl.malyszko.jerzy.pizzabms.entity.Pizza;

public interface OrderCompletionRepository
		extends CrudRepository<Pizza, Long> {

}
