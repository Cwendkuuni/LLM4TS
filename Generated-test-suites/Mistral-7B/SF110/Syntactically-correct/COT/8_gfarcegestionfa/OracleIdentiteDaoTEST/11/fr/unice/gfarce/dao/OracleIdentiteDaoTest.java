package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.Test;

import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;

 @Before
 public void setup() {
 dao = OracleIdentiteDao.getDAO();
 }

 // Test for insert method
 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 Calendar calendar = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 dao.insert(identite, "Form", calendar);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite dbIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), dbIdentite.getNom());
 assertEquals(identite.getPrenom(), dbIdentite.getPrenom());
 assertEquals(identite.getEmail(), dbIdentite.getEmail());
 assertEquals(identite.getSex(), dbIdentite.getSex());
 assertEquals(identite.getType(), dbIdentite.getType());

 em.close();
 emf.close();
 }

 // Test for update method
 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 Calendar calendar = Calendar.getInstance();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.update(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite dbIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), dbIdentite.getNom());
 assertEquals(identite.getPrenom(), dbIdentite.getPrenom());
 assertEquals(identite.getEmail(), dbIdentite.getEmail());
 assertEquals(identite.getSex(), dbIdentite.getSex());
 assertEquals(identite.getType(), dbIdentite.getType());

 em.close();
 emf.close();
 }

 // Test for delete method
 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.delete(identite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 Identite dbIdentite = em.find(Identite.class, identite.getId());
 assertNull(dbIdentite);

 em.close();
 emf.close();
 }

 // Test for find method with all parameters null
 @Test
 public void testFindIdentitesNull() {
 List<Identite> identites = dao.find(null, null, null, null, null);
 assertNotNull(identites);
 assertEquals(0, identites.size());
 }

 // Test for find method with nom parameter
 @Test
 public void testFindIdentitesNom() {
 List<Identite> identites = dao.find("Doe", null, null, null, null);
 assertNotNull(identites);
 }

 // Test for find method with prenom parameter
 @Test
 public void testFindIdentitesPrenom() {
 List<Identite> identites = dao.find(null, "Doe", null, null, null);
 assertNotNull(identites);
 }

 // Test for find method with sex parameter
 @Test
 public void testFindIdentitesSex() {
 List<Identite> identites = dao.find(null, null, "M", null, null);
 assertNotNull(identites);
 }

 // Test for find method with email parameter
 @Test
 public void testFindIdentitesEmail() {
 List<Identite> identites = dao.find(null, null, null, "john.doe@example.com", null);
 assertNotNull(identites);
 }

 // Test for find method with type parameter
 @Test(expected = IllegalArgumentException.class)
 public void testFindIdentitesTypeNull() {
 dao.find(null, null, null, null, null);
 }

 // Test for find method with type parameter CANDIDAT
 @Test
 public void testFindCandidats() {
 List<Identite> identites = dao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
 assertNotNull(identites);
 }

 // Test for find method with type parameter FORMATEUR
 @Test
 public void testFindFormateurs() {
 List<Identite> identites = dao.find("Doe", "John", null, null, Identite.TypeIdentite.FORMATEUR);
 assertNotNull(identites);
 }
}