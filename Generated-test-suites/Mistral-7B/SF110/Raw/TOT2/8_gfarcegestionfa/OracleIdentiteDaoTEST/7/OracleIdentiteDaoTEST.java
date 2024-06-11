import org.junit.*;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;
import java.util.Calendar;
import java.util.List;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;

    @Before
    public void init() {
        dao = OracleIdentiteDao.getDAO();
    }

    @Test
    public void testInsert() {
        // ... test case
    }

    @Test
    public void testUpdate() {
        // ... test case
    }

    @Test
    public void testDelete() {
        // ... test case
    }

    @Test
    public void testFind() {
        // ... test case
    }

    @Test
    public void testFindCandidat() {
        // ... test case
    }

    @Test
    public void testFindRespForm() {
        // ... test case
    }

    @After
    public void clean() {
        dao.deleteAll();
    }

}