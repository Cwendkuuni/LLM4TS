package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao.IdentiteTestData;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Sex;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManager entityManager;
 private PersistenceUnitManager persistenceUnitManager;

 @PersistenceContext
 public EntityManager getEntityManager() {
 return entityManager;
 }

 @Before
 public void setup() {
 persistenceUnitManager = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
 dao = OracleIdentiteDao.getDAO();

 // initialize test data
 entityManager.getTransaction().begin();
 dao.insert(IdentiteTestData.cand1, "John", new Date());
 dao.insert(IdentiteTestData.cand2, "Jane", new Date());
 dao.insert(IdentiteTestData.form1, "Pierre", new Date());
 dao.insert(IdentiteTestData.form2, "Paul", new Date());
 entityManager.getTransaction().commit();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite("Doe", "John", Sex.MALE, Identite.TypeIdentite.CANDIDAT);
 dao.insert(identite, "Doe", new Date());

 EntityManager em = getEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertSame(identite, foundIdentite);
 }

 @Test
 public void testUpdate() {
 Identite identite = entityManager.find(Identite.class, IdentiteTestData.cand1.getId());
 String newEmail = "new.email@example.com";
 identite.setEmail(newEmail);
 dao.update(identite);

 EntityManager em2 = getEntityManager();
 Identite foundIdentite = em2.find(Identite.class, identite.getId());
 assertEquals(newEmail, foundIdentite.getEmail());
 }

 @Test
 public void testDelete() {
 Identite identite = entityManager.find(Identite.class, IdentiteTestData.form1.getId());
 dao.delete(identite);

 EntityManager em3 = getEntityManager();
 Identite foundIdentite = em3.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 }

 @Test
 public void testFindAll() {
 List<Identite> result = dao.find(null, null, null, null, null);
 List<Identite> expected = new ArrayList<>();
 expected.addAll(List.of(IdentiteTestData.cand1, IdentiteTestData.cand2, IdentiteTestData.form1, IdentiteTestData.form2));
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), result.get(i));
 }
 }

 @Test
 public void testFindCandidat() {
 List<Candidat> result = dao.findCandidat("Doe", null, null, null);
 List<Candidat> expected = new ArrayList<>();
 expected.addAll(List.of(IdentiteTestData.cand1, IdentiteTestData.cand2));
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), result.get(i));
 }
 }

 @Test
 public void testFindRespForm() {
 List<RespFormation> result = dao.findRespForm(null, null, null, null);
 List<RespFormation> expected = new ArrayList<>();
 expected.addAll(List.of(IdentiteTestData.form1, IdentiteTestData.form2));
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), result.get(i));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullType() {
 dao.find(null, null, null, null, null);
 }
}