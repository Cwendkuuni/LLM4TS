import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"-Dprop1=val1", "--foo", "-bar", "arg1", "arg2", "--", "arg3", "-baz=val2"};
        Options options = new Options();
        options.addOption("D", "property", true, "Property value");
        options.addOption("f", "foo", false, "Print Foo");
        options.addOption("b", "baz", true, "Baz value");

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("baz");
        expected.add("val2");

        String[] result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.addOption("b", "bar", false, "Print Bar");

        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");
        expected.add("val2");

        result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.setAllowMultipleSelectableOptions(true);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "-baz", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("D");
        expected.add("prop2");
        expected.add("val2");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");

        result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.setAllowMultipleSelectableOptions(false);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "-baz", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");

        try {
            new GnuParser().parse(options, arguments, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        options.addOption("b", "baz", true, "Baz value");

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "-baz", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2", "arg4"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("D");
        expected.add("prop2");
        expected.add("val2");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg4");

        result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "-baz", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2", "arg4", "-Dprop3=val3"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("D");
        expected.add("prop2");
        expected.add("val2");
        expected.add("D");
        expected.add("prop3");
        expected.add("val3");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg4");

        result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.setAllowMultipleSelectableOptions(true);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "-baz", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2", "arg4", "-Dprop3=val3"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("D");
        expected.add("prop2");
        expected.add("val2");
        expected.add("D");
        expected.add("prop3");
        expected.add("val3");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg4");

        result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.setAllowMultipleSelectableOptions(false);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "-baz", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2", "arg4"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg4");

        result = new GnuParser().parse(options, arguments, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        String[] arguments = {"-Dprop1=val1", "--foo", "-bar", "arg1", "arg2", "--", "arg3", "-baz=val2", "arg4"};
        Options options = new Options();
        options.addOption("D", "property", true, "Property value");
        options.addOption("f", "foo", false, "Print Foo");
        options.addOption("b", "baz", true, "Baz value");

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");

        String[] result = new GnuParser().parse(options, arguments, true).clone();

        for (int i = result.length - 1; i >= 0; i--) {
            if ("b".equals(result[i])) {
                int index = Arrays.asList(arguments).lastIndexOf("baz");
                Assert.assertEquals("baz", arguments[index]);
                break;
            }
        }

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.addOption("b", "bar", false, "Print Bar");

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "arg1", "arg2", "--", "arg3", "-baz=val2", "arg4"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg4");

        result = new GnuParser().parse(options, arguments, true).clone();

        for (int i = result.length - 1; i >= 0; i--) {
            if ("b".equals(result[i])) {
                int index = Arrays.asList(arguments).lastIndexOf("baz");
                Assert.assertEquals("baz", arguments[index]);
                break;
            }
        }

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.setAllowMultipleSelectableOptions(true);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "arg1", "arg2", "--", "arg3", "-baz", "-baz", "-Dprop2=val2", "arg4"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("D");
        expected.add("prop2");
        expected.add("val2");
        expected.add("f");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("baz");
        expected.add("baz");
        expected.add("arg4");

        result = new GnuParser().parse(options, arguments, true).clone();

        for (int i = result.length - 1; i >= 0; i--) {
            if ("b".equals(result[i])) {
                int index = Arrays.asList(arguments).lastIndexOf("baz");
                Assert.assertEquals("baz", arguments[index]);
                break;
            }
        }

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        options.setAllowMultipleSelectableOptions(false);

        arguments = new String[]{"-Dprop1=val1", "--foo", "-bar", "arg1", "arg2", "--", "arg3", "-baz", "-Dprop2=val2", "arg4"};
        expected.clear();
        expected.add("D");
        expected.add("prop1");
        expected.add("val1");
        expected.add("f");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("--");
        expected.add("arg3");
        expected.add("b");
        expected.add("bar");

        String[] result2 = new GnuParser().parse(options, arguments, true).clone();

        for (int i = result2.length - 1; i >= 0; i--) {
            if ("b".equals(result2[i])) {
                int index = Arrays.asList(arguments).lastIndexOf("bar");
                Assert.assertEquals("bar", arguments[index]);
                break;
            }
        }

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result2);
    }
}