// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bubobubo.domain;

import com.bubobubo.domain.Roles;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Roles_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Roles.entityManager;
    
    public static final EntityManager Roles.entityManager() {
        EntityManager em = new Roles().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Roles.countRoleses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Roles o", Long.class).getSingleResult();
    }
    
    public static List<Roles> Roles.findAllRoleses() {
        return entityManager().createQuery("SELECT o FROM Roles o", Roles.class).getResultList();
    }
    
    public static Roles Roles.findRoles(Long id) {
        if (id == null) return null;
        return entityManager().find(Roles.class, id);
    }
    
    public static List<Roles> Roles.findRolesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Roles o", Roles.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Roles.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Roles.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Roles attached = Roles.findRoles(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Roles.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Roles.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Roles Roles.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Roles merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
