package fr.unice.gfarce.dao.test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 dao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Calendar calendar = Calendar.getInstance();
 identite.setDateNaissance(calendar.getTime());

 dao.insert(identite, "NomForm", calendar);

 Identite foundIdentite = dao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
 assertEquals(identite.getId(), foundIdentite.getId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullIdentite() {
 dao.insert(null, "NomForm", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullNomForm() {
 dao.insert(new Identite(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertIdentiteNullCalendar() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 dao.insert(identite, "NomForm", null);
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
 identite.setDateNaissance(Calendar.getInstance().getTime());

 dao.update(identite);

 Identite foundIdentite = dao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
 assertEquals(identite.getId(), foundIdentite.getId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNullIdentite() {
 dao.update(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateIdentiteNullId() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateNaissance(Calendar.getInstance().getTime());

 dao.update(identite);
 }

 @Test
 public void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateNaissance(Calendar.getInstance().getTime());

 dao.delete(identite);

 List<Identite> identites = dao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
 assertEmpty(identites);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteIdentiteNullIdentite() {
 dao.delete(null);
 }

 @Test
 public void testFindIdentites() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 List<Identite> identites = dao.find(nom, prenom, sex, email, null);
 assertNotNull(identites);
 assertEquals(1, identites.size());
 }

 @Test
 public void testFindCandidats() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = dao.findCandidat(nom, prenom, sex, email);
 assertNotNull(candidats);
 assertEquals(1, candidats.length);
 }

 @Test
 public void testFindRespFormations() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = dao.findRespForm(nom, prenom, sex, email);
 assertArrayEquals(new RespFormation[0], respFormations);
 }

 @Test
 public void testFindAllIdentites() {
 List<Identite> identites = dao.find(null, null, null, null, null);
 assertNotNull(identites);
 }

 @After
 public void tearDown() {
 // Clear the test database
 dao.deleteAll();
 }
}