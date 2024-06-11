package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "This is option 1";
 }

 @Override
 public List getPrefixes() {
 return Arrays.asList("--prefix1", "--prefix2");
 }

 @Override
 public List getTriggers() {
 return Arrays.asList("opt1", "op1");
 }

 @Override
 public void defaults(WriteableCommandLine commandLine) {
 // no implementation required
 }
 };
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, null));

 final List<String> arguments = Arrays.asList("--prefix1", "opt1", "arg");
 final Iterator<String> argumentsIterator = arguments.listIterator();
 assertTrue(option.canProcess(commandLine, argumentsIterator));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 final Set prefixes = new HashSet(Arrays.asList("--prefix1", "--prefix2", "--prefix3"));
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesWithValidPrefixes() {
 final Set prefixes = new HashSet(Arrays.asList("--prefix1", "--prefix2", "--prefix1"));
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerNeedsPrefix() {
 option.checkPrefixes(new HashSet(Arrays.asList("--prefix1", "--prefix2")));
 final String trigger = "op1";
 option.findOption(trigger);
 }

 @Test
 public void testEquals() {
 final OptionImpl option2 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "This is option 1";
 }

 @Override
 public List getPrefixes() {
 return Arrays.asList("--prefix1", "--prefix2");
 }

 @Override
 public List getTriggers() {
 return Arrays.asList("opt1", "op1");
 }
 };
 assertTrue(option.equals(option));
 assertTrue(option.equals(option2));

 final OptionImpl option3 = new OptionImpl(2, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "This is option 1";
 }

 @Override
 public List getPrefixes() {
 return Arrays.asList("--prefix1", "--prefix2");
 }

 @Override
 public List getTriggers() {
 return Arrays.asList("opt1", "op1");
 }
 };
 assertFalse(option.equals(option3));
 }

 @Test
 public void testHashCode() {
 final OptionImpl option2 = new OptionImpl(1, true) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "This is option 1";
 }

 @Override
 public List getPrefixes() {
 return Arrays.asList("--prefix1", "--prefix2");
 }

 @Override
 public List getTriggers() {
 return Arrays.asList("opt1", "op1");
 }
 };
 assertEquals(option.hashCode(), option2.hashCode());

 final OptionImpl option3 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "This is option 1";
 }

 @Override
 public List getPrefixes() {
 return Arrays.asList("--prefix1", "--prefix2");
 }

 @Override
 public List getTriggers() {
 return Arrays.asList("opt1", "op1");
 }
 };
 assertNotEquals(option.hashCode(), option3.hashCode());
 }

 @Test
 public void testFindOption() {
 final OptionImpl option2 = new OptionImpl(2, true) {
 @Override
 public String getPreferredName() {
 return "option2";
 }

 @Override
 public String getDescription() {
 return "This is option 2";
 }

 @Override
 public List getPrefixes() {
 return Arrays.asList("--prefix1", "--prefix2");
 }

 @Override
 public List getTriggers() {
 return Arrays.asList("opt2", "op2");
 }
 };
 final Option optionFound = option.findOption("opt1");
 assertNull(optionFound);
 final Option optionFound2 = option2.findOption("opt2");
 assertSame(option2, optionFound2);
 }

 @Test
 public void testIsRequired() {
 assertTrue(option.isRequired());
 }

 @Test
 public void testToString() {
 final String expected = "[--prefix1, --prefix2, -o, opt1, op1] This is option 1";
 assertEquals(expected, option.toString());
 }
}