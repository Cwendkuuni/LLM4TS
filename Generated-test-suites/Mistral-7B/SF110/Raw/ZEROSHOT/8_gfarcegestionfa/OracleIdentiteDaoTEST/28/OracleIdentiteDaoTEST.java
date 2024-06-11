package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
 private EntityManager entityManager;
 private EntityTransaction transaction;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDaoImpl.getDAO();
 entityManager = identiteDao.getEntityManager();
 transaction = entityManager.getTransaction();
 }

 @After
 public void tearDown() {
 transaction.rollback();
 entityManager.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 Calendar dateLimite = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", dateLimite);

 Identite foundIdentite = entityManager.find(Identite.class, identite.getId());
 Identite expectedIdentite = new Identite();
 expectedIdentite.setNom("Doe");
 expectedIdentite.setPrenom("John");
 expectedIdentite.setSex("M");
 expectedIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 assertEquals(expectedIdentite, foundIdentite);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 Calendar dateLimite = Calendar.getInstance();
 identite.setId(1L); // Assuming an ID is set for the test
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identiteDao.update(identite);

 Identite foundIdentite = entityManager.find(Identite.class, identite.getId());
 Identite expectedIdentite = new Identite();
 expectedIdentite.setId(1L);
 expectedIdentite.setNom("Doe");
 expectedIdentite.setPrenom("John");
 expectedIdentite.setSex("M");
 expectedIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 expectedIdentite.setEmail("john.doe@example.com");
 assertEquals(expectedIdentite, foundIdentite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 Calendar dateLimite = Calendar.getInstance();
 identite.setId(1L); // Assuming an ID is set for the test
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.delete(identite);

 Identite foundIdentite = entityManager.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(1, candidats.length);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(new RespFormation[0], respFormations);
 }
}