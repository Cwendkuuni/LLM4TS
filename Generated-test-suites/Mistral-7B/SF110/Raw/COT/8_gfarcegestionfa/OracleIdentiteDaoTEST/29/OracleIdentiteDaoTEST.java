package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runnable.MockitoRunnable;
import org.mockito.stubbing.OngoingStubbing;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 private EntityManager entityManager;

 @Before
 public void setup() {
 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 this.entityManager = emf.createEntityManager();
 this.identiteDao = new OracleIdentiteDao();
 }

 @After
 public void tearDown() {
 this.entityManager.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("Male");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 try {
 this.identiteDao.insert(identite, "testForm", new GregorianCalendar(2022, 1, 1));
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 Identite retrievedIdentite = this.entityManager.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("Male");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 Mockito.when(this.entityManager.getTransaction()).thenReturn(tx);
 Mockito.doAnswer((OngoingStubbing<Void>)invocation -> {
 invocation.callRealMethod();
 identite.setEmail("new.email@example.com");
 return null;
 }).when(this.entityManager).merge(identite);

 this.identiteDao.update(identite);

 Identite retrievedIdentite = this.entityManager.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals("new.email@example.com", retrievedIdentite.getEmail());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("Male");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 Mockito.when(this.entityManager.getTransaction()).thenReturn( tx );
 Mockito.doNothing().when(this.entityManager).remove(Mockito.any());

 this.identiteDao.delete(identite);

 assertTrue(this.entityManager.find(Identite.class, identite.getId()) == null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullParams() {
 this.identiteDao.find(null, null, null, null, null);
 }

 @Test
 public void testFindParams() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("Male1");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);

 Identite identite2 = new Identite();identite2.setId(2L);identite2.setNom("Doe2");identite2.setPrenom("John2");identite2.setEmail("john2.doe@example.com");identite2.setSex("Male2");identite2.setType(Identite.TypeIdentite.CANDIDAT);

 when(this.entityManager.createQuery(anyString())).thenReturn(Mockito.mock(TypedQuery.class));

 this.identiteDao.find(identite1.getNom(), identite1.getPrenom(), identite1.getSex(), identite1.getEmail(), identite1.getType());
 verify(this.entityManager, times(1)).createQuery(containsString("ident.nom='Doe1'") && containsString("ident.prenom='John1'") && containsString("ident.sex='Male1'") && containsString("ident.type= :CANDIDAT"));

 this.identiteDao.find(identite2.getNom(), identite2.getPrenom(), identite2.getSex(), identite2.getEmail(), identite2.getType());
 verify(this.entityManager, times(1)).createQuery(containsString("ident.nom='Doe2'") && containsString("ident.prenom='John2'") && containsString("ident.sex='Male2'") && containsString("ident.type= :CANDIDAT"));
 }

 @Test
 public void testFindCandidat() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("Male1");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("Male2");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);

 Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe3");
 identite3.setPrenom("John3");
 identite3.setEmail("john3.doe@example.com");
 identite3.setSex("Male3");
 identite3.setType(Identite.TypeIdentite.CANDIDAT);

 when(this.entityManager.createQuery(anyString())).thenReturn(Mockito.mock(TypedQuery.class));

 List<Identite> resultList = this.identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
 verify(this.entityManager, times(1)).createQuery(containsString("ident.nom='Doe'") && containsString("ident.prenom='John'") && containsString("ident.type= :CANDIDAT"));
 assertEquals(Arrays.asList(identite1, identite3), resultList);
 }

 @Test
 public void testFindRespForm() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("Male1");
 identite1.setType(Identite.TypeIdentite.FORMATEUR);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("Male2");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);

 Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe3");
 identite3.setPrenom("John3");
 identite3.setEmail("john3.doe@example.com");
 identite3.setSex("Male3");
 identite3.setType(Identite.TypeIdentite.FORMATEUR);

 when(this.entityManager.createQuery(anyString())).thenReturn(Mockito.mock(TypedQuery.class));

 List<Identite> resultList = this.identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.FORMATEUR);
 verify(this.entityManager, times(1)).createQuery(containsString("ident.nom='Doe'") && containsString("ident.prenom='John'") && containsString("ident.type= :FORMATEUR"));
 assertEquals(Arrays.asList(identite1), resultList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindEmptyStrings() {
 this.identiteDao.find(null, null, null, null, null);
 }

 @Test
 public void testFindNullType() {
 this.identiteDao.find(null, null, null, null, null);
 }

}