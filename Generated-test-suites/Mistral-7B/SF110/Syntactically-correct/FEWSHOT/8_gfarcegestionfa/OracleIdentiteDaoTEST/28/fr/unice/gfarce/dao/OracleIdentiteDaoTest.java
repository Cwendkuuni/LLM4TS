package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 dao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 em.close();
 emf.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 dao.insert(identite, "nomForm", new GregorianCalendar(2022, 1, 1).getTime());

 List<Identite> identites = em.createQuery("SELECT i FROM Identite i WHERE i.email = :email", Identite.class)
 .setParameter("email", "john.doe@example.com")
 .getResultList();
 assertEquals(1, identites.size());
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

 Identite existingIdentite = em.find(Identite.class, identite.getId());
 existingIdentite.setEmail("john.doe_updated@example.com");

 dao.update(identite);

 Identite updatedIdentite = em.find(Identite.class, identite.getId());
 assertEquals("john.doe_updated@example.com", updatedIdentite.getEmail());
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

 EntityManager transactionalEm = em.getTransactionalEntityManager();
 EntityTransaction tx = transactionalEm.getTransaction();
 tx.begin();
 Identite existingIdentite = transactionalEm.find(Identite.class, identite.getId());
 dao.delete(existingIdentite);
 tx.commit();

 List<Identite> identites = em.createQuery("SELECT i FROM Identite i WHERE i.email = :email", Identite.class)
 .setParameter("email", "john.doe@example.com")
 .getResultList();
 assertEquals(0, identites.size());
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = dao.find(nom, prenom, sex, email, type);
 assertTrue(identites != null && identites.size() > 0);
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Candidat[] candidaats = dao.findCandidat(nom, prenom, sex, email);
 assertTrue(candidaats != null && candidaats.length > 0);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = dao.findRespForm(nom, prenom, sex, email);
 assertNull(respFormations);
 }
}