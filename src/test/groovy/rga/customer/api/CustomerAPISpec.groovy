package rga.customer.api

import groovy.json.JsonSlurper
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import rga.customer.api.domain.Customer
import spock.lang.FailsWith
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@WebIntegrationTest
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = App)
@Stepwise
class CustomerAPISpec extends Specification {

    @Shared
    RestTemplate restTemplate

    @Shared
    HttpHeaders httpHeaders

    def setupSpec() {
        httpHeaders = new HttpHeaders()
        def encoded = new String(Base64.encoder.encode('user:user'.getBytes()))
        print(encoded)
        httpHeaders.set('Authorization', "Basic ${encoded}")

        restTemplate = new RestTemplate()
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            void handleError(ClientHttpResponse response) throws IOException {
                // to swallow the exception in order to verify status code
            }
        })
    }

    @FailsWith(Exception)
    def "unauth behavior should be rejected"() {
        expect:
        restTemplate.postForEntity(url, new Customer('test', 'test'), Customer, Collections.emptyMap())

        where:
        url = 'http://localhost:8080/customer'
    }

    def "post should create new entry"() {
        expect:
        final HttpEntity request = new HttpEntity<Customer>(new Customer(firstName, lastName), httpHeaders)
        def respEntity = restTemplate.exchange(url, HttpMethod.POST, request, Customer)
        assert respEntity.statusCode == status

        where:
        firstName | lastName | status
        'Jotaro'  | 'Kujo'   | HttpStatus.CREATED
        'Mohammed'| 'Avdol'  | HttpStatus.CREATED
        null      | null     | HttpStatus.CONFLICT

        url = 'http://localhost:8080/customer'
    }

    def "put should update existent entity"() {
        when:
        final HttpEntity request = new HttpEntity<Customer>(new Customer(firstName, lastName), httpHeaders)
        def respEntity = restTemplate.exchange(url + "/${id}", HttpMethod.PUT, request, Customer)
        assert respEntity.statusCode == status

        and:
        respEntity = restTemplate.exchange(url + "/${id}", HttpMethod.GET,
                                           new HttpEntity<Customer>(httpHeaders),
                                           Customer, Collections.emptyMap())

        then:
        assert respEntity.body.firstName == firstName
        assert respEntity.body.lastName == lastName

        where:
        firstName | lastName  | id | status
        'Joseph'  | 'Joestar' | 1  | HttpStatus.NO_CONTENT

        url = 'http://localhost:8080/customer'
    }

    def "delete should remove existent entity"() {
        expect:
        assert restTemplate.exchange(url + "/${id}", HttpMethod.DELETE,
                                     new HttpEntity<Customer>(httpHeaders),
                                     Customer, Collections.emptyMap())?.statusCode == status

        assert restTemplate.exchange(url + "/${id}", HttpMethod.GET,
                                     new HttpEntity<Customer>(httpHeaders),
                                     Customer, Collections.emptyMap())?.statusCode == HttpStatus.NOT_FOUND

        where:
        id | status
        2  | HttpStatus.NO_CONTENT

        url = 'http://localhost:8080/customer'
    }

    def "list should have multiple returned"() {
        setup:
        10.times { idx ->
            def request = new HttpEntity<Customer>(new Customer("first_${idx}", "last_${idx}"), httpHeaders)
            def respEntity = restTemplate.exchange(url, HttpMethod.POST, request, Customer)
            assert respEntity.statusCode == HttpStatus.CREATED
        }

        expect:
        def response = restTemplate.exchange(url + "/?page=0&size=10", HttpMethod.GET,
                                             new HttpEntity<String>(httpHeaders),
                                             String, Collections.emptyMap())

        assert response.statusCode == HttpStatus.OK
        assert new JsonSlurper().parseText(response.body).page.size == 10

        where:
        url = 'http://localhost:8080/customer'
    }

}