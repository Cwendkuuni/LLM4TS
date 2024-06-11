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

 private IdentiteDao dao;

 @Before
 public void setUp() {
 this.dao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("TestNom");
 identite.setPrenom("TestPrenom");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.insert(identite, null, null);

 EntityManagerFactory emf = null;
 EntityManager em = null;
 List<Identite> list = null;

 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 list = (List<Identite>)em.createQuery("select i from Identite i where i.nom = 'TestNom'").getResultList();
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertTrue(list.size() > 0);
 Identite retrievedIdentite = list.get(0);
 assertEquals("TestNom", retrievedIdentite.getNom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 Identite identite = null;
 this.dao.insert(identite, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 Identite identite = new Identite();
 identite.setNom(null);
 this.dao.insert(identite, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDateLimite() {
 Identite identite = new Identite();
 identite.setNom("TestNom");
 identite.setPrenom("TestPrenom");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = null;
 this.dao.insert(identite, null, dateLimite);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("OldNom");
 identite.setPrenom("OldPrenom");
 identite.setEmail("oldEmail@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite newIdentite = new Identite();
 newIdentite.setId(1L);
 newIdentite.setNom("NewNom");
 newIdentite.setPrenom("NewPrenom");
 newIdentite.setEmail("newEmail@example.com");
 newIdentite.setSex("M");
 newIdentite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.update(newIdentite);

 EntityManagerFactory emf = null;
 EntityManager em = null;
 Identite retrievedIdentite = null;

 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 retrievedIdentite = (Identite)em.find(Identite.class, 1L);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertEquals("NewNom", retrievedIdentite.getNom());
 assertEquals("NewPrenom", retrievedIdentite.getPrenom());
 assertEquals("newEmail@example.com", retrievedIdentite.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = null;
 this.dao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("TestNomToDelete");
 identite.setPrenom("TestPrenomToDelete");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 this.dao.insert(identite, null, null);
 this.dao.delete(identite);

 EntityManagerFactory emf = null;
 EntityManager em = null;
 List<Identite> list = null;

 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 list = (List<Identite>)em.createQuery("select i from Identite i where i.nom = 'TestNomToDelete'").getResultList();
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertTrue(list.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = null;
 this.dao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "TestNom";
 String prenom = "TestPrenom";
 String sex = "M";
 String email = "test@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(type);
 this.dao.insert(identite, null, null);

 EntityManagerFactory emf = null;
 EntityManager em = null;
 List<Identite> list = null;

 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 list = this.dao.find(nom, prenom, sex, email, type);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertTrue(list.size() > 0);
 Identite retrievedIdentite = list.get(0);
 assertEquals(nom, retrievedIdentite.getNom());
 assertEquals(prenom, retrievedIdentite.getPrenom());
 assertEquals(sex, retrievedIdentite.getSex());
 assertEquals(email, retrievedIdentite.getEmail());
 assertEquals(type, retrievedIdentite.getType());
 }

 @Test
 public void testFindWithNullParams() {
 EntityManagerFactory emf = null;
 EntityManager em = null;
 List<Identite> list = null;

 try {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 list = this.dao.find(null, null, null, null, null);
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 assertTrue(list.isEmpty());
 }

 @Test
 public void testFindCandidat() {
 String nom = "TestNom";
 String prenom = "TestPrenom";
 String sex = "M";
 String email = "test@example.com";

 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 this.dao.insert(identite, null, null);

 Candidat[] result = this.dao.findCandidat(nom, prenom, sex, email);
 assertTrue(result.length > 0);
 }

 @Test
 public void testFindCandidatWithNullParams() {
 Candidat[] result = this.dao.findCandidat(null, null, null, null);
 assertArrayEquals(new Candidat[0], result);
 }

 @Test
 public void testFindRespForm() {
 String nom = "TestNom";
 String prenom = "TestPrenom";
 String sex = "M";
 String email = "test@example.com";

 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setSex(sex);
 identite.setEmail(email);
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 this.dao.insert(identite, null, null);

 RespFormation[] result = this.dao.findRespForm(nom, prenom, sex, email);
 assertTrue(result.length > 0);
 }

 @Test
 public void testFindRespFormWithNullParams() {
 RespFormation[] result = this.dao.findRespForm(null, null, null, null);
 assertArrayEquals(new RespFormation[0], result);
 }
}