package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

 private EntityManagerFactory emf;
 private EntityManager em;
 private IdentiteDao identiteDao;

 @Before
 public void setUp() {
 Class<?> contextClass = getClass();
 PersistenceUnitManager unitManager = Persistence.createPersistenceUnitManager();
 PersistenceUnitTransaction transaction = unitManager.getTransaction();
 try {
 transaction.begin();
 emf = unitManager.createEntityManagerFactory(contextClass.getPackage().getName() + ":junit");
 identiteDao = OracleIdentiteDao.getDAO();
 } finally {
 transaction.commit();
 }
 em = emf.createEntityManager();
 }

 @After
 public void tearDown() {
 if (em != null) {
 em.close();
 }
 if (emf != null) {
 emf.close();
 }
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
 Calendar calendar = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateNaissance(formatter.parse("25/12/1990"));
 identiteDao.insert(identite, null, null);

 List<Identite> result = identiteDao.find(null, identite.getPrenom(), null, identite.getEmail(), null);
 assertEquals(1, result.size());
 Identite retrievedIdentite = result.get(0);
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
 Calendar calendar = Calendar.getInstance();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateNaissance(formatter.parse("25/12/1990"));
 identiteDao.insert(identite, null, null);

 identite.setEmail("john.doe@newemail.com");
 identiteDao.update(identite);

 List<Identite> result = identiteDao.find(null, identite.getPrenom(), null, identite.getEmail(), null);
 assertEquals(1, result.size());
 Identite retrievedIdentite = result.get(0);
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
 Calendar calendar = Calendar.getInstance();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateNaissance(formatter.parse("25/12/1990"));
 identiteDao.insert(identite, null, null);

 identiteDao.delete(identite);

 List<Identite> result = identiteDao.find(null, identite.getPrenom(), null, identite.getEmail(), null);
 assertEquals(0, result.size());
 }

 @Test
 public void testFind() {
 Identite identite = new Identite();
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
 Calendar calendar = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateNaissance(formatter.parse("25/12/1990"));
 identiteDao.insert(identite, null, null);

 List<Identite> result = identiteDao.find(identite.getNom(), identite.getPrenom(), null, identite.getEmail(), null);
 assertEquals(1, result.size());
 Identite retrievedIdentite = result.get(0);
 assertEquals(identite.getNom(), retrievedIdentite.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
 assertEquals(identite.getSex(), retrievedIdentite.getSex());
 assertEquals(identite.getType(), retrievedIdentite.getType());
 assertEquals(identite.getEmail(), retrievedIdentite.getEmail());

 result = identiteDao.find(null, identite.getPrenom(), null, identite.getEmail(), null);
 assertEquals(1, result.size());
 Identite retrievedIdentite2 = result.get(0);
 assertEquals(identite.getNom(), retrievedIdentite2.getNom());
 assertEquals(identite.getPrenom(), retrievedIdentite2.getPrenom());
 assertEquals(identite.getSex(), retrievedIdentite2.getSex());
 assertEquals(identite.getType(), retrievedIdentite2.getType());
 assertEquals(identite.getEmail(), retrievedIdentite2.getEmail());
 }

 @Test
 public void testFindCandidat() {
 Identite identite = new Identite();
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
 Calendar calendar = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 identite.setEmail("john.doe@example.com");
 identite.setDateNaissance(formatter.parse("25/12/1990"));
 identiteDao.insert(identite, null, null);

 Candidat[] result = identiteDao.findCandidat(identite.getNom(), identite.getPrenom(), null, identite.getEmail());
 assertEquals(1, result.length);
 Candidat retrievedCandidat = result[0];
 assertEquals(identite.getNom(), retrievedCandidat.getNom());
 assertEquals(identite.getPrenom(), retrievedCandidat.getPrenom());

 result = identiteDao.findCandidat(identite.getNom(), null, null, identite.getEmail());
 assertEquals(1, result.length);
 Candidat retrievedCandidat2 = result[0];
 assertEquals(identite.getNom(), retrievedCandidat2.getNom());
 }

 @Test
 public void testFindRespForm() {
 Identite identite = new Identite();
 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
 Calendar calendar = Calendar.getInstance();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.FORMATEUR);
 identite.setEmail("john.doe@example.com");
 identite.setDateNaissance(formatter.parse("25/12/1990"));
 identiteDao.insert(identite, null, null);

 RespFormation[] result = identiteDao.findRespForm(identite.getNom(), identite.getPrenom(), null, identite.getEmail());
 assertEquals(1, result.length);
 RespFormation retrievedRespForm = result[0];
 assertEquals(identite.getNom(), retrievedRespForm.getNom());
 assertEquals(identite.getPrenom(), retrievedRespForm.getPrenom());
 }
}