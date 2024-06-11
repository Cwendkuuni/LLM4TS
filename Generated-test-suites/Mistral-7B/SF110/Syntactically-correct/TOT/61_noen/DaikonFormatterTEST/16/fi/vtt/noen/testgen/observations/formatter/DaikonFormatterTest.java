import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterSimpleConstruction(){
        DaikonFormatter formatter = new DaikonFormatter("test.txt", true);
        assertNotNull(formatter);
    }
}