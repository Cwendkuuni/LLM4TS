package de.devcity.parseargs.test;

import de.devcity.parseargs.Arguments.*;
import de.devcity.parseargs.ArgsParser;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class ArgsParserTest {
 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 }

 @Before
 public void setUp() throws Exception {
 }

 @After
 public void tearDown() throws Exception {
 }

 @Test
 public void testGetArgsCount() {
 String[] args = new String[]{"arg1", "arg2", "arg3"};
 ArgsParser parser = new ArgsParser(args);
 assertEquals(3, parser.getArgsCount());
 }

 @Test
 public void testParseSwitchArgument() {
 String[] args = new String[]{"--option", "arg1", "--switch", "value"};
 ArgsParser parser = new ArgsParser(args);
 SwitchArgument result = parser.parseSwitchArgument("option");
 assertEquals(2, result.getIndex());
 assertEquals("option", result.getKey());
 assertEquals(true, result.isLong());

 result = parser.parseSwitchArgument("switch");
 assertEquals(3, result.getIndex());
 assertEquals("switch", result.getKey());
 assertEquals(true, result.isLong());

 result = parser.parseSwitchArgument("nonexistent");
 assertEquals(-1, result.getIndex());
 assertEquals("nonexistent", result.getKey());
 assertEquals(false, result.isLong());
 }

 @Test
 public void testParseParameterArgument() {
 String[] args = new String[]{"--param=value1", "--param2", "--param=value2"};
 ArgsParser parser = new ArgsParser(args);
 ParameterArgument result = parser.parseParameterArgument("param");
 assertEquals(0, result.getIndex());
 assertEquals("param", result.getKey());
 assertEquals("value1", result.getValue());

 result = parser.parseParameterArgument("param2");
 assertEquals(1, result.getIndex());
 assertEquals("param2", result.getKey());
 assertNull(result.getValue());

 result = parser.parseParameterArgument("nonexistent");
 assertEquals(-1, result.getIndex());
 assertNull(result);
 }

 @Test
 public void testParseStringArgument() {
 String[] args = new String[]{"--string=value1", "--string2", "--string=value2"};
 ArgsParser parser = new ArgsParser(args);
 List<StringArgument> result = parser.parseStringArgument();
 assertEquals(2, result.size());
 assertEquals("value1", result.get(0).getValue());
 assertEquals(1, result.get(0).getIndex());
 assertEquals("value2", result.get(1).getValue());
 assertEquals(2, result.get(1).getIndex());
 }
}