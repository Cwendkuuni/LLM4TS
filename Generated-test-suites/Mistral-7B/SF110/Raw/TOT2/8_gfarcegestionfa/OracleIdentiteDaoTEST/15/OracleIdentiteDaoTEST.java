import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    OracleIdentiteDao dao = OracleIdentiteDao.getDAO();

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        Identite result = dao.insert(identite, "test", new GregorianCalendar(2022, 11, 30));
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        dao.insert(identite, "test", new GregorianCalendar(2022, 11, 30));
        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(identite.getId());
        updatedIdentite.setEmail("updatedEmail@test.com");
        dao.update(updatedIdentite);
        Identite result = dao.findById(updatedIdentite.getId());
        assertEquals("updatedEmail@test.com", result.getEmail());
    }

    // Test for delete method can be added here
}