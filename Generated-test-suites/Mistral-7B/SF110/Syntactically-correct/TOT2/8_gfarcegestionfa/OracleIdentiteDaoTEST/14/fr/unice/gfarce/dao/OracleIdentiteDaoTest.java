package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.junit.runner.*;

import fr.unice.gfarce.*;
import fr.unice.gfarce.dao.*;

@RunWith(JUnit4.class)
public class OracleIdentiteDaoTest
{
    private static IdentiteDao identiteDao;
    private static EntityManagerFactory emf;

    @BeforeClass
    public static void init()
    {
        identiteDao = OracleIdentiteDao.getDAO();
        emf = Persistence.createEntityManagerFactory("Identite");
    }

    @AfterClass
    public static void close()
    {
        if (emf != null)
        {
            emf.close();
        }
    }

    @Before
    public void setUp()
    {
        if (identiteDao == null)
        {
            identiteDao = OracleIdentiteDao.getDAO();
        }
    }

    @Test
    public void testInsert()
    {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.insert(identite, "form.html", new GregorianCalendar(2022, 11, 3));
        Identite retrievedIdentite = identiteDao.find(identite.getNom(), identite.getPrenom(), null, null, null)[0];

        assertTrue(retrievedIdentite.getId() > 0);
    }

    @Test
    public void testUpdate()
    {
        Identite identite = new Identite();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        em.persist(identite);
        tx.commit();

        identite.setEmail("john.doe@example.com");

        identiteDao.update(identite);
        Identite retrievedIdentite = identiteDao.find(identite.getNom(), identite.getPrenom(), null, null, null)[0];

        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
    }

    @Test
    public void testDelete()
    {
        Identite identite = new Identite();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        em.persist(identite);
        tx.commit();

        identiteDao.delete(identite);
        Identite retrievedIdentite = identiteDao.find(identite.getNom(), identite.getPrenom(), null, null, null);

        assertNull(retrievedIdentite);
    }

    @Test
    public void testFind_all()
    {
        Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite1, "form1.html", new GregorianCalendar(2022, 11, 3));

        Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite2, "form2.html", new GregorianCalendar(2022, 11, 4));

        Identite[] results = identiteDao.find(null, null, null, null, null);

        assertTrue(results.length == 2);
    }

    @Test
    public void testFind_nom_candidat()
    {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "form.html", new GregorianCalendar(2022, 11, 3));

        Identite[] results = identiteDao.find(identite.getNom(), null, null, null, Identite.TypeIdentite.CANDIDAT);

        assertTrue(results.length > 0);
    }

    @Test
    public void testFind_nom_formateur()
    {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite, "form.html", new GregorianCalendar(2022, 11, 3));

        Identite[] results = identiteDao.find(identite.getNom(), null, null, null, Identite.TypeIdentite.FORMATEUR);

        assertTrue(results.length > 0);
    }

    @Test
    public void testInsert_nullIdentite()
    {
        Identite identite = null;
        try
        {
            identiteDao.insert(identite, "form.html", new GregorianCalendar(2022, 11, 3));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testUpdate_nullIdentite()
    {
        Identite identite = null;
        try
        {
            identiteDao.update(identite);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testDelete_nullIdentite()
    {
        Identite identite = null;
        try
        {
            identiteDao.delete(identite);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testFind_nullParam()
    {
        try
        {
            identiteDao.find(null, null, null, null, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testFind_nullType()
    {
        try
        {
            identiteDao.find(null, null, null, null, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testFind_persistenceException()
    {
        EntityManagerFactory emfLocal = null;
        EntityManager em = null;
        EntityTransaction tx = null;

        try
        {
            emfLocal = Persistence.createEntityManagerFactory("Identite");
            em = emfLocal.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            IdentiteDao identiteDaoLocal = new OracleIdentiteDao();
            identiteDaoLocal.setEm(em);

            Identite identite = new Identite();
            identite.setNom("Doe");
            identite.setPrenom("John");
            identite.setSex("M");
            identite.setType(Identite.TypeIdentite.CANDIDAT);
            identiteDaoLocal.insert(identite, "form.html", new GregorianCalendar(2022, 11, 3));

            List<Identite> results = identiteDaoLocal.find(identite.getNom(), null, null, null, null);
            assertTrue(results.size() > 0);

            tx.rollback();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (tx != null)
            {
                tx.commit(); // Rollback failed, commit instead to test if PersistenceException is correctly caught
            }
        }
        finally
        {
            if (em != null)
            {
                em.close();
            }
            if (emfLocal != null)
            {
                emfLocal.close();
            }
        }

        try
        {
            Identite[] results = identiteDao.find(null, null, null, null, null);
            fail("Expected PersistenceException");
        }
        catch (PersistenceException e)
        {
            // Expected
        }
    }
}