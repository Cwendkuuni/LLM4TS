import org.junit.Assert;
import org.junit.Test;
import de.devcity.parseargs.*;
import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testConstructor() {
        String[] args = {"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        Assert.assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[0]);
        Assert.assertEquals(0, parser.getArgsCount());

        String[] args = {"arg1"};
        parser = new ArgsParser(args);
        Assert.assertEquals(1, parser.getArgsCount());

        args = new String[]{"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        Assert.assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2"};
        parser.setArgs(args);
        Assert.assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        Assert.assertEquals(0, parser.getArgsLeftCount());

        String[] args = {"arg1", null};
        parser = new ArgsParser(args);
        Assert.assertEquals(1, parser.getArgsLeftCount());

        args = new String[]{"arg1", null, null, "arg4"};
        parser = new ArgsParser(args);
        Assert.assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-s", "-a"});

        Assert.assertEquals(new SwitchArgument(0, "s", true), parser.parseSwitchArgument("s"));
        Assert.assertEquals(new SwitchArgument(1, "a", true), parser.parseSwitchArgument("a"));
        Assert.assertEquals(new SwitchArgument(-1, "b", false), parser.parseSwitchArgument("b"));
    }

    @Test
    public void testParseSwitchArgumentLong() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--longSwitch", "arg1"});
        Assert.assertEquals(new SwitchArgument(0, "longSwitch", true), parser.parseSwitchArgument("--longSwitch"));
    }

    @Test
    public void testParseSwitchArgumentMissing() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1"});
        Assert.assertEquals(new SwitchArgument(-1, "", false), parser.parseSwitchArgument("s"));
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p arg", "arg1"});
        Assert.assertEquals(new ParameterArgument(0, "p", "arg"), parser.parseParameterArgument("p"));

        parser.setArgs(new String[]{"--param arg", "value"});
        Assert.assertEquals(new ParameterArgument(0, "param", "value"), parser.parseParameterArgument("param"));
    }

    @Test
    public void testParseParameterArgumentMissingValue() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p arg"});
        Assert.assertEquals(null, parser.parseParameterArgument("p"));
    }

    @Test
    public void testParseParameterArgumentMissingArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1"});
        Assert.assertEquals(null, parser.parseParameterArgument("p"));
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--arg", "value1", "value2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        Assert.assertEquals(2, stringArguments.size());
        Assert.assertEquals("value1", stringArguments.get(0).getValue());
        Assert.assertEquals("value2", stringArguments.get(1).getValue());
    }

    @Test
    public void testParseStringArgumentEmptyValue() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--arg", ""});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        Assert.assertEquals(1, stringArguments.size());
        Assert.assertEquals("", stringArguments.get(0).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringArgumentMissingArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"arg1"});
        parser.parseStringArgument();
    }
}