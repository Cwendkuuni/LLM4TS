package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;
import org.mockito.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    private IdentiteDao identiteDao;
    private Identite identiteMock;

    @Before
    public void setUp() {
        emf = Mockito.mock(EntityManagerFactory.class);
        em = Mockito.mock(EntityManager.class);
        tx = Mockito.mock(EntityTransaction.class);
        identiteDao = new OracleIdentiteDao();
        identiteDao.emf = emf;
        identiteDao.em = em;
        identiteMock = Mockito.mock(Identite.class);
    }

    @Test
    public void testInsert() {
        given(emf.createEntityManager()).willReturn(em);
        given(em.getTransaction()).willReturn(tx);
        when(em.persist((Object) identiteMock)).thenReturn(identiteMock);
        identiteDao.insert(identiteMock, "nomForm", new GregorianCalendar(1990, 1, 1));
        verify(tx).commit();
    }

    @Test(expected = Exception.class)
    public void testInsert_Exception() {
        given(emf.createEntityManager()).willThrow(new PersistenceException());
        identiteDao.insert(identiteMock, "nomForm", new GregorianCalendar(1990, 1, 1));
        verify(tx).rollback();
    }

    @Test
    public void testUpdate() {
        given(emf.createEntityManager()).willReturn(em);
        given(em.getTransaction()).willReturn(tx);
        given(em.find((Class) identiteMock.getClass(), (Object) identiteMock.getId())).willReturn(identiteMock);
        given(em.merge((Object) identiteMock)).willReturn(identiteMock);
        identiteDao.update(identiteMock);
        verify(tx).commit();
    }

    @Test(expected = Exception.class)
    public void testUpdate_Exception() {
        given(emf.createEntityManager()).willThrow(new PersistenceException());
        identiteDao.update(identiteMock);
        verify(tx).rollback();
    }

    @Test
    public void testDelete() {
        given(emf.createEntityManager()).willReturn(em);
        given(em.getTransaction()).willReturn(tx);
        given(em.find((Class) identiteMock.getClass(), (Object) identiteMock.getId())).willReturn(identiteMock);
        identiteDao.delete(identiteMock);
        verify(tx).commit();
    }

    @Test(expected = Exception.class)
    public void testDelete_Exception() {
        given(emf.createEntityManager()).willThrow(new PersistenceException());
        identiteDao.delete(identiteMock);
        verify(tx).rollback();
    }

    @Test
    public void testFind() {
        given(emf.createEntityManager()).willReturn(em);
        given(em.getTransaction()).willReturn(tx);

        List<Identite> list = Arrays.asList(identiteMock, new Identite());
        when(em.createQuery("select ident from Identite as ident")).thenReturn(mock(TypedQuery.class));
        when(em.createQuery("select ident from Identite as ident").getResultList()).thenReturn(list);

        List<Identite> result = identiteDao.find(null, null, null, null, null);
        assertEquals(2, result.size());
    }

    @Test
    public void testFind_TypeIdentite_CANDIDAT() {
        given(emf.createEntityManager()).willReturn(em);
        given(em.getTransaction()).willReturn(tx);

        List<Identite> list = Arrays.asList(identiteMock);
        when(em.createQuery("select ident from Identite as ident where ident.nom like '%' and ident.prenom like '%' and ident.sex like '%' and ident.type= :CANDIDAT")).thenReturn(mock(TypedQuery.class));
        when(em.createQuery("select ident from Identite as ident where ident.nom like '%' and ident.prenom like '%' and ident.sex like '%' and ident.type= :CANDIDAT").setParameter("CANDIDAT", (Object) Identite.TypeIdentite.CANDIDAT)).thenReturn(mock(TypedQuery.class));
        when(em.createQuery("select ident from Identite as ident where ident.nom like '%' and ident.prenom like '%' and ident.sex like '%' and ident.type= :CANDIDAT").getResultList()).thenReturn(list);

        List<Identite> result = identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, result.size());
    }

    @Test
    public void testFind_TypeIdentite_FORMATEUR() {
        given(emf.createEntityManager()).willReturn(em);
        given(em.getTransaction()).willReturn(tx);

        List<Identite> list = Arrays.asList(identiteMock);
        when(em.createQuery("select ident from Identite as ident where ident.nom like '%' and ident.prenom like '%' and ident.sex like '%' and ident.type= :FORMATEUR")).thenReturn(mock(TypedQuery.class));
        when(em.createQuery("select ident from Identite as ident where ident.nom like '%' and ident.prenom like '%' and ident.sex like '%' and ident.type= :FORMATEUR")).setParameter("FORMATEUR", (Object) Identite.TypeIdentite.FORMATEUR).thenReturn(mock(TypedQuery.class));
        when(em.createQuery("select ident from Identite as ident where ident.nom like '%' and ident.prenom like '%' and ident.sex like '%' and ident.type= :FORMATEUR").getResultList()).thenReturn(list);

        List<Identite> result = identiteDao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, result.size());
    }

    @After
    public void tearDown() {
        reset(emf, em, tx, identiteMock);
    }
}