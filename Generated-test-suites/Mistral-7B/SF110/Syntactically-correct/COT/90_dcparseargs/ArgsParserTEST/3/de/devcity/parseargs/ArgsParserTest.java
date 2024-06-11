import org.junit.Test;
import static org.junit.Assert.*;
import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;
 private String[] inputArgs = new String[]{"-a", "--long-argument-a", "-b", "--long-argument-b=value", "-c", "--long-argument-c=value2", "--", "value3", "value4"};

 @Before
 public void setUp() {
 argsParser = new ArgsParser(inputArgs);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(7, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(inputArgs, argsParser.getArgs());
 }

 @Test
 public void testSetArgs() {
 String[] newArgs = new String[]{"arg1", "arg2", "arg3"};
 argsParser.setArgs(newArgs);
 assertNotSame(inputArgs, argsParser.getArgs());
 assertArrayEquals(newArgs, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgumentA = argsParser.parseSwitchArgument("a");
 SwitchArgument switchArgumentB = argsParser.parseSwitchArgument("b");
 SwitchArgument switchArgumentC = argsParser.parseSwitchArgument("c");
 SwitchArgument switchArgumentInvalid = argsParser.parseSwitchArgument("x");

 assertEquals(0, switchArgumentA.getIndex());
 assertEquals("a", switchArgumentA.getKey());
 assertTrue(switchArgumentA.isLong());

 assertEquals(3, switchArgumentB.getIndex());
 assertEquals("b", switchArgumentB.getKey());
 assertTrue(switchArgumentB.isLong());

 assertEquals(-1, switchArgumentC.getIndex());
 assertEquals("c", switchArgumentC.getKey());
 assertFalse(switchArgumentC.isLong());

 assertNull(switchArgumentInvalid);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseSwitchArgumentInvalidKey() {
 argsParser.parseSwitchArgument("x");
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgumentA = argsParser.parseParameterArgument("a");
 ParameterArgument parameterArgumentBValue = argsParser.parseParameterArgument("b");
 ParameterArgument parameterArgumentBIndex = argsParser.parseParameterArgument("b");
 ParameterArgument parameterArgumentCValue = argsParser.parseParameterArgument("c");
 ParameterArgument parameterArgumentCIndex = argsParser.parseParameterArgument("c");
 ParameterArgument parameterArgumentInvalid = argsParser.parseParameterArgument("x");

 assertEquals(1, parameterArgumentA.getIndex());
 assertEquals("a", parameterArgumentA.getKey());
 assertNull(parameterArgumentA.getValue());

 assertEquals(5, parameterArgumentBValue.getIndex());
 assertEquals("b", parameterArgumentBValue.getKey());
 assertEquals("value", parameterArgumentBValue.getValue());

 assertEquals(3, parameterArgumentBIndex.getIndex());
 assertEquals("b", parameterArgumentBIndex.getKey());
 assertEquals(null, parameterArgumentBIndex.getValue());

 assertEquals(4, parameterArgumentCValue.getIndex());
 assertEquals("c", parameterArgumentCValue.getKey());
 assertEquals("value2", parameterArgumentCValue.getValue());

 assertEquals(-1, parameterArgumentCIndex.getIndex());
 assertEquals("c", parameterArgumentCIndex.getKey());
 assertNull(parameterArgumentCIndex.getValue());

 assertNull(parameterArgumentInvalid);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentNoValue() {
 argsParser.parseParameterArgument("a");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentInvalidKey() {
 argsParser.parseParameterArgument("x");
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 StringArgument stringArgumentOne = stringArguments.get(0);
 StringArgument stringArgumentTwo = stringArguments.get(1);

 assertEquals(1, stringArgumentOne.getIndex());
 assertEquals("value3", stringArgumentOne.getValue());

 assertEquals(2, stringArgumentTwo.getIndex());
 assertEquals("value4", stringArgumentTwo.getValue());
 }

 @Test
 public void testParseStringArgumentEmpty() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(0, stringArguments.size());
 }

}