// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bubobubo.web;

import com.bubobubo.domain.Roles;
import com.bubobubo.domain.Users;
import com.bubobubo.web.ApplicationConversionServiceFactoryBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Roles, String> ApplicationConversionServiceFactoryBean.getRolesToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.bubobubo.domain.Roles, java.lang.String>() {
            public String convert(Roles roles) {
                return new StringBuilder().append(roles.getNameDa()).toString();
            }
        };
    }
    
    public Converter<Long, Roles> ApplicationConversionServiceFactoryBean.getIdToRolesConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.bubobubo.domain.Roles>() {
            public com.bubobubo.domain.Roles convert(java.lang.Long id) {
                return Roles.findRoles(id);
            }
        };
    }
    
    public Converter<String, Roles> ApplicationConversionServiceFactoryBean.getStringToRolesConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.bubobubo.domain.Roles>() {
            public com.bubobubo.domain.Roles convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Roles.class);
            }
        };
    }
    
    public Converter<Users, String> ApplicationConversionServiceFactoryBean.getUsersToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.bubobubo.domain.Users, java.lang.String>() {
            public String convert(Users users) {
                return new StringBuilder().append(users.getUsernameDa()).append(" ").append(users.getPasswordDa()).toString();
            }
        };
    }
    
    public Converter<Long, Users> ApplicationConversionServiceFactoryBean.getIdToUsersConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.bubobubo.domain.Users>() {
            public com.bubobubo.domain.Users convert(java.lang.Long id) {
                return Users.findUsers(id);
            }
        };
    }
    
    public Converter<String, Users> ApplicationConversionServiceFactoryBean.getStringToUsersConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.bubobubo.domain.Users>() {
            public com.bubobubo.domain.Users convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Users.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getRolesToStringConverter());
        registry.addConverter(getIdToRolesConverter());
        registry.addConverter(getStringToRolesConverter());
        registry.addConverter(getUsersToStringConverter());
        registry.addConverter(getIdToUsersConverter());
        registry.addConverter(getStringToUsersConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
