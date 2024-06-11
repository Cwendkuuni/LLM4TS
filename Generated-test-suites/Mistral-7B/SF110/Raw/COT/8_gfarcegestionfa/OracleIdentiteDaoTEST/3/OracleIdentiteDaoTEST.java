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
 public void setup() {
 this.identiteDao = new OracleIdentiteDao();
 }

 @Test
 public void testInsertIdentite() {
 // create test data
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 // call method under test
 this.identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1).getTime());
 // verify result
 // TODO: implement validation logic
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullIdentite() {
 // call method under test with null argument
 this.identiteDao.insert(null, "form", new GregorianCalendar(2022, 1, 1).getTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullForm() {
 // call method under test with null argument
 this.identiteDao.insert(new Identite(), null, new GregorianCalendar(2022, 1, 1).getTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullDate() {
 // call method under test with null argument
 this.identiteDao.insert(new Identite(), "form", null);
 }

 @Test
 public void testUpdateIdentite() {
 // create test data
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 // call method under test
 this.identiteDao.update(identite);
 // verify result
 // TODO: implement validation logic
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNullIdentite() {
 // call method under test with null argument
 this.identiteDao.update(null);
 }

 @Test
 public void testDeleteIdentite() {
 // create test data
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 this.identiteDao.insert(identite, "form", new GregorianCalendar(2022, 1, 1).getTime());
 // call method under test
 this.identiteDao.delete(identite);
 // verify result
 // TODO: implement validation logic
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteIdentiteNullIdentite() {
 // call method under test with null argument
 this.identiteDao.delete(null);
 }

 @Test
 public void testFindIdentites() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setEmail("jane.smith@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 // call method under test
 List<Identite> result = this.identiteDao.find(null, null, null, null, null);
 // verify result
 assertEquals(2, result.size());
 assertTrue(result.contains(identite1));
 assertTrue(result.contains(identite2));
 }

 @Test
 public void testFindIdentitesByNom() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setEmail("jane.doe@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 // call method under test
 List<Identite> result = this.identiteDao.find("Doe", null, null, null, null);
 // verify result
 assertEquals(2, result.size());
 assertTrue(result.contains(identite1));
 assertTrue(result.contains(identite2));
 }

 @Test
 public void testFindIdentitesByPrenom() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("John");
 identite2.setEmail("john.smith@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 // call method under test
 List<Identite> result = this.identiteDao.find(null, "John", null, null, null);
 // verify result
 assertEquals(2, result.size());
 assertTrue(result.contains(identite1));
 assertTrue(result.contains(identite2));
 }

 @Test
 public void testFindIdentitesBySex() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setEmail("jane.smith@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 // call method under test
 List<Identite> result = this.identiteDao.find(null, null, "M", null, null);
 // verify result
 assertEquals(1, result.size());
 assertTrue(result.contains(identite1));
 }

 @Test
 public void testFindIdentitesByType() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setEmail("jane.smith@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 // call method under test
 List<Identite> result = this.identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 // verify result
 assertEquals(1, result.size());
 assertTrue(result.contains(identite1));
 }

 @Test
 public void testFindCandidats() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 // call method under test
 Candidat[] result = this.identiteDao.findCandidat("Doe", null, null, null);
 // verify result
 assertEquals(1, result.length);
 assertTrue(result[0] instanceof Candidat);
 }

 @Test
 public void testFindRespFormations() {
 // create test data
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.FORMATEUR);
 // call method under test
 RespFormation[] result = this.identiteDao.findRespForm("Doe", null, null, null);
 // verify result
 assertEquals(1, result.length);
 assertTrue(result[0] instanceof RespFormation);
 }

}