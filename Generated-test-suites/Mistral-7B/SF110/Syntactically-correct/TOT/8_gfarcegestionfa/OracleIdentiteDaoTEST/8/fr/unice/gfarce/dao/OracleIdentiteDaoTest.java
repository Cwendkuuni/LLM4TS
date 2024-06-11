package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@Transactional
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManagerFactory emf;
 private EntityManager em;

 @Before
 public void before() {
 MockitoAnnotations.initMocks(this);
 dao = new OracleIdentiteDaoImpl();
 emf = mock(PersistenceUnitManager.class);
 dao.setPersistUnitName("Identite");
 when(emf.createEntityManagerFactory(dao.getPersistUnitName())).thenReturn(emf);
 em = mock(EntityManager.class);
 when(emf.createEntityManager()).thenReturn(em);
 dao.setEm(em);
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar dateLimite = Calendar.getInstance();
 dateLimite.set(Calendar.YEAR, 2022);
 identite.setDateLimite(dateLimite.getTime());

 dao.insert(identite, null, null);

 verify(em, times(1)).getTransaction();
 verify(em, times(1)).persist((Object)identite);
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
 identite.setDateLimite(new Date());

 Identite identiteDb = new Identite();
 identiteDb.setId(1L);
 identiteDb.setNom("Jane");
 identiteDb.setPrenom("Doe");
 identiteDb.setEmail("jane.doe@example.com");
 identiteDb.setSex("F");
 identiteDb.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDb.setDateLimite(new Date());

 when(em.find((Class)Identite.class, (Object)1L)).thenReturn(identiteDb);

 dao.update(identite);

 verify(em, times(1)).getTransaction();
 verify(em, times(1)).find((Class)Identite.class, (Object)1L);
 verify(em, times(1)).merge((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 dao.update(null);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);

 when(em.find((Class)Identite.class, (Object)1L)).thenReturn(identite);

 dao.delete(identite);

 verify(em, times(1)).getTransaction();
 verify(em, times(1)).find((Class)Identite.class, (Object)1L);
 verify(em, times(1)).remove((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 dao.delete(null);
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setDateLimite(new Date());
List<Identite> identites = new ArrayList<Identite>();
identites.add(identite1);
when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).setParameter(eq("CANDIDAT"), any())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).setParameter(eq("FORMATEUR"), any())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(identites);

List<Identite> result = dao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
assertEquals(1, result.size());
assertEquals(identite1, result.get(0));
}

@Test
public void testFindEmptyResult() {
List<Identite> identites = new ArrayList<Identite>();
when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(identites);

List<Identite> result = dao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
assertEquals(0, result.size());
}

@Test
public void testFindNullParameters() {
List<Identite> identites = new ArrayList<Identite>();
when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(identites);

List<Identite> result = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
assertEquals(0, result.size());
}

@Test
public void testFindAll() {
Identite identite1 = new Identite();
identite1.setId(1L);
identite1.setNom("Doe1");
identite1.setPrenom("John1");
identite1.setEmail("john1.doe@example.com");
identite1.setSex("M");
identite1.setType(Identite.TypeIdentite.CANDIDAT);
identite1.setDateLimite(new Date());
Identite identite2 = new Identite();
identite2.setId(2L);
identite2.setNom("Doe2");
identite2.setPrenom("John2");
identite2.setEmail("john2.doe@example.com");
identite2.setSex("F");
identite2.setType(Identite.TypeIdentite.FORMATEUR);
identite2.setDateLimite(new Date());
List<Identite> identites = new ArrayList<Identite>();
identites.add(identite1);
identites.add(identite2);
when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(identites);

List<Identite> result = dao.find(null, null, null, null, null);
assertEquals(2, result.size());
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
identite.setDateLimite(new Date());
Candidat candidat = new Candidat();
candidat.setIdentite(identite);

when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).setParameter("CANDIDAT", (Object)Identite.TypeIdentite.CANDIDAT)).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(Arrays.asList(candidat));

Candidat[] result = dao.findCandidat("Doe", "John", null, null);
assertEquals(1, result.length);
assertEquals(candidat, result[0]);
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
identite.setDateLimite(new Date());
RespFormation respFormation = new RespFormation();
respFormation.setIdentite(identite);

when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).setParameter("FORMATEUR", (Object)Identite.TypeIdentite.FORMATEUR)).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(Arrays.asList(respFormation));

RespFormation[] result = dao.findRespForm("Doe", "John", null, null);
assertEquals(1, result.length);
assertEquals(respFormation, result[0]);
}

@Test
public void testFindNullResult() {
when(em.createQuery(anyString())).thenReturn(mock(Query.class));
when(em.createQuery(anyString()).getResultList()).thenReturn(null);

Candidat[] result = dao.findCandidat("Doe", "John", null, null);
assertEquals(0, result.length);
}
}