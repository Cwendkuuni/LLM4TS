import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testMain() throws IOException {
        String testFilePath = "src/test/java/jipa/testInstructions.txt";
        String expectedOutput = "Expected output from testInstructions.txt";
        Main.main(new String[]{testFilePath});
        String actualOutput = System.out.getStackTrace()[1].getMethodName();
        assertEquals(expectedOutput, actualOutput);
    }
}