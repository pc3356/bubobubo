package com.bubobubo.service;

import javax.inject.Named;

@Named
public class SpringSecurityServiceImpl implements SpringSecurityService {
    @Override
    public boolean isAuthenticated(String user, String password) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
