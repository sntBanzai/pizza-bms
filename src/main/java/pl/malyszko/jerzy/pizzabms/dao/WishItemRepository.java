package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.WishItem;

@Repository
public interface WishItemRepository extends CrudRepository<WishItem, Long> {

}
