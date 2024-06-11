import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;
import javax.persistence.EntityManager;
import org.junit.Test;

public class OracleIdentiteDaoTest {

    private final OracleIdentiteDao dao = OracleIdentiteDao.getDAO();

    @Test
    public void testInsert() {
        Identite identite = new Identite("Nom1", "Prenom1", 'M', "email@example.com", Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));

        // check if the entity was inserted correctly
        EntityManager em = OracleIdentiteDao.getEntityManager();
        Identite fetchedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(identite, fetchedIdentite);
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite("Nom1", "Prenom1", 'M', "email@example.com", Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));

        Identite updatedIdentite = new Identite("Nom1", "Prenom2", 'M', "newEmail@example.com", Identite.TypeIdentite.CANDIDAT);
        dao.update(updatedIdentite);

        // check if the entity was updated correctly
        EntityManager em = OracleIdentiteDao.getEntityManager();
        Identite fetchedIdentite = em.find(Identite.class, identite.getId());
        assertEquals(updatedIdentite, fetchedIdentite);
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite("Nom1", "Prenom1", 'M', "email@example.com", Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));

        dao.delete(identite);

        // check if the entity was deleted correctly
        EntityManager em = OracleIdentiteDao.getEntityManager();
        Identite fetchedIdentite = em.find(Identite.class, identite.getId());
        assertNull(fetchedIdentite);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullIdentite() {
        dao.insert(null, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertEmptyIdentite() {
        Identite identite = new Identite();
        dao.insert(identite, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullIdentite() {
        dao.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateInvalidIdentite() {
        Identite identite = new Identite("", "Prenom1", 'M', null, Identite.TypeIdentite.CANDIDAT);
        dao.update(identite);
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite("Nom1", "Prenom1", 'M', "email@example.com", Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite("Nom2", "Prenom2", 'F', "email2@example.com", Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));
        dao.insert(identite2, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));

        List<Identite> result = dao.find(null, null, null, null, null);

        // check if the list contains both entities
        assertEquals(2, result.size());
        assertTrue(result.contains(identite1));
        assertTrue(result.contains(identite2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNullIds() {
        dao.find(null, null, null, null, null);
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite("Nom1", "Prenom1", 'M', "email@example.com", Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite("Nom2", "Prenom2", 'F', "email2@example.com", Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));
        dao.insert(identite2, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));

        Candidat[] result = dao.findCandidat(null, null, null, null);

        // check if the array contains only candidates
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof Candidat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCandidatNullParams() {
        dao.findCandidat(null, null, null, null);
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite("Nom1", "Prenom1", 'M', "email@example.com", Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite("Nom2", "Prenom2", 'F', "email2@example.com", Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));
        dao.insert(identite2, "OracleIdentiteDaoTest", new GregorianCalendar(2022, 1, 1));

        RespFormation[] result = dao.findRespForm(null, null, null, null);

        // check if the array contains only formators
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof RespFormation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindRespFormNullParams() {
        dao.findRespForm(null, null, null, null);
    }
}