package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.junit.runner.*;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

@RunWith(JUnitRunner.class)
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
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 30));

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());

 assertEquals("Doe", foundIdentite.getNom());
 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testInsert_Exception() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 30));
 fail("Exception expected");
 } catch (Exception e) {
 // Expected exception
 tx.commit();
 }

 em.close();
 emf.close();
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.update(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());

 assertEquals("Doe", foundIdentite.getNom());
 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testUpdate_Exception() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 identiteDao.update(identite);
 fail("Exception expected");
 } catch (Exception e) {
 // Expected exception
 tx.commit();
 }

 em.close();
 emf.close();
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());

 assertNull(foundIdentite);
 em.close();
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testDelete_Exception() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 identiteDao.delete(identite);
 fail("Exception expected");
 } catch (Exception e) {
 // Expected exception
 tx.commit();
 }

 em.close();
 emf.close();
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);

 assertEquals(1, identites.size());
 assertEquals(nom, identites.get(0).getNom());
 }

 @Test
 public void testFind_NullParam() {
 String nom = null;
 String prenom = "John";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);

 assertEquals(0, identites.size());
 }

 @Test
 public void testFind_EmptyList() {
 String nom = "";
 String prenom = "";
 String sex = "";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, null, type);

 assertEquals(0, identites.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);

 assertEquals(1, candidats.length);
 assertEquals(nom, candidats[0].getNom());
 assertEquals(prenom, candidats[0].getPrenom());
 }

 @Test
 public void testFindCandidat_NullParam() {
 String nom = null;
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);

 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindCandidat_EmptyList() {
 String nom = "";
 String prenom = "";
 String sex = "";
 String email = "";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);

 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);

 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespForm_NullParam() {
 String nom = null;
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);

 assertEquals(0, respFormations.length);
 }

 @Test
 public void testFindRespForm_EmptyList() {
 String nom = "";
 String prenom = "";
 String sex = "";
 String email = "";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);

 assertEquals(0, respFormations.length);
 }
}