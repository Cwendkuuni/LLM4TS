package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 private EntityManagerFactory emf;

 @Before
 public void setup() {
 identiteDao = OracleIdentiteDaoImpl.getDAO();
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
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateCreation(calendar.getTime());

 identiteDao.insert(identite, "testForm", calendar);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotNull(foundIdentite);
 assertTrue(foundIdentite.getNom().equals("Doe"));
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
 Calendar calendar = Calendar.getInstance();
 identite.setDateCreation(calendar.getTime());

 Identite oldIdentite = new Identite();
 oldIdentite.setId(identite.getId());
 oldIdentite.setEmail("john.doe@oldemail.com");

 identiteDao.update(identite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotNull(foundIdentite);
 assertTrue(foundIdentite.getEmail().equals("john.doe@example.com"));
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
 assertTrue(identites.size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNull() {
 Identite identite = null;
 identiteDao.insert(identite, "testForm", new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteEmptyNom() {
 Identite identite = new Identite();
 identite.setNom("");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullEmail() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail(null);
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteEmptyType() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(null);
 identiteDao.insert(identite, "testForm", new Date());
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateNonExistingIdentite() {
 Identite identite = new Identite();
 identite.setId(100L);
 identite.setEmail("john.doe@example.com");
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 identiteDao.delete(identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNonExistingIdentite() {
 Identite identite = new Identite();
 identite.setId(100L);
 identiteDao.delete(identite);
 }
}