package rga.customer.api.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Customer domain object, the major resource of the RESTful service.
 *
 * @author ccw
 */
@Entity
@Table(
    name = "customer"
)
@Data
@RequiredArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private final String firstName;

    @Column(name = "last_name")
    private final String lastName;

    Customer() {
        this.firstName = null;
        this.lastName = null;
    }
}