/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coursework.bean;

import com.coursework.dao.PrivilegeDAO;
import com.coursework.model.Privilege;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author admin
 */
@ManagedBean
@SessionScoped
public class PrivilegeBean implements Serializable {

    private int idPriv;
    private String namePriv;

    /**
     * Удалить привилегию
     * @throws Exception
     */
    public void deletePrivilege() throws Exception {
        PrivilegeDAO.deletePrivilege(idPriv);
    }

    /**
     * Добавить привилегию
     */
    public void addPrivilege() {
        Privilege p = new Privilege(namePriv);
        try {
            if (PrivilegeDAO.addPrivilege(p)) {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage("nameInput", new FacesMessage(FacesMessage.SEVERITY_WARN, "Такое имя используется", null));
            }
        } catch (Exception ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_WARN, "Ошибка соединения", null));
            Logger.getLogger(PrivilegeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Получить список привилегий
     * @return список привилегий
     */
    public List<Privilege> getListPrivilege() {
        List<Privilege> res = new ArrayList<Privilege>();
        try {
           res= PrivilegeDAO.getAllPrivilege();
        } catch (Exception ex) {
            Logger.getLogger(PrivilegeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  res;
    }

    /**
     * Получить ID привилегии
     *
     * @return ID привилегии
     */
    public int getIdPriv() {
        return idPriv;
    }

    /**
     * Установить ID привилегии
     *
     * @param idPriv новый ID привилегии
     */
    public void setIdPriv(int idPriv) {
        this.idPriv = idPriv;
    }

    /**
     * Получить имя привилегии
     *
     * @return имя привилегии
     */
    public String getName_priv() {
        return namePriv;
    }

    /**
     * Установить имя привилегии
     *
     * @param namePriv новое имя привилегии
     */
    public void setName_priv(String namePriv) {
        this.namePriv = namePriv;
    }

    /**
     * Пустой конструктор
     */
    public PrivilegeBean() {
    }
}
