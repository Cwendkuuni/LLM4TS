import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testJSJshopInstanceWithDomainAndProblemFiles() {
        JSJshop jsJshop = new JSJshop("domain.file", "problem.file");
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }
}