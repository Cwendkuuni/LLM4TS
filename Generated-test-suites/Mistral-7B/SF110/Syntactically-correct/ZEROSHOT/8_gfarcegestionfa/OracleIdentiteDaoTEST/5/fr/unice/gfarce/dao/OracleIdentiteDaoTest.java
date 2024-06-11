package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager entityManager;
 @InjectMocks
 private OracleIdentiteDao dao;

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Test Nom");
 identite.setPrenom("Test Prenom");
 identite.setEmail("test@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 dao.insert(identite, "Formulaire", calendar);

 // Verify that the entity was persisted
 verify(entityManager).persist(identite);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertWithNullEntity() {
 dao.insert(null, "Formulaire", new Date());

 // Verify that an exception is thrown
 verify(entityManager).createEntityManager();
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Test Nom Updated");
 identite.setPrenom("Test Prenom Updated");
 identite.setEmail("test@example.com");
 identite.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar = Calendar.getInstance();
 identite.setDateLimite(calendar.getTime());

 dao.update(identite);

 // Verify that the entity was updated
 verify(entityManager).merge(identite);
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateWithNullEntity() {
 dao.update(null);

 // Verify that an exception is thrown
 verify(entityManager).createEntityManager();
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);

 dao.delete(identite);

 // Verify that the entity was deleted
 verify(entityManager).remove(identite);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteWithNullEntity() {
 dao.delete(null);

 // Verify that an exception is thrown
 verify(entityManager).createEntityManager();
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Test Nom 1");
 identite1.setPrenom("Test Prenom 1");
 identite1.setEmail("test1@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);
 Calendar calendar1 = Calendar.getInstance();
 identite1.setDateLimite(calendar1.getTime());

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Test Nom 2");
 identite2.setPrenom("Test Prenom 2");
 identite2.setEmail("test2@example.com");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);
 Calendar calendar2 = Calendar.getInstance();
 identite2.setDateLimite(calendar2.getTime());

 List<Identite> identites = Arrays.asList(identite1, identite2);

 when(entityManager.createQuery("select ident from Identite as ident where nom = :nom and prenom = :prenom and sex = :sex and type = :type"))
 .thenReturn(mock(TypedQuery.class));

 when(mock(TypedQuery.class).setParameter("nom", "Test Nom")).thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("prenom", "Test Prenom"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("sex", "Test Sex"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("type", Identite.TypeIdentite.CANDIDAT))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).getResultList()).thenReturn(identites);

 List<Identite> result = dao.find("Test Nom", "Test Prenom", "Test Sex", "", Identite.TypeIdentite.CANDIDAT);

 assertEquals(identite1, result.get(0));
 }

 @Test
 public void testFindEmptyResult() {
 when(entityManager.createQuery("select ident from Identite as ident where nom = :nom and prenom = :prenom and sex = :sex and type = :type"))
 .thenReturn(mock(TypedQuery.class));

 when(mock(TypedQuery.class).getResultList()).thenReturn(Collections.emptyList());

 List<Identite> result = dao.find("Test Nom", "Test Prenom", "Test Sex", "", Identite.TypeIdentite.CANDIDAT);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindCandidat() {
 Candidat candidat1 = new Candidat();
 candidat1.setId(1L);
 candidat1.setNom("Test Nom 1");
 candidat1.setPrenom("Test Prenom 1");
 candidat1.setEmail("test1@example.com");
 candidat1.setSex("Test Sex 1");

 Candidat candidat2 = new Candidat();
 candidat2.setId(2L);
 candidat2.setNom("Test Nom 2");
 candidat2.setPrenom("Test Prenom 2");
 candidat2.setEmail("test2@example.com");
 candidat2.setSex("Test Sex 2");

 List<Identite> identites = Arrays.asList(new Identite(candidat1), new Identite(candidat2));

 when(entityManager.createQuery("select ident from Identite as ident where nom = :nom and prenom = :prenom and sex = :sex and type = :type"))
 .thenReturn(mock(TypedQuery.class));

 when(mock(TypedQuery.class).setParameter("nom", "Test Nom"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("prenom", "Test Prenom"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("sex", "Test Sex"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("type", Identite.TypeIdentite.CANDIDAT))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).getResultList()).thenReturn(identites);

 Candidat[] result = dao.findCandidat("Test Nom", "Test Prenom", "Test Sex", "");

 assertEquals(candidat1, result[0]);
 }

 @Test
 public void testFindCandidatEmptyResult() {
 when(entityManager.createQuery("select ident from Identite as ident where nom = :nom and prenom = :prenom and sex = :sex and type = :type"))
 .thenReturn(mock(TypedQuery.class));

 when(mock(TypedQuery.class).getResultList()).thenReturn(Collections.emptyList());

 Candidat[] result = dao.findCandidat("Test Nom", "Test Prenom", "Test Sex", "");

 assertTrue(Arrays.isEmpty(result));
 }

 @Test
 public void testFindRespFormation() {
 RespFormation respFormation1 = new RespFormation();
 respFormation1.setId(1L);
 respFormation1.setNom("Test Nom 1");
 respFormation1.setPrenom("Test Prenom 1");
 respFormation1.setEmail("test1@example.com");
 respFormation1.setSex("Test Sex 1");

 RespFormation respFormation2 = new RespFormation();
 respFormation2.setId(2L);
 respFormation2.setNom("Test Nom 2");
 respFormation2.setPrenom("Test Prenom 2");
 respFormation2.setEmail("test2@example.com");
 respFormation2.setSex("Test Sex 2");

 List<Identite> identites = Arrays.asList(new Identite(respFormation1), new Identite(respFormation2));

 when(entityManager.createQuery("select ident from Identite as ident where nom = :nom and prenom = :prenom and sex = :sex and type = :type"))
 .thenReturn(mock(TypedQuery.class));

 when(mock(TypedQuery.class).setParameter("nom", "Test Nom"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("prenom", "Test Prenom"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("sex", "Test Sex"))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).setParameter("type", Identite.TypeIdentite.FORMATEUR))
 .thenReturn(mock(TypedQuery.class));
 when(mock(TypedQuery.class).getResultList()).thenReturn(identites);

 RespFormation[] result = dao.findRespForm("Test Nom", "Test Prenom", "Test Sex", "");

 assertEquals(respFormation1, result[0]);
 }

 @Test
 public void testFindRespFormEmptyResult() {
 when(entityManager.createQuery("select ident from Identite as ident where nom = :nom and prenom = :prenom and sex = :sex and type = :type"))
 .thenReturn(mock(TypedQuery.class));

 when(mock(TypedQuery.class).getResultList()).thenReturn(Collections.emptyList());

 RespFormation[] result = dao.findRespForm("Test Nom", "Test Prenom", "Test Sex", "");

 assertTrue(Arrays.isEmpty(result));
 }
}