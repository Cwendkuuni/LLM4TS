package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManagerFactory emf;
 private EntityManager em;
 private EntityTransaction tx;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 tx = em.getTransaction();
 dao = new OracleIdentiteDaoImpl();
 }

 @After
 public void teardown() {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "testForm", new Calendar.Builder().setYear(2020).build());

 final Identite found = em.find(Identite.class, identite.getId());
 assertNotNull(found);
 assertEquals(identite.getNom(), found.getNom());
 assertEquals(identite.getPrenom(), found.getPrenom());
 assertEquals(identite.getSex(), found.getSex());
 assertEquals(identite.getEmail(), found.getEmail());
 assertEquals(identite.getType(), found.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertWithNullEntity() {
 dao.insert(null, "testForm", new Calendar.Builder().setYear(2020).build());
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testUpdate() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 em.persist(identite);
 tx.commit();

 final Identite existing = em.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), existing.getNom());
 assertEquals(identite.getPrenom(), existing.getPrenom());
 assertEquals(identite.getSex(), existing.getSex());
 assertEquals(identite.getEmail(), existing.getEmail());
 assertEquals(identite.getType(), existing.getType());

 identite.setEmail("new.email@example.com");
 dao.update(identite);
 tx.commit();

 final Identite updated = em.find(Identite.class, identite.getId());
 assertNotNull(updated);
 assertEquals(identite.getNom(), updated.getNom());
 assertEquals(identite.getPrenom(), updated.getPrenom());
 assertEquals(identite.getSex(), updated.getSex());
 assertEquals(identite.getType(), updated.getType());
 assertEquals(updated.getEmail(), "new.email@example.com");
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateWithNullEntity() {
 dao.update(null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = EntityNotFoundException.class)
 public void testDeleteWithNullEntity() {
 dao.delete(null);
 fail("Should throw an EntityNotFoundException");
 }

 @Test
 public void testDelete() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 em.persist(identite);
 tx.commit();
 final Identite existing = em.find(Identite.class, identite.getId());
 assertNotNull(existing);

 dao.delete(identite);
 tx.commit();
 final Identite deleted = em.find(Identite.class, identite.getId());
 assertNull(deleted);
 }

 @Test(expected = EntityNotFoundException.class)
 public void testFindEmpty() {
 final List<Identite> result = dao.find(null, null, null, null, null);
 assertEmpty(result);
 }

 @Test
 public void testFindCandidat() {
 final Identite identite1 = new Identite();
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe1@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite1);
 tx.commit();

 final Identite identite2 = new Identite();
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe2@example.com");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite2);
 tx.commit();

 final List<Identite> result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindRespForm() {
 final Identite identite1 = new Identite();
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe1@example.com");
 identite1.setType(Identite.TypeIdentite.FORMATEUR);
 em.persist(identite1);
 tx.commit();

 final Identite identite2 = new Identite();
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe2@example.com");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 em.persist(identite2);
 tx.commit();

 final List<Identite> result = dao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindWithFilter() {
 final Identite identite1 = new Identite();
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe1@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite1);
 tx.commit();

 final Identite identite2 = new Identite();
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe2@example.com");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 em.persist(identite2);
 tx.commit();

 final List<Identite> result = dao.find("Doe", "John", "M", null, null);
 assertEquals(1, result.size());
 final Identite resultIdentite = result.get(0);
 assertEquals(identite1.getNom(), resultIdentite.getNom());
 assertEquals(identite1.getPrenom(), resultIdentite.getPrenom());
 assertEquals(identite1.getSex(), resultIdentite.getSex());
 assertEquals(identite1.getType(), resultIdentite.getType());
 }

 @Test
 public void testCreateQueryWithParameters() {
 final List<Identite> result = dao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(2, result.size());
 }

 @Test
 public void testCreateQueryWithAllParameters() {
 final List<Identite> result = dao.find("Doe", "John", "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, result.size());
 final Identite resultIdentite = result.get(0);
 assertEquals("Doe", resultIdentite.getNom());
 assertEquals("John", resultIdentite.getPrenom());
 assertEquals("M", resultIdentite.getSex());
 assertEquals("john.doe@example.com", resultIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, resultIdentite.getType());
 }
}