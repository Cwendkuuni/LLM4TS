package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    private OracleIdentiteDao dao;
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @Before
    public void initialize() {
        dao = OracleIdentiteDao.getDAO();
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "NomForm", new Date());

        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());

        tx.rollback();
        em.close();
        emf.close();
    }

    @Test
    public void testUpdate() {
        EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory("Identite");
        EntityManager emLocal = emfLocal.createEntityManager();
        EntityTransaction txLocal = emLocal.getTransaction();

        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        emLocal.persist(identite);
        txLocal.commit();

        identite.setEmail("new.email@example.com");

        dao.update(identite);

        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());

        txLocal.rollback();
        emLocal.close();
        emfLocal.close();
        em.close();
        tx.rollback();
        emf.close();
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "NomForm", new Date());

        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
        assertEquals(identite.getSex(), retrievedIdentite.getSex());
        assertEquals(identite.getType(), retrievedIdentite.getType());

        dao.delete(retrievedIdentite);

        Identite deletedIdentite = em.find(Identite.class, retrievedIdentite.getId());
        assertNull(deletedIdentite);

        tx.rollback();
        em.close();
        emf.close();
    }

    @Test
    public void testFind() {
        Identite identite = new Identite();
        identite.setNom("Smith");
        identite.setPrenom("Jane");
        identite.setSex('F');
        identite.setEmail("jane.smith@example.com");
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        dao.insert(identite, "NomForm", new Date());

        List<Identite> identites = dao.find(null, null, null, null, null);
        assertEquals(1, identites.size());
        assertEquals(identite.getNom(), identites.get(0).getNom());
        assertEquals(identite.getPrenom(), identites.get(0).getPrenom());
        assertEquals(identite.getEmail(), identites.get(0).getEmail());
        assertEquals(identite.getSex(), identites.get(0).getSex());
        assertEquals(identite.getType(), identites.get(0).getType());

        identites = dao.find("Doe", null, null, null, null);
        assertEquals(0, identites.size());

        identites = dao.find(null, "Dupont", null, null, null);
        assertEquals(0, identites.size());

        identites = dao.find(null, null, "M", null, null);
        assertEquals(2, identites.size());

        identites = dao.find(null, null, null, "jane.smith@example.com", null);
        assertEquals(1, identites.size());
        assertEquals(identite.getNom(), identites.get(0).getNom());
        assertEquals(identite.getPrenom(), identites.get(0).getPrenom());
        assertEquals(identite.getEmail(), identites.get(0).getEmail());
        assertEquals(identite.getSex(), identites.get(0).getSex());
        assertEquals(identite.getType(), identites.get(0).getType());

        tx.rollback();
        em.close();
        emf.close();
    }

    @Test
    public void testFindCandidat() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "NomForm", new Date());

        Candidat[] candidates = dao.findCandidat(null, null, null, null);
        assertEquals(1, candidates.length);
        assertEquals(identite.getNom(), candidates[0].getNom());
        assertEquals(identite.getPrenom(), candidates[0].getPrenom());
        assertEquals(identite.getEmail(), candidates[0].getEmail());

        tx.rollback();
        em.close();
        emf.close();
    }

    @Test
    public void testFindRespForm() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        dao.insert(identite, "NomForm", new Date());

        RespFormation[] respFormations = dao.findRespForm(null, null, null, null);
        assertEquals(1, respFormations.length);
        assertEquals(identite.getNom(), respFormations[0].getNom());
        assertEquals(identite.getPrenom(), respFormations[0].getPrenom());

        tx.rollback();
        em.close();
        emf.close();
    }
}