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
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 // Clean up any data created during the tests
 identiteDao.delete(new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT));
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteDao.insert(identite, null, dateLimite);
 List<Identite> result = identiteDao.find("test", null, null, "test@test.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, null);
 Identite updatedIdentite = new Identite("updatedTest", "updatedTest", "updatedTest", "updatedTest@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.update(updatedIdentite);
 List<Identite> result = identiteDao.find("updatedTest", null, null, "updatedTest@test.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, result.size());
 assertEquals("updatedTest", result.get(0).getNom());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite("test", "test", "test", "test@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, null, null);
 identiteDao.delete(identite);
 List<Identite> result = identiteDao.find("test", null, null, "test@test.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(0, result.size());
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite("identite1", "test", "test", "test1@test.com", Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("identite2", "test", "test", "test2@test.com", Identite.TypeIdentite.CANDIDAT);
 Identite identite3 = new Identite("identite3", "test", "test", "test3@test.com", Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, null, null);
 identiteDao.insert(identite2, null, null);
 identiteDao.insert(identite3, null, null);
 List<Identite> result = identiteDao.find(null, null, null, null, null);
 assertEquals(3, result.size());
 }

 @Test
 public void testFindCandidat() {
 Identite identite1 = new Identite("identite1", "test", "test", "test1@test.com", Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("identite2", "test", "test", "test2@test.com", Identite.TypeIdentite.CANDIDAT);
 Identite identite3 = new Identite("identite3", "test", "test", "test3@test.com", Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite1, null, null);
 identiteDao.insert(identite2, null, null);
 identiteDao.insert(identite3, null, null);
 Candidat[] result = identiteDao.findCandidat("test", null, null, "test1@test.com");
 assertEquals(1, result.length);
 assertEquals("identite1", result[0].getId().getNom());
 }

 @Test
 public void testFindRespForm() {
 Identite identite1 = new Identite("identite1", "test", "test", "test1@test.com", Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite("identite2", "test", "test", "test2@test.com", Identite.TypeIdentite.FORMATEUR);
 Identite identite3 = new Identite("identite3", "test", "test", "test3@test.com", Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite1, null, null);
 identiteDao.insert(identite2, null, null);
 identiteDao.insert(identite3, null, null);
 RespFormation[] result = identiteDao.findRespForm("test", null, null, "test2@test.com");
 assertEquals(1, result.length);
 assertEquals("identite2", result[0].getId().getNom());
 }
}