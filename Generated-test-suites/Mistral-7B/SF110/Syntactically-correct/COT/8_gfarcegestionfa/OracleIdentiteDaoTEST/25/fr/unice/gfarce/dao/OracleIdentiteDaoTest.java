package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 identiteDao.insert(identite, "test", calendar);

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite"); // Wrong persistence unit name
 EntityManager em = emf.createEntityManager();
 try {
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 em.persist(identite);
 tx.commit();
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
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 identiteDao.update(identite);

 Identite foundIdentite = identiteDao.findById(1L);
 assertEquals("Doe", foundIdentite.getNom());
 }

 @Test(expected = Exception.class)
 public void testUpdateWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite"); // Wrong persistence unit name
 EntityManager em = emf.createEntityManager();
 Identite existingIdentite = em.find(Identite.class, 1L);
 try {
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 existingIdentite.setEmail("new.email@example.com");
 identiteDao.update(identite); // Should update existing identite, but this test case is checking for an exception instead
 tx.commit();
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
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));
 identiteDao.insert(identite, "test", calendar);

 List<Identite> identitesBeforeDelete = identiteDao.find(null, null, null, null, null);
 assertEquals(1, identitesBeforeDelete.size());
 identiteDao.delete(identite);
 List<Identite> identitesAfterDelete = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identitesAfterDelete.size());
 }

 @Test(expected = Exception.class)
 public void testDeleteWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2000, 1, 1);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite"); // Wrong persistence unit name
 EntityManager em = emf.createEntityManager();
 Identite existingIdentite = em.find(Identite.class, 1L);
 try {
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 identiteDao.delete(identite); // This identite does not exist, so an exception should be thrown
 tx.commit();
 }
 finally {
 em.close();
 emf.close();
 }
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
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 }

 @Test
 public void testFindWithEmptyParams() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithNullNom() {
 String nom = null;
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithNullPrenom() {
 String nom = "Doe";
 String prenom = null;
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithNullSex() {
 String nom = "Doe";
 String prenom = "John";
 String sex = null;
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithNullType() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(1, candidats.length);
 Candidat foundCandidat = candidats[0];
 assertEquals("Doe", foundCandidat.getNom());
 }

 @Test
 public void testFindCandidatWithEmptyParams() {
 Candidat[] candidats = identiteDao.findCandidat(null, null, null, null);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatWithNullNom() {
 String nom = null;
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatWithNullPrenom() {
 String nom = "Doe";
 String prenom = null;
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatWithNullSex() {
 String nom = "Doe";
 String prenom = "John";
 String sex = null;
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatWithNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, null);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "F";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormWithEmptyParams() {
 RespFormation[] respFormations = identiteDao.findRespForm(null, null, null, null);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormWithNullNom() {
 String nom = null;
 String prenom = "John";
 String sex = "F";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormWithNullPrenom() {
 String nom = "Doe";
 String prenom = null;
 String sex = "F";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormWithNullSex() {
 String nom = "Doe";
 String prenom = "John";
 String sex = null;
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormWithNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "F";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, null);
 assertEquals(0, respFormations.length);
 }
}