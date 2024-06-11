import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 private EntityManagerFactory emf;
 private EntityManager em;
 private EntityTransaction tx;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 tx = em.getTransaction();
 tx.begin();
 }

 @After
 public void tearDown() {
 if (tx != null) {
 tx.rollback();
 }
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "test", new Date());

 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertEquals("john.doe@example.com", fetchedIdentite.getEmail());
 assertEquals("M", fetchedIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, fetchedIdentite.getType());
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 em.persist(identite);
 tx.commit();

 Identite updatedIdentite = new Identite();
 updatedIdentite.setId(identite.getId());
 updatedIdentite.setEmail("john.doe2@example.com");
 updatedIdentite.setNom("Doe2");

 identiteDao.update(updatedIdentite);

 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("Doe2", fetchedIdentite.getNom());
 assertEquals("john.doe2@example.com", fetchedIdentite.getEmail());
 }

 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 em.persist(identite);
 tx.commit();

 identiteDao.delete(identite);

 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertNull(fetchedIdentite);
 }

 @Test
 public void testFindIdentites() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertNotNull(identites);
 assertEquals(1, identites.size());
 Identite identite = identites.get(0);
 assertEquals(nom, identite.getNom());
 assertEquals(prenom, identite.getPrenom());
 assertEquals(sex, identite.getSex());
 assertEquals(email, identite.getEmail());
 assertEquals(type, identite.getType());
 }

 @Test
 public void testFindCandidats() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 List<Candidat> candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertNotNull(candidats);
 assertEquals(1, candidats.length);
 Candidat candidat = candidats[0];
 assertNotNull(candidat);
 }

 @Test (expected = NullPointerException.class)
 public void testFindCandidatsWithNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 List<Candidat> candidats = identiteDao.findCandidat(nom, prenom, sex, null);
 fail("Expected NullPointerException but got " + candidats);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindCandidatsWithInvalidType() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = null;
 List<Candidat> candidats = identiteDao.findCandidat(nom, prenom, sex, email, type);
 fail("Expected IllegalArgumentException but got " + candidats);
 }

 @Test
 public void testFindRespFormations() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 List<RespFormation> respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 assertEquals(0, respFormations.length);
 }

 @Test (expected = NullPointerException.class)
 public void testFindRespFormWithNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 List<RespFormation> respFormations = identiteDao.findRespForm(nom, prenom, sex, null);
 fail("Expected NullPointerException but got " + respFormations);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindRespFormWithInvalidType() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = null;
 List<RespFormation> respFormations = identiteDao.findRespForm(nom, prenom, sex, email, type);
 fail("Expected IllegalArgumentException but got " + respFormations);
 }
}