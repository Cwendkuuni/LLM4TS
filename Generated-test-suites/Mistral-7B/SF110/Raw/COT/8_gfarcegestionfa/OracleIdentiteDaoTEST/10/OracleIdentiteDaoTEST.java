package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

@RunWith(JUnitRunner.class)
public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("test", "test", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new GregorianCalendar(2022, 11, 12));

 // Check if the entity was correctly persisted
 EntityManagerFactory emf = null;
 EntityManager em = null;
 Identite foundIdentite = null;
 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 Transaction transaction = em.getTransaction();
 transaction.begin();
 foundIdentite = em.find(Identite.class, identite.getId());
 transaction.commit();
 } catch (PersistenceException e) {
 e.printStackTrace();
 fail("Database error during test");
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 // Check if the entity properties are correct
 assertNotNull(foundIdentite);
 assertNotNull(foundIdentite.getId());
 assertEquals("test", foundIdentite.getNom());
 assertEquals("test", foundIdentite.getPrenom());
 assertEquals("M", foundIdentite.getSex());
 assertEquals("test@test.com", foundIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("test", "test", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new GregorianCalendar(2022, 11, 12));

 // Update the entity
 Identite updatedIdentite = new Identite("updatedTest", "updatedTest", "M", "updatedTest@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(updatedIdentite);

 // Check if the entity was correctly updated
 EntityManagerFactory emf = null;
 EntityManager em = null;
 Identite foundIdentite = null;
 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 Transaction transaction = em.getTransaction();
 transaction.begin();
 foundIdentite = em.find(Identite.class, identite.getId());
 transaction.commit();
 } catch (PersistenceException e) {
 e.printStackTrace();
 fail("Database error during test");
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 // Check if the entity properties are updated
 assertNotNull(foundIdentite);
 assertEquals("updatedTest", foundIdentite.getNom());
 assertEquals("updatedTest", foundIdentite.getPrenom());
 assertEquals("M", foundIdentite.getSex());
 assertEquals("updatedTest@test.com", foundIdentite.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 Identite identite = null;
 identiteDao.insert(identite, "testForm", new GregorianCalendar(2022, 11, 12));
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertEmptyForm() {
 Identite identite = new Identite("", "", "", "", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "", new GregorianCalendar(2022, 11, 12));
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullForm() {
 Identite identite = new Identite("test", "test", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, new GregorianCalendar(2022, 11, 12));
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertFutureDate() {
 Identite identite = new Identite("test", "test", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new GregorianCalendar(2023, 11, 12));
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite("test", "test", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new GregorianCalendar(2022, 11, 12));
 identiteDao.delete(identite);

 // Check if the entity was correctly deleted
 EntityManagerFactory emf = null;
 EntityManager em = null;
 Identite foundIdentite = null;
 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 Transaction transaction = em.getTransaction();
 transaction.begin();
 foundIdentite = em.find(Identite.class, identite.getId());
 transaction.commit();
 } catch (PersistenceException e) {
 e.printStackTrace();
 fail("Database error during test");
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 // Check if the entity was deleted
 assertNull(foundIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 identiteDao.delete(identite);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFind() {
 String nom = "test";
 String prenom = "test";
 String sex = "M";
 String email = "test@test.com";
 IdentiteType type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);

 // Check if the list is not empty
 assertFalse(identites.isEmpty());

 // Check if the first element in the list matches the given criteria
 Identite firstIdentite = identites.get(0);
 assertEquals(nom, firstIdentite.getNom());
 assertEquals(prenom, firstIdentite.getPrenom());
 assertEquals(sex, firstIdentite.getSex());
 assertEquals(email, firstIdentite.getEmail());
 assertEquals(type, firstIdentite.getType());
 }

 @Test
 public void testFindEmpty() {
 String nom = "";
 String prenom = "";
 String sex = "";
 String email = "";
 IdentiteType type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);

 // Check if the list is empty
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindNull() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 IdentiteType type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);

 // Check if the list is empty
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindAll() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);

 // Check if the list is not empty
 assertFalse(identites.isEmpty());
 }

}