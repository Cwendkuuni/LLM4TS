import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphoneMethod() {
        Caverphone testCaverphone = new Caverphone();
        String input = "HelloWorld";
        String expectedOutput = "H2l3o2w1r1d1";
        String actualOutput = testCaverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Edge cases and exception handling scenarios to be added in next steps
}