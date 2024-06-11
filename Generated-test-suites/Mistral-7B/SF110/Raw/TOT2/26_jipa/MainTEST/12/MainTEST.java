import org.junit.jupiter.api.Test;
import jipa.Main;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    @Test
    public void testMain() {
        String fileName = "testInstructions.txt";
        String expectedOutput = "Expected output after processing instructions";

        Main.main(new String[]{"testInstructions.txt"});
        assertEquals(expectedOutput, System.out.getHistory().get(0));
    }
}