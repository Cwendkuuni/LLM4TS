package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

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

 private OracleIdentiteDao identiteDao;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDaoImpl.getDAO();
 }

 @After
 public void tearDown() {
 // Cleanup code here
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 Calendar dateLimite = new GregorianCalendar(2025, 11, 31).getTimeInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "nomForm", dateLimite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 Calendar dateLimite = new GregorianCalendar(2025, 11, 31).getTimeInstance();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Identite existingIdentite = new Identite();
 existingIdentite.setId(1L);
 existingIdentite.setNom("John Doe");
 existingIdentite.setEmail("john.doe@example.com");
 existingIdentite.setSex("M");
 existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.update(identite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 Calendar dateLimite = new GregorianCalendar(2025, 11, 31).getTimeInstance();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteDao.insert(identite, "nomForm", dateLimite);
 identiteDao.delete(identite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNull(foundIdentite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> result = identiteDao.find(nom, prenom, sex, email, type);
 assertFalse(result.isEmpty());

 for (Identite identite : result) {
 assertEquals(nom, identite.getNom());
 assertEquals(prenom, identite.getPrenom());
 assertEquals(sex, identite.getSex());
 assertEquals(email, identite.getEmail());
 assertEquals(type, identite.getType());
 }
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] result = identiteDao.findCandidat(nom, prenom, sex, email);
 assertNotNull(result);
 assertFalse(Arrays.asList(result).isEmpty());

 for (Candidat candidat : result) {
 assertEquals(nom, candidat.getNom());
 assertEquals(prenom, candidat.getPrenom());
 assertEquals(sex, candidat.getSex());
 assertEquals(email, candidat.getEmail());
 }
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] result = identiteDao.findRespForm(nom, prenom, sex, email);
 assertNotNull(result);
 assertFalse(Arrays.asList(result).isEmpty());

 for (RespFormation respFormation : result) {
 assertEquals(nom, respFormation.getNom());
 assertEquals(prenom, respFormation.getPrenom());
 assertEquals(sex, respFormation.getSex());
 assertEquals(email, respFormation.getEmail());
 }
 }

}