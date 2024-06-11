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
import javax.persistence.TypedQuery;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private static final String PERSIST_UNIT_NAME = "Identite";

    @org.junit.BeforeClass
    public static void setupClass() {
        Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
    }

    @org.junit.AfterClass
    public static void teardownClass() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNativeQuery("DELETE FROM Identite").executeUpdate();
        tx.commit();
        em.close();
        emf.close();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
        EntityManager em = emf.createEntityManager();
        return em;
    }

    @org.junit.Test
    public void testInsert() {
        EntityManager em = getEntityManager();
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        OracleIdentiteDao identiteDao = OracleIdentiteDao.getDAO();
        identiteDao.insert(identite, null, dateLimite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), foundIdentite.getNom());
        assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
        assertEquals(identite.getSex(), foundIdentite.getSex());
        assertEquals(identite.getType(), foundIdentite.getType());
        assertEquals(dateLimite, foundIdentite.getDateLimite());

        em.close();
    }

    @org.junit.Test
    public void testUpdate() {
        EntityManager em = getEntityManager();
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);
        em.persist(identite);

        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(identite.getId());
        updatedIdentite.setEmail("newEmail@example.com");
        updatedIdentite.setNom("Doe Updated");
        updatedIdentite.setPrenom("John Updated");

        OracleIdentiteDao identiteDao = OracleIdentiteDao.getDAO();
        identiteDao.update(updatedIdentite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertEquals(updatedIdentite.getEmail(), foundIdentite.getEmail());
        assertEquals(updatedIdentite.getNom(), foundIdentite.getNom());
        assertEquals(updatedIdentite.getPrenom(), foundIdentite.getPrenom());
        assertEquals(updatedIdentite.getSex(), foundIdentite.getSex());
        assertEquals(updatedIdentite.getType(), foundIdentite.getType());
        assertEquals(dateLimite, foundIdentite.getDateLimite());

        em.close();
    }

    @org.junit.Test
    public void testDelete() {
        EntityManager em = getEntityManager();
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);
        em.persist(identite);

        OracleIdentiteDao identiteDao = OracleIdentiteDao.getDAO();
        identiteDao.delete(identite);

        Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNull(foundIdentite);

        em.close();
    }

    @org.junit.Test
    public void testFind() {
        EntityManager em = getEntityManager();
        Identite identite1 = new Identite();
        identite1.setNom("Doe 1");
        identite1.setPrenom("John 1");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite1 = calendar.getTime();
        identite1.setDateLimite(dateLimite1);
        em.persist(identite1);

        Identite identite2 = new Identite();
        identite2.setNom("Doe 2");
        identite2.setPrenom("John 2");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar2 = Calendar.getInstance();
        Date dateLimite2 = calendar2.getTime();
        identite2.setDateLimite(dateLimite2);
        em.persist(identite2);

        OracleIdentiteDao identiteDao = OracleIdentiteDao.getDAO();
        List<Identite> identites = identiteDao.find(null, null, null, null, null);
        assertEquals(2, identites.size());

        List<Identite> filteredIdentites = identiteDao.find(null, "Doe", null, null, null);
        assertEquals(1, filteredIdentites.size());
        assertEquals(identite1.getNom(), filteredIdentites.get(0).getNom());

        List<Identite> filteredIdentites2 = identiteDao.find("John", null, null, null, null);
        assertEquals(1, filteredIdentites2.size());
        assertEquals(identite1.getPrenom(), filteredIdentites2.get(0).getPrenom());

        List<Identite> filteredIdentites3 = identiteDao.find(null, null, "M", null, null);
        assertEquals(2, filteredIdentites3.size());

        List<Identite> filteredIdentites4 = identiteDao.find(null, null, null, "john@example.com", null);
        assertEquals(0, filteredIdentites4.size());

        List<Identite> filteredIdentites5 = identiteDao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertEquals(0, filteredIdentites5.size());

        List<Identite> filteredIdentites6 = identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(2, filteredIdentites6.size());

        List<Identite> filteredIdentites7 = identiteDao.find("Doe 1", null, null, null, null);
        assertEquals(1, filteredIdentites7.size());
        assertEquals(identite1.getNom(), filteredIdentites7.get(0).getNom());

        List<Identite> filteredIdentites8 = identiteDao.find(null, "John 1", null, null, null);
        assertEquals(0, filteredIdentites8.size());

        List<Identite> filteredIdentites9 = identiteDao.find(null, null, "F", null, null);
        assertEquals(0, filteredIdentites9.size());

        List<Candidat> candidats = identiteDao.findCandidat(null, null, null, null);
        assertEquals(2, candidats.length);

        List<RespFormation> respFormations = identiteDao.findRespForm(null, null, null, null);
        assertEquals(0, respFormations.length);

        em.close();
    }
}