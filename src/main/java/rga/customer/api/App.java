package rga.customer.api;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Major entry point of the whole application.
 *
 * @author ccw
 */
@SpringBootApplication
public class App {

    /**
     * To expose the h2 web console for debugging
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/h2/*");
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}