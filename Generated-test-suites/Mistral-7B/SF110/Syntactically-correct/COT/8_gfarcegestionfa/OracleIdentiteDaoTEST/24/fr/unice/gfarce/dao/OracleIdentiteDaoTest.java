package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest
{
    private EntityManagerFactory emf;
    private EntityManager em;
    private IdentiteDao identiteDao;

    @Before
    public void setUp()
    {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        identiteDao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown()
    {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testInsert()
    {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail("test@test.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.insert(identite, "Form1", calendar);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals("Test", retrievedIdentite.getNom());
        assertEquals("Test", retrievedIdentite.getPrenom());
        assertEquals("M", retrievedIdentite.getSex());
        assertEquals("test@test.com", retrievedIdentite.getEmail());
        assertEquals(Identite.TypeIdentite.CANDIDAT, retrievedIdentite.getType());
    }

    @Test(expected = Exception.class)
    public void testInsertException()
    {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail("test@test.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("Identite");
        EntityManager em2 = emf2.createEntityManager();
        IdentiteDao identiteDao2 = OracleIdentiteDao.getDAO();

        try {
            identiteDao2.insert(identite, "Form1", calendar);
            fail("Expected exception not thrown");
        }
        catch (Exception e) {
            // expected exception
        }
        finally {
            em2.close();
            emf2.close();
        }
    }

    @Test
    public void testUpdate()
    {
        String oldEmail = "oldEmail@oldEmail.com";
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail(oldEmail);
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "Form1", calendar);

        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(identite.getId());
        updatedIdentite.setNom("UpdatedTest");
        updatedIdentite.setPrenom("UpdatedTest");
        updatedIdentite.setEmail("updatedEmail@updatedEmail.com");
        updatedIdentite.setSex("M");
        updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.update(updatedIdentite);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals("UpdatedTest", retrievedIdentite.getNom());
        assertEquals("UpdatedTest", retrievedIdentite.getPrenom());
        assertEquals("M", retrievedIdentite.getSex());
        assertEquals("updatedEmail@updatedEmail.com", retrievedIdentite.getEmail());
        assertEquals(Identite.TypeIdentite.CANDIDAT, retrievedIdentite.getType());
    }

    @Test
    public void testUpdateNoChange()
    {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail("test@test.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "Form1", calendar);

        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(identite.getId());
        updatedIdentite.setNom("Test");
        updatedIdentite.setPrenom("Test");
        updatedIdentite.setEmail("test@test.com");
        updatedIdentite.setSex("M");
        updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.update(updatedIdentite);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertEquals("Test", retrievedIdentite.getNom());
        assertEquals("Test", retrievedIdentite.getPrenom());
        assertEquals("M", retrievedIdentite.getSex());
        assertEquals("test@test.com", retrievedIdentite.getEmail());
        assertEquals(Identite.TypeIdentite.CANDIDAT, retrievedIdentite.getType());
    }

    @Test(expected = Exception.class)
    public void testUpdateException()
    {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail("test@test.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "Form1", calendar);

        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(-1); // invalid ID
        updatedIdentite.setNom("UpdatedTest");
        updatedIdentite.setPrenom("UpdatedTest");
        updatedIdentite.setEmail("updatedEmail@updatedEmail.com");
        updatedIdentite.setSex("M");
        updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);

        try {
            identiteDao.update(updatedIdentite);
            fail("Expected exception not thrown");
        }
        catch (Exception e) {
            // expected exception
        }
    }

    @Test
    public void testDelete()
    {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail("test@test.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "Form1", calendar);

        identiteDao.delete(identite);
        Identite retrievedIdentite = em.find(Identite.class, identite.getId());
        assertNull(retrievedIdentite);
    }

    @Test(expected = Exception.class)
    public void testDeleteException()
    {
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setEmail("test@test.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "Form1", calendar);

        Identite invalidIdentite = new Identite();
        invalidIdentite.setId(-1); // invalid ID

        try {
            identiteDao.delete(invalidIdentite);
            fail("Expected exception not thrown");
        }
        catch (Exception e) {
            // expected exception
        }
    }

    @Test
    public void testFind()
    {
        Identite identite1 = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("M");
        identite1.setEmail("test1@test1.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite1, "Form1", calendar);

        Identite identite2 = new Identite();
        Calendar calendar2 = Calendar.getInstance();
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("M");
        identite2.setEmail("test2@test2.com");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite2, "Form2", calendar2);

        List<Identite> result = identiteDao.find(null, null, null, null, null);
        assertEquals(2, result.size());

        List<Identite> resultFiltered = identiteDao.find("Test", null, null, null, null);
        assertEquals(1, resultFiltered.size());
        assertEquals("Test1", resultFiltered.get(0).getNom());

        List<Identite> resultFiltered2 = identiteDao.find(null, "Test2", null, null, null);
        assertEquals(1, resultFiltered2.size());
        assertEquals("Test2", resultFiltered2.get(0).getPrenom());

        List<Identite> resultFiltered3 = identiteDao.find(null, null, "M", null, null);
        assertEquals(2, resultFiltered3.size());

        List<Identite> resultFiltered4 = identiteDao.find(null, null, null, "test1@test1.com", null);
        assertEquals(1, resultFiltered4.size());
        assertEquals("test1@test1.com", resultFiltered4.get(0).getEmail());

        List<Identite> resultFiltered5 = identiteDao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertEquals(0, resultFiltered5.size());
    }

    @Test
    public void testFindCandidat()
    {
        Identite identite1 = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("M");
        identite1.setEmail("test1@test1.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite1, "Form1", calendar);

        Identite identite2 = new Identite();
        Calendar calendar2 = Calendar.getInstance();
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("M");
        identite2.setEmail("test2@test2.com");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite2, "Form2", calendar2);

        Candidat[] result = identiteDao.findCandidat("Test1", null, null, null);
        assertEquals(1, result.length);
        assertEquals("Test1", result[0].getNom());
        assertEquals("Test1", result[0].getPrenom());
        assertEquals("M", result[0].getSex());
        assertEquals("test1@test1.com", result[0].getEmail());

        Candidat[] resultEmpty = identiteDao.findCandidat("Test3", null, null, null);
        assertEquals(0, resultEmpty.length);
    }

    @Test
    public void testFindRespForm()
    {
        Identite identite1 = new Identite();
        Calendar calendar = Calendar.getInstance();
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("F");
        identite1.setEmail("test1@test1.com");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite1, "Form1", calendar);

        Identite identite2 = new Identite();
        Calendar calendar2 = Calendar.getInstance();
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("M");
        identite2.setEmail("test2@test2.com");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite2, "Form2", calendar2);

        RespFormation[] result = identiteDao.findRespForm("Test1", null, null, null);
        assertEquals(1, result.length);
        assertEquals("Test1", result[0].getNom());
        assertEquals("Test1", result[0].getPrenom());
        assertEquals("F", result[0].getSex());
        assertEquals("test1@test1.com", result[0].getEmail());

        RespFormation[] resultEmpty = identiteDao.findRespForm("Test3", null, null, null);
        assertEquals(0, resultEmpty.length);
    }
}