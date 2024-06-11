package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 private EntityManagerFactory emf;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 }

 @After
 public void tearDown() {
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 11, 20));

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite, foundIdentite);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManager em = emf.createEntityManager();
 Identite existingIdentite = em.find(Identite.class, identite.getId());
 existingIdentite.setEmail("john.doe@newemail.com");

 identiteDao.update(identite);

 Identite updatedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("john.doe@newemail.com", updatedIdentite.getEmail());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityManager em = emf.createEntityManager();
 Identite existingIdentite = em.find(Identite.class, identite.getId());
 identiteDao.delete(identite);
 assertNull(em.find(Identite.class, identite.getId()));
 }

 @Test
 public void testFindNull() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindByNom() {
 Identite identite = new Identite();
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 11, 20));

 List<Identite> identites = identiteDao.find("John", null, null, null, null);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindByPrenom() {
 Identite identite = new Identite();
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 11, 20));

 List<Identite> identites = identiteDao.find(null, "Doe", null, null, null);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindBySex() {
 Identite identite = new Identite();
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 11, 20));

 List<Identite> identites = identiteDao.find(null, null, "F", null, null);
 assertEquals(0, identites.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullType() {
 identiteDao.find(null, null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithInvalidType() {
 Identite identite = new Identite();
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType("InvalidType");
 identiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 11, 20));

 try {
 identiteDao.find(null, null, null, "InvalidType", null);
 } catch (IllegalArgumentException e) {
 return;
 }
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testFindCandidats() {
 Identite identite1 = new Identite();
 identite1.setNom("John1");
 identite1.setPrenom("Doe1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "nomForm", new GregorianCalendar(2022, 11, 20));

 Identite identite2 = new Identite();
 identite2.setNom("John2");
 identite2.setPrenom("Doe2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite2, "nomForm", new GregorianCalendar(2022, 11, 21));

 List<Candidat> candidats = identiteDao.findCandidat("John", null, null, null);
 assertEquals(2, candidats.length);
 }

 @Test
 public void testFindResponsables() {
 Identite identite1 = new Identite();
 identite1.setNom("John1");
 identite1.setPrenom("Doe1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite1, "nomForm", new GregorianCalendar(2022, 11, 20));

 Identite identite2 = new Identite();
 identite2.setNom("John2");
 identite2.setPrenom("Doe2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, "nomForm", new GregorianCalendar(2022, 11, 21));

 List<RespFormation> responsables = identiteDao.findRespForm("John", null, null, null);
 assertEquals(2, responsables.length);
 }
}