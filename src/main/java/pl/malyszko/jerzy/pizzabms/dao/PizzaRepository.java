package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.Pizza;

@Repository
public interface PizzaRepository
		extends CrudRepository<Pizza, Long> {

}
