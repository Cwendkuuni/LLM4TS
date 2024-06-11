import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Candidat;
import java.util.Date;
import java.util.Calendar;

public class OracleIdentiteDaoTest {
    
    @Test
    public void testInsert() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite("Nom", "Prenom", "Sex", "Email", Identite.TypeIdentite.CANDIDAT);
        Calendar calendar = Calendar.getInstance();
        dao.insert(identite, "form", calendar);
        // Check if the record has been inserted
        Identite fetchedIdentite = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
        assertEquals(identite, fetchedIdentite);
    }
    
    @Test
    public void testUpdate() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite("Nom", "Prenom", "Sex", "Email", Identite.TypeIdentite.CANDIDAT);
        identite.setEmail("newEmail@gmail.com");
        dao.update(identite);
        // Check if the record has been updated
        Identite fetchedIdentite = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
        assertEquals(identite.getEmail(), fetchedIdentite.getEmail());
    }
    
    @Test
    public void testDelete() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite("Nom", "Prenom", "Sex", "Email", Identite.TypeIdentite.CANDIDAT);
        dao.delete(identite);
        // Check if the record has been deleted
        List<Identite> identites = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
        assertTrue(identites.isEmpty());
    }
}