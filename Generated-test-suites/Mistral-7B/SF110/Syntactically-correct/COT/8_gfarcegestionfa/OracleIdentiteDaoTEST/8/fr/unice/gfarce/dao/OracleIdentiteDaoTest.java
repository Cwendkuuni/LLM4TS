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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 emf = Persistence.createEntityManagerFactory("Identite");
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "testForm", dateLimite);

 EntityManager em1 = emf.createEntityManager();
 Identite foundIdentite = em1.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getDateLimite(), foundIdentite.getDateLimite());

 em1.close();
 }

 @Test(expected = Exception.class)
 public void testInsertException() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "testForm", dateLimite);
 identiteDao.insert(identite, "testForm", dateLimite); // second call should throw an exception
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.update(identite);

 EntityManager em1 = emf.createEntityManager();
 Identite foundIdentite = em1.find(Identite.class, identite.getId());
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getDateLimite(), foundIdentite.getDateLimite());

 em1.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 identiteDao.update(null);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 identiteDao.insert(identite, "testForm", dateLimite);
 identiteDao.delete(identite);

 EntityManager em1 = emf.createEntityManager();
 Identite foundIdentite = em1.find(Identite.class, identite.getId());
 assertNull(foundIdentite);

 em1.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 identiteDao.delete(null);
 }

 @Test
 public void testFindNullParam() {
 List<Identite> result = identiteDao.find(null, null, null, null, null);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testFindEmptyParam() {
 String nom = "";
 String prenom = "";
 String sex = "";
 String email = "";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
 List<Identite> result = identiteDao.find(nom, prenom, sex, email, type);
 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testFindOneResult() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManager em1 = emf.createEntityManager();
 em1.getTransaction().begin();
 em1.persist(identite);
 em1.getTransaction().commit();
 em1.close();

 List<Identite> result = identiteDao.find("Doe", "John", null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, result.size());
 Identite foundIdentite = result.get(0);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getDateLimite(), foundIdentite.getDateLimite());
 }

 @Test
 public void testFindMultipleResults() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = Calendar.getInstance();
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex('M');
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar2 = Calendar.getInstance();
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);

 EntityManager em1 = emf.createEntityManager();
 em1.getTransaction().begin();
 em1.persist(identite1);
 em1.persist(identite2);
 em1.getTransaction().commit();
 em1.close();

 List<Identite> result = identiteDao.find("Doe", "John", null, null, Identite.TypeIdentite.CANDIDAT);
 assertEquals(2, result.size());
 Identite foundIdentite1 = result.get(0);
 assertEquals(identite1.getNom(), foundIdentite1.getNom());
 assertEquals(identite1.getPrenom(), foundIdentite1.getPrenom());
 assertEquals(identite1.getEmail(), foundIdentite1.getEmail());
 assertEquals(identite1.getSex(), foundIdentite1.getSex());
 assertEquals(identite1.getType(), foundIdentite1.getType());
 assertEquals(identite1.getDateLimite(), foundIdentite1.getDateLimite());

 Identite foundIdentite2 = result.get(1);
 assertEquals(identite2.getNom(), foundIdentite2.getNom());
 assertEquals(identite2.getPrenom(), foundIdentite2.getPrenom());
 assertEquals(identite2.getEmail(), foundIdentite2.getEmail());
 assertEquals(identite2.getSex(), foundIdentite2.getSex());
 assertEquals(identite2.getType(), foundIdentite2.getType());
 assertEquals(identite2.getDateLimite(), foundIdentite2.getDateLimite());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullType() {
 identiteDao.find(null, null, null, null, null);
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManager em1 = emf.createEntityManager();
 em1.getTransaction().begin();
 em1.persist(identite);
 em1.getTransaction().commit();
 em1.close();

 Candidat[] result = identiteDao.findCandidat("Doe", "John", null, "john.doe@example.com");
 assertEquals(1, result.length);
 Candidat foundCandidat = result[0];
 assertEquals(identite.getNom(), foundCandidat.getNom());
 assertEquals(identite.getPrenom(), foundCandidat.getPrenom());
 assertNotNull(foundCandidat.getCv());
 assertNotNull(foundCandidat.getDiplome());
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('F');
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityManager em1 = emf.createEntityManager();
 em1.getTransaction().begin();
 em1.persist(identite);
 Identite respForm = new Identite();
 respForm.setNom("Responsable Formation");
 respForm.setPrenom("John");
 respForm.setEmail("john.responsableformation@example.com");
 respForm.setSex('F');
 respForm.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = Calendar.getInstance();
 Date dateLimite2 = calendar2.getTime();
 respForm.setDateLimite(dateLimite2);
 em1.persist(respForm);
 em1.getTransaction().commit();
 em1.close();

 RespFormation[] result = identiteDao.findRespForm("Doe", "John", null, "john.doe@example.com");
 assertEquals(1, result.length);
 RespFormation foundRespForm = result[0];
 assertEquals(identite.getNom(), foundRespForm.getNom());
 assertEquals(identite.getPrenom(), foundRespForm.getPrenom());
 assertEquals(respForm.getNom(), foundRespForm.getResponsableFormation().getNom());
 assertNotNull(foundRespForm.getFormations());
 assertEquals(1, foundRespForm.getFormations().size());
 }

}