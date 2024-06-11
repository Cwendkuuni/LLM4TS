package de.devcity.parseargs.test;

import de.devcity.parseargs.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ArgsParserTest {

@Test
public void testGetArgsCount() {
    // Test empty args
    ArgsParser parser = new ArgsParser(new String[0]);
    assertEquals(0, parser.getArgsCount());

    // Test args with different lengths
    String[] args = {"arg1", "arg2", "arg3", "arg4"};
    ArgsParser parser2 = new ArgsParser(args);
    assertEquals(4, parser2.getArgsCount());
}

@Test
public void testParseSwitchArgument() {
    String[] args = {"-a", "--long-a"};
    ArgsParser parser = new ArgsParser(args);

    // Test short switch argument
    assertEquals(0, parser.parseSwitchArgument("a").getIndex());
    assertEquals("a", parser.parseSwitchArgument("a").getKey());
    assertTrue(parser.parseSwitchArgument("a").isLong());

    // Test non-existent switch argument
    assertNull(parser.parseSwitchArgument("b"));

    // Test long switch argument
    assertEquals(1, parser.parseSwitchArgument("--long-a").getIndex());
    assertEquals("long-a", parser.parseSwitchArgument("--long-a").getKey());
    assertTrue(parser.parseSwitchArgument("--long-a").isLong());
}

@Test
public void testParseParameterArgument() {
    String[] args = {"-a", "--param1=value1", "--param2=value2"};
    ArgsParser parser = new ArgsParser(args);

    // Test parameter argument with short key
    assertEquals(0, parser.parseParameterArgument("a").getIndex());
    assertEquals(null, parser.parseParameterArgument("a").getValue());

    // Test parameter argument with long key
    assertEquals(1, parser.parseParameterArgument("--param1").getIndex());
    assertEquals("value1", parser.parseParameterArgument("--param1").getValue());

    // Test non-existent parameter argument
    assertNull(parser.parseParameterArgument("--non-existent"));
}

@Test
public void testParseStringArgument() {
    String[] args = {"-a", "--param1=value1", "--param2=value2", "--param3=value3"};
    ArgsParser parser = new ArgsParser(args);
    List<StringArgument> stringArguments = parser.parseStringArgument();

    // Test empty string argument
    assertEquals(0, parser.getArgsCount());
    assertEquals(0, stringArguments.size());

    // Test string arguments with different lengths
    String[] args2 = {"--str1", "--str2=hello", "--str3=world"};
    ArgsParser parser2 = new ArgsParser(args2);
    List<StringArgument> stringArguments2 = parser2.parseStringArgument();
    assertEquals(3, parser2.getArgsCount());
    assertEquals(3, stringArguments2.size());
    assertEquals("hello", stringArguments2.get(0).getValue());
    assertEquals("world", stringArguments2.get(2).getValue());
}

}
