import org.junit.Test;
import static org.junit.Assert.assertEquals;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.dao.OracleIdentiteDao;

public class OracleIdentiteDaoTest {
    
    @Test
    public void testInsert() {
        OracleIdentiteDao oracleIdentiteDao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite("Test", "Test", "Test", "Test@test.com", Identite.TypeIdentite.CANDIDAT);

        oracleIdentiteDao.insert(identite, "TestForm", new GregorianCalendar(2022, 11, 30));

        List<Identite> identites = oracleIdentiteDao.find("Test", "Test", null, "Test@test.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, identites.size());
        Identite foundIdentite = identites.get(0);
        assertEquals(identite.getNom(), foundIdentite.getNom());
        assertEquals(identite.getPrenom(), foundIdentite.getPrenom());
        assertEquals(identite.getEmail(), foundIdentite.getEmail());
        assertEquals(identite.getType(), foundIdentite.getType());
    }
}