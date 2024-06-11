package fr.unice.gfarce.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private OracleIdentiteDao dao;
	private static final String PERSIST_UNIT_NAME = "Identite";
	private static final String TEST_EMAIL = "test@test.fr";

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory(PERSIST_UNIT_NAME);
		em = emf.createEntityManager();
		dao = OracleIdentiteDao.getDAO();
	}

	@After
	public void tearDown() {
		em.close();
		emf.close();
	}

	@Test
	public void testInsert() {
		Identite identite = new Identite();
		identite.setEmail(TEST_EMAIL);
		identite.setNom("Doe");
		identite.setPrenom("John");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.insert(identite, null, null);

		Identite foundIdentite = em.find(Identite.class, TEST_EMAIL);
		assertNotNull(foundIdentite);
		assertEquals(identite.getNom(), foundIdentite.getNom());
		assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
		assertEquals(identite.getEmail(), foundIdentite.getEmail());
		assertEquals(identite.getType(), foundIdentite.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullIdentite() {
		dao.insert(null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullNomForm() {
		Identite identite = new Identite();
		identite.setEmail(TEST_EMAIL);
		identite.setNom(null);
		identite.setPrenom("John");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.insert(identite, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullDateLimite() {
		Identite identite = new Identite();
		identite.setEmail(TEST_EMAIL);
		identite.setNom("Doe");
		identite.setPrenom("John");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		Calendar calendar = Calendar.getInstance();
		dao.insert(identite, null, calendar);
	}

	@Test
	public void testUpdate() {
		Identite identite = new Identite();
		identite.setEmail(TEST_EMAIL);
		identite.setNom("Doe");
		identite.setPrenom("John");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.insert(identite, null, null);
		Identite foundIdentite = em.find(Identite.class, TEST_EMAIL);

		Identite updatedIdentite = new Identite();
		updatedIdentite.setEmail(TEST_EMAIL);
		updatedIdentite.setNom("Smith");
		updatedIdentite.setPrenom("John");
		updatedIdentite.setSex("M");
		updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.update(updatedIdentite);

		foundIdentite = em.find(Identite.class, TEST_EMAIL);
		assertEquals(updatedIdentite.getNom(), foundIdentite.getNom());
		assertEquals(updatedIdentite.getPrenom(), foundIdentite.getPrenom());
		assertEquals(updatedIdentite.getEmail(), foundIdentite.getEmail());
		assertEquals(updatedIdentite.getType(), foundIdentite.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullIdentite() {
		dao.update(null);
	}

	@Test
	public void testDelete() {
		Identite identite = new Identite();
		identite.setEmail(TEST_EMAIL);
		identite.setNom("Doe");
		identite.setPrenom("John");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.insert(identite, null, null);

		Identite foundIdentite = em.find(Identite.class, TEST_EMAIL);
		assertNotNull(foundIdentite);

		dao.delete(foundIdentite);

		foundIdentite = em.find(Identite.class, TEST_EMAIL);
		assertNull(foundIdentite);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNullIdentite() {
		dao.delete(null);
	}

	@Test
	public void testFind() {
		Identite identite1 = new Identite();
		identite1.setEmail("identite1@test.fr");
		identite1.setNom("Doe1");
		identite1.setPrenom("John1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite2 = new Identite();
		identite2.setEmail("identite2@test.fr");
		identite2.setNom("Doe2");
		identite2.setPrenom("John2");
		identite2.setSex("M");
		identite2.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite3 = new Identite();
		identite3.setEmail("identite3@test.fr");
		identite3.setNom("Doe3");
		identite3.setPrenom("John3");
		identite3.setSex("F");
		identite3.setType(Identite.TypeIdentite.FORMATEUR);

		dao.insert(identite1, null, null);
		dao.insert(identite2, null, null);
		dao.insert(identite3, null, null);

		List<Identite> foundIdentites = dao.find(null, null, null, null, null);
		assertEquals(3, foundIdentites.size());
		assertArrayEquals(new String[] { "Doe1", "Doe2", "Doe3" }, convertListToStringArray(foundIdentites, "nom"));
	}

	@Test
	public void testFindWithFilter() {
		Identite identite1 = new Identite();
		identite1.setEmail("identite1@test.fr");
		identite1.setNom("Doe1");
		identite1.setPrenom("John1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite2 = new Identite();
		identite2.setEmail("identite2@test.fr");
		identite2.setNom("Doe2");
		identite2.setPrenom("John2");
		identite2.setSex("M");
		identite2.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite3 = new Identite();
		identite3.setEmail("identite3@test.fr");
		identite3.setNom("Doe3");
		identite3.setPrenom("John3");
		identite3.setSex("F");
		identite3.setType(Identite.TypeIdentite.FORMATEUR);

		Identite identite4 = new Identite();
		identite4.setEmail("identite4@test.fr");
		identite4.setNom("Doe4");
		identite4.setPrenom("John4");
		identite4.setSex("F");
		identite4.setType(Identite.TypeIdentite.FORMATEUR);

		dao.insert(identite1, null, null);
		dao.insert(identite2, null, null);
		dao.insert(identite3, null, null);
		dao.insert(identite4, null, null);

		List<Identite> foundIdentites = dao.find("Doe", "John", null, null, null);
		assertEquals(2, foundIdentites.size());
		assertEquals("Doe1", foundIdentites.get(0).getNom());
		assertEquals("Doe2", foundIdentites.get(1).getNom());
	}

	@Test
	public void testFindCandidat() {
		Identite identite1 = new Identite();
		identite1.setEmail("identite1@test.fr");
		identite1.setNom("Doe1");
		identite1.setPrenom("John1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite2 = new Identite();
		identite2.setEmail("identite2@test.fr");
		identite2.setNom("Doe2");
		identite2.setPrenom("John2");
		identite2.setSex("M");
		identite2.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite3 = new Identite();
		identite3.setEmail("identite3@test.fr");
		identite3.setNom("Doe3");
		identite3.setPrenom("John3");
		identite3.setSex("F");
		identite3.setType(Identite.TypeIdentite.FORMATEUR);

		Identite identite4 = new Identite();
		identite4.setEmail("identite4@test.fr");
		identite4.setNom("Doe4");
		identite4.setPrenom("John4");
		identite4.setSex("F");
		identite4.setType(Identite.TypeIdentite.FORMATEUR);

		Candidat candidat1 = new Candidat();
		candidat1.setNom("Doe1");
		candidat1.setPrenom("John1");
		candidat1.setSex("M");

		Candidat candidat2 = new Candidat();
		candidat2.setNom("Doe2");
		candidat2.setPrenom("John2");
		candidat2.setSex("M");

		dao.insert(identite1, null, null);
		dao.insert(identite2, null, null);
		dao.insert(identite3, null, null);
		dao.insert(identite4, null, null);

		Candidat[] foundCandidats = dao.findCandidat("Doe", "John", null, null);
		assertEquals(2, foundCandidats.length);
		assertSame(candidat1, foundCandidats[0]);
		assertSame(candidat2, foundCandidats[1]);
	}

	@Test
	public void testFindRespForm() {
		Identite identite1 = new Identite();
		identite1.setEmail("identite1@test.fr");
		identite1.setNom("Doe1");
		identite1.setPrenom("John1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.FORMATEUR);

		Identite identite2 = new Identite();
		identite2.setEmail("identite2@test.fr");
		identite2.setNom("Doe2");
		identite2.setPrenom("John2");
		identite2.setSex("M");
		identite2.setType(Identite.TypeIdentite.FORMATEUR);

		Identite identite3 = new Identite();
		identite3.setEmail("identite3@test.fr");
		identite3.setNom("Doe3");
		identite3.setPrenom("John3");
		identite3.setSex("F");
		identite3.setType(Identite.TypeIdentite.CANDIDAT);

		Identite identite4 = new Identite();
		identite4.setEmail("identite4@test.fr");
		identite4.setNom("Doe4");
		identite4.setPrenom("John4");
		identite4.setSex("F");
		identite4.setType(Identite.TypeIdentite.FORMATEUR);

		RespFormation formation1 = new RespFormation();
		formation1.setNom("Formation1");
		formation1.setIdentite(identite1);

		RespFormation formation2 = new RespFormation();
		formation2.setNom("Formation2");
		formation2.setIdentite(identite2);

		dao.insert(identite1, null, null);
		dao.insert(identite2, null, null);
		dao.insert(identite3, null, null);
		dao.insert(identite4, null, null);
		dao.insert(formation1, null, null);
		dao.insert(formation2, null, null);

		RespFormation[] foundRespFormations = dao.findRespForm("Doe", "John", null, null);
		assertEquals(2, foundRespFormations.length);
		assertSame(formation1, foundRespFormations[0]);
		assertSame(formation2, foundRespFormations[1]);
	}

	@Test
	public void testFindEmpty() {
		List<Identite> foundIdentites = dao.find(null, null, null, null, null);
		assertEquals(0, foundIdentites.size());
	}

	private String[] convertListToStringArray(List<?> list, String propertyName) {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = ((Object)list.get(i)).toString();
		}
		return result;
	}
}