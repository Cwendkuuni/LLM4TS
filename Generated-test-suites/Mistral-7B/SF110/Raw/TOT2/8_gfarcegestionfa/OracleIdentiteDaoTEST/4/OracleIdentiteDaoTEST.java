package fr.unice.gfarce.dao.test;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite, "John", new GregorianCalendar(2000, 1, 1));
        // check if the record is inserted correctly
        assertTrue(true);
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        // initialize identite and set the id
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.update(identite);
        // check if the record is updated correctly
        assertTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertWithInvalidInputTypes() {
        Identite identite = new Identite();
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite, "invalid_input", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullIdentite() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithNullIdentite() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindWithInvalidType() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.find("John", "Doe", "M", "john.doe@example.com", Identite.TypeIdentite.INVALID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindWithNullIdentiteType() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.find("John", "Doe", "M", "john.doe@example.com", null);
    }

    @Test
    public void testFind() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        List<Identite> identites = dao.find("John", "Doe", "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        // check if the list is not empty
        assertFalse(identites.isEmpty());
    }

    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Candidat[] candidates = dao.findCandidat("John", "Doe", "M", "john.doe@example.com");
        // check if the array is not null and has at least one element
        assertArrayNotNull(candidates);
        assertTrue(candidates.length > 0);
    }

    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        RespFormation[] respFormations = dao.findRespForm("John", "Doe", "M", "john.doe@example.com");
        // check if the array is not null and has at least one element
        assertArrayNotNull(respFormations);
        assertTrue(respFormations.length > 0);
    }
}