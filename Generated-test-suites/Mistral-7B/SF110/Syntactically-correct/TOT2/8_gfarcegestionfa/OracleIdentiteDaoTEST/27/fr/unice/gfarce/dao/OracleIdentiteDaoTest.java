import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoTest;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class OracleIdentiteDaoTest {
    @Test
    public void testInsert() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();

        dao.insert(identite, "Test", calendar);
        assertNotNull(identite.getId());
    }

    @Test
    public void testInsertIdentiteAlreadyExists() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        Identite identite2 = new Identite();
        Calendar calendar = Calendar.getInstance();
        String email = "test@email.com";

        // Insert identite with the given email
        dao.insert(identite, "Test", calendar);

        // Try to insert another identite with the same email
        identite2.setEmail(email);
        dao.insert(identite2, "Test2", calendar);
    }

    @Test(expected = NullPointerException.class)
    public void testInsertNullIdentite() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = null;
        String nomForm = "Test";
        Calendar dateLimite = Calendar.getInstance();

        dao.insert(identite, nomForm, dateLimite);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullNomForm() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();
        String nomForm = null;

        dao.insert(identite, nomForm, calendar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullDateLimite() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        String nomForm = "Test";
        Calendar calendar = null;

        dao.insert(identite, nomForm, calendar);
    }

    @Test
    public void testUpdate() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        Identite updatedIdentite = new Identite();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        em.persist(identite);
        tx.commit();
        em.close();

        updatedIdentite.setEmail("new@email.com");
        updatedIdentite.setNom("NewTest");
        updatedIdentite.setPrenom("NewTest");

        dao.update(updatedIdentite);

        // Test if the updated Identite has been saved in the database
        Identite result = null;
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        result = (Identite) em.find(Identite.class, updatedIdentite.getId());
        tx.commit();
        em.close();

        assertEquals(updatedIdentite.getEmail(), result.getEmail());
        assertEquals(updatedIdentite.getNom(), result.getNom());
        assertEquals(updatedIdentite.getPrenom(), result.getPrenom());

        emf.close();
    }

    @Test
    public void testDelete() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Identite identite = new Identite();
        tx.begin();

        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        em.persist(identite);
        tx.commit();

        dao.delete(identite);

        Identite result = em.find(Identite.class, identite.getId());
        assertNull(result);

        tx.begin();
        em.remove(identite);
        tx.commit();

        em.close();
        emf.close();
    }

    @Test
    public void testFind() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Identite identite1 = new Identite();
        Identite identite2 = new Identite();
        identite1.setEmail("test@email1.com");
        identite1.setNom("Test1");
        identite1.setPrenom("Test1");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        em.persist(identite1);

        identite2.setEmail("test@email2.com");
        identite2.setNom("Test2");
        identite2.setPrenom("Test2");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        em.persist(identite2);

        tx.commit();

        List<Identite> result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(2, result.size());

        List<Identite> result1 = dao.find("Test1", null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, result1.size());
        assertEquals(identite1.getEmail(), result1.get(0).getEmail());

        List<Identite> result2 = dao.find(null, "Test2", null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, result2.size());
        assertEquals(identite2.getEmail(), result2.get(0).getEmail());

        em.close();
        emf.close();
    }

    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Identite identite = new Identite();
        List<Candidat> candidates = new ArrayList<>();

        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Candidat candidat = new Candidat();
        candidat.setIdentite(identite);
        candidat.setDateNaissance(Date.valueOf(LocalDate.of(1990, 1, 1)));
        em.persist(identite);
        em.persist(candidat);

        tx.commit();

        List<Candidat> result = dao.findCandidat("Test", null, null, "test@email.com");
        assertEquals(1, result.length);
        assertEquals(identite.getEmail(), result[0].getIdentite().getEmail());

        em.close();
        emf.close();
    }

    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Identite identite = new Identite();
        List<RespFormation> respFormations = new ArrayList<>();

        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        RespFormation respFormation = new RespFormation();
        respFormation.setIdentite(identite);
        respFormation.setDateDebut(Date.valueOf(LocalDate.of(2022, 1, 1)));
        respFormation.setDateFin(Date.valueOf(LocalDate.of(2022, 12, 31)));
        em.persist(identite);
        em.persist(respFormation);

        tx.commit();

        List<RespFormation> result = dao.findRespForm("Test", null, null, "test@email.com");
        assertEquals(1, result.length);
        assertEquals(identite.getEmail(), result[0].getIdentite().getEmail());

        em.close();
        emf.close();
    }

    @Test
    public void testFindEmptyList() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        tx.commit();
        em.close();

        List<Identite> result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertTrue(Collections.isEmpty(result));

        emf.close();
    }

    @Test
    public void testFindNullList() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();

        List<Identite> result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertArrayEquals(new Identite[0], result.toArray(new Identite[0]));
    }

    @Test
    public void testInsertException() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();

        Identite identite = null; // Test with null Identite
        String nomForm = null; // Test with null nomForm
        Calendar dateLimite = null; // Test with null dateLimite

        dao.insert(identite, nomForm, dateLimite);
    }
}