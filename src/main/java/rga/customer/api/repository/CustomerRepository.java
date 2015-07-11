package rga.customer.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import rga.customer.api.domain.Customer;

/**
 * Basic CRUD manipulation of Customer domain object.
 * All the methods have been exposed as RESTful resource endpoints and restrict to 'ROLE_USER' ACL.
 *
 * @author ccw
 */
@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}