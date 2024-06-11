package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
 import java.util.Arrays;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.List;

 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.EntityTransaction;
 import javax.persistence.PersistenceException;
 import javax.persistence.TypedQuery;

 import fr.unice.gfarce.dao.OracleIdentiteDao;
 import fr.unice.gfarce.identity.Candidat;
 import fr.unice.gfarce.identity.Identite;
 import fr.unice.gfarce.identity.RespFormation;

 public class OracleIdentiteDaoTest {

 private static final EntityManagerFactory EM_FACTORY;
 private static final EntityManager EM;
 private static final EntityTransaction TX;

 static {
 EntityManagerFactory emf = null;
 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 } catch (Throwable e1) {
 throw new ExceptionInInitializerError(e1);
 }
 EM_FACTORY = emf;
 EM = emf.createEntityManager();
 TX = EM.getTransaction();
 }

 @org.junit.Before
 public void setUp() throws Exception {
 TX.begin();
 }

 @org.junit.After
 public void tearDown() throws Exception {
 if (TX.isActive()) {
 TX.rollback();
 }
 EM.close();
 EM_FACTORY.close();
 }

 @org.junit.Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);
 OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
 dao.insert(identite, null, dateLimite);
 assertNotNull(identite.getId());
 }

 @org.junit.Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Identite ident = EM.find(Identite.class, identite.getId());
 ident.setEmail("johndoe@example.com");
 OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
 dao.update(ident);
 assertEquals("johndoe@example.com", ident.getEmail());
 }

 @org.junit.Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
 dao.delete(identite);
 assertNull(EM.find(Identite.class, identite.getId()));
 }

 @org.junit.Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> resultList = OracleIdentiteDao.getDAO().find(nom, prenom, sex, null, type);
 Identite expectedIdentite = new Identite();
 expectedIdentite.setNom("Doe");
 expectedIdentite.setPrenom("John");
 expectedIdentite.setSex('M');
 expectedIdentite.setType(type);
 Assert.assertEquals(Arrays.asList(expectedIdentite), resultList);
 }

 @org.junit.Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "johndoe@example.com";
 Candidat[] result = OracleIdentiteDao.getDAO().findCandidat(nom, prenom, sex, email);
 Candidat expectedCandidat = new Candidat();
 expectedCandidat.setId(1L);
 expectedCandidat.setEmail(email);
 expectedCandidat.setNom("Doe");
 expectedCandidat.setPrenom("John");
 expectedCandidat.setSex('M');
 assertTrue(Arrays.equals(new Candidat[] { expectedCandidat }, result));
 }

 @org.junit.Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "F";
 String email = "johndoe@example.com";
 RespFormation[] result = OracleIdentiteDao.getDAO().findRespForm(nom, prenom, sex, email);
 RespFormation expectedRespForm = new RespFormation();
 expectedRespForm.setId(1L);
 expectedRespForm.setEmail(email);
 expectedRespForm.setNom("Doe");
 expectedRespForm.setPrenom("John");
 expectedRespForm.setSex('F');
 assertTrue(Arrays.equals(new RespFormation[] { expectedRespForm }, result));
 }

 @org.junit.Test
 public void testFindWithNullParams() {
 List<Identite> resultList = OracleIdentiteDao.getDAO().find(null, null, null, null, null);
 assertTrue(resultList.isEmpty());
 }

 @org.junit.Test(expected = PersistenceException.class)
 public void testUpdateWithNullIdentite() {
 OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
 dao.update(null);
 }

 @org.junit.Test(expected = PersistenceException.class)
 public void testUpdateWithNullTransaction() {
 Identite identite = new Identite();
 OracleIdentiteDao dao = new OracleIdentiteDao();
 dao.update(identite);
 }

 @org.junit.Test(expected = PersistenceException.class)
 public void testDeleteWithNullIdentite() {
 OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
 dao.delete(null);
 }
 }