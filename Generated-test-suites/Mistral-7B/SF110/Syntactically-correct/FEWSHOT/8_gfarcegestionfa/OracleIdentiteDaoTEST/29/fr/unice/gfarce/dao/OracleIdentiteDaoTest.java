package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.dao.Identite.TypeIdentite;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private OracleIdentiteDao identiteDao;
    private Identite identite;
    private Identite identite2;
    private Candidat candidat;
    private RespFormation respFormation;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        identiteDao = OracleIdentiteDao.getDAO();
        identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setEmail("john.doe@example.com");
        identite.setType(TypeIdentite.CANDIDAT);
        identite2 = new Identite();
        identite2.setNom("Doe");
        identite2.setPrenom("Jane");
        identite2.setSex("F");
        identite2.setEmail("jane.doe@example.com");
        identite2.setType(TypeIdentite.FORMATEUR);
        candidat = new Candidat();
        candidat.setEmail("john.doe@example.com");
        candidat.setNom("Doe");
        candidat.setPrenom("John");
        candiat.setSex("M");
        respFormation = new RespFormation();
        respFormation.setEmail("jane.doe@example.com");
        respFormation.setNom("Doe");
        respFormation.setPrenom("Jane");
        respFormation.setSex("F");

        try {
            em.getTransaction().begin();
            em.persist(identite);
            em.persist(identite2);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        Identite identiteToInsert = new Identite();
        identiteToInsert.setNom("Miller");
        identiteToInsert.setPrenom("Tom");
        identiteToInsert.setSex("M");
        identiteToInsert.setEmail("tom.miller@example.com");
        identiteToInsert.setType(TypeIdentite.FORMATEUR);
        identiteDao.insert(identiteToInsert, "Miller", new GregorianCalendar(2022, 11, 15).getTime());
        Identite identiteRetrieved = em.find(Identite.class, identiteToInsert.getId());
        assertNotNull(identiteRetrieved);
        assertEquals(identiteToInsert.getNom(), identiteRetrieved.getNom());
        assertEquals(identiteToInsert.getPrenom(), identiteRetrieved.getPrenom());
        assertEquals(identiteToInsert.getSex(), identiteRetrieved.getSex());
        assertEquals(identiteToInsert.getType(), identiteRetrieved.getType());
    }

    @Test
    public void testUpdate() {
        identite.setEmail("john.doe@newemail.com");
        identiteDao.update(identite);
        Identite identiteRetrieved = em.find(Identite.class, identite.getId());
        assertEquals(identiteRetrieved.getEmail(), "john.doe@newemail.com");
    }

    @Test
    public void testDelete() {
        identiteDao.delete(identite);
        assertNull(em.find(Identite.class, identite.getId()));
    }

    @Test
    public void testFind() {
        List<Identite> result = identiteDao.find(null, null, null, null, null);
        assertNotNull(result);
        result = identiteDao.find("Doe", null, null, null, null);
        assertSame(Arrays.asList(identite, identite2), result);
        result = identiteDao.find(null, "Smith", null, null, null);
        assertNotNull(result);
        result = identiteDao.find("Doe", "John", null, "john.doe@example.com", null);
        assertSame(Arrays.asList(identite), result);
        result = identiteDao.find("Doe", "John", null, "jane.doe@example.com", null);
        assertSame(Arrays.asList(identite2), result);
        result = identiteDao.find("Doe", "John", "M", null, null);
        assertSame(Arrays.asList(identite, identite2), result);
        result = identiteDao.find("Doe", "John", "M", "john.doe@example.com", null);
        assertSame(Arrays.asList(identite), result);
        result = identiteDao.find("Doe", "John", "M", "jane.doe@example.com", Identite.TypeIdentite.FORMATEUR);
        assertSame(Arrays.asList(identite2), result);
    }

    @Test
    public void testFindCandidat() {
        Candidat[] result = identiteDao.findCandidat(null, null, null, null);
        assertNotNull(result);
        result = identiteDao.findCandidat("Doe", null, null);
        assertSame(Arrays.asList(identite, identite2).toArray(new Identite[0]), result);
        result = identiteDao.findCandidat(null, "Smith", null);
        assertNull(result);
        result = identiteDao.findCandidat("Doe", "John", null);
        assertSame(Arrays.asList(identite).toArray(new Identite[0]), result);
    }

    @Test
    public void testFindRespForm() {
        RespFormation[] result = identiteDao.findRespForm(null, null, null, null);
        assertNotNull(result);
        result = identiteDao.findRespForm("Doe", null, null);
        assertSame(Arrays.asList(identite2).toArray(new Identite[0]), result);
        result = identiteDao.findRespForm(null, "Smith", null);
        assertNull(result);
        result = identiteDao.findRespForm("Doe", "Jane", null);
        assertSame(Arrays.asList(identite2).toArray(new Identite[0]), result);
    }

    @Test(expected = PersistenceException.class)
    public void testInsertNullIdentite() {
        Identite identiteToInsert = null;
        identiteDao.insert(identiteToInsert, null, null);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateNullIdentite() {
        Identite identiteToUpdate = null;
        identiteDao.update(identiteToUpdate);
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteNullIdentite() {
        Identite identiteToDelete = null;
        identiteDao.delete(identiteToDelete);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNullCriteria() {
        identiteDao.find(null, null, null, null, null);
    }

    @Test
    public void testFindEmptyList() {
        List<Identite> result = identiteDao.find("", "", "", "", null);
        assertTrue(result.isEmpty());
    }

    @After
    public void cleanup() {
        try {
            em.getTransaction().begin();
            em.remove(identite);
            em.remove(identite2);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        em.close();
        emf.close();
    }
}