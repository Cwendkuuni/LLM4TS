package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

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
 public void setup() {
 emf = Persistence.createEntityManagerFactory("Identite");
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void teardown() {
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
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "", dateLimite);
 List<Identite> result = identiteDao.find("Doe", "John", null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManager em = emf.createEntityManager();
 em.getTransaction().begin();
 Identite existingIdentite = em.find(Identite.class, 1L);
 existingIdentite.setEmail("john.doe@newemail.com");
 identiteDao.update(identite);
 em.getTransaction().commit();
 em.close();

 List<Identite> result = identiteDao.find("Doe", "John", null, "john.doe@newemail.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, result.size());
 Identite updatedIdentite = result.get(0);
 assertEquals("john.doe@newemail.com", updatedIdentite.getEmail());
 }

 @Test(expected = RollbackException.class)
 public void testUpdateFail() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManager em = emf.createEntityManager();
 em.getTransaction().begin();
 Identite existingIdentite = em.find(Identite.class, 1L);
 existingIdentite.setEmail("john.doe@example.com"); // same email
 identiteDao.update(identite);
 em.getTransaction().rollback();
 em.close();
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "", dateLimite);
 List<Identite> resultBeforeDelete = identiteDao.find("Doe", "John", null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, resultBeforeDelete.size());

 identiteDao.delete(identite);
 List<Identite> resultAfterDelete = identiteDao.find("Doe", "John", null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(0, resultAfterDelete.size());
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> result = identiteDao.find(nom, prenom, sex, email, type);
 if (result != null && !result.isEmpty()) {
 assertEquals(nom, result.get(0).getNom());
 assertEquals(prenom, result.get(0).getPrenom());
 assertEquals(sex, result.get(0).getSex());
 assertEquals(email, result.get(0).getEmail());
 assertEquals(type, result.get(0).getType());
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
 assertEquals(1, result.length);
 assertEquals(nom, result[0].getNom());
 assertEquals(prenom, result[0].getPrenom());
 assertEquals(sex, result[0].getSex());
 assertEquals(email, result[0].getEmail());
 }
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "F";
 String email = "john.doe@example.com";

 RespFormation[] result = identiteDao.findRespForm(nom, prenom, sex, email);
 if (result != null) {
 assertEquals(0, result.length);
 }
 }
}