import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class PosixParserTest {
    private Options options;
    private String[] arguments;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with an argument");
        arguments = new String[]{"--a", "--b", "1", "--c", "3", "--"};
    }

    @Test
    public void testFlatten() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "1", "--c", "3"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--a", "--b=1", "--c=3", "--"}, result);
    }
}