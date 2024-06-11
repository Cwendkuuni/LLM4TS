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
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Identite.Sex;
import fr.unice.gfarce.identity.Identite.TypeIdentite;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 this.identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("John Doe", "Doe", "john.doe@example.com", Sex.MALE, TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2022, 1, 1);
 Date dateLimite = calendar.getTime();
 this.identiteDao.insert(identite, "test", dateLimite);
 // Verify the insertion by checking if the entity is found in the database
 Identite foundIdentite = this.identiteDao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), TypeIdentite.CANDIDAT).get(0);
 assertEquals(identite, foundIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 this.identiteDao.insert(null, "test", new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite("John Doe", "Doe", "john.doe@example.com", Sex.MALE, TypeIdentite.CANDIDAT);
 this.identiteDao.insert(identite, null, new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullCalendar() {
 Identite identite = new Identite("John Doe", "Doe", "john.doe@example.com", Sex.MALE, TypeIdentite.CANDIDAT);
 this.identiteDao.insert(identite, "test", null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("John Doe", "Doe", "john.doe@example.com", Sex.MALE, TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2022, 1, 1);
 Date dateLimite = calendar.getTime();
 this.identiteDao.insert(identite, "test", dateLimite);

 Identite updatedIdentite = new Identite("John Doe", "Doe", "john.updated@example.com", Sex.MALE, TypeIdentite.CANDIDAT);
 this.identiteDao.update(updatedIdentite);

 // Verify the update by checking if the entity in the database has been updated
 Identite foundIdentite = this.identiteDao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), TypeIdentite.CANDIDAT).get(0);
 assertNotEquals(identite, foundIdentite);
 assertEquals(updatedIdentite, foundIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite updatedIdentite = new Identite("John Doe", "Doe", "john.doe@example.com", Sex.MALE, TypeIdentite.CANDIDAT);
 this.identiteDao.update(updatedIdentite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "MALE";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> result = this.identiteDao.find(nom, prenom, sex, null, type);
 // Verify the result
 assertFalse(result.isEmpty());
 Identite foundIdentite = result.get(0);
 assertEquals(nom, foundIdentite.getNom());
 assertEquals(prenom, foundIdentite.getPrenom());
 assertEquals(sex, foundIdentite.getSex().toString());
 assertEquals(type, foundIdentite.getType());
 }

 @Test
 public void testFindEmpty() {
 String nom = "NonExistent";
 String prenom = "Name";
 String sex = "MALE";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> result = this.identiteDao.find(nom, prenom, sex, null, type);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindWithEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "MALE";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> result = this.identiteDao.find(nom, prenom, null, email, type);
 // Verify the result
 assertEquals(1, result.size());
 Identite foundIdentite = result.get(0);
 assertEquals(nom, foundIdentite.getNom());
 assertEquals(prenom, foundIdentite.getPrenom());
 assertEquals(sex, foundIdentite.getSex().toString());
 assertEquals(type, foundIdentite.getType());
 assertEquals(email, foundIdentite.getEmail());
 }

 @Test
 public void testFindWithTypeCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "MALE";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> result = this.identiteDao.find(nom, prenom, null, email, type);
 // Verify the result
 assertEquals(1, result.size());
 Identite foundIdentite = result.get(0);
 assertEquals(nom, foundIdentite.getNom());
 assertEquals(prenom, foundIdentite.getPrenom());
 assertEquals(sex, foundIdentite.getSex().toString());
 assertEquals(type, foundIdentite.getType());
 assertEquals(email, foundIdentite.getEmail());
 }

 @Test
 public void testFindWithTypeFormateur() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "MALE";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.FORMATEUR;
 List<Identite> result = this.identiteDao.find(nom, prenom, null, email, type);
 // Verify the result is empty as no identities with given email and type exist
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindAll() {
 List<Identite> result = this.identiteDao.find(null, null, null, null, null);
 // Verify the result
 assertFalse(result.isEmpty());
 }

 @Test
 public void testFindCandidats() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "MALE";
 String email = "john.doe@example.com";
 Candidat[] result = this.identiteDao.findCandidat(nom, prenom, sex, email);
 // Verify the result
 assertArrayEquals(new Candidat[] { new Candidat("John Doe", "Doe", "john.doe@example.com", Sex.MALE, TypeIdentite.CANDIDAT) }, result);
 }

 @Test
 public void testFindNoCandidats() {
 String nom = "NonExistent";
 String prenom = "Name";
 String sex = "MALE";
 String email = "john.doe@example.com";
 Candidat[] result = this.identiteDao.findCandidat(nom, prenom, sex, email);
 // Verify the result is empty
 assertArrayEquals(new Candidat[0], result);
 }

 @Test
 public void testFindResponsablesFormation() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "MALE";
 String email = "john.doe@example.com";
 RespFormation[] result = this.identiteDao.findRespForm(nom, prenom, sex, email);
 // Verify the result is empty as no identities with given email and type exist
 assertArrayEquals(new RespFormation[0], result);
 }

}