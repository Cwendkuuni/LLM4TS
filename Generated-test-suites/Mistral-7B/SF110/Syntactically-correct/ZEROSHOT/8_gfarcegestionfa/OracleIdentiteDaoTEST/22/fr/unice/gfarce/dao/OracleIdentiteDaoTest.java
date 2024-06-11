package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = new GregorianCalendar(2022, 11, 15);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 oracleIdentiteDao.insert(identite, "form", calendar);

 // Verify that the entity was persisted
 assertEquals(1, oracleIdentiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT).size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 oracleIdentiteDao.insert(null, "form", new GregorianCalendar(2022, 11, 15).getTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullForm() {
 oracleIdentiteDao.insert(new Identite(), null, new GregorianCalendar(2022, 11, 15).getTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullDate() {
 oracleIdentiteDao.insert(new Identite(), "form", null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");

 Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(1L);
 identiteToUpdate.setNom("Smith");
 identiteToUpdate.setPrenom("Jane");
 identiteToUpdate.setSex("F");
 identiteToUpdate.setEmail("jane.smith@example.com");

 oracleIdentiteDao.update(identiteToUpdate);

 // Verify that the entity was updated
 assertEquals("Smith", oracleIdentiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT).get(0).getNom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 oracleIdentiteDao.update(null);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");

 oracleIdentiteDao.insert(identite, "form", new GregorianCalendar(2022, 11, 15).getTime());
 List<Identite> identitesBeforeDelete = oracleIdentiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);

 oracleIdentiteDao.delete(identite);

 // Verify that the entity was deleted
 assertEquals(0, oracleIdentiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT).size());
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 identite1.setEmail("john1.doe1@example.com");
 oracleIdentiteDao.insert(identite1, "form1", new GregorianCalendar(2022, 11, 15).getTime());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.CANDIDAT);
 identite2.setEmail("john2.doe2@example.com");
 oracleIdentiteDao.insert(identite2, "form2", new GregorianCalendar(2022, 11, 16).getTime());

 List<Identite> identites = oracleIdentiteDao.find(null, "Doe", null, null, Identite.TypeIdentite.CANDIDAT);

 // Verify that the correct identities were returned
 assertEquals(2, identites.size());
 assertEquals("Doe1", identites.get(0).getNom());
 assertEquals("Doe2", identites.get(1).getNom());
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 oracleIdentiteDao.insert(identite, "form", new GregorianCalendar(2022, 11, 15).getTime());

 Candidat[] candidats = oracleIdentiteDao.findCandidat("Doe", null, null, "john.doe@example.com");

 // Verify that the correct candidates were returned
 assertEquals(1, candidats.length);
 assertEquals("Doe", candidats[0].getNom());
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identite.setEmail("john.doe@example.com");
 oracleIdentiteDao.insert(identite, "form", new GregorianCalendar(2022, 11, 15).getTime());

 RespFormation[] respFormations = oracleIdentiteDao.findRespForm("Doe", null, null, "john.doe@example.com");

 // Verify that no respformations were returned, as this identite is not a respformation
 assertNull(respFormations);
 }
}