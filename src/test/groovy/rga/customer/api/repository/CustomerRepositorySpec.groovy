package rga.customer.api.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import rga.customer.api.App
import rga.customer.api.domain.Customer
import rga.customer.api.listener.CustomerEventHandler
import rga.customer.api.security.AuthUser
import spock.lang.FailsWith
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = App)
class CustomerRepositorySpec extends Specification {

    @Autowired
    CustomerRepository customerRepository

    def auth() {
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken('user', 'user', AuthorityUtils.createAuthorityList('ROLE_USER'));
        token.details = new AuthUser(2, 'user', 'user', 1, token.authorities)
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    def "existent customer should be found"() {
        setup:
        auth()
        final Customer customer = new Customer(first, last)
        customer.setUserId(2)
        final Customer saved = customerRepository.save(customer)

        expect:
        assert customerRepository.findOne(customer.getId()) == saved

        cleanup:
        customerRepository.delete(saved)

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