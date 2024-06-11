package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.dao.Identite.TypeIdentite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private OracleIdentiteDao dao;

    @Before
    public void setup() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
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
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);

        dao.insert(identite, "Doer", new Calendar.Builder().setYear(2000).setMonth(1).setDayOfMonth(1).build().getTime());

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(foundIdentite);
        assertEquals("Doe", foundIdentite.getNom());
        assertEquals("John", foundIdentite.getPrenom());
        assertEquals("Doer", identite.getNomForm());
        assertEquals(new Date(2000, 1, 1), foundIdentite.getDateLimite());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        em.persist(identite);

        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(identite.getId());
        updatedIdentite.setEmail("john.doe@example.com");
        updatedIdentite.setNom("John Doe");

        dao.update(updatedIdentite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(foundIdentite);
        assertEquals("John Doe", foundIdentite.getNom());
        assertEquals("john.doe@example.com", foundIdentite.getEmail());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        em.persist(identite);

        dao.delete(identite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNull(foundIdentite);
    }

    @Test
    public void testFindIdentite() {
        Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setSex("M");
        identite1.setType(TypeIdentite.CANDIDAT);
        em.persist(identite1);

        Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex("M");
        identite2.setType(TypeIdentite.CANDIDAT);
        em.persist(identite2);

        List<Identite> result = dao.find(null, null, null, null, null);
        assertEquals(2, result.size());

        List<Identite> resultFiltered = dao.find(null, "John", null, null, null);
        assertEquals(2, resultFiltered.size());

        List<Identite> resultFilteredEmail = dao.find("Doe1", null, null, "john1@example.com", null);
        assertEquals(1, resultFilteredEmail.size());
        assertEquals("Doe1", resultFilteredEmail.get(0).getNom());
        assertEquals("john1@example.com", resultFilteredEmail.get(0).getEmail());
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setSex("M");
        identite1.setType(TypeIdentite.CANDIDAT);
        em.persist(identite1);

        Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex("M");
        identite2.setType(TypeIdentite.FORMATEUR);
        em.persist(identite2);

        Candidat[] result = dao.findCandidat(null, null, null, null);
        assertEquals(1, result.length);
        assertEquals("Doe1", result[0].getNom());

        Candidat[] resultFilteredNom = dao.findCandidat("Doe", null, null, null);
        assertEquals(1, resultFilteredNom.length);
        assertEquals("Doe1", resultFilteredNom[0].getNom());

        Candidat[] resultFilteredPrenom = dao.findCandidat(null, "John", null, null);
        assertEquals(1, resultFilteredPrenom.length);
        assertEquals("Doe1", resultFilteredPrenom[0].getNom());
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setSex("M");
        identite1.setType(TypeIdentite.FORMATEUR);
        em.persist(identite1);

        Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex("M");
        identite2.setType(TypeIdentite.CANDIDAT);
        em.persist(identite2);

        RespFormation[] result = dao.findRespForm(null, null, null, null);
        assertEquals(1, result.length);
        assertEquals("Doe1", result[0].getNom());

        RespFormation[] resultFilteredNom = dao.findRespForm("Doe", null, null, null);
        assertEquals(1, resultFilteredNom.length);
        assertEquals("Doe1", resultFilteredNom[0].getNom());

        RespFormation[] resultFilteredSex = dao.findRespForm(null, null, "M", null);
        assertEquals(1, resultFilteredSex.length);
        assertEquals("Doe1", resultFilteredSex[0].getNom());
    }
}