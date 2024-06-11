package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setup() {
 this.emf = Persistence.createEntityManagerFactory("Identite");
 this.em = this.emf.createEntityManager();
 this.identiteDao = new OracleIdentiteDao();
 }

 @After
 public void teardown() {
 this.em.close();
 this.emf.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.identiteDao.insert(identite, "form1.html", new GregorianCalendar(2022, 11, 30));
 assertEquals(1, this.em.createQuery("SELECT COUNT(*) FROM Identite", Identite.class).getSingleResult().intValue());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.identiteDao.update(identite);
 Identite dbIdentite = this.em.find(Identite.class, 1);
 assertEquals(identite, dbIdentite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1);
 this.identiteDao.delete(identite);
 assertEquals(0, this.em.createQuery("SELECT COUNT(*) FROM Identite", Identite.class).getSingleResult().intValue());
 }

 @Test
 public void testFindAll() {
 List<Identite> identites = this.identiteDao.find(null, null, null, null, null);
 assertNotNull(identites);
 assertTrue(identites.size() > 0);
 }

 @Test
 public void testFindCandidatNull() {
 List<Identite> identites = this.identiteDao.find("", "", "", null, Identite.TypeIdentite.CANDIDAT);
 assertNotNull(identites);
 assertTrue(identites.size() > 0);
 }

 @Test
 public void testFindCandidatEmpty() {
 List<Identite> identites = this.identiteDao.find("John", "", "", null, Identite.TypeIdentite.CANDIDAT);
 assertNotNull(identites);
 assertTrue(identites.size() > 0);
 }

 @Test
 public void testFindCandidatExists() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 this.identiteDao.insert(identite, "form1.html", new GregorianCalendar(2022, 11, 30));
 List<Identite> identites = this.identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
 assertNotNull(identites);
 assertEquals(1, identites.size());
 assertEquals(identite, identites.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatInvalidType() {
 this.identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.FORMATEUR);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNull() {
 this.identiteDao.update(null);
 }

 @Test(expected = EntityNotFoundException.class)
 public void testUpdateNonExistent() {
 Identite identite = new Identite();
 identite.setId(100);
 this.identiteDao.update(identite);
 }

 @Test(expected = EntityNotFoundException.class)
 public void testDeleteNull() {
 this.identiteDao.delete(null);
 }

 @Test(expected = EntityNotFoundException.class)
 public void testDeleteNonExistent() {
 Identite identite = new Identite();
 identite.setId(100);
 this.identiteDao.delete(identite);
 }

 @Test
 public void testFindRespFormNull() {
 List<Identite> identites = this.identiteDao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
 assertNotNull(identites);
 assertTrue(identites.size() > 0);
 }

 @Test
 public void testFindRespFormEmpty() {
 List<Identite> identites = this.identiteDao.find("John", "", "", null, Identite.TypeIdentite.FORMATEUR);
 assertNotNull(identites);
 assertTrue(identites.size() > 0);
 }

 @Test
 public void testFindRespFormExists() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 this.identiteDao.insert(identite, "form1.html", new GregorianCalendar(2022, 11, 30));
 List<Identite> identites = this.identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.FORMATEUR);
 assertNotNull(identites);
 assertEquals(1, identites.size());
 assertEquals(identite, identites.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormInvalidType() {
 this.identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
 }
}