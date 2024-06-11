import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphoneConstructor() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111111111111", caverphone.caverphone(""));
    }
}