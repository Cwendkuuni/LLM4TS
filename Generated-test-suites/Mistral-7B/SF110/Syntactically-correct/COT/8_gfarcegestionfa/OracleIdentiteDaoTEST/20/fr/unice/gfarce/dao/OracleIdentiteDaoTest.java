package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManagerFactory emf;
 private EntityManager em;

 @Before
 public void setUp() {
 dao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, "Formulaire", dateLimite);

 List<Identite> identites = em.createQuery("FROM Identite WHERE nom = :nom AND prenom = :prenom AND sex = :sex AND type = :type AND dateLimite = :dateLimite", identite)
 .setParameter("nom", identite.getNom())
 .setParameter("prenom", identite.getPrenom())
 .setParameter("sex", identite.getSex())
 .setParameter("type", identite.getType())
 .setParameter("dateLimite", identite.getDateLimite())
 .getResultList();

 assertTrue(identites.size() > 0);
 }

 @Test(expected = Exception.class)
 public void testInsertException() {
 Identite identite = new Identite();
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 dao.insert(identite, "Formulaire", dateLimite);
 fail();
 } catch (Exception e) {
 tx.commit();
 } finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(1L);
 identiteToUpdate.setEmail("newemail@email.com");
 identiteToUpdate.setNom("UpdatedTest");
 identiteToUpdate.setPrenom("UpdatedTest");
 identiteToUpdate.setSex("M");
 identiteToUpdate.setType(Identite.TypeIdentite.CANDIDAT);
 identiteToUpdate.setDateLimite(dateLimite);

 dao.update(identiteToUpdate);

 Identite updatedIdentite = em.find(Identite.class, 1L);
 assertEquals("UpdatedTest", updatedIdentite.getNom());
 }

 @Test(expected = Exception.class)
 public void testUpdateException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Test");
 identite.setPrenom("Test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 dao.update(identite);
 fail();
 } catch (Exception e) {
 tx.rollback();
 } finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testDelete() {
 Identite identiteToDelete = new Identite();
 identiteToDelete.setId(1L);
 identiteToDelete.setNom("Test");
 identiteToDelete.setPrenom("Test");
 identiteToDelete.setSex("M");
 identiteToDelete.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteToDelete.setDateLimite(dateLimite);

 dao.delete(identiteToDelete);

 List<Identite> identites = em.createQuery("FROM Identite WHERE id = :id", identiteToDelete)
 .setParameter("id", identiteToDelete.getId())
 .getResultList();

 assertTrue(identites.isEmpty());
 }

 @Test(expected = Exception.class)
 public void testDeleteException() {
 Identite identiteToDelete = new Identite();
 identiteToDelete.setId(1L);
 identiteToDelete.setNom("Test");
 identiteToDelete.setPrenom("Test");
 identiteToDelete.setSex("M");
 identiteToDelete.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identiteToDelete.setDateLimite(dateLimite);

 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();

 try {
 dao.delete(new Identite());
 fail();
 } catch (Exception e) {
 tx.rollback();
 } finally {
 em.close();
 emf.close();
 }
 }

 @Test
 public void testFind() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 String email = "test@email.com";

 List<Identite> identites = dao.find(nom, prenom, sex, email, type);

 assertTrue(identites.size() > 0);
 }

 @Test
 public void testFindNullParam() {
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = dao.find(null, null, null, null, type);

 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindEmptyParam() {
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = dao.find("", "", "", "", type);

 assertTrue(identites.isEmpty());
 }

 @Test
 public void testFindMultipleIdentites() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 String email1 = "test1@email.com";
 String email2 = "test2@email.com";

 Identite identite1 = new Identite();
 identite1.setNom(nom);
 identite1.setPrenom(prenom);
 identite1.setSex(sex);
 identite1.setType(type);
 identite1.setEmail(email1);
 identite1.setDateLimite(new Date());
 em.persist(identite1);

 Identite identite2 = new Identite();
 identite2.setNom(nom);
 identite2.setPrenom(prenom);
 identite2.setSex(sex);
 identite2.setType(type);
 identite2.setEmail(email2);
 identite2.setDateLimite(new Date());
 em.persist(identite2);

 List<Identite> identites = dao.find(nom, prenom, sex, null, type);

 assertEquals(2, identites.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@email.com";

 Candidat[] candidats = dao.findCandidat(nom, prenom, sex, email);

 assertTrue(candidats.length > 0);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Test";
 String prenom = "Test";
 String sex = "M";
 String email = "test@email.com";

 RespFormation[] respFormations = dao.findRespForm(nom, prenom, sex, email);

 assertTrue(respFormations.length > 0);
 }

 @Test
 public void testFindCandidatNullParam() {
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 Candidat[] candidats = dao.findCandidat(null, null, null, null);

 assertTrue(candidats.length == 0);
 }

 @Test
 public void testFindRespFormNullParam() {
 Identite.TypeIdentite type = Identite.TypeIdentite.FORMATEUR;

 RespFormation[] respFormations = dao.findRespForm(null, null, null, null);

 assertTrue(respFormations.length == 0);
 }

 @After
 public void tearDown() {
 emf.close();
 }
}