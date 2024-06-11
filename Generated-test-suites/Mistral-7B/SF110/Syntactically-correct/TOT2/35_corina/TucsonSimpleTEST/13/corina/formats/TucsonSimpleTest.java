import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        String expected = I18n.getText("format.tucsonsimple");
        assertEquals(expected, result);
    }
}