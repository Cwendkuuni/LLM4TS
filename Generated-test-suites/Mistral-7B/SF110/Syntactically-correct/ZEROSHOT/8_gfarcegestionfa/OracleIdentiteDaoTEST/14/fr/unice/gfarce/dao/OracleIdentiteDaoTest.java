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
import fr.unice.gfarce.dao.OracleIdentiteDaoTest;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 dao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 // Cleanup code here
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("test", "test", "test", 'M', Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 dao.insert(identite, "form1", dateLimite);
 Identite identiteRetrieved = dao.findById(identite.getId());
 assertEquals(identite, identiteRetrieved);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 Identite identite = null;
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 dao.insert(identite, "form1", dateLimite);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite("test", "test", "test", 'M', Identite.TypeIdentite.CANDIDAT);
 dao.insert(identite, null, new Date());
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDateLimite() {
 Identite identite = new Identite("test", "test", "test", 'M', Identite.TypeIdentite.CANDIDAT);
 dao.insert(identite, "form1", null);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("test", "test", "test", 'M', Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 dao.insert(identite, "form1", dateLimite);
 Identite identiteToUpdate = new Identite("testUpdated", "testUpdated", "testUpdated", 'M', Identite.TypeIdentite.CANDIDAT);
 identiteToUpdate.setId(identite.getId());
 identiteToUpdate.setEmail("testUpdatedEmail@test.com");
 dao.update(identiteToUpdate);
 Identite identiteRetrieved = dao.findById(identite.getId());
 assertEquals(identiteToUpdate, identiteRetrieved);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = null;
 identite.setId(1L);
 identite.setEmail("testEmail@test.com");
 dao.update(identite);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullEmail() {
 Identite identite = new Identite("test", "test", "test", 'M', Identite.TypeIdentite.CANDIDAT);
 identite.setId(1L);
 dao.update(identite);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite("test", "test", "test", 'M', Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 dao.insert(identite, "form1", dateLimite);
 dao.delete(identite);
 Identite identiteRetrieved = dao.findById(identite.getId());
 assertNull(identiteRetrieved);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 identite.setId(1L);
 dao.delete(identite);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testFind() {
 String nom = "test";
 String prenom = "test";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = dao.find(nom, prenom, sex, null, type);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindEmpty() {
 String nom = "";
 String prenom = "";
 String sex = "";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = dao.find(nom, prenom, sex, null, type);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite("testCandidat", "testCandidat", "testCandidat", 'M', Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 dao.insert(identite, "form1", dateLimite);
 Candidat[] candidats = dao.findCandidat("testCandidat", "testCandidat", "M", null);
 assertEquals(1, candidats.length);
 assertEquals(identite, candidats[0]);
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite("testRespForm", "testRespForm", "testRespForm", 'M', Identite.TypeIdentite.FORMATEUR);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 dao.insert(identite, "form1", dateLimite);
 RespFormation[] respFormations = dao.findRespForm("testRespForm", "testRespForm", "M", null);
 assertEquals(1, respFormations.length);
 assertEquals(identite, respFormations[0]);
 }

 @Test
 public void testFindAll() {
 List<Identite> identites = new ArrayList<Identite>();
 Identite identite1 = new Identite("test1", "test1", "test1", 'M', Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("test2", "test2", "test2", 'F', Identite.TypeIdentite.FORMATEUR);
 identites.add(identite1);
 identites.add(identite2);
 for (Identite identite : identites) {
 dao.insert(identite, "form1", new Date());
 }
 Identite[] identitesRetrieved = dao.findAll();
 assertEquals(identites.size(), identitesRetrieved.length);
 }
}