import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testMainWithValidInputFile() {
        String inputFile = "testInputFile.txt";
        String expectedOutput = "Expected output from the program with the given input file.";
        String actualOutput = "";

        Main.main(new String[] { inputFile });
        try (BufferedReader reader = new BufferedReader(new FileReader("output.txt"))) {
            actualOutput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(expectedOutput, actualOutput);
    }
}