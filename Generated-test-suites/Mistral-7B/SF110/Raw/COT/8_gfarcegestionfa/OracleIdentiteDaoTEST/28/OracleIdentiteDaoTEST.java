package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoTest;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private static OracleIdentiteDao identiteDao;

 @BeforeClass
 public static void init() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setEmail("test@test.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "TestForm", new GregorianCalendar(2022, 11, 30));

 List<Identite> identites = identiteDao.find(null, null, null, "test@test.com", Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.size() > 0);
 Identite retrievedIdentite = identites.get(0);
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 Identite identite = new Identite();
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setEmail("test@test.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 identiteDao.insert(identite, "TestForm", new GregorianCalendar(2022, 11, 30));
 fail("Exception should be thrown");
 }
 catch (Exception e) {
 // expected exception
 }
 finally {
 tx.rollback();
 em.close();
 emf.close();
 }
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("TestUpdate");
 identite.setPrenom("TestUpdate");
 identite.setSex("F");
 identite.setEmail("testUpdate@test.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setNom("Test");
 existingIdentite.setPrenom("Test");
 existingIdentite.setSex("M");
 existingIdentite.setEmail("test@test.com");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.update(identite);

 Identite retrievedIdentite = identiteDao.findById(identite.getId());
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 }

 @Test(expected = EntityNotFoundException.class)
 public void testUpdateWithNotFoundException() {
 Identite identite = new Identite();
 identite.setId(100L);
 identite.setNom("TestUpdate");
 identite.setPrenom("TestUpdate");
 identite.setSex("F");
 identite.setEmail("testUpdate@test.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("TestDelete");
 identite.setPrenom("TestDelete");
 identite.setSex("M");
 identite.setEmail("testDelete@test.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);

 List<Identite> identites = identiteDao.find(null, null, null, "testDelete@test.com", Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.isEmpty());
 }

 @Test(expected = EntityNotFoundException.class)
 public void testDeleteWithNotFoundException() {
 Identite identite = new Identite();
 identite.setId(100L);
 identite.setNom("TestDelete");
 identite.setPrenom("TestDelete");
 identite.setSex("M");
 identite.setEmail("testDelete@test.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertFalse(identites.isEmpty());
 Identite retrievedIdentite = identites.get(0);
 assertEquals(nom, retrievedIdentite.getNom());
 assertEquals(prenom, retrievedIdentite.getPrenom());
 assertEquals(sex, retrievedIdentite.getSex());
 assertEquals(type, retrievedIdentite.getType());
 }

 @Test
 public void testFindEmpty() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindByType() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.FORMATEUR;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindCandidats() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";

 Candidat[] candidates = identiteDao.findCandidat(nom, prenom, sex, email);
 assertNotNull(candidates);
 }

 @Test
 public void testFindCandidatsEmpty() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";

 Candidat[] candidates = identiteDao.findCandidat(nom, prenom, sex, email);
 assertArrayEquals(new Candidat[0], candidates);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "F";
 String email = "test@test.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 }

 @Test
 public void testFindRespFormEmpty() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "F";
 String email = "test@test.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(new RespFormation[0], respFormations);
 }
}