package com.bubobubo.service;

import javax.inject.Named;

@Named
public class SpringSecurityServiceImpl implements SpringSecurityService {
    @Override
    public boolean isAuthenticated(String user, String password) {
        return false;
    }
}
