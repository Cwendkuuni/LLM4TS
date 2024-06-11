package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.junit.runner.*;

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
 Identite identite = new Identite();
 Calendar dateLimite = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "test_form", dateLimite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite, foundIdentite);
 em.close();
 emf.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 Identite identite = null;
 identiteDao.insert(identite, "test_form", new GregorianCalendar(2022, 1, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite();
 identite.setNom(null);
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, new GregorianCalendar(2022, 1, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDateLimite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "test_form", null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite, foundIdentite);
 em.close();
 emf.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = null;
 identiteDao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.delete(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 em.close();
 emf.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 identiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertFalse(identites.isEmpty());
 }

 @Test
 public void testFindEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindWithNullParameter() {
 String nom = null;
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindWithNoResult() {
 String nom = "Unknown";
 String prenom = "Unknown";
 String sex = "Unknown";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindWithResults() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertFalse(identites.isEmpty());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertFalse(ArrayUtils.isEmpty(candidats));
 }

 @Test
 public void testFindCandidatEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertArrayEquals(new Candidat[0], candidats);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertFalse(ArrayUtils.isEmpty(respFormations));
 }

 @Test
 public void testFindRespFormEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(new RespFormation[0], respFormations);
 }

 @After
 public void tearDown() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 em.getTransaction().begin();
 Identite identite = new Identite();
 identite.setNom("test");
 identite.setPrenom("test");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite);
 em.getTransaction().commit();
 em.close();
 emf.close();
 }
}