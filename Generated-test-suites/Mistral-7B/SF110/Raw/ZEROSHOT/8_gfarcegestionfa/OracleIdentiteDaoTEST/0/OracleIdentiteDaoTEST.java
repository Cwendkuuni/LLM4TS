package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;

@RunWith(MockitoJUnitRunner.class)
public class OracleIdentiteDaoTest {

 @Mock
 private EntityManager mockedEntityManager;
 @Mock
 private EntityManagerFactory mockedEntityManagerFactory;
 @Mock
 private EntityTransaction mockedEntityTransaction;

 @InjectMocks
 private OracleIdentiteDao oracleIdentiteDao;

 @Test
 public void testInsert() {
 final Identite identite = new Identite();
 identite.setEmail("test@test.fr");
 identite.setNom("test");
 identite.setPrenom("test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenReturn(mockedEntityManager);
 Mockito.when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);

 oracleIdentiteDao.insert(identite, "formulaire1", new GregorianCalendar(2022, 2, 2).getTime());

 Mockito.verify(mockedEntityManager).persist(identite);
 Mockito.verify(mockedEntityTransaction).commit();
 }

 @Test(expected = Exception.class)
 public void testInsertWithException() {
 final Identite identite = new Identite();
 identite.setEmail("test@test.fr");
 identite.setNom("test");
 identite.setPrenom("test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenThrow(PersistenceException.class);

 oracleIdentiteDao.insert(identite, "formulaire1", new GregorianCalendar(2022, 2, 2).getTime());
 }

 @Test
 public void testUpdate() {
 final Identite identite = new Identite();
 identite.setId(1);
 identite.setEmail("test@test.fr");
 identite.setNom("test");
 identite.setPrenom("test");
 identite.setSex("M");
 identite.setType(Identite.TypeIdentite.CANDIDAT);

 final Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(1);
 identiteToUpdate.setEmail("newEmail@test.fr");
 identiteToUpdate.setNom("newTest");
 identiteToUpdate.setPrenom("newTest");
 identiteToUpdate.setSex("M");
 identiteToUpdate.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenReturn(mockedEntityManager);
 Mockito.when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
 Mockito.when(mockedEntityManager.find(Identite.class, 1)).thenReturn(identite);

 oracleIdentiteDao.update(identiteToUpdate);

 Mockito.verify(mockedEntityManager).find(Identite.class, 1);
 Mockito.verify(mockedEntityManager).merge(identiteToUpdate);
 Mockito.verify(mockedEntityTransaction).commit();
 }

 @Test(expected = Exception.class)
 public void testUpdateWithException() {
 final Identite identiteToUpdate = new Identite();
 identiteToUpdate.setId(1);
 identiteToUpdate.setEmail("newEmail@test.fr");
 identiteToUpdate.setNom("newTest");
 identiteToUpdate.setPrenom("newTest");
 identiteToUpdate.setSex("M");
 identiteToUpdate.setType(Identite.TypeIdentite.CANDIDAT);

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenThrow(PersistenceException.class);
 Mockito.when(mockedEntityManager.find(Identite.class, 1)).thenReturn(new Identite());

 oracleIdentiteDao.update(identiteToUpdate);
 }

 @Test
 public void testDelete() {
 final Identite identite = new Identite();
 identite.setId(1);

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenReturn(mockedEntityManager);
 Mockito.when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
 Mockito.when(mockedEntityManager.find(Identite.class, 1)).thenReturn(identite);

 oracleIdentiteDao.delete(identite);

 Mockito.verify(mockedEntityManager).find(Identite.class, 1);
 Mockito.verify(mockedEntityManager).remove(identite);
 Mockito.verify(mockedEntityTransaction).commit();
 }

 @Test(expected = Exception.class)
 public void testDeleteWithException() {
 final Identite identite = new Identite();
 identite.setId(1);

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenThrow(PersistenceException.class);
 Mockito.when(mockedEntityManager.find(Identite.class, 1)).thenReturn(new Identite());

 oracleIdentiteDao.delete(identite);
 }

 @Test
 public void testFind() {
 final String nom = "test";
 final String prenom = "test";
 final String sex = "M";
 final String email = "test@test.fr";
 final Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

 final List<Identite> identites = new ArrayList<>();
 identites.add(new Identite());
 identites.add(new Identite());

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenReturn(mockedEntityManager);
 Mockito.when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
 Mockito.when(mockedEntityManager.createQuery("select ident from Identite as ident where ident.nom like '%" + nom + "%' and ident.prenom like '%" + prenom + "%' and ident.sex like '%" + sex + "%' and ident.type = :type")).thenReturn(Mockito.mock(TypedQuery.class));

 List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);

 Mockito.verify(mockedEntityManager).createQuery("select ident from Identite as ident where ident.nom like '%" + nom + "%' and ident.prenom like '%" + prenom + "%' and ident.sex like '%" + sex + "%' and ident.type = :type");
 assertEquals(2, result.size());
 }

 @Test
 public void testFindCandidat() {
 final String nom = "test";
 final String prenom = "test";
 final String sex = "M";
 final String email = "test@test.fr";

 final Candidat[] candidats = new Candidat[1];
 candidats[0] = new Candidat();

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenReturn(mockedEntityManager);
 Mockito.when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
 Mockito.when(mockedEntityManager.createQuery("select ident from Identite as ident where ident.nom like '%" + nom + "%' and ident.prenom like '%" + prenom + "%' and ident.sex like '%" + sex + "%' and ident.type = :type")).thenReturn(Mockito.mock(TypedQuery.class));

 Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);

 Mockito.verify(mockedEntityManager).createQuery("select ident from Identite as ident where ident.nom like '%" + nom + "%' and ident.prenom like '%" + prenom + "%' and ident.sex like '%" + sex + "%' and ident.type = :type");
 assertEquals(1, result.length);
 }

 @Test
 public void testFindRespForm() {
 final String nom = "test";
 final String prenom = "test";
 final String sex = "M";
 final String email = "test@test.fr";

 final RespFormation[] respFormations = new RespFormation[1];
 respFormations[0] = new RespFormation();

 Mockito.when(mockedEntityManagerFactory.createEntityManager()).thenReturn(mockedEntityManager);
 Mockito.when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
 Mockito.when(mockedEntityManager.createQuery("select ident from Identite as ident where ident.nom like '%" + nom + "%' and ident.prenom like '%" + prenom + "%' and ident.sex like '%" + sex + "%' and ident.type = :type")).thenReturn(Mockito.mock(TypedQuery.class));

 RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);

 Mockito.verify(mockedEntityManager).createQuery("select ident from Identite as ident where ident.nom like '%" + nom + "%' and ident.prenom like '%" + prenom + "%' and ident.sex like '%" + sex + "%' and ident.type = :type");
 assertEquals(1, result.length);
 }

}