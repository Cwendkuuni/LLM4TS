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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.unice.gfarce.dao.Identite;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    private static final String PERSIST_UNIT_NAME = "Identite";
    private static final String EMPTY_STRING = "";
    private static final String NOM = "Dupont";
    private static final String PRENOM = "Jean";
    private static final String SEX = "M";
    private static final String EMAIL = "jean.dupont@email.com";
    private static final Calendar DATE_LIMITE = Calendar.getInstance();
    private static final Date DATE_LIMITE_VALUE = DATE_LIMITE.getTime();
    private static final TypeIdentite TYPE_CANDIDAT = Identite.TypeIdentite.CANDIDAT;
    private static final TypeIdentite TYPE_FORMATEUR = Identite.TypeIdentite.FORMATEUR;

    private OracleIdentiteDao dao;
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    private List<Identite> identites;
    private Candidat candidat;
    private RespFormation respFormation;

    @PersistenceContext
    private PersistenceUnitManager puManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.emf = this.puManager.getPersistenceUnitInfo(PERSIST_UNIT_NAME).getPersistenceUnitInfo();
        this.em = this.emf.createEntityManager();
        this.tx = this.em.getTransaction();
        this.identites = new ArrayList<>();
        this.candidat = new Candidat();
        this.candidat.setNom(NOM);
        this.candidat.setPrenom(PRENOM);
        this.candidat.setSex(SEX);
        this.candidat.setEmail(EMAIL);
        this.candidat.setType(TYPE_CANDIDAT);
        this.respFormation = new RespFormation();
        this.respFormation.setNom(NOM);
        this.respFormation.setPrenom(PRENOM);
        this.respFormation.setSex(SEX);
        this.respFormation.setEmail(EMAIL);
        this.respFormation.setType(TYPE_FORMATEUR);
        this.dao = OracleIdentiteDao.getDAO();
    }

    @Test
    public void testInsert() {
        // Given
        when(this.em.find((Class)this.candidat.getClass(), (Object)this.candidat.getId())).thenReturn(null);

        // When
        this.dao.insert(this.candidat, EMPTY_STRING, DATE_LIMITE);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).persist((Object)this.candidat);
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        assertEquals(1, this.identites.size());
        assertEquals(this.candidat, this.identites.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullIdentite() {
        // Given

        // When
        this.dao.insert(null, EMPTY_STRING, DATE_LIMITE);

        // Then
        verify(this.em, never()).getTransaction();
        verify(this.em, never()).persist((Object)any());
        verify(this.em, never()).getTransaction().commit();
        verify(this.em, never()).close();
        verify(this.emf, never()).close();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testUpdate() {
        // Given
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail(EMAIL);
        identite.setNom(NOM);
        identite.setPrenom(PRENOM);
        identite.setSex(SEX);
        identite.setType(TYPE_CANDIDAT);
        when(this.em.find((Class)this.candidat.getClass(), (Object)this.candidat.getId())).thenReturn(identite);

        // When
        this.dao.update(this.candidat);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).merge((Object)this.candidat);
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        assertEquals(this.candidat, identite);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullIdentite() {
        // Given

        // When
        this.dao.update(null);

        // Then
        verify(this.em, never()).getTransaction();
        verify(this.em, never()).merge((Object)any());
        verify(this.em, never()).getTransaction().commit();
        verify(this.em, never()).close();
        verify(this.emf, never()).close();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testDelete() {
        // Given
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail(EMAIL);
        identite.setNom(NOM);
        identite.setPrenom(PRENOM);
        identite.setSex(SEX);
        identite.setType(TYPE_CANDIDAT);
        when(this.em.find((Class)this.candidat.getClass(), (Object)this.candidat.getId())).thenReturn(identite);

        // When
        this.dao.delete(this.candidat);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).remove((Object)identite);
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        verify(identite, never()).setEmail(anyString());
        verify(identite, never()).setNom(anyString());
        verify(identite, never()).setPrenom(anyString());
        verify(identite, never()).setSex(anyString());
        verify(identite, never()).setType(any(TypeIdentite.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullIdentite() {
        // Given

        // When
        this.dao.delete(null);

        // Then
        verify(this.em, never()).getTransaction();
        verify(this.em, never()).remove((Object)any());
        verify(this.em, never()).getTransaction().commit();
        verify(this.em, never()).close();
        verify(this.emf, never()).close();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testFind() {
        // Given
        Identite identite1 = new Identite();
        identite1.setId(1L);
        identite1.setEmail(EMAIL);
        identite1.setNom(NOM);
        identite1.setPrenom(PRENOM);
        identite1.setSex(SEX);
        identite1.setType(TYPE_CANDIDAT);
        Identite identite2 = new Identite();
        identite2.setId(2L);
        identite2.setEmail("jean.durand@email.com");
        identite2.setNom(NOM);
        identite2.setPrenom("Jean-Marc");
        identite2.setSex(SEX);
        identite2.setType(TYPE_CANDIDAT);
        Identite identite3 = new Identite();
        identite3.setId(3L);
        identite3.setEmail("jean.pierre@email.com");
        identite3.setNom(NOM);
        identite3.setPrenom("Jean-Pierre");
        identite3.setSex(SEX);
        identite3.setType(TYPE_FORMATEUR);
        when(this.em.createQuery(anyString())).thenAnswer(invocation -> invocation.callRealMethod());

        // When
        List<Identite> result = this.dao.find(NOM, PRENOM, SEX, EMAIL, null);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).createQuery(anyString());
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        assertEquals(2, result.size());
        assertTrue(result.contains(identite1));
        assertTrue(result.contains(identite2));
        assertFalse(result.contains(identite3));
    }

    @Test
    public void testFindCandidat() {
        // Given
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail(EMAIL);
        identite.setNom(NOM);
        identite.setPrenom(PRENOM);
        identite.setSex(SEX);
        identite.setType(TYPE_CANDIDAT);
        when(this.em.createQuery(anyString())).thenAnswer(invocation -> invocation.callRealMethod());

        // When
        Candidat[] result = this.dao.findCandidat(NOM, PRENOM, SEX, EMAIL);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).createQuery(eq("select ident from Identite as ident where ident.nom='Dupont' and ident.prenom='Jean' and ident.sex='M' and ident.type= :CANDIDAT"));
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        assertEquals(1, result.length);
        assertEquals(identite, result[0]);
    }

    @Test
    public void testFindRespForm() {
        // Given
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail(EMAIL);
        identite.setNom(NOM);
        identite.setPrenom(PRENOM);
        identite.setSex(SEX);
        identite.setType(TYPE_FORMATEUR);
        when(this.em.createQuery(anyString())).thenAnswer(invocation -> invocation.callRealMethod());

        // When
        RespFormation[] result = this.dao.findRespForm(NOM, PRENOM, SEX, EMAIL);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).createQuery(eq("select ident from Identite as ident where ident.nom='Dupont' and ident.prenom='Jean' and ident.sex='M' and ident.type= :FORMATEUR"));
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        assertEquals(1, result.length);
        assertEquals(identite, result[0]);
    }

    @Test
    public void testFindEmpty() {
        // Given
        when(this.em.createQuery(anyString())).thenAnswer(invocation -> invocation.callRealMethod());

        // When
        List<Identite> result = this.dao.find(NOM, PRENOM, SEX, EMAIL, null);

        // Then
        verify(this.em, times(1)).getTransaction();
        verify(this.em, times(1)).createQuery(anyString());
        verify(this.em, times(1)).getTransaction().commit();
        verify(this.em, times(1)).close();
        verify(this.emf, times(1)).close();
        assertEquals(0, result.size());
    }
}