
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ArgsParserTest {

    private ArgsParser argsParser;

    @Before
    public void setup() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        argsParser = new ArgsParser(args);
    }

    @Test
    public void testArgsParserInstanceWithArgs() {
        int expectedArgsCount = 3;
        int actualArgsCount = argsParser.getArgsCount();
        Assert.assertEquals(expectedArgsCount, actualArgsCount);
    }

    @Test
    public void testGetArgs() {
        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        String[] actualArgs = argsParser.getArgs();
        Assert.assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCountWithEmptyInnerArgs() {
        ArgsParser argsParser = new ArgsParser();
        int expectedArgsLeftCount = 0;
        int actualArgsLeftCount = argsParser.getArgsLeftCount();
        Assert.assertEquals(expectedArgsLeftCount, actualArgsLeftCount);
    }

    @Test
    public void testParseSwitchArgumentWithLongArgumentKey() {
        String[] args = new String[]{"--switchKey"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument expectedSwitchArgument = new SwitchArgument(0, "switchKey", true);
        SwitchArgument actualSwitchArgument = argsParser.parseSwitchArgument("switchKey");
        Assert.assertEquals(expectedSwitchArgument, actualSwitchArgument);
    }

    @Test
    public void testParseParameterArgumentWithLongArgumentKeyAndValue() {
        String[] args = new String[]{"--key", "value"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument expectedParameterArgument = new ParameterArgument(0, "key", "value");
        ParameterArgument actualParameterArgument = argsParser.parseParameterArgument("key");
        Assert.assertEquals(expectedParameterArgument, actualParameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = new String[]{"--arg", "value1", "--arg2", "value2"};
        ArgsParser argsParser = new ArgsParser(args);
        List<StringArgument> expectedStringArguments = Arrays.asList(
                new StringArgument(1, "value1"),
                new StringArgument(3, "value2")
        );
        List<StringArgument> actualStringArguments = argsParser.parseStringArgument();
        Assert.assertEquals(expectedStringArguments, actualStringArguments);
    }

    @Test
    public void testParseStringArgumentWithEmptyInnerArgs() {
        ArgsParser argsParser = new ArgsParser();
        List<StringArgument> expectedStringArguments = new ArrayList<>();
        List<StringArgument> actualStringArguments = argsParser.parseStringArgument();
        Assert.assertEquals(expectedStringArguments, actualStringArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentWithNonExistentKey() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("nonExistentKey");
        Assert.assertEquals(new SwitchArgument(-1, "nonExistentKey", false), switchArgument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringArgumentWithDuplicatedKey() {
        String[] args = new String[]{"--arg", "--arg", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        StringArgument stringArgument1 = argsParser.parseStringArgument("arg");
        StringArgument stringArgument2 = argsParser.parseStringArgument("arg");
        Assert.assertEquals(stringArgument1, stringArgument2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentWithSwitchArgumentKeyThatStartsWithDoubleHyphen() {
        String[] args = new String[]{"--arg--", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("arg--");
        Assert.assertEquals(new SwitchArgument(-1, "arg--", false), switchArgument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentWithSwitchArgumentKeyThatHasTrailingEqualsSign() {
        String[] args = new String[]{"--arg=", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("arg=");
        Assert.assertEquals(new SwitchArgument(-1, "arg=", false), switchArgument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentWithSwitchArgumentKeyThatHasTwoConsecutiveEqualsSigns() {
        String[] args = new String[]{"--arg==", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("arg==");
        Assert.assertEquals(new SwitchArgument(-1, "arg==", false), switchArgument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentWithSwitchArgumentKeyThatHasValuePrecededByEqualsSign() {
        String[] args = new String[]{"--arg=value", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("arg");
        Assert.assertEquals(new SwitchArgument(-1, "arg", false), switchArgument);
    }
}
