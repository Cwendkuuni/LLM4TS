import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = new String[]{"-Dproperty=value", "--foo=bar", "-a", "--", "-b", "--", "-c=-d", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("D", "property", true, "set the property value");
        options.addOption("f", "foo", true, "set the foo value");
        options.addOption("a", "append", false, "append mode");
        options.addOption("c", "complement", false, "complement mode");
        options.addOption("d", "disable", false, "disable mode");

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("f");
        expected.add("foo");
        expected.add("bar");
        expected.add("a");
        expected.add("--");
        expected.add("b");
        expected.add("--");
        expected.add("c");
        expected.add("d");
        expected.add("arg1");
        expected.add("arg2");

        String[] result = new GnuParser().parse(options, arguments, true);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        // Test stopAtNonOption
        result = new GnuParser().parse(options, arguments, false);
        expected.remove(expected.size() - 2);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}