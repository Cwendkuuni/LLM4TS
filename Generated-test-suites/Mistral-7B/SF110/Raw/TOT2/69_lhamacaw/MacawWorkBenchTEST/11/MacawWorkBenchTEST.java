import org.junit.Assert;
import org.junit.Test;

public class MacawWorkBenchTest {
    @Test
    public void testMain() {
        String[] arguments = new String[0]; // test with empty argument list
        MacawWorkBench.main(arguments);
        // assert that the program ends properly (e.g., System.exit is called)
        Assert.assertEquals(0, System.exitCode());
    }
}