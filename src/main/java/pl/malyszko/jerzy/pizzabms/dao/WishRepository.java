package pl.malyszko.jerzy.pizzabms.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Wish;

@Repository
public interface WishRepository extends CrudRepository<Wish, Long> {

	List<Wish> findByNick(String nick);

	Wish findByNickAndOrder(String nick, AnOrder order);
	
}
