package rga.customer.api.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import rga.customer.api.App
import rga.customer.api.domain.Customer
import spock.lang.FailsWith
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = App)
class CustomerRepositorySpec extends Specification {

    @Autowired
    CustomerRepository customerRepository

    def auth() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken('user', 'user',
                        AuthorityUtils.createAuthorityList('ROLE_USER')));
    }

    def "existent customer should be found"() {
        setup:
        auth()
        Customer customer = customerRepository.save(new Customer(first, last))

        expect:
        assert customerRepository.findOne(customer.getId()) == customer

        cleanup:
        customerRepository.delete(customer)

        where:
        first    | last
        'Jotaro' | 'Kujo'
        'Joseph' | 'Joestar'
    }

    @FailsWith(AuthenticationCredentialsNotFoundException)
    def "unauth behavior should fail"() {
        setup:
        SecurityContextHolder.clearContext();

        expect:
        customerRepository.save(new Customer(first, last))

        where:
        first    | last
        'Jotaro' | 'Kujo'
        'Joseph' | 'Joestar'
    }


}