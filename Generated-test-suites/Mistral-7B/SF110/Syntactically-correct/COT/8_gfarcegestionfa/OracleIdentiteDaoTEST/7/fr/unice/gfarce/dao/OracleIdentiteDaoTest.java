package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao oracleIdentiteDao;

 @Before
 public void setup() {
 oracleIdentiteDao = new OracleIdentiteDao();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 Calendar calendar = new GregorianCalendar(2000, 0, 1);
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.insert(identite, "TestForm", calendar);

 PowerMockito.verifyStatic(OracleIdentiteDao.class);
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 PowerMockito.verifyStatic(EntityManager.class);
 EntityManager em = emf.createEntityManager();
 PowerMockito.verifyStatic(EntityTransaction.class);
 EntityTransaction tx = em.getTransaction();
 PowerMockito.verifyStatic(EntityManager.class);
 em.persist((Object)identite);
 PowerMockito.verifyStatic(EntityTransaction.class);
 tx.commit();
 PowerMockito.verifyStatic(EntityManager.class);
 em.close();
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testInsertException() {
 Identite identite = new Identite();
 Calendar calendar = new GregorianCalendar(2000, 0, 1);
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 PowerMockito.mockStatic(EntityManagerFactory.class);
 PowerMockito.whenNew(EntityManagerFactory.class).withArguments("Identite").thenThrow(new Exception("Test Exception"));
 oracleIdentiteDao.insert(identite, "TestForm", calendar);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 Calendar calendar = new GregorianCalendar(2000, 0, 1);
 identite.setId(1L);
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setEmail("test@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(1L);
 identiteToUpdate.setEmail("newEmail@example.com");
 oracleIdentiteDao.update(identiteToUpdate);

 PowerMockito.verifyStatic(OracleIdentiteDao.class);
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 PowerMockito.verifyStatic(EntityManager.class);
 EntityManager em = emf.createEntityManager();
 PowerMockito.verifyStatic(EntityTransaction.class);
 EntityTransaction tx = em.getTransaction();
 PowerMockito.verifyStatic(EntityManager.class);
 Identite ident = (Identite)em.find((Class)Identite.class, (Object)1L);
 PowerMockito.verifyStatic(Identite.class);
 ident.setEmail("newEmail@example.com");
 PowerMockito.verifyStatic(EntityManager.class);
 em.merge((Object)identiteToUpdate);
 PowerMockito.verifyStatic(EntityTransaction.class);
 tx.commit();
 PowerMockito.verifyStatic(EntityManager.class);
 em.close();
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testUpdateException() {
 Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(1L);
 identiteToUpdate.setEmail("newEmail@example.com");
 Identite identite = new Identite();
 PowerMockito.mockStatic(EntityManagerFactory.class);
 PowerMockito.whenNew(EntityManagerFactory.class).withArguments("Identite").thenThrow(new Exception("Test Exception"));
 oracleIdentiteDao.update(identiteToUpdate);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 oracleIdentiteDao.delete(identite);

 PowerMockito.verifyStatic(OracleIdentiteDao.class);
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 PowerMockito.verifyStatic(EntityManager.class);
 EntityManager em = emf.createEntityManager();
 PowerMockito.verifyStatic(EntityTransaction.class);
 EntityTransaction tx = em.getTransaction();
 PowerMockito.verifyStatic(EntityManager.class);
 Identite ident = (Identite)em.find((Class)Identite.class, (Object)1L);
 PowerMockito.verifyStatic(EntityManager.class);
 em.remove((Object)ident);
 PowerMockito.verifyStatic(EntityTransaction.class);
 tx.commit();
 PowerMockito.verifyStatic(EntityManager.class);
 em.close();
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 emf.close();
 }

 @Test(expected = Exception.class)
 public void testDeleteException() {
 Identite identite = new Identite();
 identite.setId(1L);
 PowerMockito.mockStatic(EntityManagerFactory.class);
 PowerMockito.whenNew(EntityManagerFactory.class).withArguments("Identite").thenThrow(new Exception("Test Exception"));
 oracleIdentiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> identiteList = new ArrayList<>();
 identiteList.add(new Identite());
 identiteList.add(new Identite());
 PowerMockito.mockStatic(EntityManagerFactory.class);
 PowerMockito.when(EntityManagerFactory.createEntityManagerFactory("Identite")).thenReturn(Mockito.spy(EntityManagerFactory.class));
 PowerMockito.when(EntityManagerFactory.createEntityManager()).thenReturn(Mockito.spy(EntityManager.class));
 PowerMockito.when(EntityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.spy(javax.persistence.Query.class));
 PowerMockito.when(javax.persistence.Query.getResultList()).thenReturn(identiteList);
 List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 PowerMockito.verifyStatic(EntityManager.class);
 EntityManager em = emf.createEntityManager();
 PowerMockito.verifyStatic(javax.persistence.Query.class);
 javax.persistence.Query query = em.createQuery("select ident from Identite as ident where ident.nom='Test' and ident.prenom='Test' and ident.sex='M' and (ident.type='CANDIDAT')");
 PowerMockito.verifyStatic(EntityTransaction.class);
 EntityTransaction tx = em.getTransaction();
 PowerMockito.verifyStatic(javax.persistence.Query.class);
 List<Identite> resultList = query.getResultList();
 PowerMockito.verifyStatic(EntityManager.class);
 em.close();
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 emf.close();
 Assert.assertEquals(2, result.size());
 }

 @Test
 public void testFindEmpty() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 PowerMockito.mockStatic(EntityManagerFactory.class);
 PowerMockito.mockStatic(EntityManager.class);
 PowerMockito.when(EntityManagerFactory.createEntityManagerFactory("Identite")).thenReturn(Mockito.spy(EntityManagerFactory.class));
 PowerMockito.when(EntityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.spy(javax.persistence.Query.class));
 PowerMockito.when(javax.persistence.Query.getResultList()).thenReturn(new ArrayList<>());
 List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 PowerMockito.verifyStatic(EntityManager.class);
 EntityManager em = emf.createEntityManager();
 PowerMockito.verifyStatic(javax.persistence.Query.class);
 javax.persistence.Query query = em.createQuery("select ident from Identite as ident where ident.nom='Test' and ident.prenom='Test' and ident.sex='M' and (ident.type='CANDIDAT')");
 PowerMockito.verifyStatic(EntityTransaction.class);
 EntityTransaction tx = em.getTransaction();
 PowerMockito.verifyStatic(javax.persistence.Query.class);
 List<Identite> resultList = query.getResultList();
 PowerMockito.verifyStatic(EntityManager.class);
 em.close();
 PowerMockito.verifyStatic(EntityManagerFactory.class);
 emf.close();
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@example.com";
 Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
 Assert.assertNull(result);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@example.com";
 RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
 Assert.assertNull(result);
 }
}