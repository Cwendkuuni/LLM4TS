package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDaoImpl.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "TestForm", dateLimite);

 EntityManagerFactory emf = null;
 EntityManager em = null;
 Identite foundIdentite = null;
 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 assertEquals("M", foundIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setEmail("old.email@example.com");
 existingIdentite.setNom("OldName");
 existingIdentite.setPrenom("OldName");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setDateLimite(new Date());

 identiteDao.update(identite);

 EntityManagerFactory emf = null;
 EntityManager em = null;
 Identite foundIdentite = null;
 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.delete(identite);

 Identite foundIdentite = null;
 try {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 } catch (Exception e) {
 assertNull(foundIdentite);
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(type);
 identiteDao.insert(identite, "TestForm", new Date());

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertFalse(identites.isEmpty());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] expectedCandidats = new Candidat[] { new Candidat(1L, "Doe", "John", "john.doe@example.com", "M", new Date()) };
 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "TestForm", new Date());

 Candidat[] resultCandidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertArrayEquals(expectedCandidats, resultCandidats);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] expectedRespFormations = new RespFormation[] { new RespFormation(1L, "Doe", "John", "john.doe@example.com", "M", new Date()) };
 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite, "TestForm", new Date());

 RespFormation[] resultRespFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(expectedRespFormations, resultRespFormations);
 }
}