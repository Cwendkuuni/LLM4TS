package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManager;
 @Mock
 private EntityManagerFactory entityManagerFactory;
 @Mock
 private EntityTransaction entityTransaction;
 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setEmail("test@email.com");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 dateLimite.setTime(new Date());

 oracleIdentiteDao.insert(identite, "testForm", dateLimite);

 Mockito.verify(entityManagerFactory).createEntityManager();
 Mockito.verify(entityManager).getTransaction();
 Mockito.verify(entityManager).persist((Object)identite);
 Mockito.verify(entityManager).commit();
 Mockito.verify(entityManager).close();
 Mockito.verify(entityManagerFactory).close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 oracleIdentiteDao.insert(null, "testForm", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullForm() {
 Identite identite = new Identite();
 identite.setEmail("test@email.com");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 dateLimite.setTime(new Date());

 oracleIdentiteDao.insert(identite, null, dateLimite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDate() {
 Identite identite = new Identite();
 identite.setEmail("test@email.com");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 oracleIdentiteDao.insert(identite, "testForm", null);
 }

 @Test
 public void testUpdate() {
 Identite identite1 = new Identite();
 identite1.setEmail("testEmail1@email.com");
 identite1.setNom("testNom1");
 identite1.setPrenom("testPrenom1");
 identite1.setSex("testSex1");
 identite1.setId(1L);
 Identite identite2 = new Identite();
 identite2.setEmail("testEmail2@email.com");
 identite2.setNom("testNom2");
 identite2.setPrenom("testPrenom2");
 identite2.setSex("testSex2");
 identite2.setId(1L);

 oracleIdentiteDao.update(identite2);

 Mockito.verify(entityManagerFactory).createEntityManager();
 Mockito.verify(entityManager).getTransaction();
 Mockito.verify(entityManager).find((Class)Identite.class, (Object)1L);
 Mockito.verify(entityManager).merge((Object)identite2);
 Mockito.verify(entityManager).commit();
 Mockito.verify(entityManager).close();
 Mockito.verify(entityManagerFactory).close();
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setEmail("testEmail@email.com");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setId(1L);

 oracleIdentiteDao.delete(identite);

 Mockito.verify(entityManagerFactory).createEntityManager();
 Mockito.verify(entityManager).getTransaction();
 Mockito.verify(entityManager).find((Class)Identite.class, (Object)1L);
 Mockito.verify(entityManager).remove((Object)identite);
 Mockito.verify(entityManager).commit();
 Mockito.verify(entityManager).close();
 Mockito.verify(entityManagerFactory).close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 oracleIdentiteDao.delete(null);
 }

 @Test
 public void testFindAll() {
 Identite identite1 = new Identite();
 identite1.setEmail("testEmail1@email.com");
 identite1.setNom("testNom1");
 identite1.setPrenom("testPrenom1");
 identite1.setSex("testSex1");
 identite1.setId(1L);
 Identite identite2 = new Identite();
 identite2.setEmail("testEmail2@email.com");
 identite2.setNom("testNom2");
 identite2.setPrenom("testPrenom2");
 identite2.setSex("testSex2");
 identite2.setId(2L);
 List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);

 Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(typedQuery.getResultList()).thenReturn(identites);

 List<Identite> result = oracleIdentiteDao.find(null, null, null, null, null);

 Mockito.verify(entityManager).createQuery(Mockito.eq("select ident from Identite as ident where ident.nom is null and ident.prenom is null and ident.sex is null and ident.type is null"));
 Mockito.verify(typedQuery).getResultList();
 Mockito.assertEquals(2, result.size());
 }

 @Test
 public void testFindByNom() {
 Identite identite = new Identite();
 identite.setEmail("testEmail@email.com");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setId(1L);

 Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(typedQuery.setParameter("CANDIDAT", Mockito.any())).thenReturn(typedQuery);
 Mockito.when(typedQuery.getResultList()).thenReturn(new ArrayList<>());

 List<Identite> result = oracleIdentiteDao.find("testNom", null, null, null, Identite.TypeIdentite.CANDIDAT);

 Mockito.verify(entityManager).createQuery(Mockito.eq("select ident from Identite as ident where ident.nom='testNom' and ident.type= :CANDIDAT"));
 Mockito.verify(typedQuery).setParameter("CANDIDAT", Identite.TypeIdentite.CANDIDAT);
 Mockito.verify(typedQuery).getResultList().isEmpty();
 }

 @Test
 public void testFindByEmail() {
 Identite identite = new Identite();
 identite.setEmail("testEmail@email.com");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setId(1L);

 Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(typedQuery.getResultList()).thenReturn(new ArrayList<>());

 List<Identite> result = oracleIdentiteDao.find(null, null, null, "testEmail@email.com", null);

 Mockito.verify(entityManager).createQuery(Mockito.eq("select ident from Identite as ident where ident.email='testEmail@email.com'"));
 Mockito.verify(typedQuery).getResultList().isEmpty();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByNullParam() {
 oracleIdentiteDao.find(null, null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByEmptyListParam() {
 oracleIdentiteDao.find(new String[0], new String[0], null, null, null);
 }
}