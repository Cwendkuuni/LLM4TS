package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest implements IdentiteDao {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @After
    public void close() {
        tx.rollback();
        em.close();
        emf.close();
    }

    @Test
    public void testInsert() {
        Identite ident = new Identite();
        Calendar cal = Calendar.getInstance();

        tx.begin();
        this.insert(ident, "test", cal);
        tx.commit();

        Identite found = em.find(Identite.class, ident.getId());
        assertNotNull(found);
        assertEquals("test", found.getNom());
        assertEquals(cal.getTime(), found.getDateLimite());
    }

    @Test
    public void testUpdate() {
        Identite ident = new Identite();
        Identite savedIdent = new Identite();
        Calendar cal = Calendar.getInstance();

        tx.begin();
        this.insert(ident, "test", cal);
        savedIdent = ident;
        ident.setEmail("newEmail@email.com");
        this.update(ident);
        tx.commit();

        Identite found = em.find(Identite.class, savedIdent.getId());
        assertEquals("newEmail@email.com", found.getEmail());
        assertNotSame(ident, found);
    }

    @Test
    public void testDelete() {
        Identite ident = new Identite();
        Calendar cal = Calendar.getInstance();

        tx.begin();
        this.insert(ident, "test", cal);
        this.delete(ident);
        tx.commit();

        Identite found = em.find(Identite.class, ident.getId());
        assertNull(found);
    }

    @Test
    public void testFind() {
        Identite ident1 = new Identite();
        Identite ident2 = new Identite();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        tx.begin();
        this.insert(ident1, "ident1", cal1);
        this.insert(ident2, "ident2", cal2);
        tx.commit();

        List<Identite> results = this.find(null, null, null, null, null);
        assertEquals(2, results.size());

        results = this.find("ident1", null, null, null, null);
        assertEquals(1, results.size());
        assertEquals(ident1, results.get(0));

        results = this.find(null, "prenom", null, null, null);
        assertTrue(results.isEmpty());

        results = this.find(null, null, "sex", null, null);
        assertEquals(2, results.size());

        results = this.find(null, null, null, "email@email.com", null);
        assertEquals(0, results.size());

        results = this.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, results.size());
        assertEquals(ident1, results.get(0));

        results = this.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, results.size());
        assertEquals(ident2, results.get(0));
    }

    @Test
    public void testFindCandidat() {
        Identite ident1 = new Identite();
        Identite ident2 = new Identite();
        Candidat candidat1 = new Candidat();
        Candidat candidat2 = new Candidat();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        tx.begin();
        this.insert(ident1, "ident1", cal1);
        this.insert(ident2, "ident2", cal2);
        candidat1.setIdentite(ident1);
        candidat2.setIdentite(ident2);
        this.em.persist(candidat1);
        this.em.persist(candidat2);
        tx.commit();

        Candidat[] results = this.findCandidat(null, null, null, null);
        assertEquals(2, results.length);

        results = this.findCandidat(null, null, null, "email@email.com");
        assertEquals(1, results.length);
        assertEquals(candidat1, results[0]);

        results = this.findCandidat("nom", null, null, null);
        assertEquals(1, results.length);
        assertEquals(candidat1, results[0]);

        results = this.findCandidat(null, "prenom", null, null);
        assertTrue(results.length == 0);

        results = this.findCandidat(null, null, "sex", null);
        assertEquals(2, results.length);
    }

    @Test
    public void testFindRespForm() {
        Identite ident1 = new Identite();
        Identite ident2 = new Identite();
        RespFormation respForm1 = new RespFormation();
        RespFormation respForm2 = new RespFormation();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        tx.begin();
        this.insert(ident1, "ident1", cal1);
        this.insert(ident2, "ident2", cal2);
        respForm1.setIdentite(ident1);
        respForm1.setDateDebut(new Date(cal1.getTimeInMillis()));
        respForm1.setDateFin(new Date(cal1.getTimeInMillis() + 1000L * 60L * 60L));
        respForm2.setIdentite(ident2);
        respForm2.setDateDebut(new Date(cal2.getTimeInMillis()));
        respForm2.setDateFin(new Date(cal2.getTimeInMillis() + 1000L * 60L * 60L));
        this.em.persist(respForm1);
        this.em.persist(respForm2);
        tx.commit();

        RespFormation[] results = this.findRespForm(null, null, null, null);
        assertEquals(2, results.length);

        results = this.findRespForm(null, null, null, "email@email.com");
        assertEquals(1, results.length);
        assertEquals(respForm1, results[0]);

        results = this.findRespForm("nom", null, null, null);
        assertEquals(1, results.length);
        assertEquals(respForm1, results[0]);

        results = this.findRespForm(null, "prenom", null, null);
        assertTrue(results.length == 0);

        results = this.findRespForm(null, null, "sex", null);
        assertEquals(2, results.length);
    }
}