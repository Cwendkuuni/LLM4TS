package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.TypeIdentite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManagerMock;

 @InjectMocks
 private OracleIdentiteDao identiteDao;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 identiteDao = new OracleIdentiteDao();
 }

 @After
 public void tearDown() {
 // Close the EntityManager and EntityManagerFactory after each test
 if (identiteDao.em != null) {
 identiteDao.em.close();
 }
 if (identiteDao.emf != null) {
 identiteDao.emf.close();
 }
 }

 @Test
 public void testInsert() {
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "testForm", new GregorianCalendar(2022, 1, 1));

 // Assert that the entity has been inserted
 List<Identite> identites = identiteDao.find("Doe", "John", null, "john.doe@example.com", TypeIdentite.CANDIDAT);
 assertNotNull(identites);
 assertEquals(1, identites.size());
 assertEquals(identite, identites.get(0));
 }

 @Test
 public void testUpdate() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);

 identiteDao.update(identite);

 // Assert that the entity has been updated
 Identite dbIdentite = identiteDao.findById(1L);
 assertNotNull(dbIdentite);
 assertEquals(identite, dbIdentite);
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateNullIdentite() {
 identiteDao.update(null);
 }

 @Test
 public void testDelete() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);

 // Assert that the entity has been deleted
 List<Identite> identites = identiteDao.find("Doe", "John", null, "john.doe@example.com", TypeIdentite.CANDIDAT);
 assertNotNull(identites);
 assertEquals(0, identites.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteNullIdentite() {
 identiteDao.delete(null);
 }

 @Test
 public void testFind() {
 final List<Identite> identites = Arrays.asList(
 new Identite(1L, "Doe1", "John1", "john1@example.com", "M", TypeIdentite.CANDIDAT),
 new Identite(2L, "Doe2", "John2", "john2@example.com", "M", TypeIdentite.CANDIDAT),
 new Identite(3L, "Doe3", "John3", "john3@example.com", "F", TypeIdentite.FORMATEUR)
 );
 when(entityManagerMock.createQuery(anyString(), any(Class.class))).thenReturn(entityManagerMock);
 when(entityManagerMock.findAll()).thenReturn(identites);

 final List<Identite> result = identiteDao.find("Doe", null, null, null, null);
 assertNotNull(result);
 assertEquals(3, result.size());
 }

 @Test
 public void testFindCandidat() {
 final List<Identite> identites = Arrays.asList(
 new Candidat(1L, "Doe1", "John1", "john1@example.com", "M") {
 {
 setType(TypeIdentite.CANDIDAT);
 }
 },
 new Identite(2L, "Doe2", "John2", "john2@example.com", "F", TypeIdentite.FORMATEUR)
 );
 when(entityManagerMock.createQuery(anyString(), any(Class.class))).thenReturn(entityManagerMock);
 when(entityManagerMock.setParameter(anyInt(), anyString(), any())).thenReturn(entityManagerMock);
 when(entityManagerMock.getResultList()).thenReturn(identites);

 final Candidat[] result = identiteDao.findCandidat("Doe", "John", null, "john.doe@example.com");
 assertNotNull(result);
 assertEquals(1, result.length);
 assertEquals("Doe1", result[0].getNom());
 }

 @Test
 public void testFindRespForm() {
 final List<Identite> identites = Arrays.asList(
 new RespFormation(1L, "Doe1", "John1", "john1@example.com", "M", TypeIdentite.FORMATEUR) {
 {
 setType(TypeIdentite.FORMATEUR);
 }
 },
 new Identite(2L, "Doe2", "John2", "john2@example.com", "F", TypeIdentite.CANDIDAT)
 );
 when(entityManagerMock.createQuery(anyString(), any(Class.class))).thenReturn(entityManagerMock);
 when(entityManagerMock.setParameter(anyInt(), anyString(), any())).thenReturn(entityManagerMock);
 when(entityManagerMock.getResultList()).thenReturn(identites);

 final RespFormation[] result = identiteDao.findRespForm("Doe", "John", null, "john.doe@example.com");
 assertNotNull(result);
 assertEquals(1, result.length);
 assertEquals("Doe1", result[0].getNom());
 }

 @Test
 public void testFindNullIdentite() {
 final List<Identite> identites = Arrays.asList(new Identite());
 when(entityManagerMock.createQuery(anyString(), any(Class.class))).thenReturn(entityManagerMock);
 when(entityManagerMock.setParameter(anyInt(), anyString(), any())).thenReturn(entityManagerMock);
 when(entityManagerMock.getResultList()).thenReturn(identites);

 final List<Identite> result = identiteDao.find(null, null, null, null, null);
 assertEquals(0, result.size());
 }
}