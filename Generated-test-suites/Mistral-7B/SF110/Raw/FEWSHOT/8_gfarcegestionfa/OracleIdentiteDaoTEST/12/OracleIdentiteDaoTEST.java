package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

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
    public void before() {
        this.emf = Persistence.createEntityManagerFactory("Identite");
        this.em = this.emf.createEntityManager();
        this.tx = this.em.getTransaction();
        this.tx.begin();
    }

    @After
    public void after() {
        this.em.close();
        this.emf.close();
        this.tx.rollback();
    }

    @Test
    public void testInsert() {
        final Identite identite = new Identite("test", "test", 'M', "test@test.fr", TypeIdentite.CANDIDAT);
        final Date dateLimite = new Date();

        this.em.persist(identite);
        this.tx.commit();

        final Identite identiteFound = (Identite) this.em.find(Identite.class, identite.getId());
        assertNotNull(identiteFound);
        assertEquals(identite.getNom(), identiteFound.getNom());
        assertEquals(identite.getPrenom(), identiteFound.getPrenom());
        assertEquals(identite.getType(), identiteFound.getType());
        assertEquals(identite.getEmail(), identiteFound.getEmail());
        assertEquals(identite.getSex(), identiteFound.getSex());

        this.em.remove(identite);
        this.tx.commit();
    }

    @Test(expected = PersistenceException.class)
    public void testInsertException() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(new Identite("test", null, 'M', null, TypeIdentite.CANDIDAT));
        tx.commit();

        em.close();
        emf.close();
    }

    @Test
    public void testUpdate() {
        final Identite identite = new Identite("test", "test", 'M', "test@test.fr", TypeIdentite.CANDIDAT);
        this.em.persist(identite);
        this.tx.commit();

        final Long id = identite.getId();

        final Identite identiteUpdated = new Identite("testUpdated", "testUpdated", 'M', "testUpdated@testUpdated.fr", TypeIdentite.CANDIDAT);
        identiteUpdated.setId(id);

        this.em.merge(identiteUpdated);
        this.tx.commit();

        final Identite identiteFound = (Identite) this.em.find(Identite.class, id);
        assertEquals(identiteUpdated.getNom(), identiteFound.getNom());
        assertEquals(identiteUpdated.getPrenom(), identiteFound.getPrenom());
        assertEquals(identiteUpdated.getType(), identiteFound.getType());
        assertEquals(identiteUpdated.getEmail(), identiteFound.getEmail());
        assertEquals(identiteUpdated.getSex(), identiteFound.getSex());

        this.em.remove(identiteUpdated);
        this.tx.commit();
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateException() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        tx.begin();
        final Identite identite = new Identite("test", "test", 'M', "test@test.fr", TypeIdentite.CANDIDAT);
        this.em.persist(identite);
        this.tx.commit();

        tx.begin();
        final Identite identiteUpdated = new Identite("testUpdated", "testUpdated", 'M', "testUpdated@testUpdated.fr", TypeIdentite.CANDIDAT);
        identiteUpdated.setId(identite.getId() + 1);

        try {
            this.em.merge(identiteUpdated);
            this.tx.commit();
        } catch (PersistenceException e) {
            throw e;
        } finally {
            this.em.close();
            this.emf.close();
            this.tx.rollback();
        }
    }

    @Test
    public void testDelete() {
        final Identite identite = new Identite("test", "test", 'M', "test@test.fr", TypeIdentite.CANDIDAT);
        this.em.persist(identite);
        this.tx.commit();

        final Long id = identite.getId();

        this.em.remove(identite);
        this.tx.commit();

        final Identite identiteFound = (Identite) this.em.find(Identite.class, id);
        assertNull(identiteFound);
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteException() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        tx.begin();
        final Identite identite = new Identite("test", "test", 'M', "test@test.fr", TypeIdentite.CANDIDAT);
        this.em.persist(identite);
        this.tx.commit();

        tx.begin();
        this.em.remove(new Identite("testToDelete", "testToDelete", 'M', "testToDelete@testToDelete.fr", TypeIdentite.CANDIDAT));
        this.tx.commit();

        this.em.close();
        this.emf.close();
        this.tx.rollback();
    }

    @Test
    public void testFindAll() {
        final Identite identite1 = new Identite("test1", "test1", 'M', "test1@test1.fr", TypeIdentite.CANDIDAT);
        final Identite identite2 = new Identite("test2", "test2", 'F', "test2@test2.fr", TypeIdentite.FORMATEUR);

        this.em.persist(identite1);
        this.em.persist(identite2);
        this.tx.commit();

        final List<Identite> identites = this.em.createQuery("SELECT i FROM Identite i", Identite.class).getResultList();

        assertEquals(2, identites.size());
        assertTrue(identites.contains(identite1));
        assertTrue(identites.contains(identite2));

        this.em.removeAll(identites);
        this.tx.commit();
    }

    @Test
    public void testFindByNomPrenomSexEmailTypeAndFindCandidats() {
        final Identite identite1 = new Identite("John", "Doe", 'M', "john.doe@example.com", TypeIdentite.CANDIDAT);
        final Identite identite2 = new Identite("Jane", "Doe", 'F', "jane.doe@example.com", TypeIdentite.FORMATEUR);

        this.em.persist(identite1);
        this.em.persist(identite2);
        this.tx.commit();

        final List<Identite> identites = this.em.createQuery("SELECT i FROM Identite i WHERE i.nom = :nom AND i.prenom = :prenom AND i.sex = :sex AND i.email = :email AND i.type = :type", Identite.class)
                .setParameter("nom", "Doe")
                .setParameter("prenom", "Jane")
                .setParameter("sex", 'F')
                .setParameter("email", "jane.doe@example.com")
                .setParameter("type", TypeIdentite.FORMATEUR)
                .getResultList();

        assertEquals(1, identites.size());
        assertTrue(identites.contains(identite2));

        final Candidat[] candidats = this.em.createEntityManager().createQuery("SELECT c FROM Candidat c WHERE c.identite.nom = :nom AND c.identite.prenom = :prenom AND c.identite.sex = :sex AND c.identite.email = :email", Candidat[].class)
                .setParameter("nom", "Doe")
                .setParameter("prenom", "Jane")
                .setParameter("sex", 'F')
                .setParameter("email", "jane.doe@example.com")
                .getResultList()
                .toArray(new Candidat[0]);

        assertEquals(0, candidats.length);

        this.em.removeAll(identites);
        this.tx.commit();
    }

    @Test
    public void testFindByNomPrenomSexEmailTypeAndFindFormateurs() {
        final Identite identite1 = new Identite("John", "Doe", 'M', "john.doe@example.com", TypeIdentite.CANDIDAT);
        final Identite identite2 = new Identite("Jane", "Doe", 'F', "jane.doe@example.com", TypeIdentite.FORMATEUR);

        this.em.persist(identite1);
        this.em.persist(identite2);
        this.tx.commit();

        final List<Identite> identites = this.em.createQuery("SELECT i FROM Identite i WHERE i.nom = :nom AND i.prenom = :prenom AND i.sex = :sex AND i.email = :email AND i.type = :type", Identite.class)
                .setParameter("nom", "Doe")
                .setParameter("prenom", "Jane")
                .setParameter("sex", 'F')
                .setParameter("email", "jane.doe@example.com")
                .setParameter("type", TypeIdentite.FORMATEUR)
                .getResultList();

        assertEquals(1, identites.size());
        assertTrue(identites.contains(identite2));

        final RespFormation[] respFormations = this.em.createEntityManager().createQuery("SELECT r FROM RespFormation r WHERE r.identite.nom = :nom AND r.identite.prenom = :prenom AND r.identite.sex = :sex AND r.identite.email = :email", RespFormation[].class)
                .setParameter("nom", "Doe")
                .setParameter("prenom", "Jane")
                .setParameter("sex", 'F')
                .setParameter("email", "jane.doe@example.com")
                .getResultList()
                .toArray(new RespFormation[0]);

        assertEquals(0, respFormations.length);

        this.em.removeAll(identites);
        this.tx.commit();
    }
}