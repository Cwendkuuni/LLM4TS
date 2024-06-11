package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao identiteDao;
 @PersistenceContext
 private EntityManager entityManager;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 identiteDao = OracleIdentiteDao.getDAO();
 }

 @After
 public void cleanup() {
 // Cleanup code here
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar);

 identiteDao.insert(identite, "form.nom", calendar);

 Identite foundIdentite = entityManager.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(identite.getNom(), foundIdentite.getNom());
 assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(identite.getSex(), foundIdentite.getSex());
 assertEquals(identite.getEmail(), foundIdentite.getEmail());
 assertEquals(identite.getType(), foundIdentite.getType());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 entityManager.persist(identite);

 Identite updatedIdentite = new Identite();
 updatedIdentite.setId(identite.getId());
 updatedIdentite.setNom("Smith");
 updatedIdentite.setPrenom("Jane");
 updatedIdentite.setEmail("jane.smith@example.com");
 updatedIdentite.setType(Identite.TypeIdentite.FORMATEUR);

 identiteDao.update(updatedIdentite);

 Identite foundIdentite = entityManager.find(Identite.class, identite.getId());
 assertNotNull(foundIdentite);
 assertEquals(updatedIdentite.getNom(), foundIdentite.getNom());
 assertEquals(updatedIdentite.getPrenom(), foundIdentite.getPrenom());
 assertEquals(updatedIdentite.getEmail(), foundIdentite.getEmail());
 assertEquals(updatedIdentite.getType(), foundIdentite.getType());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 entityManager.persist(identite);

 identiteDao.delete(identite);

 Identite foundIdentite = entityManager.find(Identite.class, identite.getId());
 assertNull(foundIdentite);
 }

 @Test
 public void testFind() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> identites = identiteDao.find(nom, prenom, sex, email, type);
 assertTrue(identites.size() > 0);
 Identite foundIdentite = identites.get(0);
 assertEquals(nom, foundIdentite.getNom());
 assertEquals(prenom, foundIdentite.getPrenom());
 assertEquals(sex, foundIdentite.getSex());
 assertEquals(email, foundIdentite.getEmail());
 assertEquals(type, foundIdentite.getType());
 }

 @Test
 public void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] candidats = identiteDao.findCandidat(nom, prenom, sex, email);
 assertTrue(candidats.length > 0);
 Candidat foundCandidat = candidats[0];
 assertEquals(nom, foundCandidat.getNom());
 assertEquals(prenom, foundCandidat.getPrenom());
 assertEquals(sex, foundCandidat.getSex());
 assertEquals(email, foundCandidat.getEmail());
 }

 @Test
 public void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] respFormations = identiteDao.findRespForm(nom, prenom, sex, email);
 assertTrue(respFormations.length > 0);
 RespFormation foundRespFormation = respFormations[0];
 assertEquals(nom, foundRespFormation.getNom());
 assertEquals(prenom, foundRespFormation.getPrenom());
 assertEquals(sex, foundRespFormation.getSex());
 assertEquals(email, foundRespFormation.getEmail());
 }
}