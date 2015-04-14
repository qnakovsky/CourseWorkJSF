/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.coursework.dao;

import com.coursework.model.Application;
import com.coursework.model.Command;
import com.coursework.model.Permission;
import com.coursework.model.Privilege;
import com.coursework.model.Role;
import com.coursework.model.User;
import com.coursework.util.UtilHibernate;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Kunakovsky A.
 */
public class PrivilegeDAO {
    public static boolean addPrivilege(Privilege r)
    {
        EntityManager entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (entityManager.find(Privilege.class, r.getNamePriv()) != null) {
                return false;
            }
            entityManager.persist(r);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction() != null) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
        return true;
    }
    public static List<Privilege> getAllPrivilege()
    {
       EntityManager  entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
       entityManager.getTransaction().begin();
       Query q = entityManager.createQuery("SELECT u FROM Privilege u");
       List<Privilege> userList = (List<Privilege>)q.getResultList();       
       entityManager.getTransaction().commit();
       entityManager.close();
       return userList;
    }
    public static Privilege getPrivilegeById(int id_priv)
    {
       EntityManager  entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
       entityManager.getTransaction().begin();
       Privilege res = entityManager.find(Privilege.class, id_priv);
       entityManager.getTransaction().commit();
       entityManager.close();
       return res;
    }
    public static void deletePrivilege(int id_priv) throws Exception
    {
       EntityManager entityManager = UtilHibernate.getEntityManagerFactory().createEntityManager();
        try {        
            entityManager.getTransaction().begin();      
            Privilege privilege = entityManager.find(Privilege.class, id_priv);
            for (Application app : privilege.getApps()) {
                for (Permission perm : app.getPermissions()) {
                    for (Role role : perm.getRoles()) {
                        for (User user : role.getUsers()) {
                            Command c = new Command(user.getIdUser(), perm.getApplication().getIdApp(),
                                    perm.getPrivelege().getIdPriv(), "del", new Date());
                            entityManager.persist(c);
                        }
                    }
                }
                app.getPrivs().remove(privilege);
            }
            entityManager.remove(privilege);
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