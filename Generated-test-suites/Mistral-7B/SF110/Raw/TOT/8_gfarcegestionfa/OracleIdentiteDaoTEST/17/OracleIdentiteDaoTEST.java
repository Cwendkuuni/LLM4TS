package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

class OracleIdentiteDaoTest {

 private static EntityManagerFactory emf;
 private static EntityManager em;
 private static OracleIdentiteDao dao;

 @BeforeAll
 static void init() {
 emf = Persistence.createEntityManagerFactory("Identite");
 em = emf.createEntityManager();
 dao = OracleIdentiteDaoImpl.getDAO();
 }

 @AfterAll
 static void close() {
 em.close();
 emf.close();
 }

 @Test
 void testInsertIdentite() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 dao.insert(identite, null, dateLimite);

 TypedQuery<Identite> query = em.createQuery("SELECT i FROM Identite i WHERE i.email = :email", Identite.class);
 query.setParameter("email", identite.getEmail());
 Identite result = query.getSingleResult();

 assertNotNull(result);
 assertNotNull(result.getId());
 }

 @Test
 void testUpdateIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 Identite original = em.find(Identite.class, identite.getId());
 assertNotNull(original);

 dao.update(identite);

 Identite updated = em.find(Identite.class, identite.getId());
 assertNotNull(updated);
 assertNotNull(updated.getId());
 assertNotNull(updated.getEmail());
 assertNotNull(updated.getNom());
 assertNotNull(updated.getPrenom());
 assertNotNull(updated.getSex());
 assertNotNull(updated.getType());
 assertNotNull(updated.getDateLimite());

 assertNotEquals(original.getEmail(), updated.getEmail());
 assertNotEquals(original.getNom(), updated.getNom());
 assertNotEquals(original.getPrenom(), updated.getPrenom());
 assertNotEquals(original.getSex(), updated.getSex());
 assertNotEquals(original.getType(), updated.getType());
 assertNotEquals(original.getDateLimite(), updated.getDateLimite());
 }

 @Test
 void testDeleteIdentite() {
 Identite identite = new Identite();
 identite.setId(1L);

 dao.delete(identite);

 TypedQuery<Identite> query = em.createQuery("SELECT i FROM Identite i WHERE i.id = :id", Identite.class);
 query.setParameter("id", identite.getId());
 Identite result = query.getSingleResult();

 assertNull(result);
 }

 @Test
 void testFindIdentite() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";
 Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 List<Identite> result = dao.find(nom, prenom, sex, email, type);

 assertNotNull(result);
 assertFalse(result.isEmpty());

 Identite identite = result.get(0);
 assertNotNull(identite);
 assertNotNull(identite.getId());
 assertNotNull(identite.getEmail());
 assertNotNull(identite.getNom());
 assertNotNull(identite.getPrenom());
 assertNotNull(identite.getSex());
 assertNotNull(identite.getType());
 assertNotNull(identite.getDateLimite());
 }

 @Test
 void testFindCandidat() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 Candidat[] result = dao.findCandidat(nom, prenom, sex, email);

 assertNotNull(result);
 assertFalse(Arrays.stream(result).anyMatch(c -> c == null));
 assertFalse(Arrays.stream(result).anyMatch(c -> c.getId() == null));
 }

 @Test
 void testFindRespForm() {
 String nom = "Doe";
 String prenom = "John";
 String sex = "M";
 String email = "john.doe@example.com";

 RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);

 assertNotNull(result);
 assertFalse(Arrays.stream(result).anyMatch(rf -> rf == null));
 assertFalse(Arrays.stream(result).anyMatch(rf -> rf.getId() == null));
 }

 @Test
 void testExceptionOnInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityTransaction tx = em.getTransaction();
 tx.begin();

 dao.insert(identite, null, dateLimite);

 assertThrows(PersistenceException.class, () -> {
 tx.commit();
 });
 }

 @Test
 void testExceptionOnUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setEmail("john.doe@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 Date dateLimite = calendar.getTime();
 identite.setDateLimite(dateLimite);

 EntityTransaction tx = em.getTransaction();
 tx.begin();
 Identite original = em.find(Identite.class, identite.getId());
 dao.update(identite);

 assertThrows(PersistenceException.class, () -> {
 tx.commit();
 });
 }

 @Test
 void testExceptionOnDelete() {
 Identite identite = new Identite();
 identite.setId(1L);

 EntityTransaction tx = em.getTransaction();
 tx.begin();
 Identite original = em.find(Identite.class, identite.getId());
 dao.delete(identite);

 assertThrows(PersistenceException.class, () -> {
 tx.commit();
 });
 }
}