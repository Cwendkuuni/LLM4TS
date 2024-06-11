import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Identite;
import java.util.Calendar;

public class OracleIdentiteDaoTest {
    @Test
    public void testInsert() {
        OracleIdentiteDao identiteDao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite("John Doe", "John", "Doe", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        identiteDao.insert(identite, "testForm", calendar);
        Identite retrievedIdentite = identiteDao.findById(identite.getId());
        assertNotNull(retrievedIdentite);
    }
}