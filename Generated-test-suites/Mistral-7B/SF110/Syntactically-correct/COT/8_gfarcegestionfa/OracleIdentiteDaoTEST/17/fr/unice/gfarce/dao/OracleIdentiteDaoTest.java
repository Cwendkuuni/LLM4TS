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

 private IdentiteDao identiteDao;

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
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite.setDateNaiss(date);

 identiteDao.insert(identite, "test", date);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertEquals(identite, foundIdentite);
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite.setDateNaiss(date);

 String exceptionMessage = "Error message when inserting identite";
 expectThrows(Exception.class, () -> identiteDao.insert(identite, "test", date)).withMessage(exceptionMessage);
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
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite.setDateNaiss(date);

 Identite updatedIdentite = new Identite();
 updatedIdentite.setId(identite.getId());
 updatedIdentite.setEmail("john.doe_updated@example.com");
 updatedIdentite.setNom("Doe_updated");
 updatedIdentite.setPrenom("John_updated");
 updatedIdentite.setSex("M");
 updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 updatedIdentite.setDateNaiss(date);

 identiteDao.update(updatedIdentite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotEquals(identite, foundIdentite);
 assertEquals("Doe_updated", foundIdentite.getNom());
 assertEquals("John_updated", foundIdentite.getPrenom());
 assertEquals("john.doe_updated@example.com", foundIdentite.getEmail());
 }

 @Test(expected = Exception.class)
 public void testUpdateWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite updatedIdentite = new Identite();
 updatedIdentite.setId(identite.getId());
 updatedIdentite.setEmail("john.doe@example.com"); // same email
 updatedIdentite.setNom("Doe_updated");
 updatedIdentite.setPrenom("John_updated");
 updatedIdentite.setSex("M");
 updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 updatedIdentite.setDateNaiss(new Date());

 String exceptionMessage = "Error message when updating identite with same email";
 expectThrows(Exception.class, () -> identiteDao.update(updatedIdentite)).withMessage(exceptionMessage);
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
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite.setDateNaiss(date);
 identiteDao.insert(identite, "test", date);

 identiteDao.delete(identite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNull(foundIdentite);
 }

 @Test(expected = Exception.class)
 public void testDeleteWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.delete(identite);

 String exceptionMessage = "Error message when deleting non-existent identite";
 expectThrows(Exception.class, () -> identiteDao.delete(identite)).withMessage(exceptionMessage);
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite1.setDateNaiss(date);
 identiteDao.insert(identite1, "test", date);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe@example.com");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite2, "test", date);

 List<Identite> result = identiteDao.find(null, null, null, null, null);
 assertEquals(2, result.size());

 List<Identite> filteredResult = new ArrayList<>();
 for (Identite identite : result) {
 if (identite.getNom().startsWith("Doe")) {
 filteredResult.add(identite);
 }
 }
 assertEquals(2, filteredResult.size());
 assertEquals("Doe1", filteredResult.get(0).getNom());
 assertEquals("Doe2", filteredResult.get(1).getNom());
 }

 @Test
 public void testFindWithEmptyParams() {
 List<Identite> result = identiteDao.find(null, null, null, null, null);
 assertEquals(0, result.size());
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite.setDateNaiss(date);
 identiteDao.insert(identite, "test", date);

 Candidat[] result = identiteDao.findCandidat(null, null, null, null);
 assertEquals(1, result.length);
 assertEquals("Doe", result[0].getNom());
 assertEquals("John", result[0].getPrenom());
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 identite.setDateNaiss(date);
 identiteDao.insert(identite, "test", date);

 RespFormation[] result = identiteDao.findRespForm(null, null, null, null);
 assertEquals(1, result.length);
 assertEquals("Doe", result[0].getNom());
 assertEquals("John", result[0].getPrenom());
 }

 @Test
 public void testFindWithEmptyCandidat() {
 Candidat[] result = identiteDao.findCandidat(null, null, null, null);
 assertEquals(0, result.length);
 }

 @Test
 public void testFindWithEmptyRespForm() {
 RespFormation[] result = identiteDao.findRespForm(null, null, null, null);
 assertEquals(0, result.length);
 }
}