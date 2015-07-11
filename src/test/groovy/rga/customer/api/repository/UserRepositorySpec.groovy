package rga.customer.api.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.security.authentication.encoding.ShaPasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration
import rga.customer.api.App
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = App)
class UserRepositorySpec extends Specification {

    @Shared
    ShaPasswordEncoder encoder = new ShaPasswordEncoder(256)

    @Autowired
    UserRepository userRepository

    def "existent customer should be found"() {
        expect:
        def encoded = encoder.encodePassword(password, salt)
        print(encoded)
        assert userRepository.findByUserNameAndPassword(user, encoded).orElse(null)

        where:
        user     | password | salt
        'user'   | 'user'   | 7237291199533735258
        'admin'  | 'admin'  | 481486397342615386
    }

}