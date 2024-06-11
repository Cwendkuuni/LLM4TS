package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

@ org.junit.runner.RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private OracleIdentiteDao dao;
 private EntityManager entityManagerMock;

 @Before
 public void setUp() {
 entityManagerMock = mock(EntityManager.class);
 dao = new OracleIdentiteDao() {
 EntityManager em = entityManagerMock;
 };
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 dao.insert(identite, "nomForm", new GregorianCalendar(2022, 11, 11));
 verify(entityManagerMock, times(1)).persist((Object)identite);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 when(entityManagerMock.find((Class)Identite.class, (Object)identite.getId())).thenReturn(identite);
 dao.update(identite);
 verify(entityManagerMock, times(1)).merge((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNoIdentite() {
 dao.update(null);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 when(entityManagerMock.find((Class)Identite.class, (Object)identite.getId())).thenReturn(identite);
 dao.delete(identite);
 verify(entityManagerMock, times(1)).remove((Object)identite);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 dao.delete(null);
 }

 @Test
 public void testFind() {
 String nom = "nom";
 String prenom = "prenom";
 String sex = "sex";
 String email = "email";
 IdentiteType identiteType = IdentiteType.CANDIDAT;
 List<Identite> expectedIdentites = new ArrayList<>();
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("nom1");
 identite1.setPrenom("prenom1");
 identite1.setSex("sex1");
 identite1.setEmail("email1@email.com");
 identite1.setType(IdentiteType.CANDIDAT);
 expectedIdentites.add(identite1);

 when(entityManagerMock.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(entityManagerMock.setParameter(anyString(), anyObject())).thenReturn(entityManagerMock);
 when(entityManagerMock.getResultList()).thenReturn(expectedIdentites);

 List<Identite> resultIdentites = dao.find(nom, prenom, sex, email, identiteType);
 assertEquals(expectedIdentites, resultIdentites);
 }

 @Test
 public void testFindNoResults() {
 String nom = "nom";
 String prenom = "prenom";
 String sex = "sex";
 String email = "email";
 IdentiteType identiteType = IdentiteType.CANDIDAT;

 when(entityManagerMock.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(entityManagerMock.getResultList()).thenReturn(Collections.emptyList());

 List<Identite> resultIdentites = dao.find(nom, prenom, sex, email, identiteType);
 assertNotNull(resultIdentites);
 assertEquals(0, resultIdentites.size());
 }

 @Test
 public void testFindCandidat() {
 String nom = "nom";
 String prenom = "prenom";
 String sex = "sex";
 String email = "email";
 Candidat[] expectedCandidats = new Candidat[1];
 Candidat candidat = new Candidat();
 candidat.setId(1L);
 expectedCandidats[0] = candidat;

 when(entityManagerMock.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(entityManagerMock.setParameter(eq("CANDIDAT"), eq(IdentiteType.CANDIDAT))).thenReturn(entityManagerMock);
 when(entityManagerMock.getResultList()).thenReturn(Arrays.asList(candidat));

 Candidat[] resultCandidats = dao.findCandidat(nom, prenom, sex, email);
 assertEquals(expectedCandidats, resultCandidats);
 }

 @Test
 public void testFindRespForm() {
 String nom = "nom";
 String prenom = "prenom";
 String sex = "sex";
 String email = "email";
 RespFormation[] expectedRespFormations = new RespFormation[1];
 RespFormation respFormation = new RespFormation();
 respFormation.setId(1L);
 expectedRespFormations[0] = respFormation;

 when(entityManagerMock.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(entityManagerMock.setParameter(eq("FORMATEUR"), eq(IdentiteType.FORMATEUR))).thenReturn(entityManagerMock);
 when(entityManagerMock.getResultList()).thenReturn(Arrays.asList(respFormation));

 RespFormation[] resultRespFormations = dao.findRespForm(nom, prenom, sex, email);
 assertEquals(expectedRespFormations, resultRespFormations);
 }

 @Test
 public void testFindNoResultsCandidat() {
 String nom = "nom";
 String prenom = "prenom";
 String sex = "sex";
 String email = "email";

 when(entityManagerMock.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(entityManagerMock.getResultList()).thenReturn(Collections.emptyList());

 Candidat[] resultCandidats = dao.findCandidat(nom, prenom, sex, email);
 assertNotNull(resultCandidats);
 assertEquals(0, resultCandidats.length);
 }

 @Test
 public void testFindNoResultsRespForm() {
 String nom = "nom";
 String prenom = "prenom";
 String sex = "sex";
 String email = "email";

 when(entityManagerMock.createQuery(anyString())).thenReturn(mock(TypedQuery.class));
 when(entityManagerMock.getResultList()).thenReturn(Collections.emptyList());

 RespFormation[] resultRespFormations = dao.findRespForm(nom, prenom, sex, email);
 assertNotNull(resultRespFormations);
 assertEquals(0, resultRespFormations.length);
 }
}