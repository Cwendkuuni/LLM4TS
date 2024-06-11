package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.junit.runner.*;

import fr.unice.gfarce.*;
import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

@RunWith(JUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1));
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1));
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1)); // Second call should throw an exception
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1));
 Identite updatedIdentite = new Identite("testUpdated", "testUpdated", "testUpdated", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(updatedIdentite);
 Identite foundIdentite = (Identite) identiteDao.findById(updatedIdentite.getId()).get(0);
 assertEquals(updatedIdentite.getNom(), foundIdentite.getNom());
 }

 @Test(expected = Exception.class)
 public void testUpdateWithException() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1));
 Identite updatedIdentite = new Identite("testUpdated", "testUpdated", "testUpdated", null, Identite.TypeIdentite.CANDIDAT); // Email should not be null
 identiteDao.update(updatedIdentite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1));
 identiteDao.delete(identite);
 List<Identite> identites = identiteDao.find(null, null, null, "test@test.com", Identite.TypeIdentite.CANDIDAT);
 assertFalse(identites.size() > 0);
 }

 @Test(expected = Exception.class)
 public void testDeleteWithException() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1));
 identiteDao.delete(null); // Identite should not be null
 }

 @Test
 public void testFind() {
 String nom = "test";
 String prenom = "test";
 String sex = "test";
 String email = "test@test.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertFalse(identites.isEmpty());
 }

 @Test
 public void testFindEmpty() {
 String nom = "nonexistent";
 String prenom = "nonexistent";
 String sex = "nonexistent";
 String email = "nonexistent@nonexistent.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindCandidat() {
 String nom = "test";
 String prenom = "test";
 String sex = "test";
 String email = "test@test.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertNotNull(candidats);
 }

 @Test
 public void testFindRespForm() {
 String nom = "test";
 String prenom = "test";
 String sex = "test";
 String email = "test@test.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 }

 @Test
 public void testFindMixed() {
 String nom = "test";
 String prenom = "test";
 String sex = "test";
 String email = "test@test.com";
 Identite.TypeIdentite type = null; // Should return all types
 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertFalse(identites.isEmpty());
 }
}