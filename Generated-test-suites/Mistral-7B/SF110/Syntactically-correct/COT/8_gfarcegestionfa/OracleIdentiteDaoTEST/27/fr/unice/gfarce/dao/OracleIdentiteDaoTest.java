package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.junit.runner.*;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

@RunWith(JUnit4.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 dao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "", null);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite ident = em.find(Identite.class, identite.getId());

 assertEquals("Doe", ident.getNom());
 assertEquals("John", ident.getPrenom());
 assertEquals("john.doe@example.com", ident.getEmail());
 assertEquals('M', ident.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, ident.getType());

 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "", null);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite ident = em.find(Identite.class, identite.getId());

 fail("Exception should have been thrown but no exception was caught.");
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

 dao.update(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite ident = em.find(Identite.class, identite.getId());

 assertEquals("Doe", ident.getNom());
 assertEquals("John", ident.getPrenom());
 assertEquals("john.doe@example.com", ident.getEmail());
 assertEquals('M', ident.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, ident.getType());

 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testUpdateWithNotFoundException() {
 Identite identite = new Identite();
 identite.setId(100L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.delete(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite ident = em.find(Identite.class, identite.getId());

 assertNull(ident);

 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testDeleteWithNotFoundException() {
 Identite identite = new Identite();
 identite.setId(100L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = dao.find(nom, prenom, sex, null, type);

 assertEquals(1, identites.size());
 Identite ident = identites.get(0);
 assertEquals("Doe", ident.getNom());
 assertEquals("John", ident.getPrenom());
 assertEquals(sex, ident.getSex());
 assertEquals(type, ident.getType());
 }

 @Test
 public void testFindWithNoResults() {
 String nom = "Smith";
 String prenom = "Jane";
 String sex = "F";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identites = dao.find(nom, prenom, sex, null, type);

 assertEquals(0, identites.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidats = dao.findCandidat(nom, prenom, sex, email);

 assertEquals(1, candidats.length);
 Candidat candidat = candidats[0];
 assertEquals(nom, candidat.getNom());
 assertEquals(prenom, candidat.getPrenom());
 assertEquals(sex, candidat.getSex());
 assertEquals(email, candidat.getEmail());
 }

 @Test
 public void testFindCandidatWithNoResults() {
 String nom = "Smith";
 String prenom = "Jane";
 String sex = "F";
 String email = "jane.smith@example.com";
 Candidat[] candidats = dao.findCandidat(nom, prenom, sex, email);

 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = dao.findRespForm(nom, prenom, sex, email);

 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespFormWithResults() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.FORMATEUR);

 dao.update(identite);

 RespFormation[] respFormations = dao.findRespForm(nom, prenom, sex, email);

 assertEquals(1, respFormations.length);
 RespFormation respFormation = respFormations[0];
 assertEquals(nom, respFormation.getNom());
 assertEquals(prenom, respFormation.getPrenom());
 assertEquals(sex, respFormation.getSex());
 assertEquals(email, respFormation.getEmail());
 }

 @After
 public void tearDown() {
 // None necessary
 }
}