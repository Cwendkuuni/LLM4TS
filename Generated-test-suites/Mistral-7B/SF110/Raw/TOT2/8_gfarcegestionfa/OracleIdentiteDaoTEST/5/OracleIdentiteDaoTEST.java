import static org.junit.Assert.*;
import org.junit.Test;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Identite;
import java.util.Calendar;

public class OracleIdentiteDaoTest {
    @Test
    public void testInsert() {
        OracleIdentiteDao identiteDao = OracleIdentiteDao.getDAO();

        Identite identite = new Identite();
        Calendar calendar = Calendar.getInstance();

        identiteDao.insert(identite, "John", calendar);

        assertNotNull(identite.getId());
    }
}