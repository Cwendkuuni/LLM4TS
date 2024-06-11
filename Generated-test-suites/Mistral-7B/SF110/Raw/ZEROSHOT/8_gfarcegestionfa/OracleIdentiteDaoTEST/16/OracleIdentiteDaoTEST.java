package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

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
 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.persist((Object)identite)).thenReturn(identite);

 oracleIdentiteDao.insert(identite, "NomForm", new Date());

 Mockito.verify(entityManager).persist((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 oracleIdentiteDao.insert(null, "NomForm", new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 oracleIdentiteDao.insert(identite, null, new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDateLimite() {
 oracleIdentiteDao.insert(identite, "NomForm", null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Identite dbIdentite = Mockito.mock(Identite.class);
 Mockito.when(dbIdentite.getId()).thenReturn(identite.getId());
 Mockito.when(dbIdentite.getEmail()).thenReturn("john.doe@old.com");
 Mockito.when(entityManager.find((Class)identite.getClass(), (Object)identite.getId())).thenReturn(dbIdentite);
 Mockito.when(entityManager.merge((Object)identite)).thenReturn(identite);

 oracleIdentiteDao.update(identite);

 Mockito.verify(entityManager).merge((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 oracleIdentiteDao.update(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullNom() {
 List<Identite> result = oracleIdentiteDao.find(null, null, null, null, null);
 assertTrue(result.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullPrenom() {
 List<Identite> result = oracleIdentiteDao.find("Nom", null, null, null, null);
 assertTrue(result.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullSex() {
 List<Identite> result = oracleIdentiteDao.find("Nom", "Prenom", null, null, null);
 assertTrue(result.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullEmail() {
 List<Identite> result = oracleIdentiteDao.find("Nom", "Prenom", "Sex", null, null);
 assertTrue(result.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullType() {
 List<Identite> result = oracleIdentiteDao.find("Nom", "Prenom", "Sex", null, null);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFind() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.createQuery("select ident from Identite as ident where ident.nom=:nom and ident.prenom=:prenom and ident.sex=:sex and ident.type=:type"))
 .thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(typedQuery.getResultList()).thenReturn(Arrays.asList(identite));

 List<Identite> result = oracleIdentiteDao.find("Doe", "John", "M", null, null);

 assertEquals(1, result.size());
 assertEquals(identite, result.get(0));
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.createQuery("select ident from Identite as ident where ident.nom=:nom and ident.prenom=:prenom and ident.sex=:sex and ident.type=:type"))
 .thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(typedQuery.getResultList()).thenReturn(Arrays.asList(identite));

 Candidat[] result = oracleIdentiteDao.findCandidat("Doe", "John", "M", null);

 assertEquals(1, result.length);
 assertEquals(identite, result[0]);
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);

 Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.createQuery("select ident from Identite as ident where ident.nom=:nom and ident.prenom=:prenom and ident.sex=:sex and ident.type=:type"))
 .thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(typedQuery.getResultList()).thenReturn(Arrays.asList(identite));

 RespFormation[] result = oracleIdentiteDao.findRespForm("Doe", "John", "M", null);

 assertEquals(1, result.length);
 assertEquals(identite, result[0]);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);

 Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Identite dbIdentite = Mockito.mock(Identite.class);
 Mockito.when(entityManager.find((Class)identite.getClass(), (Object)identite.getId())).thenReturn(dbIdentite);
 Mockito.when(entityManager.remove((Object)dbIdentite)).thenReturn(dbIdentite);

 oracleIdentiteDao.delete(identite);

 Mockito.verify(entityManager).remove((Object)dbIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 oracleIdentiteDao.delete(null);
 }
}