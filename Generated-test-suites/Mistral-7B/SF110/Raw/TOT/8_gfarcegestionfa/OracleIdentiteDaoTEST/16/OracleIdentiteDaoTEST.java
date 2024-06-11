package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao.IdentiteTest;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest
{
    private static final String PERSIST_UNIT_NAME = "Identite";
    private static final EntityManagerFactory EMF;
    private static OracleIdentiteDao identiteDao;

    static
    {
        EMF = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
        identiteDao = OracleIdentiteDao.getDAO();
    }

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp()
    {
        em = EMF.createEntityManager();
        identiteDao.setEm(em);
    }

    @After
    public void cleanUp()
    {
        try
        {
            em.getTransaction().begin();
            List<Identite> identites = em.createQuery("select i from Identite i", Identite.class).getResultList();
            for (Identite identite : identites)
            {
                identiteDao.delete(identite);
            }
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
    }

    @Test
    public void testInsert()
    {
        final IdentiteTest identiteTest = new IdentiteTest("John Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertNull(identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT).get(0));
        identiteDao.insert(identiteTest, "TestInsert", new Date());
        assertNotNull(identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT).get(0));
        assertNotSame(identiteTest, identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT).get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertNullIdentite()
    {
        identiteDao.insert(null, "TestInsertNullIdentite", new Date());
    }

    @Test(expected = PersistenceException.class)
    public void testInsertFailure()
    {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try
        {
            emf = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            identiteDao.setEm(em);
            IdentiteTest identiteTest = new IdentiteTest("John Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
            em.persist((Object)identiteTest);
            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (tx != null)
            {
                tx.rollback();
            }
            throw e;
        }
        finally
        {
            if (em != null)
            {
                em.close();
            }
            if (emf != null)
            {
                emf.close();
            }
        }
        identiteDao.insert(new IdentiteTest("", "", (short)0, null, Identite.TypeIdentite.CANDIDAT), "TestInsertFailure", new Date());
    }

    @Test
    public void testUpdate()
    {
        final IdentiteTest identiteTest = new IdentiteTest("John Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identiteTest, "TestUpdate", new Date());
        Identite identite = identiteDao.find(identiteTest.getNom(), identiteTest.getPrenom(), identiteTest.getSex(), identiteTest.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
        assertNotNull(identite);
        identiteTest.setEmail("john.doe.updated@example.com");
        identiteDao.update(identiteTest);
        Optional<Identite> identiteOptional = identiteDao.findOptional(identiteTest.getId());
        assertNotNull(identiteOptional);
        Identite updatedIdentite = identiteOptional.get();
        assertNotSame(identiteTest, updatedIdentite);
        assertEquals(updatedIdentite.getEmail(), identiteTest.getEmail());
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateNonExistentIdentite()
    {
        final IdentiteTest identiteTest = new IdentiteTest("John Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        identiteDao.update(identiteTest);
    }

    @Test
    public void testDelete()
    {
        final IdentiteTest identiteTest = new IdentiteTest("John Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identiteTest, "TestDelete", new Date());
        identiteDao.delete(identiteTest);
        assertNull(identiteDao.find(identiteTest.getNom(), identiteTest.getPrenom(), identiteTest.getSex(), identiteTest.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0));
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteNonExistentIdentite()
    {
        identiteDao.delete(new IdentiteTest("", "", (short)0, null, Identite.TypeIdentite.CANDIDAT));
    }

    @Test
    public void testFind()
    {
        identiteDao.insert(new IdentiteTest("Alice", "Doe", 'F', "alice.doe@example.com", Identite.TypeIdentite.CANDIDAT), "TestFindCandidatFemale", new Date());
        identiteDao.insert(new IdentiteTest("Bob", "Smith", 'M', "bob.smith@example.com", Identite.TypeIdentite.CANDIDAT), "TestFindCandidatMale", new Date());
        identiteDao.insert(new IdentiteTest("Charlie", "Brown", 'M', "charlie.brown@example.com", Identite.TypeIdentite.FORMATEUR), "TestFindFormateurMale", new Date());
        List<Identite> identites = identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(2, identites.size());
        Identite alice = identites.get(0);
        Identite bob = identites.get(1);
        assertEquals(alice.getEmail(), "alice.doe@example.com");
        assertEquals(bob.getEmail(), "bob.smith@example.com");
        List<Identite> identitesFemale = identiteDao.find(null, null, "F", null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, identitesFemale.size());
        Identite charlie = identiteDao.find(null, null, null, "charlie.brown@example.com", Identite.TypeIdentite.FORMATEUR).get(0);
        assertEquals(charlie.getEmail(), "charlie.brown@example.com");
        assertEquals(charlie.getType(), Identite.TypeIdentite.FORMATEUR);
    }

    @Test
    public void testFindCandidat()
    {
        IdentiteTest identiteTest1 = new IdentiteTest("Alice", "Doe", 'F', "alice.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identiteTest1, "TestFindCandidat", new Date());
        Candidat[] candidates = identiteDao.findCandidat(null, null, null, null);
        assertArrayEquals(new Candidat[]{identiteTest1}, candidates);
        IdentiteTest identiteTest2 = new IdentiteTest("Bob", "Smith", 'M', "bob.smith@example.com", Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identiteTest2, "TestFindCandidatNotCandidat", new Date());
        assertArrayEquals(new Candidat[0], identiteDao.findCandidat(null, null, null, null));
    }

    @Test
    public void testFindRespForm()
    {
        IdentiteTest identiteTest1 = new IdentiteTest("Alice", "Doe", 'F', "alice.doe@example.com", Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identiteTest1, "TestFindRespFormFemale", new Date());
        RespFormation[] respFormations = identiteDao.findRespForm(null, null, null, null);
        assertArrayEquals(new RespFormation[]{identiteTest1}, respFormations);
        IdentiteTest identiteTest2 = new IdentiteTest("Bob", "Smith", 'M', "bob.smith@example.com", Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identiteTest2, "TestFindRespFormNotFormateur", new Date());
        assertArrayEquals(new RespFormation[0], identiteDao.findRespForm(null, null, null, null));
    }
}