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
import fr.unice.gfarce.dao.OracleIdentiteDao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Identite;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;

 @Before
 public void setup() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void cleanup() {
 // Cleanup code here
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "TestForm", new Calendar.Builder().setYear(2022).setMonth(1).setDayOfMonth(1).build().getTime());

 // Check if the record is inserted
 Identite fetchedIdentite = identiteDao.findByIdentite("john.doe@example.com");
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertEquals("M", fetchedIdentite.getSex());
 assertEquals("john.doe@example.com", fetchedIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, fetchedIdentite.getType());
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
 identiteDao.update(identite);

 // Check if the record is updated
 Identite fetchedIdentite = identiteDao.findByIdentite("john.doe@example.com");
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertEquals("M", fetchedIdentite.getSex());
 assertEquals("john.doe@example.com", fetchedIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, fetchedIdentite.getType());
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
 identiteDao.delete(identite);

 // Check if the record is deleted
 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);

 // Check if the records are fetched correctly
 assertEquals(1, identites.size());
 Identite identite = identites.get(0);
 assertEquals(nom, identite.getNom());
 assertEquals(prenom, identite.getPrenom());
 assertEquals(sex, identite.getSex());
 assertEquals(email, identite.getEmail());
 assertEquals(type, identite.getType());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);

 // Check if the records are fetched correctly
 assertEquals(1, candidats.length);
 assertEquals(nom, candidats[0].getNom());
 assertEquals(prenom, candidats[0].getPrenom());
 assertEquals(sex, candidats[0].getSex());
 assertEquals(email, candidats[0].getEmail());
 // Check if the candidate properties are set correctly
 assertEquals(1, candidats[0].getNombreFormations());
 assertEquals(new Date(), candidats[0].getDateDerniereMaj());
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);

 // Check if the records are fetched correctly
 assertEquals(0, respFormations.length);
 }
}