package com.bubobubo.service;

/**
 * Created by IntelliJ IDEA.
 * User: charlieivie
 * Date: 01/04/2012
 * Time: 20:41
 * To change this template use File | Settings | File Templates.
 */
public interface SpringSecurityService {
    
    boolean isAuthenticated(String user, String password);
    
}
