package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.junit.runner.*;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

@RunWith(JUnit4.class)
public class OracleIdentiteDaoTest {

 private static IdentiteDao identiteDao;
 private static EntityManagerFactory emf;

 @BeforeClass
 public static void setUpClass() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 }

 @AfterClass
 public static void tearDownClass() {
 if (emf != null) {
 emf.close();
 }
 }

 @Before
 public void setUp() {
 EntityManager em = emf.createEntityManager();
 em.getTransaction().begin();
 // Add your set up code here
 em.getTransaction().commit();
 em.close();
 }

 @After
 public void tearDown() {
 EntityManager em = emf.createEntityManager();
 em.getTransaction().begin();
 // Add your tear down code here
 em.getTransaction().rollback();
 em.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "NomForm", new GregorianCalendar(2022, Calendar.JANUARY, 1));

 EntityManager em = emf.createEntityManager();
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
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

 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 Identite existingIdentite = em.find(Identite.class, identite.getId());
 existingIdentite.setEmail("new.email@example.com");
 identiteDao.update(identite);
 tx.commit();
 em.close();

 EntityManager em2 = emf.createEntityManager();
 Identite foundIdentite = em2.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals("new.email@example.com", foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 em2.close();
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

 EntityManager em = emf.createEntityManager();
 EntityTransaction tx = em.getTransaction();
 tx.begin();
 Identite existingIdentite = em.find(Identite.class, identite.getId());
 identiteDao.delete(identite);
 tx.commit();
 em.close();

 EntityManager em2 = emf.createEntityManager();
 Identite foundIdentite = em2.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 em2.close();
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertNotNull(identites);
 assertEquals(identites.size(), 1);
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
 assertNotNull(candidats);
 assertEquals(candidats.length, 1);
 Candidat candidat = candidats[0];
 assertNotNull(candidat);
 assertEquals(nom, candidat.getNom());
 assertEquals(prenom, candidat.getPrenom());
 assertEquals(sex, candidat.getSex());
 assertEquals(email, candidat.getEmail());
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(respFormations);
 assertEquals(respFormations.length, 0);
 }
}