package com.bubobubo.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Users {

    @NotNull
    @Size(min = 3, max = 30)
    private String usernameDa;

    @Size(max = 100)
    private String passwordDa;

    @NotNull
    private Boolean enabledDa;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Roles> roles = new HashSet<Roles>();
}
