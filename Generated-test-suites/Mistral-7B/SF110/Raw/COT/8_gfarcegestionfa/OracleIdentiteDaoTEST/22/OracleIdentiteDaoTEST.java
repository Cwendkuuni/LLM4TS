package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;

 @Before
 public void setUp() {
 this.identiteDao = OracleIdentiteDaoImpl.getDAO();
 }

 @Test
 public void testInsertMethod() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 dateLimite.set(Calendar.YEAR, 2022);
 dateLimite.set(Calendar.MONTH, 12);
 dateLimite.set(Calendar.DAY_OF_MONTH, 31);
 identite.setDateLimite(formatter.format(dateLimite.getTime()));
 this.identiteDao.insert(identite, "testForm", dateLimite);

 // Check if the record is inserted correctly
 Identite fetchedIdentite = this.identiteDao.findById(identite.getId());
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertEquals('M', fetchedIdentite.getSex());
 assertEquals("john.doe@example.com", fetchedIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, fetchedIdentite.getType());
 assertEquals("2022-12-31", fetchedIdentite.getDateLimite());
 }

 @Test(expected = Exception.class)
 public void testInsertMethodWithException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 dateLimite.set(Calendar.YEAR, 2022);
 dateLimite.set(Calendar.MONTH, 12);
 dateLimite.set(Calendar.DAY_OF_MONTH, 32); // Invalid date
 this.identiteDao.insert(identite, "testForm", dateLimite);
 }

 @Test
 public void testUpdateMethod() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Identite existingIdentite = this.identiteDao.findById(identite.getId());
 existingIdentite.setEmail("new.email@example.com");
 this.identiteDao.update(identite);

 // Check if the record is updated correctly
 Identite fetchedIdentite = this.identiteDao.findById(identite.getId());
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertEquals('M', fetchedIdentite.getSex());
 assertEquals("new.email@example.com", fetchedIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, fetchedIdentite.getType());
 }

 @Test(expected = Exception.class)
 public void testUpdateMethodWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 this.identiteDao.update(identite); // Should have an existing record in the database first
 }

 @Test
 public void testDeleteMethod() {
 Identite identite = new Identite();
 identite.setId(1L);
 this.identiteDao.delete(identite);

 // Check if the record is deleted correctly
 assertNull(this.identiteDao.findById(identite.getId()));
 }

 @Test(expected = Exception.class)
 public void testDeleteMethodWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 this.identiteDao.delete(identite); // Should have a valid record in the database first
 }

 @Test
 public void testFindMethod() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = this.identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindMethodWithNoResults() {
 String nom = "NonExistent";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = this.identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindMethodWithDifferentTypes() {
 String nom = "Doe";
 String prenom = "John";
 String sex = null;
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = null;
 List<Identite> identites = this.identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(2, identites.size()); // Assuming there are 2 records of different types with the same name and email
 }

 @Test
 public void testFindCandidatMethod() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = this.identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(1, candidats.length);
 }

 @Test
 public void testFindRespFormMethod() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "F"; // Assuming there is a female formateur with the same name and email
 String email = "john.doe@example.com";
 RespFormation[] respFormations = this.identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(1, respFormations.length);
 }

 @Test
 public void testFindRespFormMethodWithNoResults() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "F";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = this.identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }
}