package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    private OracleIdentiteDao dao;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        dao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testInsert() {
        final Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        final Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        identite.setDateLimite(dateLimite.getTime());

        dao.insert(identite, null, dateLimite);

        final Identite foundIdentite = em.find(Identite.class, identite.getId());
        assertNotNull(foundIdentite);
        assertEquals("Doe", foundIdentite.getNom());
        assertEquals("John", foundIdentite.getPrenom());
        assertEquals("M", foundIdentite.getSex());
        assertEquals(TypeIdentite.CANDIDAT, foundIdentite.getType());
        assertEquals(dateLimite.getTime(), foundIdentite.getDateLimite());
    }

    @Test
    public void testUpdate() {
        final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory("Identite");
        final EntityManager emLocal = emfLocal.createEntityManager();

        final Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        final Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        identite.setDateLimite(dateLimite.getTime());
        emLocal.getTransaction().begin();
        emLocal.persist(identite);
        emLocal.getTransaction().commit();

        final long id = identite.getId();
        final Identite updatedIdentite = new Identite();
        updatedIdentite.setId(id);
        updatedIdentite.setEmail("john.doe@example.com");
        updatedIdentite.setNom("Doe");
        updatedIdentite.setPrenom("John");
        updatedIdentite.setSex("M");
        updatedIdentite.setType(TypeIdentite.CANDIDAT);
        updatedIdentite.setDateLimite(dateLimite.getTime());

        dao.update(updatedIdentite);

        final Identite foundIdentite = em.find(Identite.class, id);
        assertEquals("john.doe@example.com", foundIdentite.getEmail());
    }

    @Test
    public void testDelete() {
        final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory("Identite");
        final EntityManager emLocal = emfLocal.createEntityManager();

        final Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        final Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        identite.setDateLimite(dateLimite.getTime());
        emLocal.getTransaction().begin();
        emLocal.persist(identite);
        emLocal.getTransaction().commit();

        final long id = identite.getId();

        dao.delete(new Identite());
        dao.delete(new Identite());
        dao.delete(new Identite());
        dao.delete(new Identite());

        dao.delete(new Identite(id));

        assertNull(em.find(Identite.class, id));
    }

    @Test
    public void testFind() {
        final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory("Identite");
        final EntityManager emLocal = emfLocal.createEntityManager();

        final Identite identite1 = new Identite();
        identite1.setNom("Alice");
        identite1.setPrenom("Smith");
        identite1.setSex("F");
        identite1.setType(TypeIdentite.CANDIDAT);
        identite1.setDateLimite(new Date(System.currentTimeMillis()));
        emLocal.getTransaction().begin();
        emLocal.persist(identite1);
        emLocal.getTransaction().commit();

        final Identite identite2 = new Identite();
        identite2.setNom("Bob");
        identite2.setPrenom("Johnson");
        identite2.setSex("M");
        identite2.setType(TypeIdentite.FORMATEUR);
        identite2.setDateLimite(new Date(System.currentTimeMillis()));
        emLocal.getTransaction().begin();
        emLocal.persist(identite2);
        emLocal.getTransaction().commit();

        final List<Identite> foundIdentites = dao.find(null, null, null, null, null);
        assertEquals(2, foundIdentites.size());
        assertTrue(foundIdentites.contains(identite1));
        assertTrue(foundIdentites.contains(identite2));

        final List<Identite> foundCandidats = dao.find(null, null, null, null, TypeIdentite.CANDIDAT);
        assertEquals(1, foundCandidats.size());
        assertTrue(foundCandidats.contains(identite1));

        final List<Identite> foundFormaturs = dao.find(null, null, null, null, TypeIdentite.FORMATEUR);
        assertEquals(1, foundFormaturs.size());
        assertTrue(foundFormaturs.contains(identite2));

        final Identite identite3 = new Identite();
        identite3.setNom("Doe");
        identite3.setPrenom("John");
        identite3.setSex("M");
        identite3.setType(TypeIdentite.CANDIDAT);
        identite3.setDateLimite(new Date(System.currentTimeMillis()));
        emLocal.getTransaction().begin();
        emLocal.persist(identite3);
        emLocal.getTransaction().commit();

        final List<Identite> foundIdentitesAfterInsert = dao.find(null, null, null, null, null);
        assertEquals(3, foundIdentitesAfterInsert.size());
        assertTrue(foundIdentitesAfterInsert.contains(identite1));
        assertTrue(foundIdentitesAfterInsert.contains(identite2));
        assertTrue(foundIdentitesAfterInsert.contains(identite3));
    }

    @Test
    public void testFindCandidat() {
        final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory("Identite");
        final EntityManager emLocal = emfLocal.createEntityManager();

        final Identite identite1 = new Identite();
        identite1.setNom("Alice");
        identite1.setPrenom("Smith");
        identite1.setSex("F");
        identite1.setType(TypeIdentite.CANDIDAT);
        identite1.setDateLimite(new Date(System.currentTimeMillis()));
        emLocal.getTransaction().begin();
        emLocal.persist(identite1);
        emLocal.getTransaction().commit();

        final Candidat[] foundCandidats = dao.findCandidat("Alice", null, null, null);
        assertEquals(1, foundCandidats.length);
        assertEquals(identite1.getId(), foundCandidats[0].getId());
    }

    @Test
    public void testFindRespForm() {
        final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory("Identite");
        final EntityManager emLocal = emfLocal.createEntityManager();

        final Identite identite2 = new Identite();
        identite2.setNom("Bob");
        identite2.setPrenom("Johnson");
        identite2.setSex("M");
        identite2.setType(TypeIdentite.FORMATEUR);
        identite2.setDateLimite(new Date(System.currentTimeMillis()));
        emLocal.getTransaction().begin();
        emLocal.persist(identite2);
        emLocal.getTransaction().commit();

        final RespFormation[] foundRespFormations = dao.findRespForm("Bob", null, null, null);
        assertEquals(1, foundRespFormations.length);
        assertEquals(identite2.getId(), foundRespFormations[0].getId());
    }
}