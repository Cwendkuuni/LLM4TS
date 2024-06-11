import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld";
        String expectedOutput = "H2l2o2w2r2l2o2l2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }
}