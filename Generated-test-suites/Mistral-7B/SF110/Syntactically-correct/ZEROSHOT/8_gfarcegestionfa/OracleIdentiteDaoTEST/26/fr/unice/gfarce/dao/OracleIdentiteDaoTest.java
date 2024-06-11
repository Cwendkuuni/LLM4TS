package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.*;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao oracleIdentiteDao;
 private EntityManagerFactory emf;
 private EntityManager em;
 private EntityTransaction tx;

 @Before
 public void setUp() {
 oracleIdentiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory(OracleIdentiteDao.PERSIST_UNIT_NAME);
 em = emf.createEntityManager();
 tx = em.getTransaction();
 tx.begin();
 }

 @After
 public void tearDown() {
 if (tx != null) {
 tx.rollback();
 }
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("testName", "testPrenom", "testSex", "testEmail", Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.insert(identite, null, null);
 assertNotEquals(0, em.findAll().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 oracleIdentiteDao.insert(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite("testName", "testPrenom", "testSex", "testEmail", Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.insert(identite, null, null);
 oracleIdentiteDao.insert(identite, (String)null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDateLimite() {
 Identite identite = new Identite("testName", "testPrenom", "testSex", "testEmail", Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.insert(identite, null, new GregorianCalendar(1990, 1, 1));
 oracleIdentiteDao.insert(identite, null, (Calendar)null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("testName", "testPrenom", "testSex", "testEmail", Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.insert(identite, null, null);
 Identite newIdentite = new Identite("newTestName", "newTestPrenom", "newTestSex", "newTestEmail", Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.update(identite);
 identite.setEmail("newTestEmail2");
 oracleIdentiteDao.update(identite);
 assertNotEquals(identite.getEmail(), em.find(Identite.class, identite.getId()).getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = new Identite("testName", "testPrenom", "testSex", "testEmail", Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.update(null);
 }

 @Test
 public void testFind() {
 String nom = "testName";
 String prenom = "testPrenom";
 String sex = "testSex";
 String email = "testEmail";
 IdentiteType identiteType = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = oracleIdentiteDao.find(nom, prenom, sex, email, identiteType);
 assertNotNull(identites);
 }

 @Test
 public void testFindEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 IdentiteType identiteType = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = oracleIdentiteDao.find(nom, prenom, sex, email, identiteType);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithOneResult() {
 String nom = "testName";
 String prenom = "testPrenom";
 String sex = "testSex";
 String email = "testEmail";
 IdentiteType identiteType = Identite.TypeIdentite.CANDIDAT;
 Identite identite = new Identite(nom, prenom, sex, email, identiteType);
 oracleIdentiteDao.insert(identite, null, null);
 List<Identite> identites = oracleIdentiteDao.find(nom, prenom, sex, email, identiteType);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindWithMultipleResults() {
 String nom = "testName";
 String prenom = "testPrenom";
 String sex = "testSex";
 String email = "testEmail";
 IdentiteType identiteType = Identite.TypeIdentite.CANDIDAT;
 Identite identite1 = new Identite(nom, prenom, sex, email, identiteType);
 Identite identite2 = new Identite(nom, prenom, sex, email, identiteType);
 oracleIdentiteDao.insert(identite1, null, null);
 oracleIdentiteDao.insert(identite2, null, null);
 List<Identite> identites = oracleIdentiteDao.find(nom, prenom, sex, email, identiteType);
 assertEquals(2, identites.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullIdentiteType() {
 oracleIdentiteDao.find(null, null, null, null, null);
 }

 @Test
 public void testFindCandidat() {
 String nom = "testName";
 String prenom = "testPrenom";
 String sex = "testSex";
 String email = "testEmail";
 Candidat[] candidats = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
 assertNotNull(candidats);
 }

 @Test
 public void testFindCandidatEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 Candidat[] candidats = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
 assertArrayEquals(new Candidat[0], candidats);
 }

 @Test
 public void testFindRespForm() {
 String nom = "testName";
 String prenom = "testPrenom";
 String sex = "testSex";
 String email = "testEmail";
 RespFormation[] respFormations = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 }

 @Test
 public void testFindRespFormEmpty() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 RespFormation[] respFormations = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(new RespFormation[0], respFormations);
 }
}