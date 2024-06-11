package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManager em;

 @Before
 public void setUp() {
 dao = OracleIdentiteDao.getDAO();
 em = dao.em;
 }

 @After
 public void tearDown() {
 cleanUp();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1980);
 calendar.set(Calendar.MONTH, 10);
 calendar.set(Calendar.DAY_OF_MONTH, 15);
 identite.setDateNaiss(calendar.getTime());
 identite.setEmail("john.doe@example.com");

 dao.insert(identite, "Doe", calendar);

 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(fetchedIdentite);
 assertEquals("Doe", fetchedIdentite.getNom());
 assertEquals("John", fetchedIdentite.getPrenom());
 assertEquals('M', fetchedIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, fetchedIdentite.getType());
 assertEquals(calendar.getTime(), fetchedIdentite.getDateNaiss());
 assertEquals("john.doe@example.com", fetchedIdentite.getEmail());
 }

 @Test(expected = PersistenceException.class)
 public void testInsertDuplicate() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1980);
 calendar.set(Calendar.MONTH, 10);
 calendar.set(Calendar.DAY_OF_MONTH, 15);
 identite.setDateNaiss(calendar.getTime());
 identite.setEmail("john.doe@example.com");

 dao.insert(identite, "Doe", calendar);
 dao.insert(identite, "Doe", calendar);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1980);
 calendar.set(Calendar.MONTH, 10);
 calendar.set(Calendar.DAY_OF_MONTH, 15);
 identite.setDateNaiss(calendar.getTime());
 identite.setEmail("john.doe@example.com");

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setEmail("john.doe@old.com");
 existingIdentite.setNom("Doe");
 existingIdentite.setPrenom("John");
 existingIdentite.setSex('M');
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 existingIdentite.setDateNaiss(calendar.getTime());

 em.getTransaction().begin();
 em.merge(identite);
 em.getTransaction().commit();

 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(fetchedIdentite);
 assertEquals("john.doe@old.com", fetchedIdentite.getEmail());

 dao.update(identite);

 fetchedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("john.doe@example.com", fetchedIdentite.getEmail());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1980);
 calendar.set(Calendar.MONTH, 10);
 calendar.set(Calendar.DAY_OF_MONTH, 15);
 identite.setDateNaiss(calendar.getTime());
 identite.setEmail("john.doe@example.com");

 em.getTransaction().begin();
 em.persist(identite);
 em.getTransaction().commit();

 Identite fetchedIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(fetchedIdentite);

 dao.delete(identite);

 fetchedIdentite = em.find(Identite.class, identite.getId());
 assertNull(fetchedIdentite);
 }

 @Test
 public void testFindEmpty() {
 List<Identite> identites = dao.find(null, null, null, null, null);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindByNomPrenomSexEmailType() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setEmail("john.doe@example.com");
 identite1.setDateNaiss(new Date());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setEmail("jane.doe@example.com");
 identite2.setDateNaiss(new Date());

 em.getTransaction().begin();
 em.persist(identite1);
 em.persist(identite2);
 em.getTransaction().commit();

 List<Identite> identites = dao.find("Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 List<Identite> expectedIdentites = Arrays.asList(identite1);
 assertEquals(expectedIdentites.size(), identites.size());
 for (int i = 0; i < identites.size(); i++) {
 Identite identite = identites.get(i);
 assertEquals(identite1.getId(), identite.getId());
 }
 }

 @Test
 public void testFindByNomPrenomSexEmailTypeFormateur() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setEmail("john.doe@example.com");
 identite1.setDateNaiss(new Date());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setEmail("jane.doe@example.com");
 identite2.setDateNaiss(new Date());

 Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe");
 identite3.setPrenom("John");
 identite3.setSex('M');
 identite3.setType(Identite.TypeIdentite.FORMATEUR);
 identite3.setEmail("john.doe@example.com");
 identite3.setDateNaiss(new Date());

 em.getTransaction().begin();
 em.persist(identite1);
 em.persist(identite2);
 em.persist(identite3);
 em.getTransaction().commit();

 List<Identite> identites = dao.find("Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.FORMATEUR);
 List<Identite> expectedIdentites = Arrays.asList(identite3);
 assertEquals(expectedIdentites.size(), identites.size());
 for (int i = 0; i < identites.size(); i++) {
 Identite identite = identites.get(i);
 assertEquals(identite3.getId(), identite.getId());
 }
 }

 @Test
 public void testFindByNomPrenomSexEmailTypeCandidat() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setEmail("john.doe@example.com");
 identite1.setDateNaiss(new Date());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe");
 identite2.setPrenom("Jane");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setEmail("jane.doe@example.com");
 identite2.setDateNaiss(new Date());

 Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe");
 identite3.setPrenom("John");
 identite3.setSex('M');
 identite3.setType(Identite.TypeIdentite.CANDIDAT);
 identite3.setEmail("john.doe@example.com");
 identite3.setDateNaiss(new Date());

 em.getTransaction().begin();
 em.persist(identite1);
 em.persist(identite2);
 em.persist(identite3);
 em.getTransaction().commit();

 List<Identite> identites = dao.find("Doe", "John", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 List<Identite> expectedIdentites = Arrays.asList(identite1, identite3);
 assertEquals(expectedIdentites.size(), identites.size());
 for (int i = 0; i < identites.size(); i++) {
 Identite identite = identites.get(i);
 assertEquals(expectedIdentites.get(i).getId(), identite.getId());
 }
 }

 @Test
 public void testFindByNomPrenomNull() {
 List<Identite> identites = dao.find(null, "John", 'M', null, Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindByNomNull() {
 List<Identite> identites = dao.find("Doe", null, 'M', null, Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindBySexNull() {
 List<Identite> identites = dao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindByEmailNull() {
 List<Identite> identites = dao.find("Doe", "John", 'M', null, Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindByTypeIdentiteNull() {
 List<Identite> identites = dao.find("Doe", "John", 'M', "john.doe@example.com", null);
 assertTrue(identites.isEmpty());
 }

 private void cleanUp() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
 EntityManager em = emf.createEntityManager();
 List<Identite> identites = em.createQuery("DELETE FROM Identite", Identite.class).getResultList();
 em.close();
 emf.close();
 }
}