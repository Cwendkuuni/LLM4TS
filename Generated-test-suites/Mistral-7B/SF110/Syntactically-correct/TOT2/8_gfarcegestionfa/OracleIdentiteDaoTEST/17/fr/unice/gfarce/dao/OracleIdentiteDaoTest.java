package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao.Identite;
import fr.unice.gfarce.dao.OracleIdentiteDao.Identite.TypeIdentite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    private OracleIdentiteDao dao;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        dao = OracleIdentiteDao.getDAO();
    }

    @After
    public void close() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        dao.insert(identite, "nom", dateLimite);
        // Add assertions here
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("email");
        identite.setNom("nom");
        identite.setPrenom("prenom");
        identite.setSex("sex");
        identite.setType(TypeIdentite.CANDIDAT);
        dao.update(identite);
        // Add assertions here
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        dao.delete(identite);
        // Add assertions here
    }

    @Test
    public void testFindAll() {
        List<Identite> identites = dao.find(null, null, null, null, null);
        // Add assertions here
    }

    @Test
    public void testFindByNomAndPrenom() {
        List<Identite> identites = dao.find("nom", "prenom", null, null, null);
        // Add assertions here
    }

    @Test
    public void testFindByEmail() {
        List<Identite> identites = dao.find(null, null, null, "email", null);
        // Add assertions here
    }

    @Test
    public void testFindByType() {
        List<Identite> identites = dao.find(null, null, null, null, TypeIdentite.CANDIDAT);
        // Add assertions here
    }

    @Test
    public void testInsertWithNullIdentite() {
        dao.insert(null, "nom", dateLimite);
        // Add assertions here
    }

    @Test
    public void testInsertWithNullNomForm() {
        Identite identite = new Identite();
        dao.insert(identite, null, dateLimite);
        // Add assertions here
    }

    @Test
    public void testInsertWithNullDateLimite() {
        Identite identite = new Identite();
        dao.insert(identite, "nom", null);
        // Add assertions here
    }

    @Test
    public void testUpdateWithNullIdentite() {
        dao.update(null);
        // Add assertions here
    }

    @Test
    public void testUpdateWithNullIdentiteId() {
        Identite identite = new Identite();
        identite.setId(null);
        dao.update(identite);
        // Add assertions here
    }

    @Test
    public void testDeleteWithNullIdentite() {
        Identite identite = new Identite();
        dao.delete(identite);
        // Add assertions here
    }

    @Test
    public void testDeleteWithNullIdentiteId() {
        Identite identite = new Identite();
        identite.setId(null);
        dao.delete(identite);
        // Add assertions here
    }

    @Test
    public void testFindWithNullNom() {
        List<Identite> identites = dao.find(null, null, null, null, null);
        // Add assertions here
    }

    @Test
    public void testFindWithNullPrenom() {
        List<Identite> identites = dao.find(null, null, null, null, null);
        // Add assertions here
    }

    @Test
    public void testFindWithNullSex() {
        List<Identite> identites = dao.find(null, null, null, null, null);
        // Add assertions here
    }

    @Test
    public void testFindWithNullEmail() {
        List<Identite> identites = dao.find(null, null, null, null, null);
        // Add assertions here
    }

    @Test
    public void testFindWithNullType() {
        List<Identite> identites = dao.find(null, null, null, null, null);
        // Add assertions here
    }
}