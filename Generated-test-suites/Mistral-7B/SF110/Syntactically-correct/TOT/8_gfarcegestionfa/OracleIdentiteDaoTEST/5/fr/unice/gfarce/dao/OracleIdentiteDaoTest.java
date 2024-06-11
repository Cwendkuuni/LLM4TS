import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Identite;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private EntityManagerFactory emf;
 private EntityManager em;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
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
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");

 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "test", dateLimite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setNom("Smith");
 existingIdentite.setPrenom("Jane");
 existingIdentite.setSex("F");
 existingIdentite.setType(Identite.TypeIdentite.FORMATEUR);
 existingIdentite.setEmail("jane.smith@example.com");
 existingIdentite.setDateLimite(new Date());

 em.persist(existingIdentite);
 em.getTransaction().commit();

 identiteDao.update(identite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertNotEquals(existingIdentite.getNom(), foundIdentite.getNom());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());

 em.persist(identite);
 em.getTransaction().commit();

 identiteDao.delete(identite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertNotNull(identites);
 if (identites.size() > 0) {
 Identite identite = identites.get(0);
 assertEquals(nom, identite.getNom());
 assertEquals(prenom, identite.getPrenom());
 assertEquals(sex, identite.getSex());
 assertEquals(email, identite.getEmail());
 assertEquals(type, identite.getType());
 }
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertNotNull(candidats);
 if (candidats.length > 0) {
 Candidat candidat = candidats[0];
 assertEquals(nom, candidat.getNom());
 assertEquals(prenom, candidat.getPrenom());
 assertEquals(sex, candidat.getSex());
 assertEquals(email, candidat.getEmail());
 }
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 if (respFormations.length > 0) {
 RespFormation respFormation = respFormations[0];
 assertEquals(nom, respFormation.getNom());
 assertEquals(prenom, respFormation.getPrenom());
 assertEquals(sex, respFormation.getSex());
 assertEquals(email, respFormation.getEmail());
 }
 }

 @Test
 public void testFindEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 Identite.TypeIdentite type = null;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertNotNull(identites);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindInvalidType() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 Identite.TypeIdentite type = null;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertNotNull(identites);
 assertEquals(0, identites.size());
 }
}