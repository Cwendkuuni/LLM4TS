package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    private static final String PERSIST_UNIT_NAME = "Identite";
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    private IdentiteDao identiteDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        emf = mock(EntityManagerFactory.class);
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        doReturn(emf).when(em).getEntityManagerFactory();
        doReturn(em).when(emf).createEntityManager();
        doReturn(tx).when(em).getTransaction();
        identiteDao = new OracleIdentiteDao();
        identiteDao.setPersistUnitName(PERSIST_UNIT_NAME);
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
        final Identite identite = new Identite();
        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);

        identiteDao.insert(identite, "nomForm", getCalendar(2023, 1, 1));

        verify(em).persist((Object)identite);
        verify(tx).commit();
    }

    @Test(expected = PersistenceException.class)
    public void testInsertWithException() {
        final Identite identite = new Identite();
        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        when(em.getTransaction()).thenReturn(tx);
        doThrow(new PersistenceException()).when(tx).commit();

        identiteDao.insert(identite, "nomForm", getCalendar(2023, 1, 1));

        verify(em).persist((Object)identite);
        verify(tx).rollback();
    }

    @Test
    public void testUpdate() {
        final Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        final Identite existingIdentite = new Identite();
        existingIdentite.setId(1L);
        existingIdentite.setEmail("existing@email.com");
        existingIdentite.setNom("Existing");
        existingIdentite.setPrenom("Existing");
        existingIdentite.setSex('M');
        existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);

        when(em.find((Class<Identite>)identite.getClass(), (Object)identite.getId())).thenReturn(existingIdentite);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);

        identiteDao.update(identite);

        verify(existingIdentite).setEmail(identite.getEmail());
        verify(existingIdentite).setNom(identite.getNom());
        verify(existingIdentite).setPrenom(identite.getPrenom());
        verify(existingIdentite).setSex(identite.getSex());
        verify(existingIdentite).setType(identite.getType());
        verify(em).merge((Object)identite);
        verify(tx).commit();
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithException() {
        final Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail("test@email.com");
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        final Identite existingIdentite = new Identite();
        existingIdentite.setId(1L);
        existingIdentite.setEmail("existing@email.com");
        existingIdentite.setNom("Existing");
        existingIdentite.setPrenom("Existing");
        existingIdentite.setSex('M');
        existingIdentite.setType(Identite.TypeIdentite.CANDIDAT);

        when(em.find((Class<Identite>)identite.getClass(), (Object)identite.getId())).thenReturn(existingIdentite);
        when(em.getTransaction()).thenReturn(tx);
        doThrow(new PersistenceException()).when(tx).commit();

        identiteDao.update(identite);

        verify(existingIdentite).setEmail(identite.getEmail());
        verify(existingIdentite).setNom(identite.getNom());
        verify(existingIdentite).setPrenom(identite.getPrenom());
        verify(existingIdentite).setSex(identite.getSex());
        verify(existingIdentite).setType(identite.getType());
        verify(em).merge((Object)identite);
        verify(tx).rollback();
    }

    @Test
    public void testDelete() {
        final Identite identite = new Identite();
        identite.setId(1L);

        when(em.find((Class<Identite>)identite.getClass(), (Object)identite.getId())).thenReturn(identite);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);

        identiteDao.delete(identite);

        verify(em).remove((Object)identite);
        verify(tx).commit();
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteWithException() {
        final Identite identite = new Identite();
        identite.setId(1L);

        when(em.find((Class<Identite>)identite.getClass(), (Object)identite.getId())).thenReturn(identite);
        when(em.getTransaction()).thenReturn(tx);
        doThrow(new PersistenceException()).when(tx).commit();

        identiteDao.delete(identite);

        verify(em).remove((Object)identite);
        verify(tx).rollback();
    }

    @Test
    public void testFind() {
        final Identite identite1 = new Identite();
        identite1.setId(1L);
        identite1.setEmail("email1@email.com");
        identite1.setNom("Nom1");
        identite1.setPrenom("Prenom1");
        identite1.setSex('M');
        identite1.setType(Identite.TypeIdentite.CANDIDAT);

        final Identite identite2 = new Identite();
        identite2.setId(2L);
        identite2.setEmail("email2@email.com");
        identite2.setNom("Nom2");
        identite2.setPrenom("Prenom2");
        identite2.setSex('F');
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        final List<Identite> expectedList = new ArrayList<>();
        expectedList.add(identite1);
        expectedList.add(identite2);

        when(em.createQuery("select ident from Identite as ident where ident.nom='Nom1' and ident.prenom='Prenom1' and ident.sex='M' and ident.type=:type"))
                .thenReturn(createTypedQuery(expectedList));

        final String nom = "Nom1";
        final String prenom = "Prenom1";
        final String sex = "M";
        final Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        final List<Identite> resultList = identiteDao.find(nom, prenom, sex, null, type);

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testFindEmpty() {
        when(em.createQuery("select ident from Identite as ident where ident.nom='Nom1' and ident.prenom='Prenom1' and ident.sex='M' and ident.type=:type"))
                .thenReturn(createTypedQuery(new ArrayList<>()));

        final String nom = "Nom1";
        final String prenom = "Prenom1";
        final String sex = "M";
        final Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        final List<Identite> resultList = identiteDao.find(nom, prenom, sex, null, type);

        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testFindCandidat() {
        final Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail("email@email.com");
        identite.setNom("Nom");
        identite.setPrenom("Prenom");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        when(em.createQuery("select ident from Identite as ident where ident.nom='Nom' and ident.prenom='Prenom' and ident.email=:email and ident.type=:type"))
                .thenReturn(createTypedQuery(identite));

        final String nom = "Nom";
        final String prenom = "Prenom";
        final String email = "email@email.com";

        final Candidat[] result = identiteDao.findCandidat(nom, prenom, null, email);

        assertArrayEquals(new Candidat[]{identite}, result);
    }

    @Test
    public void testFindCandidatEmpty() {
        when(em.createQuery("select ident from Identite as ident where ident.nom='Nom' and ident.prenom='Prenom' and ident.email=:email and ident.type=:type"))
                .thenReturn(createTypedQuery(new ArrayList<>()));

        final String nom = "Nom";
        final String prenom = "Prenom";
        final String email = "email@email.com";

        final Candidat[] result = identiteDao.findCandidat(nom, prenom, null, email);

        assertNull(result);
    }

    @Test
    public void testFindRespForm() {
        final Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail("email@email.com");
        identite.setNom("Nom");
        identite.setPrenom("Prenom");
        identite.setSex('F');
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        when(em.createQuery("select ident from Identite as ident where ident.nom='Nom' and ident.prenom='Prenom' and ident.email=:email and ident.type=:type"))
                .thenReturn(createTypedQuery(identite));

        final String nom = "Nom";
        final String prenom = "Prenom";
        final String email = "email@email.com";

        final RespFormation[] result = identiteDao.findRespForm(nom, prenom, null, email);

        assertArrayEquals(new RespFormation[]{identite}, result);
    }

    @Test
    public void testFindRespFormEmpty() {
        when(em.createQuery("select ident from Identite as ident where ident.nom='Nom' and ident.prenom='Prenom' and ident.email=:email and ident.type=:type"))
                .thenReturn(createTypedQuery(new ArrayList<>()));

        final String nom = "Nom";
        final String prenom = "Prenom";
        final String email = "email@email.com";

        final RespFormation[] result = identiteDao.findRespForm(nom, prenom, null, email);

        assertNull(result);
    }

    private Calendar getCalendar(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    private TypedQuery<Identite> createTypedQuery(List<Identite> resultList) {
        final TypedQuery<Identite> typedQuery = mock(TypedQuery.class);
        when(typedQuery.getResultList()).thenReturn(resultList);
        return typedQuery;
    }
}