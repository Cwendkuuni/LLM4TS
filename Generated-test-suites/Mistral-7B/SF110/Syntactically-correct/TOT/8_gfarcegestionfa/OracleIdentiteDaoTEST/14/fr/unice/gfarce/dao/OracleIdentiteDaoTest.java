package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoTest;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 emf = Mockito.mock(EntityManagerFactory.class);
 em = Mockito.mock(EntityManager.class);
 dao = new OracleIdentiteDao();
 dao.setEmf(emf);
 }

 @Test
 public void testInsert() {
 // Given a new Identite object
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 // When the insert method is called
 doNothing(() -> dao.insert(identite, "testForm", new Date()));

 // Then the entity manager should persist the object
 verify(em).persist((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertWithNullIdentite() {
 // Given a null Identite object
 doThrow(new IllegalArgumentException("Identite cannot be null"))
 .when(dao).insert(null, "testForm", new Date());

 // When the insert method is called
 dao.insert(null, "testForm", new Date());
 }

 @Test(expected = PersistenceException.class)
 public void testInsertWithException() {
 // Given an exception is thrown by the entity manager
 doThrow(new PersistenceException("Persistence failed"))
 .when(em).persist((Object)any());

 // When the insert method is called
 dao.insert(identite, "testForm", new Date());

 // Then an exception should be thrown
 verify(em).persist((Object)identite);
 verifyThrows(PersistenceException.class, () -> dao.insert(identite, "testForm", new Date()));
 }

 @Test
 public void testUpdate() {
 // Given an existing Identite object
 final Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setNom("Doe");
 existingIdentite.setPrenom("John");
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);

 // Given a new Identite object with updated fields
 final Identite newIdentite = new Identite();
 newIdentite.setId(1L);
 newIdentite.setNom("Doe");
 newIdentite.setPrenom("John Updated");
 newIdentite.setEmail("john.doe.updated@example.com");
 newIdentite.setSex("M");
 newIdentite.setType(Identite.TypeIdentite.CANDIDAT);

 // When the update method is called
 doNothing(() -> dao.update(newIdentite));

 // Then the entity manager should update the object
 verify(em).find((Class)Identite.class, (Object)existingIdentite.getId());
 verify(em).merge((Object)newIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullIdentite() {
 // Given a null Identite object
 doThrow(new IllegalArgumentException("Identite cannot be null"))
 .when(dao).update(null);

 // When the update method is called
 dao.update(null);
 }

 @Test(expected = PersistenceException.class)
 public void testUpdateWithException() {
 // Given an exception is thrown by the entity manager
 doThrow(new PersistenceException("Persistence failed"))
 .when(em).merge((Object)any());

 // When the update method is called
 dao.update(identite);

 // Then an exception should be thrown
 verify(em).find((Class)Identite.class, (Object)identite.getId());
 verifyThrows(PersistenceException.class, () -> dao.update(identite));
 }

 @Test
 public void testDelete() {
 // Given an existing Identite object with ID 1
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 // When the delete method is called
 doNothing(() -> dao.delete(identite));

 // Then the entity manager should find and remove the object
 verify(em).find((Class)Identite.class, (Object)identite.getId());
 verify(em).remove((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteWithNullIdentite() {
 // Given a null Identite object
 doThrow(new IllegalArgumentException("Identite cannot be null"))
 .when(dao).delete(null);

 // When the delete method is called
 dao.delete(null);
 }

 @Test(expected = PersistenceException.class)
 public void testDeleteWithException() {
 // Given an exception is thrown by the entity manager
 doThrow(new PersistenceException("Persistence failed"))
 .when(em).remove((Object)any());

 // When the delete method is called
 dao.delete(identite);

 // Then an exception should be thrown
 verify(em).find((Class)Identite.class, (Object)identite.getId());
 verifyThrows(PersistenceException.class, () -> dao.delete(identite));
 }

 @Test
 public void testFind() {
 // Given some identities in the database
 final Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);

 final Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);

 final Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe3");
 identite3.setPrenom("John3");
 identite3.setSex("F");
 identite3.setType(Identite.TypeIdentite.CANDIDAT);

 // Given a call to the find method with empty parameters
 final List<Identite> result = dao.find(null, null, null, null, null);

 // Then the method should return an empty list
 assertEquals(0, result.size());

 // Given a call to the find method with some parameters
 final List<Identite> result2 = dao.find("Doe1", "John1", null, null, null);

 // Then the method should return a list containing the first identity
 assertEquals(1, result2.size());
 assertEquals(identite1, result2.get(0));

 // Given a call to the find method with some parameters matching multiple identities
 final List<Identite> result3 = dao.find("Doe", "John", null, null, null);

 // Then the method should return a list containing both identities with the given name and prenom
 assertEquals(2, result3.size());
 assertTrue(Arrays.asList(identite1, identite2).containsAll(result3));
 }

 @Test
 public void testFindCandidat() {
 // Given some identities in the database
 final Candidat candidat1 = new Candidat();
 candidat1.setId(1L);
 candidat1.setNom("Doe1");
 candidat1.setPrenom("John1");
 candidat1.setSex("M");
 candidat1.setEmail("john1.doe@example.com");
 candidat1.setType(Identite.TypeIdentite.CANDIDAT);

 final Candidat candidat2 = new Candidat();
 candidat2.setId(2L);
 candidat2.setNom("Doe2");
 candidat2.setPrenom("John2");
 candidat2.setSex("M");
 candidat2.setEmail("john2.doe@example.com");
 candidat2.setType(Identite.TypeIdentite.CANDIDAT);

 // Given a call to the findCandidat method with empty parameters
 final Candidat[] result = dao.findCandidat(null, null, null, null);

 // Then the method should return an empty array
 assertArrayEquals(new Candidat[0], result);

 // Given a call to the findCandidat method with some parameters
 final Candidat[] result2 = dao.findCandidat("Doe1", "John1", null, null);

 // Then the method should return an array containing the first candidate
 assertArrayEquals(new Candidat[] { candidat1 }, result2);

 // Given a call to the findCandidat method with some parameters matching multiple candidates
 final Candidat[] result3 = dao.findCandidat("Doe", "John", null, null);

 // Then the method should return an array containing both candidates with the given name and prenom
 assertArrayEquals(new Candidat[] { candidat1, candidat2 }, result3);
 }

 @Test
 public void testFindRespForm() {
 // Given some identities in the database
 final RespFormation respFormation1 = new RespFormation();
 respFormation1.setId(1L);
 respFormation1.setNom("Doe1");
 respFormation1.setPrenom("John1");
 respFormation1.setSex("M");
 respFormation1.setEmail("john1.doe@example.com");
 respFormation1.setType(Identite.TypeIdentite.FORMATEUR);

 final RespFormation respFormation2 = new RespFormation();
 respFormation2.setId(2L);
 respFormation2.setNom("Doe2");
 respFormation2.setPrenom("John2");
 respFormation2.setSex("M");
 respFormation2.setEmail("john2.doe@example.com");
 respFormation2.setType(Identite.TypeIdentite.FORMATEUR);

 // Given a call to the findRespForm method with empty parameters
 final RespFormation[] result = dao.findRespForm(null, null, null, null);

 // Then the method should return an empty array
 assertArrayEquals(new RespFormation[0], result);

 // Given a call to the findRespForm method with some parameters
 final RespFormation[] result2 = dao.findRespForm("Doe1", "John1", null, null);

 // Then the method should return an array containing the first responsable formation
 assertArrayEquals(new RespFormation[] { respFormation1 }, result2);

 // Given a call to the findRespForm method with some parameters matching multiple responsables formations
 final RespFormation[] result3 = dao.findRespForm("Doe", "John", null, null);

 // Then the method should return an array containing both responsables formations with the given name and prenom
 assertArrayEquals(new RespFormation[] { respFormation1, respFormation2 }, result3);
 }
}