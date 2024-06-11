import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    
    @Test
    public void testDaikonFormatter() {
        String fileName = "test.dtrace";
        DaikonFormatter formatter = new DaikonFormatter(fileName, false);
        assertTrue(formatter instanceof DaikonFormatter);
        assertNull(formatter.inputs);
        assertEquals(fileName, formatter.getFileName());
        assertFalse(formatter.isSimple());
    }
}