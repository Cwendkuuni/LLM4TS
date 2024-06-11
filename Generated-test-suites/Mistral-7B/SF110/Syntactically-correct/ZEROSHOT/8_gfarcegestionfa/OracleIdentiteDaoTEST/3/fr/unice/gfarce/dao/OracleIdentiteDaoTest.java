package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TransactionRequiredException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 if (em != null && em.isOpen()) {
 em.close();
 }
 if (emf != null && emf.isOpen()) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("John Doe", "John", "Doe", 'M', Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteDao.insert(identite, "testForm", dateLimite);

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertSame(identite, foundIdentite);

 em.close();
 }

 @Test(expected = TransactionRequiredException.class)
 public void testInsertWithoutTransaction() {
 Identite identite = new Identite("John Doe", "John", "Doe", 'M', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());
 fail("Expected TransactionRequiredException but no exception was thrown.");
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("John Doe", "John", "Doe", 'M', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());

 Identite updatedIdentite = new Identite("Jane Doe", "Jane", "Doe", 'F', Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(updatedIdentite);

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertSame(updatedIdentite, foundIdentite);

 em.close();
 }

 @Test(expected = TransactionRequiredException.class)
 public void testUpdateWithoutTransaction() {
 Identite identite = new Identite("John Doe", "John", "Doe", 'M', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());

 Identite updatedIdentite = new Identite("Jane Doe", "Jane", "Doe", 'F', Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(updatedIdentite);
 fail("Expected TransactionRequiredException but no exception was thrown.");
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite("John Doe", "John", "Doe", 'M', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());
 identiteDao.delete(identite);

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNull(foundIdentite);

 em.close();
 }

 @Test(expected = TransactionRequiredException.class)
 public void testDeleteWithoutTransaction() {
 Identite identite = new Identite("John Doe", "John", "Doe", 'M', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());
 identiteDao.delete(identite);
 fail("Expected TransactionRequiredException but no exception was thrown.");
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite("John Doe1", "John1", "Doe1", 'M', Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("John Doe2", "John2", "Doe2", 'M', Identite.TypeIdentite.CANDIDAT);
 Identite identite3 = new Identite("John Doe3", "John3", "Doe3", 'M', Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite1, "testForm", new Date());
 identiteDao.insert(identite2, "testForm", new Date());
 identiteDao.insert(identite3, "testForm", new Date());

 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(3, identites.size());

 emf.getCache().evictAll(); // Clear the cache to test find again
 identites = identiteDao.find(null, null, null, null, null);
 assertEquals(3, identites.size());
 }

 @Test
 public void testFindCandidat() {
 Identite identite1 = new Identite("John Doe1", "John1", "Doe1", 'M', Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("John Doe2", "John2", "Doe2", 'M', Identite.TypeIdentite.CANDIDAT);
 Identite identite3 = new Identite("John Doe3", "John3", "Doe3", 'M', Identite.TypeIdentite.FORMATEUR);
 Identite identite4 = new Identite("Jane Doe", "Jane1", "Doe1", 'F', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "testForm", new Date());
 identiteDao.insert(identite2, "testForm", new Date());
 identiteDao.insert(identite3, "testForm", new Date());
 identiteDao.insert(identite4, "testForm", new Date());

 Candidat[] candidats = identiteDao.findCandidat("Doe", "John", null, null);
 assertEquals(2, candidats.length);
 assertArrayEquals(new Object[] { identite1, identite2 }, candidats);
 }

 @Test
 public void testFindRespForm() {
 Identite identite1 = new Identite("John Doe1", "John1", "Doe1", 'M', Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("John Doe2", "John2", "Doe2", 'M', Identite.TypeIdentite.FORMATEUR);
 Identite identite3 = new Identite("John Doe3", "John3", "Doe3", 'M', Identite.TypeIdentite.FORMATEUR);
 Identite identite4 = new Identite("Jane Doe", "Jane1", "Doe1", 'F', Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "testForm", new Date());
 identiteDao.insert(identite2, "testForm", new Date());
 identiteDao.insert(identite3, "testForm", new Date());
 identiteDao.insert(identite4, "testForm", new Date());

 RespFormation[] respFormations = identiteDao.findRespForm("Doe", "John", null, null);
 assertEquals(1, respFormations.length);
 assertArrayEquals(new Object[] { identite2 }, respFormations);
 }

 @Test
 public void testFindNull() {
 Candidat[] candidats = identiteDao.findCandidat(null, null, null, null, null);
 assertEquals(0, candidats.length);
 }
}