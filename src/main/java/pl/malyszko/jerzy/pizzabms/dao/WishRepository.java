package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.Wish;

@Repository
public interface WishRepository extends CrudRepository<Wish, Long> {

}
