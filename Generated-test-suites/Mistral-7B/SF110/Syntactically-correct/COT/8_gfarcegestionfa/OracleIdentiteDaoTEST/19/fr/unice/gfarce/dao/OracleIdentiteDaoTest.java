package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private static IdentiteDao identiteDao;
    private static OracleIdentiteDao oracleIdentiteDao;
    private static EntityManagerFactory emf;

    @Before
    public void setup() {
        identiteDao = OracleIdentiteDao.getDAO();
        oracleIdentiteDao = (OracleIdentiteDao) identiteDao;
        emf = Persistence.createEntityManagerFactory("Identite");
    }

    @After
    public void teardown() {
        if (oracleIdentiteDao != null) {
            oracleIdentiteDao = null;
        }
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite("John Doe", "John", 'M', "jdoe@example.com");
        oracleIdentiteDao.insert(identite, null, null);
        Identite retrievedIdentite = oracleIdentiteDao.findById(identite.getId());
        assertNotNull(retrievedIdentite);
    }

    @Test(expected = NullPointerException.class)
    public void testInsertNullIdentite() {
        oracleIdentiteDao.insert(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullNomForm() {
        Identite identite = new Identite("", "John Doe", 'M', "jdoe@example.com");
        oracleIdentiteDao.insert(identite, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullDateLimite() {
        Identite identite = new Identite("John Doe", "John", 'M', "jdoe@example.com");
        oracleIdentiteDao.insert(identite, null, new Date(0));
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite("John Doe", "John", 'M', "jdoe@example.com");
        oracleIdentiteDao.insert(identite, null, null);

        Identite updatedIdentite = new Identite("John Doe", "John updated", 'M', "jdoe@example.com");
        oracleIdentiteDao.update(updatedIdentite);
        Identite retrievedIdentite = oracleIdentiteDao.findById(identite.getId());
        assertTrue(updatedIdentite.getNom().equals(retrievedIdentite.getNom()));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateNullIdentite() {
        oracleIdentiteDao.update(null);
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite("John Doe", "John", 'M', "jdoe@example.com");
        oracleIdentiteDao.insert(identite, null, null);
        oracleIdentiteDao.delete(identite);
        Identite retrievedIdentite = oracleIdentiteDao.findById(identite.getId());
        assertTrue(retrievedIdentite == null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteNullIdentite() {
        oracleIdentiteDao.delete(null);
    }

    @Test
    public void testFindAll() {
        Identite identite1 = new Identite("John Doe 1", "John 1", 'M', "john1@example.com");
        Identite identite2 = new Identite("John Doe 2", "John 2", 'M', "john2@example.com");

        oracleIdentiteDao.insert(identite1, null, null);
        oracleIdentiteDao.insert(identite2, null, null);

        List<Identite> allIdentites = oracleIdentiteDao.find(null, null, null, null, null);

        assertTrue(allIdentites.size() == 2);
        assertNotNull(allIdentites.get(0));
        assertNotNull(allIdentites.get(1));
    }

    @Test
    public void testFindCandidat() {
        Identite identite = new Identite("John Doe", "John", 'M', "jdoe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        oracleIdentiteDao.insert(identite, null, null);

        Candidat[] candidats = oracleIdentiteDao.findCandidat(null, null, null, null);
        assertNotNull(candidats);
        assertTrue(candidats.length > 0);
    }

    @Test
    public void testFindRespForm() {
        Identite identite = new Identite("John Doe", "John", 'M', "jdoe@example.com");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        oracleIdentiteDao.insert(identite, null, null);

        RespFormation[] respFormations = oracleIdentiteDao.findRespForm(null, null, null, null);
        assertNotNull(respFormations);
        assertTrue(respFormations.length > 0);
    }

    @Test(expected = TransactionRequiredException.class)
    public void testFindWithTransactionRequired() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            oracleIdentiteDao = new OracleIdentiteDao();
            List<Identite> identites = oracleIdentiteDao.find(null, null, null, null, null);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }
}