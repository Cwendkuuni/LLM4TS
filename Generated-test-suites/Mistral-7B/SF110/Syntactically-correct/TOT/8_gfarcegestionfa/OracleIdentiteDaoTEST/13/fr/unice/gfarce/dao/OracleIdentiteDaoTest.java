package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 private EntityManagerFactory emf;

 @Before
 public void setup() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Calendar dateLimite = Calendar.getInstance();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "nomForm", dateLimite);

 Identite retrievedIdentite = identiteDao.find(identite.getNom(), null, null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);

 Assert.assertEquals(identite.getNom(), retrievedIdentite.getNom());
 Assert.assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 Assert.assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
 Assert.assertEquals(identite.getSex(), retrievedIdentite.getSex());
 Assert.assertEquals(identite.getType(), retrievedIdentite.getType());
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite dbIdentite = identiteDao.find(identite.getNom(), null, null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);

 identite.setNom("Smith");
 identite.setEmail("john.smith@example.com");
 identite.setSex("F");
 identiteDao.update(identite);

 Identite updatedIdentite = identiteDao.find(identite.getNom(), null, null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);

 Assert.assertNotEquals(identite.getNom(), dbIdentite.getNom());
 Assert.assertEquals(identite.getNom(), updatedIdentite.getNom());
 Assert.assertNotEquals(identite.getEmail(), dbIdentite.getEmail());
 Assert.assertEquals(identite.getEmail(), updatedIdentite.getEmail());
 Assert.assertEquals(identite.getSex(), updatedIdentite.getSex());
 }

 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);

 List<Identite> deletedIdentite = identiteDao.find(identite.getNom(), null, null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT);

 Assert.assertEquals(0, deletedIdentite.size());
 }

 @Test
 public void testFindIdentites() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "nomForm", null);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setEmail("jane.smith@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite2, "nomForm", null);

 List<Identite> identites = identiteDao.find(null, null, null, null, null);

 Assert.assertEquals(2, identites.size());
 }

 @Test
 public void testFindCandidats() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", null);

 Candidat[] candidats = identiteDao.findCandidat(identite.getNom(), null, null, identite.getEmail());

 Assert.assertNotNull(candidats);
 Assert.assertEquals(1, candidats.length);
 Assert.assertTrue(candidats[0] instanceof Candidat);
 }

 @Test
 public void testFindRespFormations() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite, "nomForm", null);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setEmail("jane.smith@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, "nomForm", null);

 RespFormation[] respFormations = identiteDao.findRespForm(identite.getNom(), null, null, identite.getEmail());

 Assert.assertNotNull(respFormations);
 Assert.assertEquals(1, respFormations.length);
 Assert.assertTrue(respFormations[0] instanceof RespFormation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullName() {
 Identite identite = new Identite();
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "nomForm", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNullName() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom(null);
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.update(identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteIdentiteNullName() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom(null);
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindIdentitesNullType() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(null);
 identiteDao.insert(identite, "nomForm", null);

 List<Identite> identites = identiteDao.find(null, null, null, null, null);

 Assert.assertEquals(0, identites.size());
 }

 @After
 public void teardown() {
 if (emf != null) {
 emf.close();
 }
 }
}