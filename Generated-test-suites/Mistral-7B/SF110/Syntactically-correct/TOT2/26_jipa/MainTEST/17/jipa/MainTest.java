import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testMain() {
        String[] args = {"inputFile.txt"};
        Main.main(args);
        // Add assertions here to check the expected behavior of the main() method
        // For example, you can check if the instructions were loaded correctly by checking the value of Main.TOTAL_INSTRUCTIONS
    }
}