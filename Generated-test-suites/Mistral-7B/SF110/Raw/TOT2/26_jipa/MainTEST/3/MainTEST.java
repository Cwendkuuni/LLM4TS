import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import jipa.Main;

class MainTest {

    @Test
    void testMain() throws IOException {
        String fileName = "testInstructions.txt";
        String expectedOutput = "Expected output from the test instructions";

        Main.main(new String[]{fileName});

        String output = System.out.getFileName().substring(0, System.out.getFileName().lastIndexOf('.'));
        String actualOutput = new String(Files.readAllBytes(Paths.get(output + ".out")));

        assertEquals(expectedOutput, actualOutput);
    }
}