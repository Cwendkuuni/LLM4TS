import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testMain() {
        String validFile = "testData/validInstructions.txt";
        String invalidFile = "testData/invalidInstructions.txt";
        String emptyFile = "testData/emptyInstructions.txt";

        Main.main(new String[]{validFile});
        assertEquals(Main.TOTAL_INSTRUCTIONS, Main.iPtr);

        Main.main(new String[]{});
        assertEquals(0, Main.iPtr);

        Main.main(new String[]{invalidFile});
        assertTrue(Main.instruction[0].isEmpty());
    }
}