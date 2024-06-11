package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private EntityManagerFactory emf;

 @org.junit.Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 }

 @org.junit.After
 public void tearDown() {
 if (identiteDao != null) {
 identiteDao = null;
 }
 if (emf != null) {
 emf.close();
 emf = null;
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new Date());

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite, foundIdentite);
 em.close();
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

 Identite existingIdentite = new Identite();
 existingIdentite.setId(identite.getId());
 existingIdentite.setEmail("john.doe@oldemail.com");
 emf.getTransaction().begin();
 identiteDao.update(identite);
 emf.getTransaction().commit();

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals(identite, foundIdentite);
 em.close();
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "nomForm", new Date());

 Long id = identite.getId();
 identiteDao.delete(identite);

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, id);
 assertNull(foundIdentite);
 em.close();
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertEquals(1, identites.size());
 Identite identite = identites.get(0);
 assertEquals(nom, identite.getNom());
 assertEquals(prenom, identite.getPrenom());
 assertEquals(sex, identite.getSex());
 assertEquals(email, identite.getEmail());
 assertEquals(type, identite.getType());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertEquals(1, candidats.length);
 Candidat candidat = candidats[0];
 assertNotNull(candidat.getIdentite());
 assertEquals(nom, candidat.getIdentite().getNom());
 assertEquals(prenom, candidat.getIdentite().getPrenom());
 assertEquals(sex, candidat.getIdentite().getSex());
 assertEquals(email, candidat.getIdentite().getEmail());
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(new RespFormation[0], respFormations);
 }

}