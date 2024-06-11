package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 identiteDao.insert(identite, "testForm", calendar);

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 em.close();
 }

 @Test(expected = Exception.class)
 public void testInsertException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 IdentiteDao dao = new OracleIdentiteDao();
 try {
 dao.insert(identite, "testForm", calendar);
 fail("Expected an exception");
 }
 catch (Exception e) {
 // Expected exception
 }
 finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 identiteDao.update(identite);

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals("Doe", foundIdentite.getNom());
 em.close();
 }

 @Test(expected = Exception.class)
 public void testUpdateException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 IdentiteDao dao = new OracleIdentiteDao();
 try {
 dao.update(identite);
 fail("Expected an exception");
 }
 catch (Exception e) {
 // Expected exception
 }
 finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 identiteDao.delete(identite);

 List<Identite> identites = new ArrayList<Identite>();
 EntityManager em = emf.createEntityManager();
 identites = em.createQuery("from Identite", Identite.class).getResultList();
 assertEquals(0, identites.size());
 em.close();
 }

 @Test(expected = Exception.class)
 public void testDeleteException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 IdentiteDao dao = new OracleIdentiteDao();
 try {
 dao.delete(identite);
 fail("Expected an exception");
 }
 catch (Exception e) {
 // Expected exception
 }
 finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testFind() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite, "testForm", calendar);
 List<Identite> identites = new ArrayList<Identite>();
 identites = identiteDao.find(null, null, null, null, null);
 assertEquals(1, identites.size());
 em.close();
 }

 @Test
 public void testFindEmpty() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 List<Identite> identites = new ArrayList<Identite>();
 identites = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identites.size());
 em.close();
 }

 @Test
 public void testFindByNomAndPrenom() {
 Identite identite1 = new Identite();
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setId(1L);
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite1.setDateLimite(calendar.getTime());

 Identite identite2 = new Identite();
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setId(2L);
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 calendar.add(Calendar.DAY_OF_MONTH, 1);
 identite2.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite1, "testForm", calendar);
 identiteDao.insert(identite2, "testForm2", calendar);
 List<Identite> identites = new ArrayList<Identite>();
 identites = identiteDao.find("Doe", "John", null, null, null);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 em.close();
 }

 @Test
 public void testFindByNomAndEmail() {
 Identite identite1 = new Identite();
 identite1.setNom("Doe");
 identite1.setEmail("john.doe@example.com");
 identite1.setId(1L);
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite1.setDateLimite(calendar.getTime());

 Identite identite2 = new Identite();
 identite2.setNom("Doe");
 identite2.setEmail("jane.doe@example.com");
 identite2.setId(2L);
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 calendar.add(Calendar.DAY_OF_MONTH, 1);
 identite2.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite1, "testForm", calendar);
 identiteDao.insert(identite2, "testForm2", calendar);
 List<Identite> identites = new ArrayList<Identite>();
 identites = identiteDao.find("Doe", null, null, "john.doe@example.com", null);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 em.close();
 }

 @Test
 public void testFindByEmailAndType() {
 Identite identite1 = new Identite();
 identite1.setEmail("john.doe@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex('M');
 Calendar calendar = Calendar.getInstance();
 identite1.setDateLimite(calendar.getTime());

 Identite identite2 = new Identite();
 identite2.setEmail("jane.doe@example.com");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setId(2L);
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setSex('F');
 calendar.add(Calendar.DAY_OF_MONTH, 1);
 identite2.setDateLimite(calendar.getTime());

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite1, "testForm", calendar);
 identiteDao.insert(identite2, "testForm2", calendar);
 List<Identite> identites = new ArrayList<Identite>();
 identites = identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 em.close();
 }

 @Test
 public void testFindAll() {
 Identite identite1 = new Identite();
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite1.setDateLimite(calendar.getTime());
 identite1.setId(1L);

 Identite identite2 = new Identite();
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 calendar.add(Calendar.DAY_OF_MONTH, 1);
 identite2.setDateLimite(calendar.getTime());
 identite2.setId(2L);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite1, "testForm", calendar);
 identiteDao.insert(identite2, "testForm2", calendar);
 List<Identite> identites = new ArrayList<Identite>();
 identites = identiteDao.find(null, null, null, null, null);
 assertEquals(2, identites.size());
 em.close();
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());
 identite.setId(1L);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite, "testForm", calendar);
 Candidat[] candidats = identiteDao.findCandidat("Doe", "John", null, null);
 assertEquals(1, candidats.length);
 em.close();
 }

 @Test
 public void testFindCandidatEmpty() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Candidat[] candidats = identiteDao.findCandidat("Doe", "John", null, null);
 assertEquals(0, candidats.length);
 em.close();
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());
 identite.setId(1L);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 identiteDao.insert(identite, "testForm", calendar);
 RespFormation[] respFormations = identiteDao.findRespForm("Doe", "John", null, null);
 assertEquals(1, respFormations.length);
 em.close();
 }

 @Test
 public void testFindRespFormEmpty() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 RespFormation[] respFormations = identiteDao.findRespForm("Doe", "John", null, null);
 assertEquals(0, respFormations.length);
 em.close();
 }

}