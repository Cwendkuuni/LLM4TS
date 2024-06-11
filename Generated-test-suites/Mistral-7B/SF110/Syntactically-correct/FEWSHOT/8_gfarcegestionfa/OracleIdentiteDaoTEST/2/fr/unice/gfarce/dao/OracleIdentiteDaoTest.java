package fr.unice.gfarce.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private EntityTransaction tx;
 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 tx = em.getTransaction();
 dao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 if (tx != null) {
 tx.rollback();
 }
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
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "Doe", new Date());
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertSame(identite, foundIdentite);
 }

 @Test
 public void testInsertNullIdentite() {
 dao.insert(null, "Doe", new Date());
 assertNull(em.find(Identite.class, null));
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite dbIdentite = new Identite();
 dbIdentite.setId(1L);
 dbIdentite.setEmail("john.doe@old.com");
 dbIdentite.setNom("Smith");
 dbIdentite.setPrenom("John");
 dbIdentite.setSex('M');
 dbIdentite.setType(Identite.TypeIdentite.CANDIDAT);

 em.persist(identite);
 em.persist(dbIdentite);
 dao.update(identite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertSame(identite, foundIdentite);
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 }

 @Test
 public void testUpdateNullIdentite() {
 Identite identite = new Identite();
 dao.update(identite);
 assertNull(em.find(Identite.class, identite.getId()));
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 em.persist(identite);
 dao.delete(identite);
 assertNull(em.find(Identite.class, identite.getId()));
 }

 @Test
 public void testDeleteNullIdentite() {
 Identite identite = new Identite();
 dao.delete(identite);
 assertNull(em.find(Identite.class, identite.getId()));
 }

 @Test
 public void testFind() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite);

 List<Identite> result = dao.find(null, null, null, null, null);
 assertEquals(1, result.size());
 assertSame(identite, result.get(0));

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Smith");
 identite2.setPrenom("John");
 identite2.setSex('M');
 identite2.setEmail("john.smith@example.com");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite2);

 result = dao.find("Doe", null, null, null, null);
 assertEquals(1, result.size());
 assertSame(identite, result.get(0));

 result = dao.find("Doe", null, 'F', null, null);
 assertEquals(0, result.size());

 result = dao.find(null, "John", null, null, null);
 assertEquals(2, result.size());

 result = dao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(1, result.size());
 assertSame(identite, result.get(0));

 result = dao.find(null, null, null, "john.smith@example.com", null);
 assertEquals(1, result.size());
 assertNotSame(identite, result.get(0));

 result = dao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
 assertEquals(0, result.size());

 result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 em.persist(identite);

 Candidat[] result = dao.findCandidat("Doe", null, null);
 assertEquals(1, result.length);
 assertSame(identite, result[0]);

 result = dao.findCandidat(null, null, null);
 assertEquals(0, result.length);
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex('M');
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 em.persist(identite);

 RespFormation[] result = dao.findRespForm("Doe", null, null);
 assertEquals(1, result.length);
 assertSame(identite, result[0]);

 result = dao.findRespForm(null, null, null);
 assertEquals(0, result.length);
 }
}