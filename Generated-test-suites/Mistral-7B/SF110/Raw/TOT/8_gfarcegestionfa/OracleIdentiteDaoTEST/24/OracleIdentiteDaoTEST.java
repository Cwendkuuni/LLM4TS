package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private EntityManagerFactory emf;
 private EntityManager em;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "NomForm", new GregorianCalendar(2022, 11, 25));
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 Assert.assertEquals(identite.getNom(), foundIdentite.getNom());
 Assert.assertEquals(identite.getEmail(), foundIdentite.getEmail());
 Assert.assertEquals(identite.getType(), foundIdentite.getType());
 }

 @Test
 public void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.update(identite);
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 Assert.assertEquals(identite.getNom(), foundIdentite.getNom());
 Assert.assertEquals(identite.getEmail(), foundIdentite.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullIdentite() {
 identiteDao.update(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullId() {
 Identite identite = new Identite();
 identiteDao.update(identite);
 }

 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identiteDao.delete(identite);
 Identite foundIdentite = em.find(Identite.class, identite.getId());
 Assert.assertNull(foundIdentite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullIdentiteToDelete() {
 identiteDao.delete(null);
 }

 @Test
 public void testFindIdentites() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 Assert.assertNotNull(identites);
 Assert.assertEquals(identites.size(), 0);

 Identite identite1 = new Identite();
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "NomForm1", new GregorianCalendar(2022, 11, 25));

 Identite identite2 = new Identite();
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite2, "NomForm2", new GregorianCalendar(2022, 11, 25));

 List<Identite> identites2 = identiteDao.find(null, null, null, null, null);
 Assert.assertNotNull(identites2);
 Assert.assertEquals(identites2.size(), 2);

 Identite foundIdentite1 = identites2.get(0);
 Assert.assertEquals(identite1.getNom(), foundIdentite1.getNom());
 Assert.assertEquals(identite1.getEmail(), foundIdentite1.getEmail());

 Identite foundIdentite2 = identites2.get(1);
 Assert.assertEquals(identite2.getNom(), foundIdentite2.getNom());
 Assert.assertEquals(identite2.getEmail(), foundIdentite2.getEmail());
 }

 @Test
 public void testFindCandidats() {
 List<Candidat> candidats = identiteDao.findCandidat("Doe", "John", "M", "john.doe@example.com");
 Assert.assertArrayEquals(new Candidat[0], candidats);

 Identite identite = new Identite();identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "NomForm1", new GregorianCalendar(2022, 11, 25));

 Candidat candidat = new Candidat();
 candidat.setNom("Doe");
 candidat.setPrenom("John");
 candidat.setEmail("john.doe@example.com");
 candidat.setSex("M");

 identiteDao.insert(candidat, "NomForm2", new GregorianCalendar(2022, 11, 25));

 List<Candidat> candidats2 = identiteDao.findCandidat("Doe", "John", "M", "john.doe@example.com");
 Assert.assertArrayEquals(new Candidat[1], candidats2);
 Assert.assertEquals(candidats2[0].getNom(), candidat.getNom());
 Assert.assertEquals(candidats2[0].getPrenom(), candidat.getPrenom());
 }

 @Test
 public void testFindResponsablesFormations() {
 List<RespFormation> responsablesFormations = identiteDao.findRespForm("Doe", "John", "M", "john.doe@example.com");
 Assert.assertArrayEquals(new RespFormation[0], responsablesFormations);

 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite, "NomForm1", new GregorianCalendar(2022, 11, 25));

 RespFormation responsableFormation = new RespFormation();
 responsableFormation.setNom("Doe");
 responsableFormation.setPrenom("John");
 responsableFormation.setEmail("john.doe@example.com");
 responsableFormation.setSex("M");

 identiteDao.insert(responsableFormation, "NomForm2", new GregorianCalendar(2022, 11, 25));

 List<RespFormation> responsablesFormations2 = identiteDao.findRespForm("Doe", "John", "M", "john.doe@example.com");
 Assert.assertArrayEquals(new RespFormation[1], responsablesFormations2);
 Assert.assertEquals(responsablesFormations2[0].getNom(), responsableFormation.getNom());
 Assert.assertEquals(responsablesFormations2[0].getPrenom(), responsableFormation.getPrenom());
 }
}