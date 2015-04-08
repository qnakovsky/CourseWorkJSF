/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.curswork.dao;

import com.curswork.model.Command;
import com.curswork.model.Permission;
import com.curswork.model.Role;
import com.curswork.model.User;
import com.curswork.util.UtilHibernate;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Kunakovsky A.
 */
public class PermissionDAO {
    public static void  addPermission(Permission r)
    {        
      EntityManager entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
      entityManager.getTransaction().begin();
      entityManager.merge(r);            
      entityManager.getTransaction().commit();
      entityManager.close();
    }
    public static List<Permission> getAllPermission()
    {
       EntityManager  entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
       entityManager.getTransaction().begin();
       Query q = entityManager.createQuery("SELECT u FROM Permission u");
       List<Permission> userList = (List<Permission>)q.getResultList();       
       entityManager.getTransaction().commit();
       entityManager.close();
       return userList;
    }
    public static Permission getPermissionById(int id_perm)
    {
       EntityManager  entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
       entityManager.getTransaction().begin();
       Permission res = entityManager.find(Permission.class, id_perm);
       entityManager.getTransaction().commit();
       entityManager.close();
       return res;
    }
    public static void deletePermission(int id_perm) throws Exception
    {
        EntityManager entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
        try {        
            entityManager.getTransaction().begin();      
            Permission perm = entityManager.find(Permission.class, id_perm);
            for (Role role : perm.getRoles()) {
                for (User user : role.getUsers()) {
                    Command c = new Command(user.getIdUser(), perm.getApplication().getIdApp(),
                            perm.getPrivelege().getIdPriv(), "del", new Date());
                    entityManager.persist(c);
                } 
               role.getPermissions().remove(perm);
            }
            entityManager.remove(perm);
            entityManager.getTransaction().commit();      
        } catch (Exception e) {
            if(entityManager.getTransaction()!=null)
               entityManager.getTransaction().rollback();
            throw e;
        }
        finally
        {
              entityManager.close();
        }
    }

}
