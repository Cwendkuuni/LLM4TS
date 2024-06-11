package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.time.Calendar;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManager entityManagerMock;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 entityManagerMock = Mockito.mock(EntityManager.class);
 dao = new OracleIdentiteDaoImpl();
 Mockito.when(entityManagerMock.createEntityManagerFactory(Mockito.anyString())).then(invocation -> entityManagerMock);
 dao.setEm(entityManagerMock);
 }

 @Test
 public void testInsert() {
 final Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "Form1", Calendar.getInstance());

 Mockito.verify(entityManagerMock).persist((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 dao.insert(null, "Form1", Calendar.getInstance());

 Mockito.verifyNoInteractions(entityManagerMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullForm1() {
 dao.insert(identite, null, Calendar.getInstance());

 Mockito.verifyNoInteractions(entityManagerMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullCalendar() {
 dao.insert(identite, "Form1", null);

 Mockito.verifyNoInteractions(entityManagerMock);
 }

 @Test
 public void testUpdate() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(entityManagerMock.find((Class<Identite>)Identite.class, Mockito.anyLong())).thenReturn(identite);

 dao.update(new Identite());

 Mockito.verifyNoInteractions(entityManagerMock);

 dao.update(identite);

 Mockito.verify(entityManagerMock).merge((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 dao.update(null);

 Mockito.verifyNoInteractions(entityManagerMock);
 }

 @Test
 public void testDelete() {
 final Identite identite = new Identite();
 identite.setId(1L);

 Mockito.when(entityManagerMock.find((Class<Identite>)Identite.class, Mockito.anyLong())).thenReturn(identite);

 dao.delete(identite);

 Mockito.verify(entityManagerMock).remove((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 dao.delete(null);

 Mockito.verifyNoInteractions(entityManagerMock);
 }

 @Test
 public void testFind() {
 final Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 final Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 final List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);

 Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.getResultList()).thenReturn(identites);

 final String nom = "Doe";
 final String prenom = "John";
 final String sex = "M";
 final String email = "john.doe@example.com";
 final Identite.TypeIdentite type = Identite.TypeIdentite.ALL;

 final List<Identite> result = dao.find(nom, prenom, sex, email, type);

 assertEquals(2, result.size());
 }

 @Test
 public void testFindWithTypeCandidat() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 final List<Identite> identites = new ArrayList<>();
 identites.add(identite);

 Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.setParameter(Mockito.anyInt(), Mockito.any())).then(invocation -> {});
 Mockito.when(entityManagerMock.getResultList()).thenReturn(identites);

 final String nom = "Doe";
 final String prenom = "John";
 final String sex = "M";
 final String email = "john.doe@example.com";
 final Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 final List<Identite> result = dao.find(nom, prenom, sex, email, type);

 assertEquals(1, result.size());
 }

 @Test
 public void testFindWithTypeFormateur() {
 final Identite identite = new Identite();
 identite.setId(2L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);

 final List<Identite> identites = new ArrayList<>();
 identites.add(identite);

 Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.setParameter(Mockito.anyInt(), Mockito.any())).then(invocation -> {});
 Mockito.when(entityManagerMock.getResultList()).thenReturn(identites);

 final String nom = "Doe";
 final String prenom = "John";
 final String sex = "M";
 final String email = "john.doe@example.com";
 final Identite.TypeIdentite type = Identite.TypeIdentite.FORMATEUR;

 final List<Identite> result = dao.find(nom, prenom, sex, email, type);

 assertEquals(1, result.size());
 }

 @Test
 public void testFindWithTypeAll() {
 final Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 final Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 final List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);

 Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.getResultList()).thenReturn(identites);

 final String nom = "Doe";
 final String prenom = "John";
 final String sex = "M";
 final String email = "john.doe@example.com";
 final Identite.TypeIdentite type = Identite.TypeIdentite.ALL;

 final List<Identite> result = dao.find(nom, prenom, sex, email, type);

 assertEquals(2, result.size());
 }

 @Test
 public void testFindCandidat() {
 final Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 final Candidat candidat = new Candidat();
 candidat.setId(1L);
 candidat.setNom("Doe");
 candidat.setPrenom("John");
 candidat.setSex("M");
 candidat.setEmail("john.doe@example.com");
 final List<Identite> identites = new ArrayList<>();
 identites.add(identite);

 Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.setParameter(Mockito.anyInt(), Mockito.any())).then(invocation -> {});
 Mockito.when(entityManagerMock.getResultList()).thenReturn(identites);

 final String nom = "Doe";
 final String prenom = "John";
 final String sex = "M";
 final String email = "john.doe@example.com";

 final Candidat[] result = dao.findCandidat(nom, prenom, sex, email);

 assertEquals(1, result.length);
 assertEquals(identite, result[0]);
 }

 @Test
 public void testFindRespForm() {
 final Identite identite = new Identite();
 identite.setId(2L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 final RespFormation formation = new RespFormation();
 formation.setId(1L);
 formation.setNom("Form1");
 formation.setIdentite(identite);
 final List<Identite> identites = new ArrayList<>();
 identites.add(identite);

 Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.setParameter(Mockito.anyInt(), Mockito.any())).then(invocation -> {});
 Mockito.when(entityManagerMock.getResultList()).thenReturn(identites);

 final String nom = "Doe";
 final String prenom = "John";
 final String sex = "M";
 final String email = "john.doe@example.com";

 final RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);

 assertEquals(1, result.length);
 assertEquals(identite, result[0].getIdentite());
 }
}