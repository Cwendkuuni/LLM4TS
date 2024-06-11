import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Identite;
import java.util.*;
import java.util.Calendar;

public class OracleIdentiteDaoTest {

    @Test
    public void testInsert() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        identite.setNom("Test");
        identite.setPrenom("Test");
        identite.setSex("Female");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        dao.insert(identite, null, calendar);
        List<Identite> identites = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertTrue(identites.size() > 0);
    }
}