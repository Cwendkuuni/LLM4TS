package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("unchecked")
public class OracleIdentiteDaoTest {

    @Mock
    private EntityManagerFactory emf;

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction tx;

    @InjectMocks
    private OracleIdentiteDao dao;

    @Test
    public void testInsert() {
        Identite identite = mock(Identite.class);
        String nomForm = "nomForm";
        Calendar dateLimite = mock(Calendar.class);

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.persist((Object)identite)).thenReturn(identite);

        dao.insert(identite, nomForm, dateLimite);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verify(em).persist((Object)identite);
        verify(tx).commit();
    }

    @Test(expected = Exception.class)
    public void testInsertWithException() {
        Identite identite = mock(Identite.class);
        String nomForm = "nomForm";
        Calendar dateLimite = mock(Calendar.class);

        when(emf.createEntityManagerFactory(eq("Identite"))).thenThrow(new Exception("Test exception"));

        dao.insert(identite, nomForm, dateLimite);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verifyNoMoreInteractions(em);
        verifyNoMoreInteractions(tx);
    }

    @Test
    public void testUpdate() {
        Identite identite = mock(Identite.class);
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.find((Class)identite.getClass(), (Object)identite.getId())).thenReturn(identite);
        when(identite.setEmail(email)).thenReturn(identite);
        when(identite.setNom(nom)).thenReturn(identite);
        when(identite.setPrenom(prenom)).thenReturn(identite);
        when(identite.setSex(sex)).thenReturn(identite);
        when(em.merge((Object)identite)).thenReturn(identite);
        when(tx.commit()).thenReturn(null);

        dao.update(identite);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verify(em).find((Class)identite.getClass(), (Object)identite.getId());
        verify(identite).setEmail(email);
        verify(identite).setNom(nom);
        verify(identite).setPrenom(prenom);
        verify(identite).setSex(sex);
        verify(em).merge((Object)identite);
        verify(tx).commit();
    }

    @Test(expected = Exception.class)
    public void testUpdateWithException() {
        Identite identite = mock(Identite.class);

        when(emf.createEntityManagerFactory(eq("Identite"))).thenThrow(new Exception("Test exception"));

        dao.update(identite);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verifyNoMoreInteractions(em);
        verifyNoMoreInteractions(tx);
    }

    @Test
    public void testDelete() {
        Identite identite = mock(Identite.class);
        Long id = 1L;

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.find((Class)identite.getClass(), (Object)id)).thenReturn(identite);
        when(em.remove((Object)identite)).thenReturn(identite);
        when(tx.commit()).thenReturn(null);

        dao.delete(identite);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verify(em).find((Class)identite.getClass(), (Object)id);
        verify(em).remove((Object)identite);
        verify(tx).commit();
    }

    @Test(expected = Exception.class)
    public void testDeleteWithException() {
        Identite identite = mock(Identite.class);
        Long id = 1L;

        when(emf.createEntityManagerFactory(eq("Identite"))).thenThrow(new Exception("Test exception"));

        dao.delete(identite);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verifyNoMoreInteractions(em);
        verifyNoMoreInteractions(tx);
    }

    @Test
    public void testFind() {
        List<Identite> identites = Arrays.asList(mock(Identite.class), mock(Identite.class));
        Identite identite = mock(Identite.class);
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
        when(em.find((Class)identite.getClass(), (Object)1L)).thenReturn(identite);

        when(identite.getNom()).thenReturn(nom);
        when(identite.getPrenom()).thenReturn(prenom);
        when(identite.getSex()).thenReturn(sex);
        when(identite.getType()).thenReturn(type);

        when(em.createQuery(anyString()).setParameter("CANDIDAT", (Object)type)).thenReturn(mock(TypedQuery.class));
        when(em.createQuery(anyString()).setParameter("FORMATEUR", (Object)type)).thenReturn(mock(TypedQuery.class));
        when(em.createQuery(anyString()).getResultList()).thenReturn(identites);

        List<Identite> result = dao.find(nom, prenom, sex, email, type);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verify(em).getTransaction();
        verify(em).createQuery(anyString());
        verify(em).find((Class)identite.getClass(), (Object)1L);
        verify(em).createQuery(eq("select ident from Identite as ident where ident.nom='nom' and ident.prenom='prenom' and ident.sex='sex' and (ident.type= :CANDIDAT or ident.type is null) order by ident.id asc")).setParameter("CANDIDAT", (Object)type);
        verify(em).createQuery(eq("select ident from Identite as ident where ident.nom like '%nom%' and ident.prenom like '%prenom%' and ident.sex like '%sex%' and (ident.type= :CANDIDAT or ident.type is null) order by ident.id asc")).setParameter("CANDIDAT", (Object)type);
        verify(em).getResultList();
        assertEquals(identites, result);
    }

    @Test
    public void testFindCandidat() {
        List<Identite> identites = Arrays.asList(mock(Identite.class), mock(Identite.class));
        Identite identite = mock(Identite.class);
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
        when(em.find((Class)identite.getClass(), (Object)1L)).thenReturn(identite);

        when(identite.getNom()).thenReturn(nom);
        when(identite.getPrenom()).thenReturn(prenom);
        when(identite.getSex()).thenReturn(sex);
        when(identite.isCandidat()).thenReturn(true);

        when(em.createQuery(eq("select ident from Identite as ident where ident.nom like '%nom%' and ident.prenom like '%prenom%' and ident.sex like '%sex%' and ident.type= :CANDIDAT order by ident.id asc")).getResultList()).thenReturn(identites);

        List<Identite> result = dao.find(nom, prenom, sex, email, null);
        Candidat[] resultCandidat = result.toArray(new Candidat[result.size()]);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verify(em).getTransaction();
        verify(em).createQuery(eq("select ident from Identite as ident where ident.nom like '%nom%' and ident.prenom like '%prenom%' and ident.sex like '%sex%' and ident.type= :CANDIDAT order by ident.id asc")).setParameter("CANDIDAT", (Object)Identite.TypeIdentite.CANDIDAT);
        verify(em).getResultList();
        assertEquals(identites.size(), result.size());
        for (int i = 0; i < resultCandidat.length; i++) {
            assertTrue(resultCandidat[i] instanceof Candidat);
        }
    }

    @Test
    public void testFindRespForm() {
        List<Identite> identites = Arrays.asList(mock(Identite.class), mock(Identite.class));
        Identite identite = mock(Identite.class);
        String nom = "nom";
        String prenom = "prenom";
        String sex = "sex";
        String email = "email";

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        when(em.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
        when(em.find((Class)identite.getClass(), (Object)1L)).thenReturn(identite);

        when(identite.getNom()).thenReturn(nom);
        when(identite.getPrenom()).thenReturn(prenom);
        when(identite.getSex()).thenReturn(sex);
        when(identite.isFormateur()).thenReturn(true);

        when(em.createQuery(eq("select ident from Identite as ident where ident.nom like '%nom%' and ident.prenom like '%prenom%' and ident.sex like '%sex%' and ident.type= :FORMATEUR order by ident.id asc")).getResultList()).thenReturn(identites);

        List<Identite> result = dao.find(nom, prenom, sex, email, null);
        RespFormation[] resultRespForm = result.toArray(new RespFormation[result.size()]);

        verify(emf).createEntityManagerFactory(eq("Identite"));
        verify(em).getTransaction();
        verify(em).createQuery(eq("select ident from Identite as ident where ident.nom like '%nom%' and ident.prenom like '%prenom%' and ident.sex like '%sex%' and ident.type= :FORMATEUR order by ident.id asc")).setParameter("FORMATEUR", (Object)Identite.TypeIdentite.FORMATEUR);
        verify(em).getResultList();
        assertEquals(identites.size(), result.size());
        for (int i = 0; i < resultRespForm.length; i++) {
            assertTrue(resultRespForm[i] instanceof RespFormation);
        }
    }
}