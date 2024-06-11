package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao dao;

 @Before
 public void setUp() {
 this.emf = Persistence.createEntityManagerFactory("Identite");
 this.em = this.emf.createEntityManager();
 this.dao = OracleIdentiteDaoImpl.getDAO();
 }

 @After
 public void tearDown() {
 if (this.em != null && this.em.isOpen()) {
 this.em.close();
 }
 if (this.emf != null && this.emf.isOpen()) {
 this.emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.insert(identite, "test", null);
 Identite retrievedIdentite = (Identite)this.em.find(Identite.class, identite.getId());

 assertTrue("Identite not persisted properly", retrievedIdentite != null);
 assertTrue("Identite not persisted with correct data", identite.equals(retrievedIdentite));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 this.dao.insert(null, "test", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite();
 identite.setNom(null);
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.insert(identite, "test", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullEmail() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail(null);
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.insert(identite, "test", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullSex() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex(null);
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.insert(identite, "test", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullType() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(null);

 this.dao.insert(identite, "test", null);
 }

 @Test(expected = PersistenceException.class)
 public void testInsertDuplicateIdentite() {
 Identite identite1 = new Identite();
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setEmail("john.doe@example.com");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 this.dao.insert(identite1, "test", null);
 Identite identite2 = new Identite();identite2.setNom("Doe");identite2.setPrenom("John");identite2.setEmail("john.doe@example.com");identite2.setSex('M');identite2.setType(Identite.TypeIdentite.CANDIDAT);this.dao.insert(identite2, "test", null);
 }

 @Test(expected = RollbackException.class)
 public void testInsertNullPersistenceUnitName() {
 OracleIdentiteDao dao = new OracleIdentiteDao();
 dao.insert(new Identite(), "test", null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 this.dao.insert(identite, "test", null);

 Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(identite.getId());
 identiteToUpdate.setEmail("john.doe.updated@example.com");
 this.dao.update(identiteToUpdate);

 Identite retrievedIdentite = (Identite)this.em.find(Identite.class, identite.getId());
 assertTrue("Identite not updated properly", identiteToUpdate.getEmail().equals(retrievedIdentite.getEmail()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 this.dao.update(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentiteId() {
 Identite identite = new Identite();
 this.dao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 this.dao.insert(identite, "test", null);
 this.dao.delete(identite);
 Identite retrievedIdentite = (Identite)this.em.find(Identite.class, identite.getId());
 assertNull("Identite not deleted properly", retrievedIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 this.dao.delete(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentiteId() {
 Identite identite = new Identite();
 this.dao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = this.dao.find(nom, prenom, sex, null, type);
 Identite identite = identites.get(0);
 Identite expectedIdentite = new Identite();
 expectedIdentite.setId(1L);
 expectedIdentite.setNom("Doe");
 expectedIdentite.setPrenom("John");
 expectedIdentite.setEmail("john.doe@example.com");
 expectedIdentite.setSex('M');
 expectedIdentite.setType(type);
 assertTrue("Identites not found properly", expectedIdentite.equals(identite));
 }

 @Test
 public void testFindEmpty() {
 String nom = "Invalid";
 String prenom = "Invalid";
 String sex = "Invalid";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = this.dao.find(nom, prenom, sex, null, type);
 assertTrue("Identites found when they should be empty", identites.isEmpty());
 }

 @Test
 public void testFindByEmail() {
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = this.dao.find(null, null, null, email, type);
 Identite identite = identites.get(0);
 Identite expectedIdentite = new Identite();
 expectedIdentite.setId(1L);
 expectedIdentite.setNom("Doe");
 expectedIdentite.setPrenom("John");
 expectedIdentite.setEmail("john.doe@example.com");
 expectedIdentite.setSex('M');
 expectedIdentite.setType(type);
 assertTrue("Identites not found properly", expectedIdentite.equals(identite));
 }

 @Test
 public void testFindByEmailEmpty() {
 String email = "john.doe.invalid@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = this.dao.find(null, null, null, email, type);
 assertTrue("Identites found when they should be empty", identites.isEmpty());
 }

 @Test
 public void testFindCandidats() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";

 Candidat[] candidats = this.dao.findCandidat(nom, prenom, sex, null);
 assertTrue("Candidats not found properly", candidats.length > 0);
 Candidat candidat = candidats[0];
 assertTrue("Candidats not of expected type", candidat instanceof Candidat);
 }

 @Test
 public void testFindCandidatsEmpty() {
 String nom = "Invalid";
 String prenom = "Invalid";
 String sex = "Invalid";

 Candidat[] candidats = this.dao.findCandidat(nom, prenom, sex, null);
 assertTrue("Candidats found when they should be empty", candidats.length == 0);
 }

 @Test
 public void testFindRespFormations() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";

 RespFormation[] respFormations = this.dao.findRespForm(nom, prenom, sex, null);
 assertTrue("RespFormations not found properly", respFormations.length > 0);
 RespFormation respFormation = respFormations[0];
 assertTrue("RespFormations not of expected type", respFormation instanceof RespFormation);
 }

 @Test
 public void testFindRespFormationsEmpty() {
 String nom = "Invalid";
 String prenom = "Invalid";
 String sex = "Invalid";

 RespFormation[] respFormations = this.dao.findRespForm(nom, prenom, sex, null);
 assertTrue("RespFormations found when they should be empty", respFormations.length == 0);
 }
}