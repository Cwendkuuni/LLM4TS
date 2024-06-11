package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private OracleIdentiteDao dao;

    @Before
    public void setUp() {
        this.emf = Persistence.createEntityManagerFactory("Identite");
        this.em = this.emf.createEntityManager();
        this.dao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown() {
        this.em.close();
        this.emf.close();
    }

    @Test
    @Transactional
    public void testInsert() {
        final Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        this.dao.insert(identite, null, null);

        final Identite foundIdentite = (Identite) this.em.find(Identite.class, identite.getId());
        assertNotNull(foundIdentite);
        assertEquals(identite.getNom(), foundIdentite.getNom());
        assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
        assertEquals(identite.getEmail(), foundIdentite.getEmail());
        assertEquals(identite.getSex(), foundIdentite.getSex());
        assertEquals(identite.getType(), foundIdentite.getType());
    }

    @Test
    @Transactional
    public void testUpdate() {
        final Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        this.dao.insert(identite, null, null);

        identite.setEmail("john.new@example.com");

        this.dao.update(identite);

        final Identite foundIdentite = (Identite) this.em.find(Identite.class, identite.getId());
        assertEquals(identite.getEmail(), foundIdentite.getEmail());
    }

    @Test
    @Transactional
    public void testDelete() {
        final Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setEmail("john.doe@example.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        this.dao.insert(identite, null, null);

        this.dao.delete(identite);

        assertNull(this.em.find(Identite.class, identite.getId()));
    }

    @Test
    @Transactional
    public void testFind() {
        final Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setEmail("john1.doe1@example.com");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        this.dao.insert(identite1, null, null);

        final Identite identite2 = new Identite();
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setEmail("john2.doe2@example.com");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        this.dao.insert(identite2, null, null);

        final List<Identite> foundIdentites = this.dao.find(null, null, null, null, null);
        assertEquals(2, foundIdentites.size());
        assertTrue(foundIdentites.contains(identite1));
        assertTrue(foundIdentites.contains(identite2));
    }

    @Test
    @Transactional
    public void testFindCandidat() {
        final Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setEmail("john1.doe1@example.com");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        this.dao.insert(identite1, null, null);

        final Candidat candidat1 = new Candidat();
        candidat1.setIdentite(identite1);

        this.dao.insert(candidat1, null, null);

        final Candidat[] foundCandidats = this.dao.findCandidat(null, null, null, null);
        assertEquals(1, foundCandidats.length);
        assertEquals(candidat1, foundCandidats[0]);
    }

    @Test
    @Transactional
    public void testFindRespForm() {
        final Identite identite1 = new Identite();
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setEmail("john1.doe1@example.com");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);
        this.dao.insert(identite1, null, null);

        final RespFormation formation1 = new RespFormation();
        formation1.setIdentite(identite1);
        formation1.setDateDebut(Calendar.getInstance().getTime());
        formation1.setDateFin(Calendar.getInstance().getTime());

        this.dao.insert(formation1, null, null);

        final RespFormation[] foundFormations = this.dao.findRespForm(null, null, null, null);
        assertEquals(1, foundFormations.length);
        assertEquals(formation1, foundFormations[0]);
    }
}