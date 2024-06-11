package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.TypeIdentite;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setup() {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new java.util.GregorianCalendar(2022, 1, 1));
 TypedQuery<Identite> query = em.createQuery("SELECT i FROM Identite i WHERE i.email = :email AND i.type = :type", Identite.class);
 Identite result = query.setParameter("email", "john.doe@example.com").setParameter("type", TypeIdentite.CANDIDAT).getSingleResult();
 Assert.assertEquals(identite, result);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.update(identite);
 Identite dbIdentite = em.find(Identite.class, 1L);
 Assert.assertEquals(identite, dbIdentite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new java.util.GregorianCalendar(2022, 1, 1));
 identiteDao.delete(identite);
 TypedQuery<Identite> query = em.createQuery("SELECT i FROM Identite i WHERE i.id = :id AND i.type = :type", Identite.class);
 Assert.assertNull(query.setParameter("id", 1L).setParameter("type", TypeIdentite.CANDIDAT).getSingleResult());
 }

 @Test(expected = PersistenceException.class)
 public void testInsertException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", null);
 }

 @Test(expected = PersistenceException.class)
 public void testUpdateException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 identiteDao.update(identite);
 tx.rollback();
 }

 @Test(expected = PersistenceException.class)
 public void testDeleteException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 identiteDao.delete(identite);
 tx.rollback();
 }

 @Test
 public void testFind() {
 List<Identite> result = identiteDao.find(null, null, null, "john.doe@example.com", null);
 Assert.assertEquals(1, result.size());
 Identite identite = result.get(0);
 Assert.assertEquals("Doe", identite.getNom());
 Assert.assertEquals("John", identite.getPrenom());
 Assert.assertEquals("john.doe@example.com", identite.getEmail());
 }

 @Test
 public void testFindCandidat() {
 Candidat[] result = identiteDao.findCandidat("Doe", "John", "M", "john.doe@example.com");
 Assert.assertNotNull(result);
 Assert.assertEquals(1, result.length);
 Candidat identite = result[0];
 Assert.assertEquals("Doe", identite.getNom());
 Assert.assertEquals("John", identite.getPrenom());
 Assert.assertEquals("john.doe@example.com", identite.getEmail());
 }

 @Test
 public void testFindRespForm() {
 RespFormation[] result = identiteDao.findRespForm("Doe", "John", "M", "john.doe@example.com");
 Assert.assertNull(result);
 }
}