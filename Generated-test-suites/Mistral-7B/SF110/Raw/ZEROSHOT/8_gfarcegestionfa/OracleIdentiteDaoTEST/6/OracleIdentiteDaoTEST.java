package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setEmail("test@test.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2022, 0, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 oracleIdentiteDao = Mockito.spy(oracleIdentiteDao);

 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.getTransaction().isActive()).thenReturn(true);
 Mockito.when(entityManager.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.persist(Mockito.anyObject())).thenReturn(identite);
 Mockito.when(entityManager.find(Identite.class, Mockito.anyInt())).thenReturn(identite);

 oracleIdentiteDao.insert(identite, null, null);

 Mockito.verify(entityManager, Mockito.times(1)).persist(Mockito.anyObject());
 Mockito.verifyNoMoreInteractions(entityManager);
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setEmail("test@test.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateLimite(new Date());

 oracleIdentiteDao = Mockito.spy(oracleIdentiteDao);

 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.getTransaction().isActive()).thenReturn(true);
 Mockito.when(entityManager.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.find(Identite.class, Mockito.anyInt())).thenReturn(identite);
 Mockito.when(entityManager.getTransaction().commit()).thenReturn(null);
 Mockito.when(entityManager.merge(Mockito.anyObject())).thenReturn(identite);

 oracleIdentiteDao.update(identite);

 Mockito.verify(entityManager, Mockito.times(1)).merge(Mockito.anyObject());
 Mockito.verifyNoMoreInteractions(entityManager);
 }

 @Test
 public void testDeleteIdentite() {
Identite identite = new Identite();
identite.setId(1L);

oracleIdentiteDao = Mockito.spy(oracleIdentiteDao);

Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
Mockito.when(entityManager.getTransaction().isActive()).thenReturn(true);
Mockito.when(entityManager.createEntityManager()).thenReturn(entityManager);
Mockito.when(entityManager.find(Identite.class, Mockito.anyInt())).thenReturn(identite);
Mockito.when(entityManager.getTransaction().commit()).thenReturn(null);

oracleIdentiteDao.delete(identite);

Mockito.verify(entityManager, Mockito.times(1)).remove(Mockito.anyObject());
Mockito.verifyNoMoreInteractions(entityManager);
}

 @Test
 public void testFindIdentites() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Test1");
 identite1.setPrenom("Test1");
 identite1.setEmail("test1@test.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setDateLimite(new Date());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Test2");
 identite2.setPrenom("Test2");
 identite2.setEmail("test2@test.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setDateLimite(new Date());

 List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);

 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.getTransaction().isActive()).thenReturn(true);
 Mockito.when(entityManager.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(entityManager.getTransaction().commit()).thenReturn(null);

 when(entityManager.find(Identite.class, 1)).thenReturn(identite1);
 when(entityManager.find(Identite.class, 2)).thenReturn(identite2);

 List<Identite> result = oracleIdentiteDao.find(null, null, null, null, null);

 assertEquals("Size of the result list should be equal to 2", 2, result.size());
 }

 @Test
 public void testFindCandidats() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Test1");
 identite1.setPrenom("Test1");
 identite1.setEmail("test1@test.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setDateLimite(new Date());

 Candidat candidat1 = new Candidat();
 candidat1.setId(1L);
 candidat1.setIdentite(identite1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Test2");
 identite2.setPrenom("Test2");
 identite2.setEmail("test2@test.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setDateLimite(new Date());

 Candidat candidat2 = new Candidat();
 candidat2.setId(2L);
 candidat2.setIdentite(identite2);

 List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);
 List<Candidat> candidats = new ArrayList<>();
 candidats.add(candidat1);
 candidats.add(candidat2);

 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.getTransaction().isActive()).thenReturn(true);
 Mockito.when(entityManager.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(entityManager.getTransaction().commit()).thenReturn(null);

 when(entityManager.find(Identite.class, 1)).thenReturn(identite1);
 when(entityManager.find(Identite.class, 2)).thenReturn(identite2);

 List<Identite> identiteResult = oracleIdentiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 List<Candidat> candidatResult = new ArrayList<>();
 for (Identite identite : identiteResult) {
 candidatResult.add(new Candidat(identite));
 }

 assertEquals("Size of the result list should be equal to 1", 1, candidatResult.size());
 }

 @Test
 public void testFindResponsablesFormations() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Test1");
 identite1.setPrenom("Test1");
 identite1.setEmail("test1@test.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setDateLimite(new Date());

 RespFormation respFormation1 = new RespFormation();
 respFormation1.setId(1L);
 respFormation1.setIdentite(identite1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Test2");
 identite2.setPrenom("Test2");
 identite2.setEmail("test2@test.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identite2.setDateLimite(new Date());

 RespFormation respFormation2 = new RespFormation();
 respFormation2.setId(2L);
 respFormation2.setIdentite(identite2);

 List<Identite> identites = new ArrayList<>();
 identites.add(identite1);
 identites.add(identite2);
 List<RespFormation> responsablesFormations = new ArrayList<>();
 responsablesFormations.add(respFormation1);
 responsablesFormations.add(respFormation2);

 Mockito.when(entityManager.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
 Mockito.when(entityManager.getTransaction().isActive()).thenReturn(true);
 Mockito.when(entityManager.createEntityManager()).thenReturn(entityManager);
 Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(Mockito.mock(TypedQuery.class));
 Mockito.when(entityManager.getTransaction().commit()).thenReturn(null);

 when(entityManager.find(Identite.class, 1)).thenReturn(identite1);
 when(entityManager.find(Identite.class, 2)).thenReturn(identite2);

 List<Identite> identiteResult = oracleIdentiteDao.find(null, null, null, "test2@test.com", Identite.TypeIdentite.FORMATEUR);
 List<RespFormation> responsableFormationResult = new ArrayList<>();
 for (Identite identite : identiteResult) {
 responsableFormationResult.add(new RespFormation(identite));
 }

 assertEquals("Size of the result list should be equal to 1", 1, responsableFormationResult.size());
 }
}