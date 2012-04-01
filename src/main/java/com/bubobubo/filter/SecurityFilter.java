package com.bubobubo.filter;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class SecurityFilter extends ClientFilter {

    private static final Logger logger = Logger.getLogger(SecurityFilter.class);
    private String username;
    private String password;

    public SecurityFilter() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public ClientResponse handle(ClientRequest request) throws ClientHandlerException {

        logger.debug("the request...:"+request);

        logger.debug("inside handle() method, username=" + username +
                ", password=" + password);

        return getNext().handle(request);
    }
}