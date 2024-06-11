package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.OracleIdentiteDaoImpl;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private OracleIdentiteDao dao;

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("Identite");
		em = emf.createEntityManager();
		dao = OracleIdentiteDaoImpl.getDAO();
	}

	@After
	public void tearDown() {
		if (em != null && em.isOpen()) {
			em.close();
		}
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}

	@Test
	public void testInsertIdentite() {
		Identite identite = new Identite();
		identite.setEmail("test@test.fr");
		identite.setNom("Test");
		identite.setPrenom("Test");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.insert(identite, null, null);

		Identite foundIdentite = em.find(Identite.class, identite.getId());
		assertNotNull(foundIdentite);
		assertEquals(identite.getEmail(), foundIdentite.getEmail());
		assertEquals(identite.getNom(), foundIdentite.getNom());
		assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
		assertEquals(identite.getSex(), foundIdentite.getSex());
		assertEquals(identite.getType(), foundIdentite.getType());
	}

	@Test
	public void testUpdateIdentite() {
		Identite identite = new Identite();
		identite.setEmail("test@test.fr");
		identite.setNom("Test");
		identite.setPrenom("Test");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);
		identite.setId(1L);

		dao.update(identite);

		Identite foundIdentite = em.find(Identite.class, identite.getId());
		assertNotNull(foundIdentite);
		assertEquals(identite.getEmail(), foundIdentite.getEmail());
		assertEquals(identite.getNom(), foundIdentite.getNom());
		assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
		assertEquals(identite.getSex(), foundIdentite.getSex());
		assertEquals(identite.getType(), foundIdentite.getType());
	}

	@Test
	public void testDeleteIdentite() {
		Identite identite = new Identite();
		identite.setEmail("test@test.fr");
		identite.setNom("Test");
		identite.setPrenom("Test");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);
		identite.setId(1L);

		dao.delete(identite);

		Identite foundIdentite = em.find(Identite.class, identite.getId());
		assertNull(foundIdentite);
	}

	@Test
	public void testFindIdentites() {
		Identite identite1 = new Identite();
		identite1.setEmail("test1@test.fr");
		identite1.setNom("Test1");
		identite1.setPrenom("Test1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.CANDIDAT);
		identite1.setId(1L);

		Identite identite2 = new Identite();
		identite2.setEmail("test2@test.fr");
		identite2.setNom("Test2");
		identite2.setPrenom("Test2");
		identite2.setSex("F");
		identite2.setType(Identite.TypeIdentite.FORMATEUR);
		identite2.setId(2L);

		em.getTransaction().begin();
		em.persist(identite1);
		em.persist(identite2);
		em.getTransaction().commit();

		List<Identite> foundIdentites = dao.find(null, null, null, null, null);
		assertNotNull(foundIdentites);
		assertEquals(2, foundIdentites.size());
		assertEquals(identite1, foundIdentites.get(0));
		assertEquals(identite2, foundIdentites.get(1));
	}

	@Test
	public void testFindCandidats() {
		Identite identite1 = new Identite();
		identite1.setEmail("test1@test.fr");
		identite1.setNom("Test1");
		identite1.setPrenom("Test1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.CANDIDAT);
		identite1.setId(1L);

		em.getTransaction().begin();
		em.persist(identite1);
		em.getTransaction().commit();

		Candidat[] foundCandidats = dao.findCandidat(null, null, null, null);
		assertEquals(1, foundCandidats.length);
		assertEquals(identite1, foundCandidats[0].getIdentite());
	}

	@Test
	public void testFindResponsablesFormations() {
		Identite identite1 = new Identite();
		identite1.setEmail("test1@test.fr");
		identite1.setNom("Test1");
		identite1.setPrenom("Test1");
		identite1.setSex("M");
		identite1.setType(Identite.TypeIdentite.FORMATEUR);
		identite1.setId(1L);

		em.getTransaction().begin();
		em.persist(identite1);
		em.getTransaction().commit();

		RespFormation[] foundResponsablesFormations = dao.findRespForm(null, null, null, null);
		assertEquals(1, foundResponsablesFormations.length);
		assertEquals(identite1, foundResponsablesFormations[0].getIdentite());
	}

	@Test(expected = PersistenceException.class)
	public void testInsertIdentiteWithException() {
		Identite identite = new Identite();
		identite.setEmail("test@test.fr");
		identite.setNom("Test");
		identite.setPrenom("Test");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);

		dao.insert(identite, null, null);
		dao.insert(identite, null, null);
	}

	@Test(expected = PersistenceException.class)
	public void testUpdateIdentiteWithException() {
		Identite identite = new Identite();
		Identite identiteToUpdate = new Identite();
		identite.setEmail("test@test.fr");
		identite.setNom("Test");
		identite.setPrenom("Test");
		identite.setSex("M");
		identite.setType(Identite.TypeIdentite.CANDIDAT);
		identite.setId(1L);

		em.getTransaction().begin();
		em.persist(identite);
		em.getTransaction().commit();

		identiteToUpdate.setEmail("test2@test.fr");
		identiteToUpdate.setId(identite.getId());

		dao.update(identiteToUpdate);
		dao.update(identiteToUpdate);
	}

}