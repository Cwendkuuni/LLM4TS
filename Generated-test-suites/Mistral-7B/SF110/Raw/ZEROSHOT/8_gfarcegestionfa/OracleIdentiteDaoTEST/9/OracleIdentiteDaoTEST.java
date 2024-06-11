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

 private IdentiteDao identiteDao;
 static EntityManagerFactory emf;
 static EntityManager em;
 static EntityTransaction tx;

 @BeforeClass
 public static void setUpBeforeClass() {
 emf = Persistence.createEntityManagerFactory("Identite");
 }

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 em = emf.createEntityManager();
 tx = em.getTransaction();
 tx.begin();
 }

 @After
 public void tearDown() {
 tx.rollback();
 em.close();
 }

 @AfterClass
 public static void tearDownAfterClass() {
 emf.close();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, null, null);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals("John", foundIdentite.getNom());
 assertEquals("Doe", foundIdentite.getPrenom());
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 assertEquals("M", foundIdentite.getSex());
 assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite.getType());
 }

 @Test
 public void testUpdate() {
 Identite identite = em.find(Identite.class, 1L);

 identite.setEmail("new.email@example.com");
 identite.setNom("NewName");
 identite.setPrenom("NewLastName");
 identite.setSex("F");
 identite.setType(Identite.TypeIdentite.FORMATEUR);

 identiteDao.update(identite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertEquals("NewName", foundIdentite.getNom());
 assertEquals("NewLastName", foundIdentite.getPrenom());
 assertEquals("new.email@example.com", foundIdentite.getEmail());
 assertEquals("F", foundIdentite.getSex());
 assertEquals(Identite.TypeIdentite.FORMATEUR, foundIdentite.getType());
 }

 @Test
 public void testDelete() {
 Identite identite = em.find(Identite.class, 1L);
 identiteDao.delete(identite);

 Identite foundIdentite = em.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 }

 @Test
 public void testFind() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identites.size());

 Identite identite1 = new Identite();
 identite1.setNom("Test1");
 identite1.setPrenom("Test1");
 identite1.setEmail("test1@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, null, null);

 Identite identite2 = new Identite();
 identite2.setNom("Test2");
 identite2.setPrenom("Test2");
 identite2.setEmail("test2@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, null, null);

 List<Identite> identites2 = identiteDao.find(null, null, null, null, null);
 assertEquals(2, identites2.size());

 List<Identite> identites3 = identiteDao.find("Test1", null, null, "test1@example.com", null);
 assertEquals(1, identites3.size());
 assertEquals("Test1", identites3.get(0).getNom());
 assertEquals("Test1", identites3.get(0).getPrenom());
 assertEquals("test1@example.com", identites3.get(0).getEmail());

 List<Identite> identites4 = identiteDao.find(null, null, null, "test2@example.com", Identite.TypeIdentite.FORMATEUR);
 assertEquals(1, identites4.size());
 assertEquals("Test2", identites4.get(0).getNom());
 assertEquals("Test2", identites4.get(0).getPrenom());
 assertEquals("test2@example.com", identites4.get(0).getEmail());
 }

 @Test
 public void testFindCandidat() {
 Candidat[] candidats = identiteDao.findCandidat(null, null, null, null);
 assertEquals(0, candidats.length);

 Identite identite1 = new Identite();
 identite1.setNom("Test1");
 identite1.setPrenom("Test1");
 identite1.setEmail("test1@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, null, null);

 Candidat[] candidats2 = identiteDao.findCandidat(null, null, null, "test1@example.com");
 assertEquals(1, candidats2.length);
 assertEquals("Test1", candidats2[0].getNom());
 assertEquals("Test1", candidats2[0].getPrenom());
 assertEquals("test1@example.com", candidats2[0].getEmail());
 }

 @Test
 public void testFindRespForm() {
 RespFormation[] respFormations = identiteDao.findRespForm(null, null, null, null);
 assertEquals(0, respFormations.length);

 Identite identite2 = new Identite();
 identite2.setNom("Test2");
 identite2.setPrenom("Test2");
 identite2.setEmail("test2@example.com");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, null, null);

 RespFormation[] respFormations2 = identiteDao.findRespForm(null, null, null, "test2@example.com");
 assertEquals(1, respFormations2.length);
 assertEquals("Test2", respFormations2[0].getNom());
 assertEquals("Test2", respFormations2[0].getPrenom());
 assertEquals("test2@example.com", respFormations2[0].getEmail());
 }
}