package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Identite.TypeIdentite;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private EntityManagerFactory emf;
 private EntityManager em;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 }

 @After
 public void tearDown() {
 clearDatabase();
 emf.close();
 }

 private void clearDatabase() {
 EntityManager transactionEm = emf.createEntityManager();
 try {
 transactionEm.getTransaction().begin();
 String jpql = "DELETE FROM Identite";
 transactionEm.createQuery(jpql).executeUpdate();
 transactionEm.getTransaction().commit();
 } finally {
 if (transactionEm != null) {
 transactionEm.close();
 }
 }
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, null);

 Identite persistedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(persistedIdentite);
 assertEquals("Doe", persistedIdentite.getNom());
 assertEquals("John", persistedIdentite.getPrenom());
 assertEquals("M", persistedIdentite.getSex());
 assertEquals("john.doe@example.com", persistedIdentite.getEmail());
 assertEquals(TypeIdentite.CANDIDAT, persistedIdentite.getType());
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, null);

 Identite updatedIdentite = new Identite();
 updatedIdentite.setId(identite.getId());
 updatedIdentite.setNom("Smith");
 updatedIdentite.setPrenom("Jane");
 updatedIdentite.setSex("F");
 updatedIdentite.setEmail("jane.smith@example.com");
 updatedIdentite.setType(TypeIdentite.FORMATEUR);
 identiteDao.update(updatedIdentite);

 Identite persistedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("Smith", persistedIdentite.getNom());
 assertEquals("Jane", persistedIdentite.getPrenom());
 assertEquals("F", persistedIdentite.getSex());
 assertEquals("jane.smith@example.com", persistedIdentite.getEmail());
 assertEquals(TypeIdentite.FORMATEUR, persistedIdentite.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteWithNullIdentite() {
 Identite identite = null;
 identiteDao.insert(identite, null, null);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteWithNullNomForm() {
 Identite identite = new Identite();
 identite.setNom(null);
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, null);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteWithNullEmail() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail(null);
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, null);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteWithNullIdentite() {
 Identite identite = null;
 identiteDao.update(identite);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteWithNullNom() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom(null);
 identiteDao.update(identite);
 fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteWithNullEmail() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail(null);
 identiteDao.update(identite);
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testFindIdentites() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertNotNull(identites);
 assertTrue(identites.isEmpty());

 Identite identite1 = new Identite();
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe@example.com");
 identite1.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, null, null);

 Identite identite2 = new Identite();
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe@example.com");
 identite2.setType(TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, null, null);

 List<Identite> identites2 = identiteDao.find(null, null, null, null, null);
 assertNotNull(identites2);
 assertEquals(2, identites2.size());
 }

 @Test
 public void testFindCandidats() {
 List<Candidat> candidats = identiteDao.findCandidat("Doe", "John", null, "john.doe@example.com");
 assertNotNull(candidats);
 assertTrue(candidats.isEmpty());

 Candidat candidat1 = new Candidat();
 candidat1.setNom("Doe1");
 candidat1.setPrenom("John1");
 candidat1.setSex("M");
 candidat1.setEmail("john1.doe@example.com");
 candidat1.setTypeIdentite(TypeIdentite.CANDIDAT);
 identiteDao.insert(candidat1, null, null);

 List<Candidat> candidats2 = identiteDao.findCandidat("Doe1", "John1", null, "john1.doe@example.com");
 assertNotNull(candidats2);
 assertEquals(1, candidats2.length);
 }

 @Test
 public void testFindResponsablesFormations() {
 List<RespFormation> respFormations = identiteDao.findRespForm("Doe", "John", null, "john.doe@example.com");
 assertNotNull(respFormations);
 assertTrue(respFormations.isEmpty());

 RespFormation respFormation1 = new RespFormation();
 respFormation1.setNom("Doe1");
 respFormation1.setPrenom("John1");
 respFormation1.setSex("M");
 respFormation1.setEmail("john1.doe@example.com");
 respFormation1.setTypeIdentite(TypeIdentite.FORMATEUR);
 identiteDao.insert(respFormation1, null, null);

 List<RespFormation> respFormations2 = identiteDao.findRespForm("Doe1", "John1", null, "john1.doe@example.com");
 assertNotNull(respFormations2);
 assertEquals(1, respFormations2.length);
 }
}