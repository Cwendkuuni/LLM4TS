package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.TransactionRequiredException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.TypeIdentite;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private EntityTransaction tx;
 @PersistenceContext
 private EntityManager mockEm;
 @Mock
 private PersistenceUnitManager persistenceUnitManager;
 @InjectMocks
 private OracleIdentiteDaoImpl dao;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 emf = mock(EntityManagerFactory.class);
 em = mock(EntityManager.class);
 tx = mock(EntityTransaction.class);
 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(persistenceUnitManager.getPersistenceUnitInfo(OracleIdentiteDaoImpl.PERSIST_UNIT_NAME)).thenReturn(null);
 }

 @Test
 public void testInsert() {
 // Create a mock Identite object
 final Identite identite = mock(Identite.class);
 final Calendar dateLimite = Calendar.getInstance();
 dateLimite.set(2022, 11, 30);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identite.setEmail("johndoe@example.com");
 identite.setDateLimite(new Date(dateLimite.getTimeInMillis()));
 // Mock the persist method to throw an exception
 doThrow(new Exception("Test exception")).when(em).persist((Object)identite);
 // Call the method under test
 dao.insert(identite, "form", dateLimite);
 // Assert that the exception is thrown
 try {
 dao.insert(identite, "form", dateLimite);
 fail("Expected an exception to be thrown");
 }
 catch (Exception e) {
 assertEquals("Test exception", e.getMessage());
 }
 // Reset the mocks
 reset(em, tx, persistenceUnitManager);
 // Mock the persist method to return an Identite object
 final Identite savedIdentite = mock(Identite.class);
 when(em.persist((Object)identite)).thenReturn(savedIdentite);
 // Call the method under test
 dao.insert(identite, "form", dateLimite);
 // Assert that the Identite object is persisted
 verify(em).persist((Object)identite);
 // Assert that the transaction is committed
 verify(tx).commit();
 }

 @Test
 public void testUpdate() {
 // Create a mock Identite object
 final Identite identite = mock(Identite.class);
 final Identite updatedIdentite = mock(Identite.class);
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identite.setEmail("johndoe@example.com");
 updatedIdentite.setId(1L);
 updatedIdentite.setNom("Smith");
 updatedIdentite.setPrenom("Jane");
 updatedIdentite.setSex("F");
 updatedIdentite.setType(TypeIdentite.FORMATEUR);
 updatedIdentite.setEmail("janesmith@example.com");
 // Mock the find method to return the identite object
 when(em.find((Class<Identite>)Identite.class, (Object)identite.getId())).thenReturn(identite);
 // Mock the merge method to throw an exception
 doThrow(new TransactionRequiredException("Test exception")).when(em).merge((Object)updatedIdentite);
 // Call the method under test
 dao.update(updatedIdentite);
 // Assert that the exception is thrown
 try {
 dao.update(updatedIdentite);
 fail("Expected an exception to be thrown");
 }
 catch (RuntimeException e) {
 assertEquals("Test exception", e.getMessage());
 }
 // Reset the mocks
 reset(em, tx, persistenceUnitManager);
 // Mock the find method to return the identite object
 when(em.find((Class<Identite>)Identite.class, (Object)identite.getId())).thenReturn(identite);
 // Mock the merge method to not throw an exception
 when(em.merge((Object)updatedIdentite)).thenReturn(updatedIdentite);
 // Call the method under test
 dao.update(updatedIdentite);
 // Assert that the Identite object is updated
 verify(em).find((Class<Identite>)Identite.class, (Object)identite.getId());
 verify(em).merge((Object)updatedIdentite);
 }

 @Test
 public void testDelete() {
 // Create a mock Identite object
 final Identite identite = mock(Identite.class);
 identite.setId(1L);
 // Mock the find method to return the identite object
 when(em.find((Class<Identite>)Identite.class, (Object)identite.getId())).thenReturn(identite);
 // Call the method under test
 dao.delete(identite);
 // Assert that the Identite object is deleted
 verify(em).find((Class<Identite>)Identite.class, (Object)identite.getId());
 verify(em).remove((Object)identite);
 }

 @Test
 public void testFind() {
 // Create test data
 final Identite identite1 = new Identite();
 identite1.setNom("Doe");
 identite1.setPrenom("John");
 identite1.setSex("M");
 identite1.setType(TypeIdentite.CANDIDAT);
 identite1.setEmail("johndoe@example.com");
 final Identite identite2 = new Identite();
 identite2.setNom("Smith");
 identite2.setPrenom("Jane");
 identite2.setSex("F");
 identite2.setType(TypeIdentite.FORMATEUR);
 identite2.setEmail("janesmith@example.com");
 // Mock the find method to return the test data
 when(em.createQuery("select ident from Identite ident where ident.nom = :nom and ident.prenom = :prenom and ident.sex = :sex and ident.email = :email and ident.type = :type")
 .setParameter("nom", "Doe")
 .setParameter("prenom", "John")
 .setParameter("sex", "M")
 .setParameter("email", "johndoe@example.com")
 .setParameter("type", TypeIdentite.CANDIDAT)
 .getResultList()).thenReturn(Arrays.asList(identite1));
 when(em.createQuery("select ident from Identite ident where ident.nom = :nom and ident.prenom = :prenom and ident.sex = :sex and ident.email = :email and ident.type = :type")
 .setParameter("nom", "Smith")
 .setParameter("prenom", "Jane")
 .setParameter("sex", "F")
 .setParameter("email", "janesmith@example.com")
 .setParameter("type", TypeIdentite.FORMATEUR)
 .getResultList()).thenReturn(Arrays.asList(identite2));
 // Call the method under test
 final List<Identite> result = dao.find("Doe", "John", "M", "johndoe@example.com", TypeIdentite.CANDIDAT);
 // Assert that the result contains the expected data
 assertEquals(1, result.size());
 assertEquals(identite1, result.get(0));
 }

 @Test
 public void testFindCandidat() {
 // Create test data
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);
 identite.setEmail("johndoe@example.com");
 final Candidat candidat = new Candidat();
 candidat.setId(1L);
 candidat.setNom("Doe");
 candidat.setPrenom("John");
 candidat.setSex("M");
 candidat.setEmail("johndoe@example.com");
 // Mock the find method to return the test data
 when(em.createQuery("select ident from Identite ident where ident.nom = :nom and ident.prenom = :prenom and ident.sex = :sex and ident.email = :email and ident.type = :type")
 .setParameter("nom", "Doe")
 .setParameter("prenom", "John")
 .setParameter("sex", "M")
 .setParameter("email", "johndoe@example.com")
 .setParameter("type", TypeIdentite.CANDIDAT)
 .getResultList()).thenReturn(Arrays.asList(identite));
 when(em.createQuery("select ident from Identite ident where ident.nom = :nom and ident.prenom = :prenom and ident.sex = :sex and ident.email = :email and ident.type = :type")
 .setParameter("nom", "Doe")
 .setParameter("prenom", "John")
 .setParameter("sex", "M")
 .setParameter("email", "johndoe@example.com")
 .setParameter("type", TypeIdentite.CANDIDAT)
 .getResultList()).thenReturn(Arrays.asList(identite));
 when(em.createQuery("select c from Candidat c where c.nom = :nom and c.prenom = :prenom and c.sex = :sex and c.email = :email")
 .setParameter("nom", "Doe")
 .setParameter("prenom", "John")
 .setParameter("sex", "M")
 .setParameter("email", "johndoe@example.com")
 .getResultList()).thenReturn(Arrays.asList(candidat));
 // Call the method under test
 final Candidat[] result = dao.findCandidat("Doe", "John", "M", "johndoe@example.com");
 // Assert that the result contains the expected data
 assertEquals(1, result.length);
 assertEquals(candidat, result[0]);
 }

 @Test
 public void testFindRespForm() {
 // Create test data
 final Identite identite = new Identite();
 identite.setNom("Smith");
 identite.setPrenom("Jane");
 identite.setSex("F");
 identite.setType(TypeIdentite.FORMATEUR);
 identite.setEmail("janesmith@example.com");
 final RespFormation respFormation = new RespFormation();
 respFormation.setId(1L);
 respFormation.setNom("Smith");
 respFormation.setPrenom("Jane");
 respFormation.setSex("F");
 respFormation.setEmail("janesmith@example.com");
 // Mock the find method to return the test data
 when(em.createQuery("select ident from Identite ident where ident.nom = :nom and ident.prenom = :prenom and ident.sex = :sex and ident.email = :email and ident.type = :type")
 .setParameter("nom", "Smith")
 .setParameter("prenom", "Jane")
 .setParameter("sex", "F")
 .setParameter("email", "janesmith@example.com")
 .setParameter("type", TypeIdentite.FORMATEUR)
 .getResultList()).thenReturn(Arrays.asList(identite));
 when(em.createQuery("select r from RespFormation r where r.nom = :nom and r.prenom = :prenom and r.sex = :sex and r.email = :email")
 .setParameter("nom", "Smith")
 .setParameter("prenom", "Jane")
 .setParameter("sex", "F")
 .setParameter("email", "janesmith@example.com")
 .getResultList()).thenReturn(Arrays.asList(respFormation));
 // Call the method under test
 final RespFormation[] result = dao.findRespForm("Smith", "Jane", "F", "janesmith@example.com");
 // Assert that the result contains the expected data
 assertEquals(1, result.length);
 assertEquals(respFormation, result[0]);
 }
}