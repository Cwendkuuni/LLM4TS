package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;

 @org.junit.Before
 public void setUp() {
 dao = new OracleIdentiteDaoImpl();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 Calendar calendar = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "Form", calendar.getTime());

 EntityManager em = Mockito.mock(EntityManager.class);
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 EntityTransaction tx = Mockito.mock(EntityTransaction.class);

 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(em.persist(identite)).thenReturn(identite);
 when(em.find(Identite.class, identite.getId())).thenReturn(identite);

 OracleIdentiteDao oracleDao = Mockito.spy(dao);
 oracleDao.insert(identite, "Form", calendar.getTime());

 assertEquals(identite, oracleDao.getDAO().find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0));
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateCreation(new Date());

 dao.update(identite);

 EntityManager em = Mockito.mock(EntityManager.class);
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 Identite i = Mockito.mock(Identite.class);

 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(em.find(Identite.class, identite.getId())).thenReturn(i);
 when(i.getEmail()).thenReturn(identite.getEmail());
 when(i.getNom()).thenReturn(identite.getNom());
 when(i.getPrenom()).thenReturn(identite.getPrenom());
 when(i.getSex()).thenReturn(identite.getSex());
 when(i.getType()).thenReturn(identite.getType());
 when(em.merge(identite)).thenReturn(identite);

 OracleIdentiteDao oracleDao = Mockito.spy(dao);
 oracleDao.update(identite);

 assertEquals(identite, oracleDao.getDAO().find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0));
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateCreation(new Date());

 dao.delete(identite);

 EntityManager em = Mockito.mock(EntityManager.class);
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 Identite ident = Mockito.mock(Identite.class);

 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(em.find(Identite.class, identite.getId())).thenReturn(ident);
 when(em.remove(ident)).thenReturn(ident);

 OracleIdentiteDao oracleDao = Mockito.spy(dao);
 oracleDao.delete(identite);

 List<Identite> identites = oracleDao.getDAO().find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setDateCreation(new Date());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe@example.com");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identite2.setDateCreation(new Date());

 Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe3");
 identite3.setPrenom("John3");
 identite3.setSex("F");
 identite3.setEmail("john3.doe@example.com");
 identite3.setType(Identite.TypeIdentite.FORMATEUR);
 identite3.setDateCreation(new Date());

 List<Identite> expectedIdentites = Arrays.asList(identite1, identite2, identite3);

 EntityManager em = Mockito.mock(EntityManager.class);
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 List<Identite> resultIdentites = Mockito.mock(List.class);

 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(em.createQuery("select ident from Identite as ident where ident.nom like '%Doe%' and ident.prenom like '%John%' and ident.sex like '%M%' and ident.type= :type")).thenReturn(resultIdentites);
 when(resultIdentites.size()).thenReturn(3);
 when(resultIdentites.get(0)).thenReturn(identite1);
 when(resultIdentites.get(1)).thenReturn(identite2);
 when(resultIdentites.get(2)).thenReturn(identite3);

 OracleIdentiteDao oracleDao = Mockito.spy(dao);
 List<Identite> identites = oracleDao.getDAO().find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);

 assertEquals(expectedIdentites, identites);
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateCreation(new Date());

 Candidat candidat = new Candidat();
 candidat.setId(1L);
 candidat.setIdentite(identite);
 candidat.setFormation("Formation1");

 List<Candidat> expectedCandidats = Arrays.asList(candidat);

 EntityManager em = Mockito.mock(EntityManager.class);
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 List<Identite> identites = Mockito.mock(List.class);
 List<Candidat> resultCandidats = Mockito.mock(List.class);

 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(em.createQuery("select i from Identite as i where i.nom like '%Doe%' and i.prenom like '%John%' and i.sex like '%M%' and i.type= :type")).thenReturn(resultIdentites);
 when(resultIdentites.size()).thenReturn(1);
 when(resultIdentites.get(0)).thenReturn(identite);
 when(em.createQuery("select c from Candidat as c where c.identite= :identite")).thenReturn(resultCandidats);
 when(resultCandidats.size()).thenReturn(1);
 when(resultCandidats.get(0)).thenReturn(candidat);

 OracleIdentiteDao oracleDao = Mockito.spy(dao);
 List<Candidat> candidats = oracleDao.getDAO().findCandidat("Doe", "John", null, null);

 assertEquals(expectedCandidats, candidats);
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identite.setDateCreation(new Date());

 RespFormation respFormation = new RespFormation();
 respFormation.setId(1L);
 respFormation.setIdentite(identite);
 respFormation.setFormation("Formation1");

 List<RespFormation> expectedRespFormations = Arrays.asList(respFormation);

 EntityManager em = Mockito.mock(EntityManager.class);
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 EntityTransaction tx = Mockito.mock(EntityTransaction.class);
 List<Identite> identites = Mockito.mock(List.class);
 List<RespFormation> resultRespFormations = Mockito.mock(List.class);

 when(emf.createEntityManager()).thenReturn(em);
 when(em.getTransaction()).thenReturn(tx);
 when(em.createQuery("select i from Identite as i where i.nom like '%Doe%' and i.prenom like '%John%' and i.sex like '%M%' and i.type= :type")).thenReturn(identites);
 when(identites.size()).thenReturn(1);
 when(identites.get(0)).thenReturn(identite);
 when(em.createQuery("select r from RespFormation as r where r.identite= :identite")).thenReturn(resultRespFormations);
 when(resultRespFormations.size()).thenReturn(1);
 when(resultRespFormations.get(0)).thenReturn(respFormation);

 OracleIdentiteDao oracleDao = Mockito.spy(dao);
 List<RespFormation> respFormations = oracleDao.getDAO().findRespForm("Doe", "John", null, null);

 assertEquals(expectedRespFormations, respFormations);
 }

}