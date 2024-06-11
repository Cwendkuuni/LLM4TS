import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testMain() {
        String validFilePath = "testData/validInstructions.txt";
        String emptyFilePath = "";
        String nonExistentFilePath = "testData/nonExistentFile.txt";

        Main.main(new String[]{validFilePath});
        assertEquals(Main.TOTAL_INSTRUCTIONS, Main.iPtr);

        Main.resetInstructions();
        Main.main(new String[]{emptyFilePath});
        assertTrue(Main.instruction[0].isEmpty());

        Main.resetInstructions();
        try {
            Main.main(new String[]{nonExistentFilePath});
            fail("Exception expected");
        } catch (Exception e) {
            // expected
        }
    }
}