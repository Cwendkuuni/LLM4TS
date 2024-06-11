package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import javax.persistence.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import fr.unice.gfarce.identity.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManager mockEntityManager;
 private EntityManagerFactory mockEntityManagerFactory;
 private EntityTransaction mockTransaction;

 @Before
 public void setup() {
 this.mockEntityManager = mock(EntityManager.class);
 this.mockEntityManagerFactory = mock(EntityManagerFactory.class);
 this.mockTransaction = mock(EntityTransaction.class);
 this.dao = new OracleIdentiteDao() {
 @PersistenceContext
 public EntityManager getEntityManager() {
 return this.mockEntityManager;
 }

 @PersistenceUnit(unitName = "Identite")
 public EntityManagerFactory getEntityManagerFactory() {
 return this.mockEntityManagerFactory;
 }
 };
 }

 @Test
 public void testInsert() {
 dao.insert(mock(Identite.class), "nomForm", mock(Calendar.class));
 verify(mockEntityManagerFactory).createEntityManager();
 verify(mockEntityManager, times(1)).persist((Object)any());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertWithException() {
 doThrow(Exception.class).when(mockTransaction).begin();
 dao.insert(mock(Identite.class), "nomForm", mock(Calendar.class));
 verify(mockTransaction).rollback();
 }

 @Test
 public void testUpdate() {
 final Identite identite = mock(Identite.class);
 when(mockEntityManager.find((Class)identite.getClass(), (Object)identite.getId())).thenReturn(identite);
 dao.update(identite);
 verify(mockEntityManager).merge((Object)identite);
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateWithException() {
 doThrow(Exception.class).when(mockTransaction).begin();
 dao.update(mock(Identite.class));
 verify(mockTransaction).rollback();
 }

 @Test
 public void testDelete() {
 final Identite identite = mock(Identite.class);
 doReturn(identite).when(mockEntityManager).find((Class)identite.getClass(), (Object)identite.getId());
 dao.delete(identite);
 verify(mockEntityManager).remove((Object)identite);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteWithException() {
 doThrow(Exception.class).when(mockTransaction).begin();
 dao.delete(mock(Identite.class));
 verify(mockTransaction).rollback();
 }

 @Test
 public void testFind() {
 final List<Identite> identites = Arrays.asList(mock(Identite.class), mock(Identite.class));
 when(mockEntityManager.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.getResultList()).thenReturn(identites);
 final String nom = "nom";
 final String prenom = "prenom";
 final String sex = "sex";
 final Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 final List<Identite> result = dao.find(nom, prenom, sex, null, type);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindEmpty() {
 final List<Identite> identites = Collections.emptyList();
 when(mockEntityManager.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.getResultList()).thenReturn(identites);
 final String nom = "nom";
 final String prenom = "prenom";
 final String sex = "sex";
 final Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 final List<Identite> result = dao.find(nom, prenom, sex, null, type);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testFindCandidat() {
 final Candidat candidat = mock(Candidat.class);
 final List<Identite> identites = Arrays.asList(candidat, mock(Identite.class));
 when(mockEntityManager.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.setParameter("CANDIDAT", (Object)Identite.TypeIdentite.CANDIDAT)).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.getResultList()).thenReturn(identites);
 final String nom = "nom";
 final String prenom = "prenom";
 final String sex = "sex";
 final String email = "email";
 final List<Candidat> result = dao.findCandidat(nom, prenom, sex, email);
 assertEquals(1, result.length);
 assertEquals(candidat, result[0]);
 }

 @Test
 public void testFindRespForm() {
 final RespFormation respFormation = mock(RespFormation.class);
 final List<Identite> identites = Arrays.asList(respFormation, mock(Identite.class));
 when(mockEntityManager.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.setParameter("FORMATEUR", (Object)Identite.TypeIdentite.FORMATEUR)).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.getResultList()).thenReturn(identites);
 final String nom = "nom";
 final String prenom = "prenom";
 final String sex = "sex";
 final String email = "email";
 final List<RespFormation> result = dao.findRespForm(nom, prenom, sex, email);
 assertEquals(1, result.length);
 assertEquals(respFormation, result[0]);
 }

 @Test
 public void testFindRespFormEmpty() {
 final List<Identite> identites = Collections.emptyList();
 when(mockEntityManager.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(mockTypedQuery.getResultList()).thenReturn(identites);
 final String nom = "nom";
 final String prenom = "prenom";
 final String sex = "sex";
 final String email = "email";
 final List<RespFormation> result = dao.findRespForm(nom, prenom, sex, email);
 assertNotNull(result);
 assertEquals(0, result.size());
 }
}