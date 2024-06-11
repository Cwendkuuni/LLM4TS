package org.apache.commons.cli2.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for OptionImpl
 */
public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;
 private ResourceHelper resourceHelper;

 @Before
 public void setup() {
 // Initialize test resources
 resourceHelper = new ResourceHelper();

 // Initialize test OptionImpl
 option = new OptionImpl(1, true);
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testOptionCreation() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 String[] arguments = {"arg1", "arg2"};
 ListIterator<String> argumentsIterator = Arrays.asList(arguments).listIterator();

 assertTrue(option.canProcess(commandLine, argumentsIterator));
 argumentsIterator.next();
 assertFalse(option.canProcess(commandLine, argumentsIterator));
 }

 @Test
 public void testToString() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals("", option.toString());
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, true);

 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, true);
 OptionImpl option2 = new OptionImpl(1, true);
 OptionImpl option3 = new OptionImpl(2, true);
 String name = "OptionName";

 int hashCode1 = option1.hashCode();
 OptionImpl option1WithName = new OptionImpl(1, true, name);
 int hashCode2 = option1WithName.hashCode();
 int hashCode3 = option3.hashCode();

 assertEquals(hashCode1, hashCode1);
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testFindOption() {
 OptionImpl option1 = new OptionImpl(1, true, "opt");
 OptionImpl option2 = new OptionImpl(2, false, "opt2");
 OptionImpl option3 = new OptionImpl(1, true, "opt");
 List<OptionImpl> options = new ArrayList<>();
 options.add(option1);
 options.add(option2);
 options.add(option3);

 OptionImpl foundOption = option3.findOption("opt");
 assertEquals(option3, foundOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixes() {
 OptionImpl option = new OptionImpl(1, true);
 Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));
 option.checkPrefixes(prefixes);
 }

 @Test
 public void testCheckPrefixesSuccess() {
 OptionImpl option = new OptionImpl(1, true);
 Set<String> prefixes = new HashSet<>(Arrays.asList("opt"));
 option.checkPrefixes(prefixes);
 }
}