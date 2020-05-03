package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.WishType;

@Repository
public interface WishTypeRepository extends CrudRepository<WishType, Long> {

	WishType findByName(String name);

}
