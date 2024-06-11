package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private IdentiteDao identiteDao;

    @Before
    public void setup() {
        identiteDao = OracleIdentiteDao.getDAO();
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        identiteDao.setEm(em);
    }

    @After
    public void teardown() {
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
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", new GregorianCalendar(2000, 0, 1).getTime());

        Identite fetchedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), fetchedIdentite.getNom());
        assertEquals(identite.getPrenom(), fetchedIdentite.getPrenom());
        assertEquals(identite.getSex(), fetchedIdentite.getSex());
        assertEquals(identite.getType(), fetchedIdentite.getType());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.update(identite);

        Identite fetchedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), fetchedIdentite.getNom());
        assertEquals(identite.getPrenom(), fetchedIdentite.getPrenom());
        assertEquals(identite.getSex(), fetchedIdentite.getSex());
        assertEquals(identite.getType(), fetchedIdentite.getType());

        identite.setNom("Smith");
        identite.setPrenom("Jane");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.update(identite);

        fetchedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), fetchedIdentite.getNom());
        assertEquals(identite.getPrenom(), fetchedIdentite.getPrenom());
        assertEquals(identite.getSex(), fetchedIdentite.getSex());
        assertEquals(identite.getType(), fetchedIdentite.getType());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1L);
        identiteDao.delete(identite);

        Identite fetchedIdentite = em.find(Identite.class, identite.getId());
        assertNull(fetchedIdentite);
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1L);
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite1, "nomForm", new GregorianCalendar(2000, 0, 1).getTime());

        Identite identite2 = new Identite();
        identite2.setId(2L);
        identite2.setNom("Smith");
        identite2.setPrenom("Jane");
        identite2.setSex("F");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite2, "nomForm", new GregorianCalendar(2001, 0, 1).getTime());

        List<Identite> result = identiteDao.find(null, null, null, null, null);
        assertEquals(2, result.size());

        List<Identite> resultCandidat = identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, resultCandidat.size());
        assertEquals(identite1.getId(), resultCandidat.get(0).getId());

        List<Identite> resultFormateur = identiteDao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, resultFormateur.size());
        assertEquals(identite2.getId(), resultFormateur.get(0).getId());
    }

    @Test
    public void testFindCandidat() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", new GregorianCalendar(2000, 0, 1).getTime());

        Candidat[] result = identiteDao.findCandidat("Doe", null, null, null);
        assertEquals(1, result.length);
        assertEquals(identite.getId(), result[0].getId());
    }

    @Test
    public void testFindRespForm() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite, "nomForm", new GregorianCalendar(2000, 0, 1).getTime());

        RespFormation[] result = identiteDao.findRespForm("Doe", null, null, null);
        assertEquals(1, result.length);
        assertEquals(identite.getId(), result[0].getId());
    }
}