package fr.unice.gfarce.dao.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.*;
import java.util.*;

public class OracleIdentiteDaoTest {

 private IdentiteDao identiteDao;
 private Calendar dateLimite;
 private Identite identite;

 @Before
 public void setUp() {
 identiteDao = OracleIdentiteDao.getDAO();
 dateLimite = Calendar.getInstance();
 identite = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 }

 @Test
 public void testInsert() {
 Identite identiteSame = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 try {
 identiteDao.insert(identite, "test", dateLimite);
 Identite retrievedIdentite = identiteDao.findByEmail("john.doe@example.com").get(0);
 assertEquals(identite, retrievedIdentite);
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("An error occurred during the test");
 }
 }

 @Test
 public void testUpdate() {
 Identite identiteUpdated = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 identiteUpdated.setNom("Johnny");
 identiteUpdated.setPrenom("Doe-Jones");
 try {
 identiteDao.update(identiteUpdated);
 Identite retrievedIdentite = identiteDao.findByEmail("john.doe@example.com").get(0);
 assertEquals(identiteUpdated, retrievedIdentite);
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("An error occurred during the test");
 }
 }

 @Test
 public void testDelete() {
 try {
 identiteDao.insert(identite, "test", dateLimite);
 identiteDao.delete(identite);
 assertNull(identiteDao.findByEmail("john.doe@example.com").get(0));
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("An error occurred during the test");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullIdentite() {
 try {
 identiteDao.insert(null, "test", dateLimite);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNomForm() {
 try {
 identiteDao.insert(identite, null, dateLimite);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullIdentite() {
 Identite identiteToUpdate = new Identite("John", "Doe", 'M', "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 try {
 identiteDao.update(null);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullNom() {
 try {
 identiteDao.find(null, "Doe", "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullPrenom() {
 try {
 identiteDao.find("John", null, "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullSex() {
 try {
 identiteDao.find("John", "Doe", null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullEmail() {
 try {
 identiteDao.find("John", "Doe", "M", null, Identite.TypeIdentite.CANDIDAT);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullType() {
 try {
 identiteDao.find("John", "Doe", "M", "john.doe@example.com", null);
 fail("An IllegalArgumentException should have been thrown");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @After
 public void tearDown() {
 identite = null;
 identiteDao = null;
 }

}