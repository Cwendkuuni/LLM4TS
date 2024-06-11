package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;

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
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "John Doe", new Date());

        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.update(identite);

        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.delete(identite);

        List<Identite> identites = em.createQuery("SELECT i FROM Identite i", Identite.class).getResultList();
        assertFalse(identites.contains(identite));
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1L);
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setEmail("john.doe@example.com");
        identite1.setSex('M');
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        em.getTransaction().begin();
        em.persist(identite1);
        em.getTransaction().commit();

        Identite identite2 = new Identite();
        identite2.setId(2L);
        identite2.setNom("Smith");
        identite2.setPrenom("Jane");
        identite2.setEmail("jane.smith@example.com");
        identite2.setSex('F');
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        em.getTransaction().begin();
        em.persist(identite2);
        em.getTransaction().commit();

        List<Identite> identites = dao.find(null, null, null, null, null);
        assertEquals(2, identites.size());
        Identite identiteFound1 = identites.stream().filter(i -> i.getNom().equals("Doe")).findFirst().orElse(null);
        Identite identiteFound2 = identites.stream().filter(i -> i.getNom().equals("Smith")).findFirst().orElse(null);
        assertNotNull(identiteFound1);
        assertNotNull(identiteFound2);

        List<Identite> identitesFiltered = dao.find(null, "Smith", null, null, null);
        assertEquals(1, identitesFiltered.size());
        assertEquals(identite2.getNom(), identitesFiltered.get(0).getNom());

        List<Identite> identitesFilteredTypeCandidat = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, identitesFilteredTypeCandidat.size());
        assertEquals(identite1.getNom(), identitesFilteredTypeCandidat.get(0).getNom());
    }

    @Test
    public void testFindCandidat() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        em.getTransaction().begin();
        em.persist(identite);
        em.getTransaction().commit();

        Candidat[] candidates = dao.findCandidat("Doe", null, null, "john.doe@example.com");
        assertEquals(1, candidates.length);
        assertEquals(identite.getNom(), candidates[0].getNom());
        assertEquals(identite.getPrenom(), candidates[0].getPrenom());
        assertEquals(identite.getEmail(), candidates[0].getEmail());

        Candidat[] emptyCandidates = dao.findCandidat("Alice", null, null, "john.doe@example.com");
        assertArrayEquals(new Candidat[0], emptyCandidates);
    }

    @Test
    public void testFindRespForm() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        em.getTransaction().begin();
        em.persist(identite);
        em.getTransaction().commit();

        RespFormation[] respFormations = dao.findRespForm("Doe", null, null, "john.doe@example.com");
        assertEquals(1, respFormations.length);
        assertEquals(identite.getNom(), respFormations[0].getNom());
        assertEquals(identite.getPrenom(), respFormations[0].getPrenom());
        assertEquals(identite.getEmail(), respFormations[0].getEmail());

        RespFormation[] emptyRespFormations = dao.findRespForm("Smith", null, null, "john.doe@example.com");
        assertArrayEquals(new RespFormation[0], emptyRespFormations);
    }
}