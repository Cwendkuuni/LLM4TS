package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import javax.persistence.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.dao.impl.*;
import fr.unice.gfarce.identity.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private EntityManager entityManager;
 private EntityTransaction transaction;

 @Before
 public void setup() {
 EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
 this.entityManager = Mockito.mock(EntityManager.class);
 this.transaction = Mockito.mock(EntityTransaction.class);
 when(emf.createEntityManager()).thenReturn(this.entityManager);
 when(this.entityManager.getTransaction()).thenReturn(this.transaction);
 this.identiteDao = new OracleIdentiteDao();
 this.identiteDao.setEntityManagerFactory(emf);
 }

 @Test
 public void testInsert() {
 Identite identite = new Identite();
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 doNothing().when(this.transaction).begin();
 doNothing().when(this.transaction).commit();
 this.identiteDao.insert(identite, "nomForm", new GregorianCalendar(2021, 1, 1));

 verify(this.entityManager).persist(identite);
 verify(this.transaction).commit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 this.identiteDao.insert(null, "nomForm", new GregorianCalendar(2021, 1, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 this.identiteDao.insert(new Identite(), null, new GregorianCalendar(2021, 1, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullCalendar() {
 this.identiteDao.insert(new Identite(), "nomForm", null);
 }

 @Test
 public void testUpdate() {
 Identite identite = new Identite();
 identite.setId(1L);
 identite.setNom("Doe");
 identite.setPrenom("John");
 identite.setEmail("john.doe@example.com");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 doNothing().when(this.transaction).begin();
 doNothing().when(this.transaction).commit();
 Identite dbIdentite = Mockito.mock(Identite.class);
 when(this.entityManager.find(Identite.class, 1L)).thenReturn(dbIdentite);

 this.identiteDao.update(identite);

 verify(dbIdentite).setEmail(identite.getEmail());
 verify(dbIdentite).setNom(identite.getNom());
 verify(dbIdentite).setPrenom(identite.getPrenom());
 verify(dbIdentite).setSex(identite.getSex());
 verify(dbIdentite).setType(identite.getType());
 verify(this.entityManager).merge(identite);
 verify(this.transaction).commit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 this.identiteDao.update(null);
 }

 @Test
 public void testDelete() {
 Identite identite = new Identite();
 identite.setId(1L);

 doNothing().when(this.transaction).begin();
 doNothing().when(this.transaction).commit();
 Identite dbIdentite = Mockito.mock(Identite.class);
 when(this.entityManager.find(Identite.class, 1L)).thenReturn(dbIdentite);

 this.identiteDao.delete(identite);

 verify(this.entityManager).remove(dbIdentite);
 verify(this.transaction).commit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteNullIdentite() {
 this.identiteDao.delete(null);
 }

 @Test
 public void testFind() {
 Identite identite1 = new Identite();
 identite1.setId(1L);
 identite1.setNom("Doe1");
 identite1.setPrenom("John1");
 identite1.setSex("M");
 identite1.setEmail("john1.doe@example.com");
 identite1.setType(Identite.TypeIdentite.CANDIDAT);

 Identite identite2 = new Identite();
 identite2.setId(2L);
 identite2.setNom("Doe2");
 identite2.setPrenom("John2");
 identite2.setSex("M");
 identite2.setEmail("john2.doe@example.com");
 identite2.setType(Identite.TypeIdentite.FORMATEUR);

 Identite identite3 = new Identite();
 identite3.setNom("Doe3");
 identite3.setPrenom("John3");
 identite3.setSex("F");
 identite3.setEmail("john3.doe@example.com");
 identite3.setType(Identite.TypeIdentite.CANDIDAT);

 List<Identite> identites = Arrays.asList(identite1, identite2, identite3);

 doReturn(identites).when(this.entityManager).createQuery("select ident from Identite as ident where ident.nom like :nom and ident.prenom like :prenom and ident.sex like :sex and (ident.type = :type or ident.type like :type)", Map.of("nom", "Doe", "prenom", "John", "sex", "M", "type", "%CANDIDAT%")).getResultList();

 List<Identite> result = this.identiteDao.find("Doe", "John", "M", null, Identite.TypeIdentite.CANDIDAT);

 assertEquals(2, result.size());
 assertEquals("Doe1", result.get(0).getNom());
 assertEquals("Doe2", result.get(1).getNom());
 }

 @Test
 public void testFindNullCriteria() {
 List<Identite> result = this.identiteDao.find(null, null, null, null, null);
 assertEquals(0, result.size());
 }

 @Test
 public void testFindNoMatchingIdentites() {
 doReturn(Collections.emptyList()).when(this.entityManager).createQuery("select ident from Identite as ident where ident.nom like :nom and ident.prenom like :prenom and ident.sex like :sex and (ident.type = :type or ident.type like :type)", Map.of("nom", "Doe", "prenom", "John", "sex", "M", "type", "%CANDIDAT%")).getResultList();

 List<Identite> result = this.identiteDao.find("Doe", "John", "M", null, Identite.TypeIdentite.CANDIDAT);
 assertEquals(0, result.size());
 }
}