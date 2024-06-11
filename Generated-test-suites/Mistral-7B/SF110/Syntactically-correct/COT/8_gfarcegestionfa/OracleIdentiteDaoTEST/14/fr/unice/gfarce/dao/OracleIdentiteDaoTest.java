package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 dao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, "nomForm", dateLimite);

 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 assertSame(identite, foundIdentite);
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 OracleIdentiteDao dao = OracleIdentiteDao.getDAO();

 try {
 dao.insert(identite, "nomForm", dateLimite);
 }
 catch (Exception e) {
 throw e;
 }
 finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setNom("John Doe");
 existingIdentite.setPrenom("Doe");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setDateLimite(new Date());
 em.persist(existingIdentite);
 em.flush();

 dao.update(identite);

 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 assertSame(identite, foundIdentite);
 }

 @Test(expected = Exception.class)
 public void testUpdateWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());

 Identify existingIdentite = new Identite();
 existingIdentite.setId(2L);
 existingIdentite.setNom("John Doe");
 existingIdentite.setPrenom("Doe");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setDateLimite(new Date());
 em.persist(existingIdentite);
 em.flush();

 try {
 dao.update(identite);
 }
 catch (Exception e) {
 throw e;
 }
 finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());

 em.persist(identite);
 em.flush();

 Identite foundIdentite = (Identite) em.find(Identite.class, identite.getId());
 assertSame(identite, foundIdentite);

 dao.delete(identite);

 Identite deletedIdentite = (Identite) em.find(Identite.class, identite.getId());
 assertNull(deletedIdentite);
 }

 @Test(expected = Exception.class)
 public void testDeleteWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());

 try {
 dao.delete(identite);
 }
 catch (Exception e) {
 throw e;
 }
 finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testFind() {
 List<Identite> identites = new ArrayList<>();
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setEmail("john.doe@example.com");
 identite1.setDateLimite(new Date());
 identites.add(identite1);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identite2.setEmail("jane.smith@example.com");
 identite2.setDateLimite(new Date());
 identites.add(identite2);
 identites.add(new Identite());

 em.getTransaction().begin();
 for (Identite identite : identites) {
 em.persist(identite);
 }
 em.getTransaction().commit();
 em.flush();

 List<Identite> result = dao.find(null, null, null, null, null);
 assertEquals(3, result.size());

 result = dao.find("Doe", null, null, null, null);
 assertEquals(1, result.size());

 result = dao.find(null, "Jane", null, null, null);
 assertEquals(1, result.size());

 result = dao.find(null, null, "M", null, null);
 assertEquals(2, result.size());

 result = dao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(1, result.size());

 result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 assertEquals(2, result.size());

 result = dao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());
 em.persist(identite);
 em.flush();

 Candidat[] result = dao.findCandidat("Doe", null, null, "john.doe@example.com");
 assertEquals(1, result.length);
 assertSame(identite, result[0]);

 result = dao.findCandidat(null, null, null, null);
 assertEquals(0, result.length);
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identite.setEmail("john.doe@example.com");
 identite.setDateLimite(new Date());
 em.persist(identite);
 em.flush();

 RespFormation[] result = dao.findRespForm("Doe", null, null, "john.doe@example.com");
 assertEquals(1, result.length);
 assertSame(identite, result[0].getIdentite());

 result = dao.findRespForm(null, null, null, null);
 assertEquals(0, result.length);
 }
}