package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.dao.Identite.TypeIdentite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @After
    public void tearDown() {
        if (tx != null) {
            tx.rollback();
        }
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
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite, null, null);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(retrievedIdentite);
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        dao.insert(identite, null, null);
        identite.setEmail("updatedTest@example.com");
        identite.setNom("UpdatedTest");
        dao.update(identite);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(retrievedIdentite);
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        dao.insert(identite, null, null);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(retrievedIdentite);
        dao.delete(retrievedIdentite);
        Identite deletedIdentite = em.find(Identite.class, identite.getId());
        assertNull(deletedIdentite);
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setEmail("ident1@example.com");
        identite1.setNom("Ident1");
        identite1.setPrenom("Ident1");
        identite1.setSex("M");
        identite1.setType(TypeIdentite.CANDIDAT);
        dao.insert(identite1, null, null);

        Identite identite2 = new Identite();
        identite2.setEmail("ident2@example.com");
        identite2.setNom("Ident2");
        identite2.setPrenom("Ident2");
        identite2.setSex("M");
        identite2.setType(TypeIdentite.FORMATEUR);
        dao.insert(identite2, null, null);

        List<Identite> result = dao.find(null, null, null, null, null);
        assertEquals(2, result.size());

        List<Identite> nomFilteredResult = dao.find("Ident", null, null, null, null);
        assertEquals(1, nomFilteredResult.size());
        assertEquals("Ident1", nomFilteredResult.get(0).getNom());

        List<Identite> sexFilteredResult = dao.find(null, null, "M", null, null);
        assertEquals(2, sexFilteredResult.size());

        List<Identite> emailFilteredResult = dao.find(null, null, null, "ident1@example.com", null);
        assertEquals(1, emailFilteredResult.size());
        assertEquals("Ident1", emailFilteredResult.get(0).getNom());

        List<Identite> typeFilteredResult = dao.find(null, null, null, null, TypeIdentite.CANDIDAT);
        assertEquals(1, typeFilteredResult.size());
        assertEquals("Ident1", typeFilteredResult.get(0).getNom());

        List<Identite> typeAndSexFilteredResult = dao.find(null, null, null, null, TypeIdentite.FORMATEUR);
        assertEquals(1, typeAndSexFilteredResult.size());
        assertEquals("Ident2", typeAndSexFilteredResult.get(0).getNom());
    }

    @Test
    public void testFindCandidat() {
        Candidat candidat1 = new Candidat();
        candidat1.setIdentite(new Identite());
        candidat1.getIdentite().setEmail("candidat1@example.com");
        candidat1.getIdentite().setNom("Candidat1");
        candidat1.getIdentite().setPrenom("Candidat1");
        candidat1.getIdentite().setSex("M");
        candidat1.getIdentite().setType(TypeIdentite.CANDIDAT);
        dao.insert(candidat1.getIdentite(), null, null);

        Candidat candidat2 = new Candidat();
        candidat2.setIdentite(new Identite());
        candidat2.getIdentite().setEmail("candidat2@example.com");
        candidat2.getIdentite().setNom("Candidat2");
        candidat2.getIdentite().setPrenom("Candidat2");
        candidat2.getIdentite().setSex("F");
        candidat2.getIdentite().setType(TypeIdentite.CANDIDAT);
        dao.insert(candidat2.getIdentite(), null, null);

        List<Candidat> result = dao.findCandidat("Candidat", null, null);
        assertEquals(2, result.length);

        List<Candidat> nomFilteredResult = dao.findCandidat(null, null, null);
        assertEquals(2, nomFilteredResult.length);

        List<Candidat> sexFilteredResult = dao.findCandidat(null, "F", null);
        assertEquals(1, sexFilteredResult.length);

        List<Candidat> emailFilteredResult = dao.findCandidat(null, null, "candidat1@example.com");
        assertEquals(1, emailFilteredResult.length);
    }

    @Test
    public void testFindRespForm() {
        RespFormation respFormation1 = new RespFormation();
        respFormation1.setIdentite(new Identite());
        respFormation1.getIdentite().setEmail("respForm1@example.com");
        respFormation1.getIdentite().setNom("RespForm1");
        respFormation1.getIdentite().setPrenom("RespForm1");
        respFormation1.getIdentite().setSex("M");
        respFormation1.getIdentite().setType(TypeIdentite.FORMATEUR);
        dao.insert(respFormation1.getIdentite(), null, null);

        RespFormation respFormation2 = new RespFormation();
        respFormation2.setIdentite(new Identite());
        respFormation2.getIdentite().setEmail("respForm2@example.com");
        respFormation2.getIdentite().setNom("RespForm2");
        respFormation2.getIdentite().setPrenom("RespForm2");
        respFormation2.getIdentite().setSex("M");
        respFormation2.getIdentite().setType(TypeIdentite.FORMATEUR);
        dao.insert(respFormation2.getIdentite(), null, null);

        List<RespFormation> result = dao.findRespForm("RespForm", null, null);
        assertEquals(2, result.length);

        List<RespFormation> nomFilteredResult = dao.findRespForm(null, null, null);
        assertEquals(2, nomFilteredResult.length);
    }

    @Test(expected = PersistenceException.class)
    public void testFindWithEmptyPersistenceUnitName() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("");
            EntityManager em = emf.createEntityManager();
            OracleIdentiteDao dao = new OracleIdentiteDao();
            dao.find(null, null, null, null, null);
        } catch (Exception e) {
            throw new PersistenceException("Expected PersistenceException", e);
        }
    }
}