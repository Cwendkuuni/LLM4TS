import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }
}