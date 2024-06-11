import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEqual;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"-Dprop=value1", "--", "-o", "opt", "arg1", "arg2", "--", "-Dprop2=value2"};
        Options options = new Options();
        String[] expected = {"-D", "prop=value1", "-o", "opt", "arg1", "arg2", "--", "-D", "prop2=value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEqual(expected, result);
    }
}