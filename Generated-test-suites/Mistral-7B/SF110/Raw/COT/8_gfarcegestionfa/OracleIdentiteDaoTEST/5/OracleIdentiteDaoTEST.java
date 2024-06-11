package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao = OracleIdentiteDao.getDAO();

 @Test
 public void testInsert() {
 Identite identite = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteDao.insert(identite, "testInsert", dateLimite);

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(1, identites.size());
 Identite fetchedIdentite = identites.get(0);
 assertEquals(identite.getNom(), fetchedIdentite.getNom());
 assertEquals(identite.getPrenom(), fetchedIdentite.getPrenom());
 assertEquals(identite.getSex(), fetchedIdentite.getSex());
 assertEquals(identite.getType(), fetchedIdentite.getType());
 }

 @Test(expected = Exception.class)
 public void testInsertNullIdentite() {
 Identite identite = null;
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteDao.insert(identite, "testInsert", dateLimite);
 fail("Expected an exception to be thrown");
 }

 @Test(expected = Exception.class)
 public void testInsertNullForm() {
 Identite identite = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 String form = null;
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteDao.insert(identite, form, dateLimite);
 fail("Expected an exception to be thrown");
 }

 @Test(expected = Exception.class)
 public void testInsertNullDateLimite() {
 Identite identite = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 String form = "testInsert";
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = null;
 identiteDao.insert(identite, form, dateLimite);
 fail("Expected an exception to be thrown");
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testUpdate", new Date());
 Identite updatedIdentite = new Identite("John", "Doe", 'M', "john.updated@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(updatedIdentite);

 Identite fetchedIdentite = identiteDao.findById(identite.getId()).get();
 assertEquals(updatedIdentite.getEmail(), fetchedIdentite.getEmail());
 }

 @Test(expected = Exception.class)
 public void testUpdateNullIdentite() {
 Identite identite = null;
 identiteDao.update(identite);
 fail("Expected an exception to be thrown");
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testDelete", new Date());
 identiteDao.delete(identite);

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(0, identites.size());
 }

 @Test(expected = Exception.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 identiteDao.delete(identite);
 fail("Expected an exception to be thrown");
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
 public void testFindNullNom() {
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(null, prenom, sex, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindNullPrenom() {
 String nom = "Doe";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, null, sex, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindNullSex() {
 String nom = "Doe";
 String prenom = "John";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, null, email, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindNullType() {
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
 }

 @Test
 public void testFindCandidatNullNom() {
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(null, prenom, sex, email);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatNullPrenom() {
 String nom = "Doe";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, null, sex, email);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatNullSex() {
 String nom = "Doe";
 String prenom = "John";
 String email = "john.doe@example.com";
 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, null, email);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidatNullEmail() {
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
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormNullNom() {
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(null, prenom, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormNullPrenom() {
 String nom = "Doe";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, null, sex, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormNullSex() {
 String nom = "Doe";
 String prenom = "John";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, null, email);
 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormNullEmail() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, null);
 assertEquals(0, respFormations.length);
 }

}