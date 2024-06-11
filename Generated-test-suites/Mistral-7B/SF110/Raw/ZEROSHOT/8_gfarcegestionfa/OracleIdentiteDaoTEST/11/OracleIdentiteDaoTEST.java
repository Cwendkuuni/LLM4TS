package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;

 @Before
 public void setUp() {
 dao = OracleIdentiteDaoImpl.getDAO();
 }

 @After
 public void tearDown() {
 // Clear the database after each test
 List<Identite> identites = dao.find(null, null, null, null, null);
 for (Identite identite : identites) {
 dao.delete(identite);
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Identite expectedIdentite = identite;
 Calendar calendar = new GregorianCalendar(2022, 0, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, "TestForm", dateLimite);

 Identite actualIdentite = dao.findById(identite.getId());
 assertEquals(expectedIdentite.getNom(), actualIdentite.getNom());
 assertEquals(expectedIdentite.getPrenom(), actualIdentite.getPrenom());
 assertEquals(expectedIdentite.getSex(), actualIdentite.getSex());
 assertEquals(expectedIdentite.getType(), actualIdentite.getType());
 assertEquals(expectedIdentite.getDateLimite(), actualIdentite.getDateLimite());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 dao.insert(null, "TestForm", new Date());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullForm() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setDateLimite(new Date());

 dao.insert(identite, null, new Date());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Identite expectedIdentite = identite;
 Calendar calendar = new GregorianCalendar(2022, 0, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, "TestForm", dateLimite);
 Identite actualIdentite = dao.findById(identite.getId());

 expectedIdentite.setEmail("new@email.com");
 identite.setEmail("new@email.com");

 dao.update(identite);

 Identite updatedIdentite = dao.findById(identite.getId());
 assertEquals(expectedIdentite.getEmail(), updatedIdentite.getEmail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setEmail("new@email.com");

 dao.update(identite);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2022, 0, 1);
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, "TestForm", dateLimite);
 Identite actualIdentite = dao.findById(identite.getId());
 long id = actualIdentite.getId();

 dao.delete(identite);
 Identite deletedIdentite = dao.findById(id);
 assertNull(deletedIdentite);
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);
 dao.insert(identite1, "TestForm1", dateLimite1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar2 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);
 dao.insert(identite2, "TestForm2", dateLimite2);

 Identite identite3 = new Identite();
 identite3.setId(3L);
 identite3.setNom("Doe3");
 identite3.setPrenom("John3");
 identite3.setSex("M");
 identite3.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar3 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite3 = calendar3.getTime();
 identite3.setDateLimite(dateLimite3);
 dao.insert(identite3, "TestForm3", dateLimite3);

 List<Identite> expectedIdentites = new ArrayList<>();
 expectedIdentites.add(identite1);
 expectedIdentites.add(identite2);
 expectedIdentites.add(identite3);

 List<Identite> identites = dao.find(null, null, null, null, null);
 assertEquals(expectedIdentites.toString(), identites.toString());
 }

 @Test
 public void testFindCandidat() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);
 dao.insert(identite1, "TestForm1", dateLimite1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);
 dao.insert(identite2, "TestForm2", dateLimite2);

 Candidat candidat1 = new Candidat();
 candidat1.setId(1L);
 candidat1.setNom("Doe1");
 candidat1.setPrenom("John1");
 candidat1.setSex("M");
 candidat1.setEmail("john1@test.com");

 Candidat candidat2 = new Candidat();
 candidat2.setId(2L);
 candidat2.setNom("Doe2");
 candidat2.setPrenom("John2");
 candidat2.setSex("M");
 candidat2.setEmail("john2@test.com");

 List<Candidat> expectedCandidats = new ArrayList<>();
 expectedCandidats.add(candidat1);
 expectedCandidats.add(candidat2);

 List<Candidat> candidats = dao.findCandidat("Doe", "John", "M", null);
 assertEquals(expectedCandidats.toString(), candidats.toString());
 }

 @Test
 public void testFindRespForm() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("F");
 identite1.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar1 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);
 dao.insert(identite1, "TestForm1", dateLimite1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("F");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = new GregorianCalendar(2022, 0, 1);
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);
 dao.insert(identite2, "TestForm2", dateLimite2);

 RespFormation respFormation1 = new RespFormation();
 respFormation1.setId(1L);
 respFormation1.setNom("Doe1");
 respFormation1.setPrenom("John1");
 respFormation1.setSex("F");
 respFormation1.setEmail("john1@test.com");

 RespFormation respFormation2 = new RespFormation();
 respFormation2.setId(2L);
 respFormation2.setNom("Doe2");
 respFormation2.setPrenom("John2");
 respFormation2.setSex("F");
 respFormation2.setEmail("john2@test.com");

 List<RespFormation> expectedRespFormations = new ArrayList<>();
 expectedRespFormations.add(respFormation1);
 expectedRespFormations.add(respFormation2);

 List<RespFormation> respFormations = dao.findRespForm("Doe", "John", "F", null);
 assertEquals(expectedRespFormations.toString(), respFormations.toString());
 }
}