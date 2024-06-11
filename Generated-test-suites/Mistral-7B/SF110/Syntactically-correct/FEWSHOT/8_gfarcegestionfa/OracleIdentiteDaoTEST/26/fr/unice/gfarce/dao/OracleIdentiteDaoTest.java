package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private IdentiteDao identiteDao;
    private EntityManager em;

    @Before
    public void init() {
        identiteDao = OracleIdentiteDao.getDAO();
        em = identiteDao.getEntityManager();
    }

    @After
    public void close() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "test", Calendar.getInstance());
        Identite found = em.find(Identite.class, identite.getId());
        assertNotNull(found);
        assertEquals(identite.getNom(), found.getNom());
        assertEquals(identite.getPrenom(), found.getPrenom());
        assertEquals(identite.getSex(), found.getSex());
        assertEquals(identite.getEmail(), found.getEmail());
        assertEquals(identite.getType(), found.getType());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "test", Calendar.getInstance());
        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(identite.getId());
        updatedIdentite.setEmail("new.email@example.com");
        Identite found = null;
        try {
            found = identiteDao.update(updatedIdentite);
            identite = em.find(Identite.class, identite.getId());
            assertEquals(updatedIdentite.getEmail(), identite.getEmail());
        } finally {
            if (found != null) {
                em.remove(found);
            }
        }
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "test", Calendar.getInstance());
        Identite found = em.find(Identite.class, identite.getId());
        assertNotNull(found);
        identiteDao.delete(identite);
        Identite deletedIdentite = em.find(Identite.class, identite.getId());
        assertNull(deletedIdentite);
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex('M');
        identite1.setEmail("john.doe1@example.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite1, "test", Calendar.getInstance());
        Identite identite2 = new Identite();
        identite2.setNom("Doe");
        identite2.setPrenom("John");
        identite2.setSex('M');
        identite2.setEmail("john.doe2@example.com");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite2, "test", Calendar.getInstance());
        List<Identite> result = identiteDao.find(null, null, null, null, null);
        assertNotNull(result);
        assertNotEmpty(result);
        Identite found = null;
        for (Identite identite : result) {
            if (identite.getEmail().equals("john.doe1@example.com")) {
                found = identite;
                break;
            }
        }
        assertNotNull(found);
        Identite anotherResult = em.find(Identite.class, found.getId());
        assertEquals(found.getEmail(), anotherResult.getEmail());
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex('M');
        identite1.setEmail("john.doe1@example.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite1, "test", Calendar.getInstance());
        Candidat candidat1 = new Candidat();
        candidat1.setIdentite(identite1);
        Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex('F');
        identite2.setEmail("john.doe2@example.com");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite2, "test", Calendar.getInstance());
        List<Candidat> result = identiteDao.findCandidat(null, null, null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        Candidat found = null;
        for (Candidat candidate : result) {
            if (candidate.getIdentite().getEmail().equals("john.doe1@example.com")) {
                found = candidate;
                break;
            }
        }
        assertNotNull(found);
        Candidat anotherResult = em.find(Candidat.class, found.getId());
        assertEquals(found.getIdentite().getEmail(), anotherResult.getIdentite().getEmail());
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setNom("Doe");
        identite1.setPrenom("John");
        identite1.setSex('M');
        identite1.setEmail("john.doe1@example.com");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite1, "test", Calendar.getInstance());
        RespFormation respFormation1 = new RespFormation();
        respFormation1.setIdentite(identite1);
        Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex('F');
        identite2.setEmail("john.doe2@example.com");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite2, "test", Calendar.getInstance());
        List<RespFormation> result = identiteDao.findRespForm(null, null, null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        RespFormation found = null;
        for (RespFormation respFormation : result) {
            if (respFormation.getIdentite().getEmail().equals("john.doe1@example.com")) {
                found = respFormation;
                break;
            }
        }
        assertNotNull(found);
        RespFormation anotherResult = em.find(RespFormation.class, found.getId());
        assertEquals(found.getIdentite().getEmail(), anotherResult.getIdentite().getEmail());
        assertNotSame(found.getIdentite(), anotherResult.getIdentite());
    }
}