package rga.customer.api.listener;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rga.customer.api.domain.Customer;
import rga.customer.api.security.AuthUser;

/**
 * Created by kchen on 10/29/15.
 */
@Component
@RepositoryEventHandler(Customer.class)
public class CustomerEventHandler {

    @HandleBeforeCreate
    @HandleBeforeSave
    public void handleCustomerSave(final Customer customer) {
        final AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        customer.setUserId(user.getUserId());
    }

}
