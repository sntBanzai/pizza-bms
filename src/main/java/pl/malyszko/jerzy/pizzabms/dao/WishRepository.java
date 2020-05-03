package pl.malyszko.jerzy.pizzabms.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import pl.malyszko.jerzy.pizzabms.entity.Wish;

@RepositoryRestResource(collectionResourceRel = "Wish", path = "wish")
public interface WishRepository extends PagingAndSortingRepository<Wish, Long> {

}
