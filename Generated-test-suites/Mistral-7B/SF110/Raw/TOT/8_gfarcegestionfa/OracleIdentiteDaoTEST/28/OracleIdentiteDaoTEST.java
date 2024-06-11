import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import java.util.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private EntityManagerFactory emf;

 @Before
 public void setup() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 }

 @After
 public void tearDown() {
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsertIdentite() {
 // Create a new Identite object
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 // Call the method under test
 identiteDao.insert(identite, "form1", new GregorianCalendar(2022, 11, 30));

 // Verify that the Identite has been persisted
 EntityManager em = emf.createEntityManager();
 Identite persistedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(persistedIdentite);
 assertEquals(identite.getNom(), persistedIdentite.getNom());
 assertEquals(identite.getPrenom(), persistedIdentite.getPrenom());
 assertEquals(identite.getEmail(), persistedIdentite.getEmail());
 assertEquals(identite.getSex(), persistedIdentite.getSex());
 assertEquals(identite.getType(), persistedIdentite.getType());
 }

 @Test
 public void testUpdateIdentite() {
 // Create a new Identite object
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 // Call the method under test
 identiteDao.update(identite);

 // Verify that the Identite has been updated
 EntityManager em = emf.createEntityManager();
 Identite updatedIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), updatedIdentite.getNom());
 assertEquals(identite.getPrenom(), updatedIdentite.getPrenom());
 assertEquals(identite.getEmail(), updatedIdentite.getEmail());
 assertEquals(identite.getSex(), updatedIdentite.getSex());
 assertEquals(identite.getType(), updatedIdentite.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNull() {
 // Call the method under test with a null Identite object
 identiteDao.insert(null, "form1", new GregorianCalendar(2022, 11, 30));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullForm() {
 // Call the method under test with a null form name
 identiteDao.insert(new Identite(), null, new GregorianCalendar(2022, 11, 30));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullDate() {
 // Call the method under test with a null date
 identiteDao.insert(new Identite(), "form1", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateNonExistentIdentite() {
 // Call the method under test with a nonexistent Identite object
 Identite identite = new Identite();
 identite.setId(100L);
 identiteDao.update(identite);
 }

 @Test
 public void testFindIdentites() {
 // Set up test data
 identiteDao.insert(new Identite("Alice", "Smith", "alice.smith@example.com", 'F', Identite.TypeIdentite.CANDIDAT), "form1", new GregorianCalendar(2022, 11, 30));
 identiteDao.insert(new Identite("Bob", "Jones", "bob.jones@example.com", 'M', Identite.TypeIdentite.FORMATEUR), "form1", new GregorianCalendar(2022, 11, 30));
 identiteDao.insert(new Identite("Charlie", "Brown", "charlie.brown@example.com", 'M', Identite.TypeIdentite.CANDIDAT), "form1", new GregorianCalendar(2022, 11, 30));

 // Call the method under test and verify the results
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(3, identites.size());
 assertEquals("Alice", identites.get(0).getNom());
 assertEquals("Bob", identites.get(1).getNom());
 assertEquals("Charlie", identites.get(2).getNom());

 List<Candidat> candidats = identiteDao.findCandidat(null, null, null, null);
 assertEquals(2, candidats.length);
 assertEquals("Alice", candidats[0].getNom());
 assertEquals("Charlie", candidats[1].getNom());

 List<RespFormation> formateurs = identiteDao.findRespForm(null, null, null, null);
 assertEquals(1, formateurs.length);
 assertEquals("Bob", formateurs[0].getNom());
 }
}