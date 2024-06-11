package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao = OracleIdentiteDao.getDAO();

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Calendar calendar = new GregorianCalendar(2022, 11, 15);
 identite.setDateLimite(calendar.getTime());

 identiteDao.insert(identite, "test", calendar);

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertNullIdentite() {
 identiteDao.insert(null, "test", new GregorianCalendar(2022, 11, 15).getTime());
 fail("Expected IllegalStateException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 identiteDao.insert(new Identite(), null, new GregorianCalendar(2022, 11, 15).getTime());
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDateLimite() {
 identiteDao.insert(new Identite(), "test", null);
 fail("Expected IllegalArgumentException to be thrown");
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
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 15).getTime());

 Calendar newCalendar = new GregorianCalendar(2022, 12, 15);
 identite.setEmail("new.email@example.com");
 identite.setDateLimite(newCalendar.getTime());
 identiteDao.update(identite);

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("new.email@example.com", foundIdentite.getEmail());
 assertEquals(newCalendar.getTime(), foundIdentite.getDateLimite());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = new Identite();
 identiteDao.update(identite);
 fail("Expected IllegalArgumentException to be thrown");
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
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 15).getTime());

 identiteDao.delete(identite);

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 assertEquals(0, identites.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 Identite identite = new Identite();
 identiteDao.delete(identite);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe1@example.com");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "test", new GregorianCalendar(2022, 11, 15).getTime());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe2@example.com");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, "test", new GregorianCalendar(2022, 11, 15).getTime());

 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(2, identites.size());

 List<Identite> candidats = identiteDao.findCandidat(null, null, null, "john1.doe1@example.com");
 assertEquals(1, candidats.length);
 assertEquals("Doe1", candidats[0].getNom());

 List<Identite> formateurs = identiteDao.findRespForm(null, null, null, "john2.doe2@example.com");
 assertEquals(1, formateurs.length);
 assertEquals("Doe2", formateurs[0].getNom());
 }

 @Test
 public void testFindNullSearchCriteria() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindEmptySearchCriteria() {
 List<Identite> identites = identiteDao.find("", "", "", "", null);
 assertEquals(0, identites.size());
 }

 @Test
 public void testFindWithSearchCriteria() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 15).getTime());

 List<Identite> identites = identiteDao.find("Doe", "John", null, null, null);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 }

 @Test
 public void testFindWithEmailSearchCriteria() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('M');
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 15).getTime());

 List<Identite> identites = identiteDao.find(null, null, null, "john.doe@example.com", null);
 assertEquals(1, identites.size());
 Identite foundIdentite = identites.get(0);
 assertEquals("Doe", foundIdentite.getNom());
 assertEquals("John", foundIdentite.getPrenom());
 assertEquals("john.doe@example.com", foundIdentite.getEmail());
 }

 @Test
 public void testFindWithTypeSearchCriteria() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe1@example.com");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "test", new GregorianCalendar(2022, 11, 15).getTime());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe2@example.com");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, "test", new GregorianCalendar(2022, 11, 15).getTime());

 List<Identite> candidats = identiteDao.findCandidat("Doe", "John", null, null, null);
 assertEquals(1, candidats.length);
 Identite foundIdentite = candidats[0];
 assertEquals("Doe1", foundIdentite.getNom());

 List<Identite> formateurs = identiteDao.findRespForm("Doe", "John", null, null, null);
 assertEquals(0, formateurs.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullTypeSearchCriteria() {
 List<Identite> identites = identiteDao.find(null, null, null, null, null);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCandidatNullIdentiteArray() {
 Identite[] candidats = null;
 assertEquals(0, candidates.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRespFormNullIdentiteArray() {
 Identite[] formateurs = null;
 assertEquals(0, formateurs.length);
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
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 15).getTime());

 Candidat[] candidats = identiteDao.findCandidat("Doe", "John", null, null);
 assertEquals(1, candidats.length);
 Candidat foundCandidat = candidats[0];
 assertEquals("Doe", foundCandidat.getNom());
 assertEquals("John", foundCandidat.getPrenom());
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(2L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex('F');
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite, "test", new GregorianCalendar(2022, 11, 15).getTime());

 RespFormation[] formateurs = identiteDao.findRespForm("Doe", "John", null, null);
 assertEquals(1, formateurs.length);
 RespFormation foundFormateur = formateurs[0];
 assertEquals("Doe", foundFormateur.getNom());
 }

 @Test
 public void testFindAll() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe1@example.com");
 identite1.setSex('M');
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identiteDao.insert(identite1, "test", new GregorianCalendar(2022, 11, 15).getTime());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setEmail("john2.doe2@example.com");
 identite2.setSex('F');
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 identiteDao.insert(identite2, "test", new GregorianCalendar(2022, 11, 15).getTime());

 List<Identite> identites = identiteDao.findAll();
 assertEquals(2, identites.size());
 }

}