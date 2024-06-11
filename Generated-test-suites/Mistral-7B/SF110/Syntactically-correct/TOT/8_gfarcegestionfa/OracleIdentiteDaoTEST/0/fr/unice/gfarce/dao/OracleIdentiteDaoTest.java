package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private IdentiteDao dao;
 private EntityManager entityManager;

 @Before
 public void setUp() {
 dao = OracleIdentiteDao.getDAO();
 entityManager = dao.getEntityManager();
 }

 @After
 public void tearDown() {
 // Cleanup
 List<Identite> identites = entityManager.createQuery("SELECT i FROM Identite i", Identite.class).getResultList();
 for (Identite identite : identites) {
 entityManager.remove(identite);
 }
 entityManager.getTransaction().commit();
 }

 @Test
 public void testInsertIdentite() {
 // Test inserting a valid Identite
 Identite identite = new Identite("Dupond", "Jean", "M", "jean.dupond@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, "Formulaire1", dateLimite);

 // Check if the Identite was inserted
 List<Identite> identites = entityManager.createQuery("SELECT i FROM Identite i", Identite.class).getResultList();
 Identite savedIdentite = identites.get(0);
 assertEquals("Dupond", savedIdentite.getNom());
 assertEquals("Jean", savedIdentite.getPrenom());
 assertEquals("M", savedIdentite.getSex());
 assertEquals("jean.dupond@example.com", savedIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, savedIdentite.getType());

 // Test inserting a null Identite
 dao.insert(null, "Formulaire1", dateLimite);
 // Expected error: PersistenceException: org.hibernate.PersistentObjectException: detached object [fr.unice.gfarce.identity.Identite@5421e722] with id [1] passed to persist: Classes: fr.unice.gfarce.identity.Identite; Long: 1
 }

 @Test
 public void testUpdateIdentite() {
 // Test updating a valid Identite
 Identite identite = new Identite("Dupond", "Jean", "M", "jean.dupond@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 entityManager.persist(identite);
 entityManager.getTransaction().commit();

 // Update the Identite
 identite.setEmail("new.email@example.com");
 dao.update(identite);

 // Check if the Identite was updated
 List<Identite> identites = entityManager.createQuery("SELECT i FROM Identite i", Identite.class).getResultList();
 Identite updatedIdentite = identites.get(0);
 assertEquals("Dupond", updatedIdentite.getNom());
 assertEquals("Jean", updatedIdentite.getPrenom());
 assertEquals("M", updatedIdentite.getSex());
 assertEquals("new.email@example.com", updatedIdentite.getEmail());
 assertEquals(Identite.TypeIdentite.CANDIDAT, updatedIdentite.getType());

 // Test updating an invalid Identite (id not found)
 Identite invalidIdentite = new Identite("Dupond", "Jean", "M", "jean.dupond@example.com", Identite.TypeIdentite.CANDIDAT);
 invalidIdentite.setId(Long.MAX_VALUE);
 dao.update(invalidIdentite);
 // Expected error: PersistenceException: org.hibernate.StaleStateException: Bidirectional association: fr.unice.gfarce.identity.Identite.candidats -> fr.unice.gfarce.model.Candidat: not connected to session
 }

 @Test
 public void testDeleteIdentite() {
 // Test deleting a valid Identite
 Identite identite = new Identite("Dupond", "Jean", "M", "jean.dupond@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 entityManager.persist(identite);
 entityManager.getTransaction().commit();

 dao.delete(identite);

 // Check if the Identite was deleted
 List<Identite> identites = entityManager.createQuery("SELECT i FROM Identite i", Identite.class).getResultList();
 // Expected result: empty list
 assertTrue(identites.isEmpty());

 // Test deleting an invalid Identite (id not found)
 Identite invalidIdentite = new Identite("Dupond", "Jean", "M", "jean.dupond@example.com", Identite.TypeIdentite.CANDIDAT);
 invalidIdentite.setId(Long.MAX_VALUE);
 dao.delete(invalidIdentite);
 // Expected error: PersistenceException: org.hibernate.ieder.IdentifierConstraintViolationException: entity identifiers must not be null or empty and must be unique: fr.unice.gfarce.identity.Identite.id
 }

 @Test
 public void testFindIdentites() {
 // Test finding Identites with no filters
 List<Identite> identites = dao.find(null, null, null, null, null);
 // Expected result: empty list
 assertTrue(identites.isEmpty());

 // Test finding Identites with filters
 Identite identite1 = new Identite("Doe", "John", "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = Calendar.getInstance();
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);
 entityManager.persist(identite1);
 entityManager.getTransaction().commit();

 Identite identite2 = new Identite("Smith", "Jane", "F", "jane.smith@example.com", Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = Calendar.getInstance();
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);
 entityManager.persist(identite2);
 entityManager.getTransaction().commit();

 List<Identite> filteredIdentites = dao.find(null, null, null, null, null);
 // Expected result: 2 identites in the list
 assertEquals(2, filteredIdentites.size());

 // Test finding Identites with filters for name
 List<Identite> filteredIdentitesByName = dao.find(null, "Doe", null, null, null);
 // Expected result: 1 identite in the list
 assertEquals(1, filteredIdentitesByName.size());
 assertEquals("Doe", filteredIdentitesByName.get(0).getNom());

 // Test finding Identites with filters for prenom
 List<Identite> filteredIdentitesByPrenom = dao.find(null, null, "Smith", null, null);
 // Expected result: 1 identite in the list
 assertEquals(1, filteredIdentitesByPrenom.size());
 assertEquals("Smith", filteredIdentitesByPrenom.get(0).getPrenom());

 // Test finding Identites with filters for sex
 List<Identite> filteredIdentitesBySex = dao.find(null, null, null, "F", null);
 // Expected result: 1 identite in the list
 assertEquals(1, filteredIdentitesBySex.size());
 assertEquals("Smith", filteredIdentitesBySex.get(0).getPrenom());
 assertEquals("F", filteredIdentitesBySex.get(0).getSex());

 // Test finding Identites with filters for email
 List<Identite> filteredIdentitesByEmail = dao.find(null, null, null, null, "jane.smith@example.com");
 // Expected result: 1 identite in the list
 assertEquals(1, filteredIdentitesByEmail.size());
 assertEquals("Smith", filteredIdentitesByEmail.get(0).getPrenom());
 assertEquals("jane.smith@example.com", filteredIdentitesByEmail.get(0).getEmail());

 // Test finding Identites with filters for type
 List<Identite> filteredIdentitesByType = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
 // Expected result: 1 identite in the list
 assertEquals(1, filteredIdentitesByType.size());
 assertEquals("Doe", filteredIdentitesByType.get(0).getNom());
 }

 @Test
 public void testFindCandidats() {
 // Test finding Candidats with no filters
 Candidat[] candidats = dao.findCandidat(null, null, null, null);
 // Expected result: null
 assertNull(candidats);

 // Test finding Candidats with filters
 Identite identite1 = new Identite("Doe", "John", "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = Calendar.getInstance();
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);
 entityManager.persist(identite1);
 entityManager.getTransaction().commit();

 Identite identite2 = new Identite("Doe", "Jane", "F", "jane.doe@example.com", Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = Calendar.getInstance();
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);
 entityManager.persist(identite2);
 entityManager.getTransaction().commit();

 Candidat candidat1 = new Candidat("Doe", "John");
 entityManager.persist(candidat1);
 entityManager.getTransaction().commit();
 candidat1.setIdentite(identite1);
 identite1.setCandidats(new ArrayList<Candidat>() {{ add(candidat1); }});
 entityManager.merge(identite1);
 entityManager.getTransaction().commit();

 Candidat candidat2 = new Candidat("Doe", "Jane");
 entityManager.persist(candidat2);
 entityManager.getTransaction().commit();
 candidat2.setIdentite(identite2);
 identite2.setCandidats(new ArrayList<Candidat>() {{ add(candidat2); }});
 entityManager.merge(identite2);
 entityManager.getTransaction().commit();

 Candidat[] foundCandidats = dao.findCandidat(null, "Doe", null, null);
 // Expected result: 2 candidats in the array
 assertEquals(2, foundCandidats.length);
 }

 @Test
 public void testFindResponsablesFormations() {
 // Test finding ResponsablesFormations with no filters
 RespFormation[] responsablesFormations = dao.findRespForm(null, null, null, null);
 // Expected result: null
 assertNull(responsablesFormations);

 // Test finding ResponsablesFormations with filters
 Identite identite1 = new Identite("Doe", "John", "M", "john.doe@example.com", Identite.TypeIdentite.FORMATEUR);
 Calendar calendar1 = Calendar.getInstance();
 Date dateLimite1 = calendar1.getTime();
 identite1.setDateLimite(dateLimite1);
 entityManager.persist(identite1);
 entityManager.getTransaction().commit();

 Identite identite2 = new Identite("Smith", "Jane", "F", "jane.smith@example.com", Identite.TypeIdentite.CANDIDAT);
 Calendar calendar2 = Calendar.getInstance();
 Date dateLimite2 = calendar2.getTime();
 identite2.setDateLimite(dateLimite2);
 entityManager.persist(identite2);
 entityManager.getTransaction().commit();

 RespFormation formation1 = new RespFormation("Formation1", "Formation1 description", identite1);
 entityManager.persist(formation1);
 entityManager.getTransaction().commit();

 RespFormation formation2 = new RespFormation("Formation2", "Formation2 description", identite2);
 entityManager.persist(formation2);
 entityManager.getTransaction().commit();
 identite1.setResponsablesFormations(new ArrayList<RespFormation>() {{ add(formation1); }});
 entityManager.merge(identite1);
 entityManager.getTransaction().commit();
 identite2.setResponsablesFormations(new ArrayList<RespFormation>() {{ add(formation2); }});
 entityManager.merge(identite2);
 entityManager.getTransaction().commit();

 RespFormation[] foundResponsablesFormations = dao.findRespForm(null, "Doe", null, null);
 // Expected result: 1 responsable formation in the array
 assertEquals(1, foundResponsablesFormations.length);
 assertEquals("Formation1", foundResponsablesFormations[0].getNom());
 }
}