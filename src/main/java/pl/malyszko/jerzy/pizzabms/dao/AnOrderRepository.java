package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.AnOrder;

@Repository
public interface AnOrderRepository extends CrudRepository<AnOrder, Long> {

}
