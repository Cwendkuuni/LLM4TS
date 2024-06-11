package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;
import javax.persistence.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManager;
 @Mock
 private EntityManagerFactory entityManagerFactory;
 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 String nomForm = "testNomForm";
 Calendar dateLimite = new GregorianCalendar(2022, 11, 11);
 oracleIdentiteDao.insert(identite, nomForm, dateLimite);
 Mockito.verify(entityManager).persist(identite);
 }

 @Test(expected = Exception.class)
 public void testInsertException() {
 oracleIdentiteDao.insert(null, null, null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("testEmail");
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 oracleIdentiteDao.update(identite);
 Mockito.verify(entityManager).merge(identite);
 }

 @Test(expected = Exception.class)
 public void testUpdateException() {
 Identite identite = new Identite();
 identite.setId(null);
 oracleIdentiteDao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 oracleIdentiteDao.delete(identite);
 Mockito.verify(entityManager).remove(identite);
 }

 @Test(expected = Exception.class)
 public void testDeleteException() {
 Identite identite = new Identite();
 identite.setId(null);
 oracleIdentiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("testNom");
 identite.setPrenom("testPrenom");
 identite.setSex("testSex");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 List<Identite> list = Arrays.asList(identite);
 when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 when(entityManager.createQuery(Mockito.anyString()).getResultList()).thenReturn(list);

 String nom = "testNom";
 String prenom = "testPrenom";
 String sex = "testSex";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 Identite[] result = oracleIdentiteDao.find(nom, prenom, sex, null, type);
 assertEquals(1, result.length);
 }

 @Test
 public void testFindEmpty() {
 when(entityManager.createQuery(Mockito.anyString()).getResultList()).thenReturn(Collections.emptyList());
 String nom = "testNom";
 String prenom = "testPrenom";
 String sex = "testSex";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 Identite[] result = oracleIdentiteDao.find(nom, prenom, sex, null, type);
 assertNull(result);
 }

 @Test
 public void testFindNull() {
 when(entityManager.createQuery(Mockito.anyString()).getResultList()).thenReturn(null);
 String nom = "testNom";
 String prenom = "testPrenom";
 String sex = "testSex";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 Identite[] result = oracleIdentiteDao.find(nom, prenom, sex, null, type);
 assertNull(result);
 }

 @Test
 public void testFindCandidat() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("testNom1");
 identite1.setPrenom("testPrenom1");
 identite1.setSex("testSex1");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("testNom2");
 identite2.setPrenom("testPrenom2");
 identite2.setSex("testSex2");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 List<Identite> list = Arrays.asList(identite1, identite2);
 when(entityManager.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.any())).thenAnswer(i -> i.callRealMethod());
 when(entityManager.createQuery(Mockito.anyString()).getResultList()).thenReturn(list);

 Candidat[] result = oracleIdentiteDao.findCandidat(null, null, null, null);
 assertNull(result);

 Candidat[] result1 = oracleIdentiteDao.findCandidat("testNom1", null, null, null);
 assertNotNull(result1);
 assertEquals(1, result1.length);
 }

 @Test
 public void testFindRespForm() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("testNom1");
 identite1.setPrenom("testPrenom1");
 identite1.setSex("testSex1");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("testNom2");
 identite2.setPrenom("testPrenom2");
 identite2.setSex("testSex2");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 List<Identite> list = Arrays.asList(identite1, identite2);
 when(entityManager.createQuery(Mockito.anyString()).setParameter(Mockito.anyString(), Mockito.any())).thenAnswer(i -> i.callRealMethod());
 when(entityManager.createQuery(Mockito.anyString()).getResultList()).thenReturn(list);

 RespFormation[] result = oracleIdentiteDao.findRespForm(null, null, null, null);
 assertNull(result);

 RespFormation[] result1 = oracleIdentiteDao.findRespForm("testNom1", null, null, null);
 assertNotNull(result1);
 assertEquals(1, result1.length);
 }

 @Test
 public void testPersistenceUnitName() {
 assertEquals("Identite", oracleIdentiteDao.getPersistenceUnitName());
 }
}