package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManager em;

 @Before
 public void setUp() {
 dao = OracleIdentiteDaoImpl.getDAO();
 em = dao.getEntityManager();
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "nomForm", new Date());

 Identite retrievedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(retrievedIdentite);
 assertEquals("Test", retrievedIdentite.getNom());
 assertEquals("Test", retrievedIdentite.getPrenom());
 assertEquals("test@test.com", retrievedIdentite.getEmail());
 }

 @Test
 public void testInsertIdentiteNullParameters() {
 Identite identite = null;
 assertThrows(IllegalArgumentException.class, () -> dao.insert(identite, "nomForm", new Date()));
 }

 @Test
 public void testInsertIdentiteNullNomForm() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 String nomForm = null;
 assertThrows(IllegalArgumentException.class, () -> dao.insert(identite, nomForm, new Date()));
 }

 @Test
 public void testInsertIdentiteNullDate() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Date date = null;
 assertThrows(IllegalArgumentException.class, () -> dao.insert(identite, "nomForm", date));
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setId(1L);

 dao.update(identite);

 Identite retrievedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("Test", retrievedIdentite.getNom());
 assertEquals("Test", retrievedIdentite.getPrenom());
 assertEquals("test@test.com", retrievedIdentite.getEmail());
 }

 @Test
 public void testUpdateIdentiteNullIdentite() {
 assertThrows(IllegalArgumentException.class, () -> dao.update(null));
 }

 @Test
 public void testUpdateIdentiteNullId() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setId(null);

 assertThrows(IllegalArgumentException.class, () -> dao.update(identite));
 }

 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setId(1L);

 dao.delete(identite);

 Identite retrievedIdentite = em.find(Identite.class, identite.getId());
 assertNull(retrievedIdentite);
 }

 @Test
 public void testDeleteIdentiteNullIdentite() {
 assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
 }

 @Test
 public void testFindIdentites() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = dao.find(nom, prenom, sex, email, type);
 assertNotNull(identites);
 }

 @Test
 public void testFindIdentitesNullType() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";
 assertThrows(IllegalArgumentException.class, () -> dao.find(nom, prenom, sex, email, null));
 }

 @Test
 public void testFindCandidats() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";

 Candidat[] candidats = dao.findCandidats(nom, prenom, sex, email);
 assertNotNull(candidats);
 }

 @Test
 public void testFindCandidatsNullParameters() {
 assertThrows(IllegalArgumentException.class, () -> dao.findCandidats(null, null, null, null));
 }

 @Test
 public void testFindResponsablesFormations() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@test.com";

 RespFormation[] respFormations = dao.findResponsablesFormations(nom, prenom, sex, email);
 assertNotNull(respFormations);
 }

 @Test
 public void testFindResponsablesFormationsNullParameters() {
 assertThrows(IllegalArgumentException.class, () -> dao.findResponsablesFormations(null, null, null, null));
 }

 @Test
 public void testPersistenceException() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 em.persist(new Identite());
 }
 catch (PersistenceException e) {
 assertEquals("An exception should be thrown when persisting a new entity without setting its id", e.getMessage());
 }

 tx.rollback();
 em.close();
 emf.close();
 }
}