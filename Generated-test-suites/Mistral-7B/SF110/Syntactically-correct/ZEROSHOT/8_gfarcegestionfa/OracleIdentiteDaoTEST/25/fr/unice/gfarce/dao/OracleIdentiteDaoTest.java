package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager mockEntityManager;
 @Mock
 private EntityManagerFactory mockEntityManagerFactory;
 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 Calendar dateLimite = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 oracleIdentiteDao.insert(identite, "form", dateLimite);

 // Add assertions here
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 Identite identiteToUpdate = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 identiteToUpdate.setId(1L);
 identiteToUpdate.setEmail("new.email@example.com");
 identiteToUpdate.setNom("newName");
 identiteToUpdate.setPrenom("newPrenom");
 identiteToUpdate.setSex("F");
 identiteToUpdate.setType(Identite.TypeIdentite.FORMATEUR);

 oracleIdentiteDao.update(identiteToUpdate);

 // Add assertions here
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

 oracleIdentiteDao.delete(identite);

 // Add assertions here
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = oracleIdentiteDao.find(nom, prenom, sex, email, type);

 // Add assertions here
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);

 // Add assertions here
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);

 // Add assertions here
 }
}