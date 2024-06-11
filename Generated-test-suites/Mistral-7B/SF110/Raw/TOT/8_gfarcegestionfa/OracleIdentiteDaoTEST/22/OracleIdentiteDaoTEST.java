package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Before
 public void setUp() {
 this.oracleIdentiteDao = new OracleIdentiteDao();
 }

 @Test
 public void testInsert() {
 // Setup mock entity manager
 when(this.entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
 when(this.entityManager.persist((Object)any())).thenReturn(null);

 // Test insert with valid arguments
 Identite identite = new Identite();
 oracleIdentiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 1, 1));
 verify(this.entityManager).getTransaction();
 verify(this.entityManager).persist((Object)identite);

 // Test insert with null entity manager
 oracleIdentiteDao.insert(identite, "nomForm", new GregorianCalendar(2022, 1, 1));
 assertNull(entityManager);
 }

 @Test(expected = NullPointerException.class)
 public void testInsertNullEntityManager() {
 oracleIdentiteDao.insert(null, "nomForm", new GregorianCalendar(2022, 1, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 oracleIdentiteDao.insert(null, "nomForm", new GregorianCalendar(2022, 1, 1));
 }

 @Test
 public void testUpdate() {
 // Setup mock entity manager
 Identite identite = new Identite();
 when(this.entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
 when(this.entityManager.find((Class)anyClass(), (Object)anyInt())).thenReturn(identite);
 when(this.entityManager.merge((Object)any())).thenReturn(identite);

 // Test update with valid arguments
 Identite updatedIdentite = new Identite();
 oracleIdentiteDao.update(updatedIdentite);
 verify(this.entityManager).getTransaction();
 verify(this.entityManager).find((Class)Identite.class, (Object)0);
 verify(this.entityManager).merge((Object)updatedIdentite);

 // Test update with null entity manager
 oracleIdentiteDao.update(updatedIdentite);
 assertNull(entityManager);
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateNullEntityManager() {
 oracleIdentiteDao.update(updatedIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 oracleIdentiteDao.update(null);
 }

 @Test
 public void testDelete() {
 // Setup mock entity manager
 when(this.entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
 when(this.entityManager.find((Class)anyClass(), (Object)anyInt())).thenReturn(new Identite());
 when(this.entityManager.remove((Object)any())).thenReturn(null);

 // Test delete with valid arguments
 oracleIdentiteDao.delete(new Identite());
 verify(this.entityManager).getTransaction();
 verify(this.entityManager).find((Class)Identite.class, (Object)0);
 verify(this.entityManager).remove((Object)any());

 // Test delete with null entity manager
 oracleIdentiteDao.delete(new Identite());
 assertNull(entityManager);
 }

 @Test(expected = NullPointerException.class)
 public void testDeleteNullEntityManager() {
 oracleIdentiteDao.delete(new Identite());
 }

 @Test
 public void testFind() {
 // Setup mock entity manager
 when(this.entityManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
 when(this.entityManager.createQuery(anyString())).thenReturn(mock(javax.persistence.Query.class));

 // Test find with valid arguments and return empty result
 when(this.entityManager.createQuery(anyString())).thenReturn(mock(javax.persist.Query.class)).thenReturn(mock(List.class)).thenReturn(Collections.emptyList());
 List<Identite> result = oracleIdentiteDao.find("nom", "prenom", "sex", "email", Identite.TypeIdentite.CANDIDAT);
 assertNotNull(result);
 assertEquals(0, result.size());

 // Test find with valid arguments and return result
 Identite identite1 = new Identite();
 Identite identite2 = new Identite();
 when(this.entityManager.createQuery(anyString())).thenReturn(mock(javax.persistence.Query.class)).thenReturn(mock(List.class)).thenReturn(Arrays.asList(identite1, identite2));
 result = oracleIdentiteDao.find("nom", "prenom", "sex", "email", Identite.TypeIdentite.CANDIDAT);
 assertNotNull(result);
 assertEquals(2, result.size());
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullEntityManager() {
 oracleIdentiteDao.find("nom", "prenom", "sex", "email", Identite.TypeIdentite.CANDIDAT);
 }

 @Test
 public void testFindCandidat() {
 // Test findCandidat with valid arguments and empty result
 Identite[] result = oracleIdentiteDao.findCandidat("nom", "prenom", "sex", "email");
 assertNotNull(result);
 assertEquals(0, result.length);

 // Test findCandidat with valid arguments and result
 Identite identite1 = new Identite();
 Identite identite2 = new Identite();
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 when(this.oracleIdentiteDao.find("nom", "prenom", "sex", "email", Identite.TypeIdentite.CANDIDAT)).thenReturn(Arrays.asList(identite1, identite2).toArray(new Identite[0]));
 result = oracleIdentiteDao.findCandidat("nom", "prenom", "sex", "email");
 assertNotNull(result);
 assertEquals(2, result.length);
 }

 @Test(expected = NullPointerException.class)
 public void testFindCandidatNullEntityManager() {
 oracleIdentiteDao.findCandidat("nom", "prenom", "sex", "email");
 }

 @Test
 public void testFindRespForm() {
 // Test findRespForm with valid arguments and empty result
 Identite[] result = oracleIdentiteDao.findRespForm("nom", "prenom", "sex", "email");
 assertNotNull(result);
 assertEquals(0, result.length);

 // Test findRespForm with valid arguments and result
 Identite identite1 = new Identite();
 Identite identite2 = new Identite();
 identite1.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 when(this.oracleIdentiteDao.find("nom", "prenom", "sex", "email", Identite.TypeIdentite.FORMATEUR)).thenReturn(Arrays.asList(identite1, identite2).toArray(new Identite[0]));
 result = oracleIdentiteDao.findRespForm("nom", "prenom", "sex", "email");
 assertNotNull(result);
 assertEquals(2, result.length);
 }

 @Test(expected = NullPointerException.class)
 public void testFindRespFormNullEntityManager() {
 oracleIdentiteDao.findRespForm("nom", "prenom", "sex", "email");
 }
}