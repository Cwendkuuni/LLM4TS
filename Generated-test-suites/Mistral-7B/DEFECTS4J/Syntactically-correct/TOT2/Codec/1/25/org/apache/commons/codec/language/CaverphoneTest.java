import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        String input = "ExampleString";
        String expected = "A2F2S2T2P2K2M2N2L2R2K2F2A2W3W3"; // expected output based on class implementation
        String result = new Caverphone().caverphone(input);
        assertEquals(expected, result);
    }
}