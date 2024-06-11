package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private static OracleIdentiteDao identiteDao;

 @BeforeClass
 public static void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1990);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 identiteDao.insert(identite, "test", calendar);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getType(), foundIdentite.getType());
 assertEquals(identite.getDateNaissance(), foundIdentite.getDateNaissance());
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
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1990);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 Identite oldIdentite = new Identite();
 oldIdentite.setId(1L);
 oldIdentite.setEmail("john.doe@oldemail.com");
 oldIdentite.setNom("Doe");
 oldIdentite.setPrenom("John");
 oldIdentite.setSex("M");
 oldIdentite.setType(Identite.TypeIdentite.CANDIDAT);
 oldIdentite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 identiteDao.update(identite);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotEquals(oldIdentite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
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
 Calendar calendar = Calendar.getInstance();
 calendar.set(Calendar.YEAR, 1990);
 identite.setDateNaissance(new Date(calendar.getTimeInMillis()));

 identiteDao.insert(identite, "test", calendar);

 Identite foundIdentite = identiteDao.findById(identite.getId());
 assertNotNull(foundIdentite);

 identiteDao.delete(identite);

 Identite deletedIdentite = identiteDao.findById(identite.getId());
 assertNull(deletedIdentite);
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setEmail("john1.doe@example.com");
 identite1.setSex("M");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = Calendar.getInstance();
 calendar1.set(Calendar.YEAR, 1990);
 identite1.setDateNaissance(new Date(calendar1.getTimeInMillis()));
 identiteDao.insert(identite1, "test", calendar1);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");identite2.setEmail("john2.doe@example.com");
 identite2.setSex("M");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = Calendar.getInstance();
 calendar2.set(Calendar.YEAR, 1985);
 identite2.setDateNaissance(new Date(calendar2.getTimeInMillis()));
 identiteDao.insert(identite2, "test", calendar2);

 List<Identite> result = identiteDao.find(null, null, null, null, null);
 assertEquals(2, result.size());

 List<Identite> candidates = identiteDao.findCandidat(null, null, null, null);
 assertEquals(1, candidates.size());
 assertEquals(identite1.getNom(), candidates.get(0).getNom());

 List<RespFormation> formateurs = identiteDao.findRespForm(null, null, null, null);
 assertEquals(1, formateurs.length);
 assertEquals(identite2.getNom(), formateurs[0].getNom());
 }
}