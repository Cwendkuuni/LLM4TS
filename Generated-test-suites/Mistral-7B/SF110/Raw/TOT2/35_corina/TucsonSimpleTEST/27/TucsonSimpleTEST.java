import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    @Test
    public void testMake6digitCode() throws Exception {
        Sample sample = new Sample(); // assuming Sample has a no-arg constructor
        String expectedCode = "000000";
        String actualCode = new TucsonSimple().make6digitCode(sample);
        assertEquals(expectedCode, actualCode);
    }
}