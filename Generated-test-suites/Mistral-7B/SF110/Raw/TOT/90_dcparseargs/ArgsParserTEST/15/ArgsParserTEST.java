package de.devcity.parseargs;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

@Test
public void testGetArgsCount() {
    String[] args = new String[] {"-a", "--key=value", "-b"};
    ArgsParser parser = new ArgsParser(args);
    assertEquals(3, parser.getArgsCount());
}

@Test
public void testParseSwitchArgument() {
    String[] args = new String[] {"--key", "-k", "--longKey", "--long-key", "--key=val", "--key=val2"};
    ArgsParser parser = new ArgsParser(args);
    assertEquals(new SwitchArgument(2, "key", true), parser.parseSwitchArgument("key"));
    assertEquals(new SwitchArgument(3, "longKey", true), parser.parseSwitchArgument("longKey"));
    assertNull(parser.parseSwitchArgument("invalid"));
    assertNull(parser.parseSwitchArgument("--invalid"));
    assertTrue(parser.parseSwitchArgument("--key=val").isPresent());
    assertTrue(parser.parseSwitchArgument("--key=val2").isPresent());
}

@Test
public void testParseParameterArgument() {
    String[] args = new String[] {"--key=value", "-k value", "--key value", "--key value2", "--key value3=value4"};
    ArgsParser parser = new ArgsParser(args);
    assertEquals(new ParameterArgument(0, "key", "value"), parser.parseParameterArgument("key"));
    assertEquals(new ParameterArgument(1, "k", "value"), parser.parseParameterArgument("k"));
    assertEquals(new ParameterArgument(2, "key", "value2"), parser.parseParameterArgument("key"));
    assertNull(parser.parseParameterArgument("invalid"));
    assertNull(parser.parseParameterArgument("--invalid"));
    assertTrue(parser.parseParameterArgument("--key=value4").isPresent());
}

@Test
public void testParseStringArgument() {
    String[] args = new String[] {"--key=value1", "--key=value2", "--key=value3", "--foo=bar"};
    ArgsParser parser = new ArgsParser(args);
    List<StringArgument> stringArguments = parser.parseStringArgument();
    assertEquals(3, stringArguments.size());
    assertEquals("value1", stringArguments.get(0).getValue());
    assertEquals("value2", stringArguments.get(1).getValue());
    assertEquals("value3", stringArguments.get(2).getValue());
    assertNull(parser.parseStringArgument().isPresent());
}
}