package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

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

 @Before
 public void setup() {
 identiteDao = OracleIdentiteDaoImpl.getDAO();
 entityManager = Persistence.createEntityManagerFactory("Identite").createEntityManager();
 }

 @After
 public void tearDown() {
 entityManager.close();
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
 identite.setDateNaissance(calendar.getTime());

 identiteDao.insert(identite, null, null);

 Identite foundIdentite = (Identite) entityManager.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getDateNaissance(), foundIdentite.getDateNaissance());
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
 identite.setDateNaissance(calendar.getTime());

 Identite existingIdentite = (Identite) entityManager.find(Identite.class, identite.getId());
 assertNotNull(existingIdentite);
 existingIdentite.setEmail("john.newemail@example.com");
 identiteDao.update(existingIdentite);

 Identite updatedIdentite = (Identite) entityManager.find(Identite.class, identite.getId());
 assertNotNull(updatedIdentite);
 assertEquals(existingIdentite.getNom(), updatedIdentite.getNom());
 assertEquals(existingIdentite.getPrenom(), updatedIdentite.getPrenom());
 assertEquals("john.newemail@example.com", updatedIdentite.getEmail());
 assertEquals(existingIdentite.getSex(), updatedIdentite.getSex());
 assertEquals(existingIdentite.getType(), updatedIdentite.getType());
 assertEquals(existingIdentite.getDateNaissance(), updatedIdentite.getDateNaissance());
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
 identite.setDateNaissance(calendar.getTime());

 entityManager.persist(identite);
 long id = identite.getId();
 identiteDao.delete(identite);

 assertNull(entityManager.find(Identite.class, id));
 }

 @Test(expected = PersistenceException.class)
 public void testDeleteNonExistingIdentite() {
 Identite identite = new Identite();
 identite.setId(-1L);
 identiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = new ArrayList<Identite>();
 identites.add(new Identite());
 identites.add(new Identite());
 identites.add(new Identite());

 for (Identite identite : identites) {
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(type);
 }

 List<Identite> foundIdentites = identiteDao.find(nom, prenom, sex, email, null);
 assertEquals(1, foundIdentites.size());
 assertEquals(identites.get(0).getNom(), foundIdentites.get(0).getNom());
 assertEquals(identites.get(0).getPrenom(), foundIdentites.get(0).getPrenom());
 assertEquals(identites.get(0).getSex(), foundIdentites.get(0).getSex());
 assertEquals(identites.get(0).getEmail(), foundIdentites.get(0).getEmail());
 assertEquals(identites.get(0).getType(), foundIdentites.get(0).getType());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] expectedCandidats = new Candidat[1];
 Candidat candidat = new Candidat();
 candidat.setNom(nom);
 candidat.setPrenom(prenom);
 candidat.setSex(sex);
 candidat.setEmail(email);
 expectedCandidats[0] = candidat;

 List<Identite> identites = new ArrayList<Identite>();
 identites.add(new Identite());
 identites.add(candidat);
 identites.add(new Identite());

 for (Identite identite : identites) {
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 }

 Candidat[] foundCandidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertArrayEquals(expectedCandidats, foundCandidats);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] expectedRespFormations = new RespFormation[1];
 RespFormation respFormation = new RespFormation();
 respFormation.setNom(nom);
 respFormation.setPrenom(prenom);
 respFormation.setSex(sex);
 respFormation.setEmail(email);
 expectedRespFormations[0] = respFormation;

 List<Identite> identites = new ArrayList<Identite>();
 identites.add(new Identite());
 identites.add(new Identite());
 identites.add(respFormation);

 for (Identite identite : identites) {
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 }

 RespFormation[] foundRespFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(expectedRespFormations, foundRespFormations);
 }

 @Test
 public void testFindWithNullParams() {
 List<Identite> foundIdentites = identiteDao.find(null, null, null, null, null);
 assertTrue(foundIdentites.isEmpty());
 }

 @Test
 public void testFindWithEmptyParams() {
 List<Identite> foundIdentites = identiteDao.find("", "", "", "", null);
 assertTrue(foundIdentites.isEmpty());
 }

 @Test
 public void testFindWithInvalidType() {
 List<Identite> foundIdentites = identiteDao.find("Doe", "John", "M", "john.doe@example.com", null);
 assertTrue(foundIdentites.isEmpty());
 }
}