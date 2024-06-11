package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 this.identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 // Clear the database after each test
 identiteDao.deleteAll();
 }

 @Test
 public void testInsert() {
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = new GregorianCalendar(2022, 0, 31);
 identite.setDateLimite(dateLimite.getTime());

 identiteDao.insert(identite, "testForm", dateLimite);

 final Identite retrievedIdentite = identiteDao.findById(identite.getId());
 Assert.assertEquals(identite.getId(), retrievedIdentite.getId());
 Assert.assertEquals(identite.getNom(), retrievedIdentite.getNom());
 Assert.assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 Assert.assertEquals(identite.getSex(), retrievedIdentite.getSex());
 Assert.assertEquals(identite.getType(), retrievedIdentite.getType());
 Assert.assertEquals(identite.getDateLimite(), retrievedIdentite.getDateLimite());
 }

 @Test
 public void testUpdate() {
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());

 final long id = identite.getId();
 identite.setEmail("john.doe@example.com");
 identite.setNom("Smith");
 identite.setPrenom("John");
 identite.setSex("M");
 identiteDao.update(identite);

 final Identite retrievedIdentite = identiteDao.findById(id);
 Assert.assertEquals(identite.getId(), retrievedIdentite.getId());
 Assert.assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
 Assert.assertEquals(identite.getNom(), retrievedIdentite.getNom());
 Assert.assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 Assert.assertEquals(identite.getSex(), retrievedIdentite.getSex());
 Assert.assertEquals(identite.getType(), retrievedIdentite.getType());
 }

 @Test
 public void testDelete() {
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "testForm", new Date());

 final long id = identite.getId();
 identiteDao.delete(identite);

 final Identite retrievedIdentite = identiteDao.findById(id);
 Assert.assertNull(retrievedIdentite);
 }

 @Test
 public void testFind() {
 final Identite identite1 = new Identite();
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "testForm", new Date());

 final Identite identite2 = new Identite();
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite2, "testForm", new Date());

 final Identite identite3 = new Identite();
 identite3.setNom("Doe");
 identite3.setPrenom("John");
 identite3.setSex("M");
 identite3.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite3, "testForm", new Date());

 final List<Identite> identites = identiteDao.find(null, null, null, null, null);
 Assert.assertNotNull(identites);
 Assert.assertEquals(3, identites.size());

 final List<Candidat> candidates = identiteDao.findCandidat("Doe", null, null, null);
 Assert.assertNotNull(candidates);
 Assert.assertEquals(1, candidates.length);
 Assert.assertEquals(identite1.getId(), candidates[0].getId());

 final List<RespFormation> respFormations = identiteDao.findRespForm("Doe", null, null, null);
 Assert.assertNotNull(respFormations);
 Assert.assertEquals(1, respFormations.length);
 Assert.assertEquals(identite3.getId(), respFormations[0].getId());
 }

 @Test
 public void testFindAll() {
 final List<Identite> identites = identiteDao.findAll();
 Assert.assertNotNull(identites);
 Assert.assertEquals(0, identites.size());

 identiteDao.insert(new Identite(), "testForm", new Date());
 identiteDao.insert(new Identite(), "testForm", new Date());
 identiteDao.insert(new Identite(), "testForm", new Date());

 final List<Identite> retrievedIdentites = identiteDao.findAll();
 Assert.assertEquals(3, retrievedIdentites.size());
 }
}