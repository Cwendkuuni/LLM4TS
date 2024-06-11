package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.RollbackException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManager em;
 private IdentiteDao identiteDao;
 private PersistenceUnitManager pum;

 @PersistenceContext
 public EntityManager getEntityManager() {
 return em;
 }

 @PersistenceUnit
 public void setEntityManagerFactory(EntityManagerFactory emf) {
 this.em = emf.createEntityManager();
 this.identiteDao = OracleIdentiteDao.getDAO();
 this.pum = emf.getPersistenceUnitManager();
 }

 @Before
 public void setUp() {
 pum.getPersistenceUnitInfo("Identite").getProperties().put("hibernate.hbm2ddl.auto", "create");
 setEntityManagerFactory(Persistence.createEntityManagerFactory("Identite"));
 }

 @After
 public void tearDown() {
 if (em != null) {
 try {
 em.getTransaction().rollback();
 } catch (RollbackException e) {
 e.printStackTrace();
 }
 em.close();
 }
 if (pum != null) {
 pum.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Dupond");
 identite.setPrenom("Jean");
 identite.setEmail("jean.dupond@unice.fr");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "TestForm", null);
 Identite identiteFromDB = em.find(Identite.class, identite.getId());
 assertEquals(identite, identiteFromDB);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Dupond");
 identite.setPrenom("Jean");
 identite.setEmail("jean.dupond@unice.fr");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite identiteFromDB = em.find(Identite.class, identite.getId());
 identiteFromDB.setEmail("jean.dupond@gmail.com");

 identiteDao.update(identiteFromDB);
 Identite identiteUpdated = em.find(Identite.class, identite.getId());
 assertEquals(identiteFromDB.getEmail(), identiteUpdated.getEmail());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Dupond");
 identite.setPrenom("Jean");
 identite.setEmail("jean.dupond@unice.fr");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.delete(identite);

 assertEquals(0, em.createQuery("SELECT COUNT(i) FROM Identite i WHERE i.id = :id", identite.getId()).getResultList().size());
 }

 @Test
 public void testFind() {
 String nom = "Dupond";
 String prenom = "Jean";
 String sex = "M";
 String email = "jean.dupond@unice.fr";

 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setEmail(email);
 identite.setSex(sex);
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "TestForm", null);

 Identite identiteFromDB = em.find(Identite.class, identite.getId());
 assertNotNull(identiteFromDB);

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);
 assertTrue(identites.contains(identiteFromDB));
 }

 @Test
 public void testFindCandidat() {
 String nom = "Dupond";
 String prenom = "Jean";
 String sex = "M";
 String email = "jean.dupond@unice.fr";

 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setEmail(email);
 identite.setSex(sex);
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "TestForm", null);

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertTrue(candidats.length > 0);
 }

 @Test
 public void testFindRespForm() {
 String nom = "Dupond";
 String prenom = "Jean";
 String sex = "F";
 String email = "jean.dupond@unice.fr";

 Identite identite = new Identite();
 identite.setNom(nom);
 identite.setPrenom(prenom);
 identite.setEmail(email);
 identite.setSex(sex);
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite, "TestForm", null);

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertTrue(respFormations.length > 0);
 }
}