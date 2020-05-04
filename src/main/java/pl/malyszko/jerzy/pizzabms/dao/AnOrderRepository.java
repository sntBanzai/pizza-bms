package pl.malyszko.jerzy.pizzabms.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.AnOrder;

@Repository
public interface AnOrderRepository extends CrudRepository<AnOrder, Long> {
	
	Optional<AnOrder> findByCompletedFalse();
	

}
