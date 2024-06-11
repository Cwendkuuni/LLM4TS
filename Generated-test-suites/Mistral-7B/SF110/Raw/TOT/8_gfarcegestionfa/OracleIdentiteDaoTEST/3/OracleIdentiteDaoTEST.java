package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 identiteDao.insert(identite, "DefaultPersistenceUnit", null);

 Identite retrievedIdentite = identiteDao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);

 Assert.assertEquals(identite.getNom(), retrievedIdentite.getNom());
 Assert.assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 Assert.assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
 Assert.assertEquals(identite.getType(), retrievedIdentite.getType());
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
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setNom("Jones");
 existingIdentite.setPrenom("Jane");
 existingIdentite.setSex("F");
 existingIdentite.setEmail("jane.jones@example.com");
 existingIdentite.setType(Identite.TypeIdentite.FORMATEUR);
 SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar2 = Calendar.getInstance();
 existingIdentite.setDateNaissance(calendar2.getTime());

 identiteDao.update(identite);

 Identite retrievedIdentite = identiteDao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);

 Assert.assertNotEquals(existingIdentite.getNom(), retrievedIdentite.getNom());
 Assert.assertEquals(identite.getNom(), retrievedIdentite.getNom());
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
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 long initialSize = identites.size();

 identiteDao.delete(identite);

 identites = identiteDao.find(null, null, null, null, null);

 Assert.assertEquals(initialSize - 1, identites.size());
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex("M");
 identite1.setEmail("john.doe@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite1.setDateNaissance(calendar.getTime());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Jones");
 identite2.setPrenom("Jane");
 identite2.setSex("F");
 identite2.setEmail("jane.jones@example.com");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar2 = Calendar.getInstance();
 identite2.setDateNaissance(calendar2.getTime());

 List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);

 identiteDao.insert(identite1, "DefaultPersistenceUnit", null);
 identiteDao.insert(identite2, "DefaultPersistenceUnit", null);

 List<Identite> retrievedIdentites = identiteDao.find(identite1.getNom(), identite1.getPrenom(), null, identite1.getEmail(), Identite.TypeIdentite.CANDIDAT);

 Assert.assertEquals(1, retrievedIdentites.size());
 Assert.assertEquals(identite1, retrievedIdentites.get(0));

 retrievedIdentites = identiteDao.find(identite2.getNom(), identite2.getPrenom(), null, identite2.getEmail(), Identite.TypeIdentite.FORMATEUR);

 Assert.assertEquals(1, retrievedIdentites.size());
 Assert.assertEquals(identite2, retrievedIdentites.get(0));
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
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 Identite[] retrievedCandidats = identiteDao.findCandidat(identite.getNom(), identite.getPrenom(), null, identite.getEmail());

 Assert.assertArrayNotEquals(new Identite[0], retrievedCandidats);
 Assert.assertEquals(1, retrievedCandidats.length);
 Assert.assertEquals(identite, retrievedCandidats[0]);
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(2L);
 identite.setNom("Jones");
 identite.setPrenom("Jane");
 identite.setSex("F");
 identite.setEmail("jane.jones@example.com");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 RespFormation[] retrievedRespFormations = identiteDao.findRespForm(identite.getNom(), identite.getPrenom(), null, identite.getEmail());

 Assert.assertArrayNotEquals(new RespFormation[0], retrievedRespFormations);
 Assert.assertEquals(1, retrievedRespFormations.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 Identite identite = null;
 identiteDao.insert(identite, "DefaultPersistenceUnit", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom(null);
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 identiteDao.insert(identite, "DefaultPersistenceUnit", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = null;
 identiteDao.update(identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 identiteDao.delete(identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullNom() {
 String nom = null;
 identiteDao.find(nom, null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullPrenom() {
 String prenom = null;
 identiteDao.find(null, prenom, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullSex() {
 String sex = null;
 identiteDao.find(null, null, sex, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullEmail() {
 String email = null;
 identiteDao.find(null, null, null, email, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullType() {
 Identite.TypeIdentite type = null;
 identiteDao.find(null, null, null, null, type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatNullNom() {
 String nom = null;
 identiteDao.findCandidat(nom, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatNullPrenom() {
 String prenom = null;
 identiteDao.findCandidat(null, prenom, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatNullSex() {
 String sex = null;
 identiteDao.findCandidat(null, null, sex, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatNullEmail() {
 String email = null;
 identiteDao.findCandidat(null, null, null, email);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatNullType() {
 Identite.TypeIdentite type = null;
 identiteDao.findCandidat(null, null, null, null, type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormNullNom() {
 String nom = null;
 identiteDao.findRespForm(nom, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormNullPrenom() {
 String prenom = null;
 identiteDao.findRespForm(null, prenom, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormNullSex() {
 String sex = null;
 identiteDao.findRespForm(null, null, sex, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormNullEmail() {
 String email = null;
 identiteDao.findRespForm(null, null, null, email);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormNullType() {
 Identite.TypeIdentite type = null;
 identiteDao.findRespForm(null, null, null, null, type);
 }
}