package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao.Identite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 private EntityManager entityManager;
 private EntityTransaction transaction;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 entityManager = identiteDao.getEntityManager();
 transaction = entityManager.getTransaction();
 transaction.begin();
 }

 @After
 public void tearDown() {
 if (transaction.isActive()) {
 transaction.rollback();
 }
 if (entityManager != null) {
 entityManager.close();
 }
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite("Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "InsertTest", new Calendar.Builder().setYear(2021).build().getTime());
 Identite fetchedIdentite = entityManager.find(Identite.class, identite.getId());
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertNotNull(fetchedIdentite.getEmail());
 assertNotNull(fetchedIdentite.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullArguments() {
 identiteDao.insert(null, null, null);
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite("Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "InsertTest", new Calendar.Builder().setYear(2021).build().getTime());
 identite.setNom("Doe Updated");
 identite.setEmail("john.doe.updated@example.com");
 identiteDao.update(identite);
 Identite fetchedIdentite = entityManager.find(Identite.class, identite.getId());
 assertEquals("Doe Updated", fetchedIdentite.getNom());
 assertEquals("john.doe.updated@example.com", fetchedIdentite.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNullArgument() {
 identiteDao.update(null);
 }

 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite("Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "InsertTest", new Calendar.Builder().setYear(2021).build().getTime());
 identiteDao.delete(identite);
 Identite fetchedIdentite = entityManager.find(Identite.class, identite.getId());
 assertNull(fetchedIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteIdentiteNullArgument() {
 identiteDao.delete(null);
 }

 @Test
 public void testFindIdentites() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertNotNull(identites);
 identiteDao.insert(new Identite("Test", "Test", 'M', "test@example.com", Identite.TypeIdentite.CANDIDAT), "InsertTest", new Calendar.Builder().setYear(2021).build().getTime());
 identites = identiteDao.find(null, null, null, null, null);
 assertEquals(1 + identites.size(), 1);
 }

 @Test
 public void testFindCandidats() {
 Identite identite = new Identite("Test", "Test", 'M', "test@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "InsertTest", new Calendar.Builder().setYear(2021).build().getTime());
 Candidat[] candidats = identiteDao.findCandidat("Test", null, null, "test@example.com");
 assertEquals(1, candidats.length);
 }

 @Test
 public void testFindRespFormations() {
 Identite identite = new Identite("Test", "Test", 'M', "test@example.com", Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite, "InsertTest", new Calendar.Builder().setYear(2021).build().getTime());
 RespFormation[] respFormations = identiteDao.findRespForm("Test", null, null, "test@example.com");
 assertEquals(1, respFormations.length);
 }

}