import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 identiteDao = OracleIdentiteDao.getDAO();
 em = emf.createEntityManager();
 }

 @After
 public void tearDown() {
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
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 dateLimite.set(2025, 12 - 1, 31); // Last day of the year 2025
 identite.setDateLimite(dateLimite.getTime());
 identiteDao.insert(identite, "test", dateLimite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("M", foundIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setId(1L); // Assuming an existing Identite with id 1
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(identite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("M", foundIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNull() {
 Identite identite = null;
 identiteDao.insert(identite, "test", null);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNull() {
 Identite identite = null;
 identiteDao.update(identite);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteEmptyName() {
 Identite identite = new Identite();
 identite.setNom("");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 dateLimite.set(2025, 12 - 1, 31); // Last day of the year 2025
 identite.setDateLimite(dateLimite.getTime());
 identiteDao.insert(identite, "test", dateLimite);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNoIdentiteFound() {
 Identite identite = new Identite();
 identite.setId(100L); // Non-existent Identite
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(identite);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }

 @Test
 public void testFindIdentites() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertEquals(1, identites.size()); // Assuming one matching Identite
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("M", foundIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 }

 @Test
 public void testFindIdentitesNullParameter() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindCandidats() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(1, candidats.length); // Assuming one matching Candidat
 Candidat foundCandidat = candidats[0];
 assertEquals("Doe", foundCandidat.getNom());
 assertEquals("John", foundCandidat.getPrenom());
 assertEquals("M", foundCandidat.getSex());
 assertEquals("john.doe@example.com", foundCandidat.getEmail());
 }

 @Test
 public void testFindRespFormations() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(1, respFormations.length); // Assuming one matching RespFormation
 RespFormation foundRespFormation = respFormations[0];
 assertEquals("Doe", foundRespFormation.getNom());
 assertEquals("John", foundRespFormation.getPrenom());
 assertEquals("M", foundRespFormation.getSex());
 assertEquals("john.doe@example.com", foundRespFormation.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatsNullParameter() {
 Candidat[] candidats = identiteDao.findCandidat(null, null, null, null);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormationsNullParameter() {
 RespFormation[] respFormations = identiteDao.findRespForm(null, null, null, null);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }
}