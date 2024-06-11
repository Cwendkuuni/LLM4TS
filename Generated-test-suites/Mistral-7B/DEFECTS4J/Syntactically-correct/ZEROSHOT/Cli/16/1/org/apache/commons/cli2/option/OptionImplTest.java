package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.test.MockCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Test class for OptionImpl
 */
public class OptionImplTest {

 private Option option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new MockCommandLine();
 option = new OptionBuilder()
 .withLongOpt("test")
 .withDescription("Test option")
 .withId(1)
 .create();
 }

 @Test
 public void testToString() {
 String expected = "Test option";
 String result = option.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testGetId() {
 int expected = 1;
 int result = option.getId();
 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 Option test = new OptionBuilder()
 .withLongOpt("test")
 .withDescription("Test option")
 .withId(1)
 .create();

 assertTrue(option.equals(option));
 assertTrue(option.equals(test));

 Option notEqual = new OptionBuilder()
 .withLongOpt("notTest")
 .withDescription("Not Test option")
 .withId(2)
 .create();

 assertFalse(option.equals(notEqual));
 }

 @Test
 public void testHashCode() {
 Option test = new OptionBuilder()
 .withLongOpt("test")
 .withDescription("Test option")
 .withId(1)
 .create();

 int expected = 1;
 int result = option.hashCode();
 assertEquals(expected, result);

 Option other = new OptionBuilder()
 .withLongOpt("test2")
 .withDescription("Test option 2")
 .withId(1)
 .create();

 assertNotSame(option.hashCode(), other.hashCode());
 }

 @Test
 public void testFindOption() {
 Option test = new OptionBuilder()
 .withLongOpt("test")
 .withDescription("Test option")
 .withId(1)
 .create();

 Option found = option.findOption("test");
 assertEquals(option, found);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindOptionWithWrongTrigger() {
 Option test = new OptionBuilder()
 .withLongOpt("test")
 .withDescription("Test option")
 .withId(1)
 .create();

 Option notFound = option.findOption("notTest");
 assertNull(notFound);
 }

 @Test
 public void testIsRequired() {
 assertFalse(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, null));

 String argument = "testValue";
 commandLine.parse(new String[]{option.getLongOpt(), argument});
 commandLine.parse(new String[]{ option.getLongOpt(), "--" }); // consume next option

 assertTrue(option.canProcess(commandLine, null));
 }

 @Test
 public void testCheckPrefixes() {
 Set prefixes = Collections.singleton("--test");
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithoutPrefix() {
 Set prefixes = Collections.emptySet();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithWrongPrefix() {
 Set prefixes = Arrays.asList("--notTest");
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testDefaults() {
 Option defaultOption = new OptionBuilder()
 .withLongOpt("default")
 .withDescription("Default option")
 .withId(2)
 .create();
 option.defaults(commandLine);
 assertFalse(commandLine.hasOption("test"));
 assertFalse(commandLine.hasOption("default"));
 }

 @Test
 public void testDefaultsWithValue() {
 Option defaultOption = new OptionBuilder()
 .withLongOpt("default")
 .withDescription("Default option")
 .withId(2)
 .withValueSeparator('=')
 .create();
 commandLine.parse(new String[]{"--test", "testValue"});
 commandLine.parse(new String[]{"--default=defaultValue"});
 option.defaults(commandLine);
 assertEquals("testValue", commandLine.getOptionValue("test"));
 assertEquals("defaultValue", commandLine.getOptionValue("default"));
 }
}