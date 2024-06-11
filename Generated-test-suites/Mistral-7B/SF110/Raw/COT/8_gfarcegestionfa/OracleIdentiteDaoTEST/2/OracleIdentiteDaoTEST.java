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
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "TestForm", null);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(fetchedIdentite);
 em.close();
 emf.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertWithNullIdentite() {
 identiteDao.insert(null, "TestForm", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertWithNullNomForm() {
 identiteDao.insert(new Identite(), null, null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite persistedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(persistedIdentite);

 identite.setEmail("john.doe2@example.com");
 identiteDao.update(identite);

 Identite updatedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("john.doe2@example.com", updatedIdentite.getEmail());

 em.close();
 emf.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateWithNullIdentite() {
 Identite identite = new Identite();
 identiteDao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "TestForm", null);
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite persistedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(persistedIdentite);

 identiteDao.delete(identite);
 Identite deletedIdentite = em.find(Identite.class, identite.getId());
 assertNull(deletedIdentite);

 em.close();
 emf.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteWithNullIdentite() {
 Identite identite = new Identite();
 identiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertFalse(identites.isEmpty());

 for (Identite identite : identites) {
 assertEquals(nom, identite.getNom());
 assertEquals(prenom, identite.getPrenom());
 assertEquals(sex, identite.getSex());
 assertEquals(email, identite.getEmail());
 assertEquals(type, identite.getType());
 }
 }

 @Test
 public void testFind_nomAndPrenomEmpty() {
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(null, null, sex, email, type);
 assertFalse(identites.isEmpty());

 for (Identite identite : identites) {
 assertNotNull(identite.getNom());
 assertNotNull(identite.getPrenom());
 }
 }

 @Test
 public void testFind_sexEmpty() {
 String nom = "Doe";
 String prenom = "John";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, null, email, type);
 assertFalse(identites.isEmpty());

 for (Identite identite : identites) {
 assertNotNull(identite.getSex());
 }
 }

 @Test
 public void testFind_typeEmpty() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, null);
 assertFalse(identites.isEmpty());

 for (Identite identite : identites) {
 assertNotNull(identite.getType());
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
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 }

 @After
 public void tearDown() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 em.getTransaction().begin();
 em.createQuery("DELETE FROM Identite").executeUpdate();
 em.getTransaction().commit();
 em.close();
 emf.close();
 }
}