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
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private IdentiteDao identiteDao;
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.emf = Persistence.createEntityManagerFactory("Identite");
        this.em = this.emf.createEntityManager();
        this.tx = this.em.getTransaction();
        this.identiteDao = OracleIdentiteDao.getDAO();
    }

    @After
    public void cleanup() {
        if (this.em != null) {
            this.em.close();
        }
        if (this.emf != null) {
            this.emf.close();
        }
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        this.identiteDao.insert(identite, "nomForm", dateLimite);

        verify(this.em).persist(identite);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertWithException() {
        Identite identite = new Identite();
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);

        IdentiteDao identiteDao = spy(OracleIdentiteDao.class);
        doThrow(new Exception("Test Exception")).when(identiteDao).insert(identite, "nomForm", dateLimite);

        assertThrows(IllegalStateException.class, () -> identiteDao.insert(identite, "nomForm", dateLimite));

        verify(em).persist(identite);
        verifyNoMoreInteractions(em);
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        Identite identiteToUpdate = new Identite();
        identiteToUpdate.setId(1L);
        identiteToUpdate.setEmail("john.doe@example.com");

        this.identiteDao.update(identiteToUpdate);

        verify(this.em).find(Identite.class, 1L);
        verify(this.em).getTransaction();
        verify(this.em).merge(identiteToUpdate);
        verify(this.em).getTransaction().commit();
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateWithException() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        Identite identiteToUpdate = new Identite();
        identiteToUpdate.setId(1L);
        identiteToUpdate.setEmail("john.doe@example.com");

        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(emf.createEntityManager()).thenReturn(em);
        when(em.find(Identite.class, 1L)).thenReturn(identite);
        when(em.getTransaction()).thenReturn(tx);

        doThrow(new Exception("Test Exception")).when(identiteDao).update(identiteToUpdate);

        assertThrows(IllegalStateException.class, () -> this.identiteDao.update(identiteToUpdate));

        verify(this.em).find(Identite.class, 1L);
        verify(this.em).getTransaction();
        verify(this.em).merge(identiteToUpdate);
        verify(this.em).getTransaction().rollback();
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1L);

        this.identiteDao.delete(identite);

        verify(this.em).find(Identite.class, 1L);
        verify(this.em).getTransaction();
        verify(this.em).remove(identite);
        verify(this.em).getTransaction().commit();
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteWithException() {
        Identite identite = new Identite();
        identite.setId(1L);

        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(emf.createEntityManager()).thenReturn(em);
        when(em.find(Identite.class, 1L)).thenReturn(identite);
        when(em.getTransaction()).thenReturn(tx);

        doThrow(new Exception("Test Exception")).when(identiteDao).delete(identite);

        assertThrows(IllegalStateException.class, () -> this.identiteDao.delete(identite));

        verify(this.em).find(Identite.class, 1L);
        verify(this.em).getTransaction();
        verify(this.em).remove(identite);
        verify(this.em).getTransaction().rollback();
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1L);
        identite1.setNom("Doe1");
        identite1.setPrenom("John1");
        identite1.setSex('M');
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar1 = Calendar.getInstance();
        Date dateLimite1 = calendar1.getTime();
        identite1.setDateLimite(dateLimite1);

        Identite identite2 = new Identite();
        identite2.setId(2L);
        identite2.setNom("Doe2");
        identite2.setPrenom("John2");
        identite2.setSex('M');
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar2 = Calendar.getInstance();
        Date dateLimite2 = calendar2.getTime();
        identite2.setDateLimite(dateLimite2);

        List<Identite> expectedList = new ArrayList<>();
        expectedList.add(identite1);
        expectedList.add(identite2);

        when(this.em.createQuery("select ident from Identite as ident where ident.nom='Doe' and ident.prenom='John' and ident.sex='M' and "
                + "(ident.type='CANDIDAT' or ident.type is null)")).thenReturn(mock(TypedQuery.class));

        List<Identite> resultList = this.identiteDao.find("Doe", "John", 'M', null, null);

        assertEquals(expectedList, resultList);
        verify(this.em).createQuery("select ident from Identite as ident where ident.nom='Doe' and ident.prenom='John' and ident.sex='M' and "
                + "(ident.type='CANDIDAT' or ident.type is null)");
    }

    @Test
    public void testFindCandidat() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('M');
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        Candidat candidat = new Candidat();
        candidat.setId(1L);
        candidat.setIdentite(identite);

        when(this.em.createQuery("select ident from Identite as ident where ident.nom='Doe' and ident.prenom='John' and ident.sex='M' and ident.type='CANDIDAT'")).thenReturn(mock(TypedQuery.class));

        Candidat[] result = this.identiteDao.findCandidat("Doe", "John", 'M', null);

        assertEquals(candidat, result[0]);
        verify(this.em).createQuery("select ident from Identite as ident where ident.nom='Doe' and ident.prenom='John' and ident.sex='M' and ident.type='CANDIDAT'");
    }

    @Test
    public void testFindRespForm() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("Doe");
        identite.setPrenom("John");
        identite.setSex('F');
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        Calendar calendar = Calendar.getInstance();
        Date dateLimite = calendar.getTime();
        identite.setDateLimite(dateLimite);

        RespFormation respFormation = new RespFormation();
        respFormation.setId(1L);
        respFormation.setIdentite(identite);

        when(this.em.createQuery("select ident from Identite as ident where ident.nom='Doe' and ident.prenom='John' and ident.sex='F' and ident.type='FORMATEUR'")).thenReturn(mock(TypedQuery.class));

        RespFormation[] result = this.identiteDao.findRespForm("Doe", "John", 'F', null);

        assertEquals(respFormation, result[0]);
        verify(this.em).createQuery("select ident from Identite as ident where ident.nom='Doe' and ident.prenom='John' and ident.sex='F' and ident.type='FORMATEUR'");
    }

    @Test
    public void testFindEmptyList() {
        when(this.em.createQuery("select ident from Identite as ident where ident.nom is null")).thenReturn(mock(TypedQuery.class));

        List<Identite> resultList = this.identiteDao.find(null, null, null, null, null);

        assertTrue(resultList.isEmpty());
    }
}