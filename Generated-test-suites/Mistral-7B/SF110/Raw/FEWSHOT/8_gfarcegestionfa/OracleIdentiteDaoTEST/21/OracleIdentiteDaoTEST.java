package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao identiteDao;
    private EntityManagerFactory emf;

    @Test
    public void testInsert() {
        identiteDao = OracleIdentiteDao.getDAO();

        Identite identite = new Identite();
        identite.setNom("Dupond");
        identite.setPrenom("Jean");
        identite.setEmail("jean.dupond@example.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        identiteDao.insert(identite, null, dateLimite);

        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("FROM Identite i WHERE i.email = 'jean.dupond@example.com'");
        List<Identite> resultList = query.getResultList();

        assertTrue(resultList.size() > 0);
        assertSame(identite, resultList.get(0));

        em.close();
    }

    @Test
    public void testUpdate() {
        identiteDao = OracleIdentiteDao.getDAO();

        emf = Persistence.createEntityManagerFactory("Identite");
        identiteDao = (OracleIdentiteDao) emf.createEntityManager().find(OracleIdentiteDao.class, 1L);

        Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail("new.email@example.com");
        identite.setNom("Dupond");
        identite.setPrenom("Jean");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.update(identite);

        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("FROM Identite i WHERE i.email = 'new.email@example.com'");
        List<Identite> resultList = query.getResultList();

        assertTrue(resultList.size() > 0);
        assertSame(identite, resultList.get(0));

        em.close();
        emf.close();
    }

    @Test
    public void testDelete() {
        identiteDao = OracleIdentiteDao.getDAO();

        emf = Persistence.createEntityManagerFactory("Identite");
        identiteDao = (OracleIdentiteDao) emf.createEntityManager().find(OracleIdentiteDao.class, 1L);

        identiteDao.delete(new Identite());

        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("FROM Identite i WHERE i.id = 1");
        List<Identite> resultList = query.getResultList();

        assertTrue(resultList.isEmpty());

        em.close();
        emf.close();
    }

    @Test
    public void testFind() {
        identiteDao = OracleIdentiteDao.getDAO();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        identiteDao = (OracleIdentiteDao) emf.createEntityManager().find(OracleIdentiteDao.class, 1L);

        List<Identite> resultList = identiteDao.find(null, null, null, null, null);

        EntityManager em = emf.createEntityManager();
        List<Identite> expectedList = em.createQuery("FROM Identite i").getResultList();

        assertEquals(expectedList.size(), resultList.size());
        for (Identite identite : resultList) {
            boolean found = false;
            for (Identite expectedIdentite : expectedList) {
                if (identite.getId().equals(expectedIdentite.getId())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }

        em.close();
        emf.close();
    }

    @Test
    public void testFindCandidat() {
        identiteDao = OracleIdentiteDao.getDAO();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        identiteDao = (OracleIdentiteDao) emf.createEntityManager().find(OracleIdentiteDao.class, 1L);

        List<Identite> resultList = identiteDao.find(null, null, null, "jean.dupond@example.com", Identite.TypeIdentite.CANDIDAT);

        EntityManager em = emf.createEntityManager();
        List<Candidat> expectedList = em.createQuery("FROM Candidat c").getResultList();

        assertEquals(expectedList.size(), resultList.size());
        for (Identite identite : resultList) {
            if (identite instanceof Candidat) {
                Candidat candidat = (Candidat) identite;
                boolean found = false;
                for (Candidat expectedCandidat : expectedList) {
                    if (candidat.getId().equals(expectedCandidat.getId())) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }

        em.close();
        emf.close();
    }

    @Test
    public void testFindRespForm() {
        identiteDao = OracleIdentiteDao.getDAO();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        identiteDao = (OracleIdentiteDao) emf.createEntityManager().find(OracleIdentiteDao.class, 1L);

        List<Identite> resultList = identiteDao.find(null, null, null, "jean.dupond@example.com", Identite.TypeIdentite.FORMATEUR);

        EntityManager em = emf.createEntityManager();
        List<RespFormation> expectedList = em.createQuery("FROM RespFormation rf").getResultList();

        assertEquals(expectedList.size(), resultList.size());
        for (Identite identite : resultList) {
            if (identite instanceof RespFormation) {
                RespFormation respFormation = (RespFormation) identite;
                boolean found = false;
                for (RespFormation expectedRespFormation : expectedList) {
                    if (respFormation.getId().equals(expectedRespFormation.getId())) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }

        em.close();
        emf.close();
    }

    @Test
    public void testFindNull() {
        identiteDao = OracleIdentiteDao.getDAO();

        List<Identite> resultList = identiteDao.find(null, null, null, null, null);

        assertTrue(resultList.isEmpty());
    }

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("Identite");
        identiteDao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

}