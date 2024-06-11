package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 this.emf = Persistence.createEntityManagerFactory("Identite");
 this.em = this.emf.createEntityManager();
 this.dao = OracleIdentiteDao.getDAO();
 }

 @After
 public void tearDown() {
 if (this.em != null) {
 this.em.close();
 }
 if (this.emf != null) {
 this.emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 this.dao.insert(identite, "nomForm", null);

 Identite fetchedIdentite = this.em.find(Identite.class, identite.getId());
 assertEquals("John Doe", fetchedIdentite.getNom());
 assertEquals("test@test.com", fetchedIdentite.getEmail());
 }

 @Test
 public void testInsertWithException() {
 Identite identite = new Identite();
 identite.setEmail("test@test.com");
 identite.setNom("John");
 identite.setPrenom("Doe");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 try {
 this.dao.insert(identite, "nomForm", null);
 fail("An exception should have been thrown");
 }
 catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testUpdate() {
 Identite identite = this.em.find(Identite.class, 1L);
 identite.setEmail("newEmail@newEmail.com");
 identite.setNom("NewJohn");
 identite.setPrenom("NewDoe");
 identite.setSex('F');
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 this.dao.update(identite);

 Identite fetchedIdentite = this.em.find(Identite.class, identite.getId());
 assertEquals("NewJohn", fetchedIdentite.getNom());
 assertEquals("newEmail@newEmail.com", fetchedIdentite.getEmail());
 }

 @Test
 public void testUpdateWithException() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("newEmail@newEmail.com");
 identite.setNom("NewJohn");
 identite.setPrenom("NewDoe");
 identite.setSex('F');
 identite.setType(Identite.TypeIdentite.FORMATEUR);

 try {
 this.dao.update(identite);
 fail("An exception should have been thrown");
 }
 catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testDelete() {
 Identite identite = this.em.find(Identite.class, 1L);
 this.dao.delete(identite);

 assertNull(this.em.find(Identite.class, identite.getId()));
 }

 @Test
 public void testDeleteWithException() {
 Identite identite = new Identite();
 identite.setId(1L);

 try {
 this.dao.delete(identite);
 fail("An exception should have been thrown");
 }
 catch (Exception e) {
 // expected
 }
}

 @Test
 public void testFind() {
 List<Identite> identites = this.dao.find(null, null, null, null, null);
 assertEquals(5, identites.size());

 Identite identite1 = this.em.find(Identite.class, 1L);
 List<Identite> identites1 = this.dao.find(identite1.getNom(), null, null, null, null);
 assertEquals(1, identites1.size());
 assertEquals(identite1, identites1.get(0));

 Identite identite2 = this.em.find(Identite.class, 2L);
 List<Identite> identites2 = this.dao.find(null, identite2.getPrenom(), null, null, null);
 assertEquals(1, identites2.size());
 assertEquals(identite2, identites2.get(0));

 Identite identite3 = this.em.find(Identite.class, 3L);
 List<Identite> identites3 = this.dao.find(null, null, identite3.getSex().toString(), null, null);
 assertEquals(1, identites3.size());
 assertEquals(identite3, identites3.get(0));

 Identite identite4 = this.em.find(Identite.class, 4L);
 List<Identite> identites4 = this.dao.find(null, null, null, identite4.getType().toString(), null);
 assertEquals(1, identites4.size());
 assertEquals(identite4, identites4.get(0));

 Identite identite5 = this.em.find(Identite.class, 5L);
 List<Identite> identites5 = this.dao.find(identite5.getNom(), identite5.getPrenom(), identite5.getSex().toString(), identite5.getType().toString(), null);
 assertEquals(1, identites5.size());
 assertEquals(identite5, identites5.get(0));
}

 @Test
 public void testFindWithNullParams() {
 List<Identite> identites = this.dao.find(null, null, null, null, null);
 assertEquals(5, identites.size());
}

 @Test
 public void testFindCandidat() {
 Candidat[] candidats = this.dao.findCandidat("Doe", null, null);
 assertEquals(1, candidats.length);
 assertTrue(candidats[0] instanceof Candidat);
 }

 @Test
 public void testFindCandidatWithNullParams() {
 Candidat[] candidats = this.dao.findCandidat(null, null, null);
 assertEquals(0, candidats.length);
 }

 @Test
 public void testFindRespForm() {
 RespFormation[] respFormations = this.dao.findRespForm("Doe", null, null);
 assertEquals(1, respFormations.length);
 assertTrue(respFormations[0] instanceof RespFormation);
 }

 @Test
 public void testFindRespFormWithNullParams() {
 RespFormation[] respFormations = this.dao.findRespForm(null, null, null);
 assertEquals(0, respFormations.length);
 }
}