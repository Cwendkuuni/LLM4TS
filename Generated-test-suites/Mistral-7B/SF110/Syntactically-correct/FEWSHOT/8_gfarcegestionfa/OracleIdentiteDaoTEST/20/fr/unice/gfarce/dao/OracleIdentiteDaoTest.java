package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @org.junit.Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
    }

    @org.junit.After
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex(Sex.M);
        identite.setEmail("john.doe@example.com");
        identite.setType(TypeIdentite.CANDIDAT);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite, "nomForm", new GregorianCalendar(2000, 1, 1).getTime());

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(foundIdentite);
        assertEquals(identite.getEmail(), foundIdentite.getEmail());
        assertEquals(identite.getNom(), foundIdentite.getNom());
        assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
        assertEquals(identite.getSex(), foundIdentite.getSex());
        assertEquals(identite.getType(), foundIdentite.getType());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex(Sex.M);
        identite.setEmail("john.doe@example.com");
        identite.setType(TypeIdentite.CANDIDAT);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite existingIdentite = new Identite();
        existingIdentite.setId(1);
        existingIdentite.setEmail("john.doe@example.com");
        existingIdentite.setNom("Smith");
        existingIdentite.setPrenom("John");
        existingIdentite.setSex(Sex.M);
        existingIdentite.setType(TypeIdentite.CANDIDAT);

        dao.update(identite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(foundIdentite);
        assertEquals(identite.getEmail(), foundIdentite.getEmail());
        assertEquals(identite.getNom(), foundIdentite.getNom());
        assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
        assertEquals(identite.getSex(), foundIdentite.getSex());
        assertEquals(identite.getType(), foundIdentite.getType());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex(Sex.M);
        identite.setEmail("john.doe@example.com");
        identite.setType(TypeIdentite.CANDIDAT);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.delete(identite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNull(foundIdentite);
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex(Sex.M);
        identite1.setEmail("john.doe@example.com");
        identite1.setType(TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("Roe");
        identite2.setPrenom("Jane");
        identite2.setSex(Sex.F);
        identite2.setEmail("jane.roe@example.com");
        identite2.setType(TypeIdentite.FORMATEUR);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite1, "nomForm", new GregorianCalendar(2000, 1, 1).getTime());
        dao.insert(identite2, "nomForm", new GregorianCalendar(2000, 1, 2).getTime());

        List<Identite> result = dao.find(null, null, null, null, null);
        assertEquals(2, result.size());

        result = dao.find("Doe", null, null, null, null);
        assertEquals(1, result.size());
        assertEquals(identite1, result.get(0));

        result = dao.find(null, "Jane", null, null, null);
        assertEquals(1, result.size());
        assertEquals(identite2, result.get(0));

        result = dao.find(null, null, "F", null, null);
        assertEquals(1, result.size());
        assertEquals(identite2, result.get(0));

        result = dao.find(null, null, null, "jane.roe@example.com", null);
        assertEquals(1, result.size());
        assertEquals(identite2, result.get(0));

        result = dao.find(null, null, null, null, TypeIdentite.CANDIDAT);
        assertEquals(1, result.size());
        assertEquals(identite1, result.get(0));

        result = dao.find(null, null, null, null, TypeIdentite.FORMATEUR);
        assertEquals(1, result.size());
        assertEquals(identite2, result.get(0));

        List<Identite> emptyResult = dao.find("foo", "bar", null, null, null);
        assertEquals(0, emptyResult.size());
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex(Sex.M);
        identite1.setEmail("john.doe@example.com");
        identite1.setType(TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("Roe");
        identite2.setPrenom("Jane");
        identite2.setSex(Sex.F);
        identite2.setEmail("jane.roe@example.com");
        identite2.setType(TypeIdentite.FORMATEUR);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite1, "nomForm", new GregorianCalendar(2000, 1, 1).getTime());
        dao.insert(identite2, "nomForm", new GregorianCalendar(2000, 1, 2).getTime());

        Candidat[] result = dao.findCandidat(null, null, null, null);
        assertEquals(1, result.length);
        assertEquals(identite1, Arrays.asList(result).get(0));

        result = dao.findCandidat("Doe", null, null, null);
        assertEquals(1, result.length);
        assertEquals(identite1, Arrays.asList(result).get(0));

        result = dao.findCandidat(null, "Jane", null, null);
        assertEquals(0, result.length);

        result = dao.findCandidat(null, null, "M", null);
        assertEquals(1, result.length);
        assertEquals(identite1, Arrays.asList(result).get(0));

        result = dao.findCandidat(null, null, null, "john.doe@example.com");
        assertEquals(1, result.length);
        assertEquals(identite1, Arrays.asList(result).get(0));
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex(Sex.M);
        identite1.setEmail("john.doe@example.com");
        identite1.setType(TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("Roe");
        identite2.setPrenom("Jane");
        identite2.setSex(Sex.F);
        identite2.setEmail("jane.roe@example.com");
        identite2.setType(TypeIdentite.FORMATEUR);

        RespFormation respFormation1 = new RespFormation();
        respFormation1.setId(1);
        respFormation1.setIdentite(identite1);
        respFormation1.setFormation("Formation 1");

        RespFormation respFormation2 = new RespFormation();
        respFormation2.setId(2);
        respFormation2.setIdentite(identite2);
        respFormation2.setFormation("Formation 2");

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite1, "nomForm", new GregorianCalendar(2000, 1, 1).getTime());
        dao.insert(identite2, "nomForm", new GregorianCalendar(2000, 1, 2).getTime());
        dao.insert(respFormation1, "nomForm", new GregorianCalendar(2000, 1, 1).getTime());
        dao.insert(respFormation2, "nomForm", new GregorianCalendar(2000, 1, 2).getTime());

        RespFormation[] result = dao.findRespForm(null, null, null, null);
        assertEquals(2, result.length);

        result = dao.findRespForm("Doe", null, null, null);
        assertEquals(1, result.length);
        assertEquals(respFormation1, Arrays.asList(result).get(0));

        result = dao.findRespForm(null, "Jane", null, null);
        assertEquals(1, result.length);
        assertEquals(respFormation2, Arrays.asList(result).get(0));

        result = dao.findRespForm(null, null, null, "jane.roe@example.com");
        assertEquals(1, result.length);
        assertEquals(respFormation2, Arrays.asList(result).get(0));

        result = dao.findRespForm(null, null, null, null);
        assertEquals(2, result.length);

        result = dao.findRespForm(null, null, null, "john.doe@example.com");
        assertEquals(1, result.length);
        assertEquals(respFormation1, Arrays.asList(result).get(0));

        result = dao.findRespForm(null, null, null, null);
        assertEquals(2, result.length);

        result = dao.findRespForm(null, null, null, null);
        assertEquals(2, result.length);
    }
}