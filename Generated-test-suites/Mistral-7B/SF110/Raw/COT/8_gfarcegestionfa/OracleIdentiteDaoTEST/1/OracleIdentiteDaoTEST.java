package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 em = identiteDao.getEntityManager();
 }

 @After
 public void tearDown() {
 em.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "nomForm", dateLimite);
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getDateLimite(), foundIdentite.getDateLimite());
 }

 @Test(expected = PersistenceException.class)
 public void testInsertException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 em.getTransaction().begin();
 identiteDao.insert(identite, "nomForm", dateLimite);
 em.getTransaction().commit();

 em.getTransaction().begin();
 identiteDao.insert(identite, "nomForm", dateLimite); // Should throw exception
 em.getTransaction().rollback();
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setNom("John Doe");
 existingIdentite.setPrenom("Doe");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setDateLimite(dateLimite);

 em.getTransaction().begin();
 identiteDao.update(identite);
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(existingIdentite.getEmail(), foundIdentite.getEmail());

 identite.setEmail("john.doe2@example.com");
 identiteDao.update(identite);
 Identite updatedIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite.getEmail(), updatedIdentite.getEmail());
 }

 @Test(expected = PersistenceException.class)
 public void testUpdateException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("john.doe@example.com");
 identite.setNom("John Doe");
 identite.setPrenom("Doe");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setNom("John Doe");
 existingIdentite.setPrenom("Doe");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setDateLimite(dateLimite);

 em.getTransaction().begin();
 identiteDao.update(identite);
 em.getTransaction().commit();

 em.getTransaction().begin();
 identiteDao.update(identite); // Should throw exception
 em.getTransaction().rollback();
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setNom("John Doe");
 existingIdentite.setPrenom("Do");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setDateLimite(dateLimite);

 em.getTransaction().begin();
 identiteDao.delete(identite);
 Identite deletedIdentite = em.find(Identite.class, identite.getId());
 assertNull(deletedIdentite);
 }

 @Test(expected = PersistenceException.class)
 public void testDeleteException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("john.doe@example.com");
 identite.setNom("John Doe");
 identite.setPrenom("Doe");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setNom("John Doe");
 existingIdentite.setPrenom("Doe");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setDateLimite(dateLimite);

 em.getTransaction().begin();
 identiteDao.delete(identite);
 em.getTransaction().commit();

 em.getTransaction().begin();
 identiteDao.delete(identite); // Should throw exception
 em.getTransaction().rollback();
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> resultList = identiteDao.find(nom, prenom, sex, email, type);
 if (resultList != null && !resultList.isEmpty()) {
 Identite resultIdentite = resultList.get(0);
 assertEquals(nom, resultIdentite.getNom());
 assertEquals(prenom, resultIdentite.getPrenom());
 assertEquals(sex, resultIdentite.getSex());
 assertEquals(email, resultIdentite.getEmail());
 assertEquals(type, resultIdentite.getType());
 }
 }

 @Test
 public void testFindEmpty() {
 String nom = "None";
 String prenom = "None";
 String sex = "None";
 String email = "none@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> resultList = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testFindNull() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> resultList = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testFindWithNullValues() {
 String nom = null;
 String prenom = null;
 String sex = null;
 String email = null;
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> resultList = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testFindAll() {
 List<Identite> resultList = identiteDao.find(null, null, null, null, null);
 if (resultList != null && !resultList.isEmpty()) {
 List<String> emails = new ArrayList<>();
 for (Identite identite : resultList) {
 emails.add(identite.getEmail());
 }
 assertTrue(emails.size() > 0);
 }
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] result = identiteDao.findCandidat(nom, prenom, sex, email);
 if (result != null) {
 Candidat resultCandidat = result[0];
 assertEquals(nom, resultCandidat.getNom());
 assertEquals(prenom, resultCandidat.getPrenom());
 assertEquals(sex, resultCandidat.getSex());
 assertEquals(email, resultCandidat.getEmail());
 }
 }

 @Test
 public void testFindCandidatEmpty() {
 String nom = "None";
 String prenom = "None";
 String sex = "None";
 String email = "none@example.com";

 Candidat[] result = identiteDao.findCandidat(nom, prenom, sex, email);
 assertNull(result);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] result = identiteDao.findRespForm(nom, prenom, sex, email);
 if (result != null) {
 RespFormation resultRespForm = result[0];
 assertEquals(nom, resultRespForm.getNom());
 assertEquals(prenom, resultRespForm.getPrenom());
 assertEquals(sex, resultRespForm.getSex());
 assertEquals(email, resultRespForm.getEmail());
 }
 }

 @Test
 public void testFindRespFormEmpty() {
 String nom = "None";
 String prenom = "None";
 String sex = "None";
 String email = "none@example.com";

 RespFormation[] result = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNull(result);
 }
}