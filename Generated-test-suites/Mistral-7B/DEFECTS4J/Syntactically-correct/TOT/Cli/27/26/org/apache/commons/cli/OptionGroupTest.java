package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 // Test case for addOption() method
 @Test
 public void testAddOption() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 group.addOption(option1);
 group.addOption(option2);

 Collection<Option> options = group.getOptions();
 assertEquals(2, options.size());
 assertEquals(option1, options.iterator().next());
 assertEquals(option2, options.iterator().next());
 }

 // Test case for getNames() method
 @Test
 public void testGetNames() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 group.addOption(option1);
 group.addOption(option2);

 Collection<String> names = group.getNames();
 assertEquals(2, names.size());
 assertEquals("o1", names.iterator().next());
 assertEquals("o2", names.iterator().next());
 }

 // Test case for setSelected() method with valid option
 @Test
 public void testSetSelectedValid() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 assertEquals("o1", group.getSelected());

 group.setSelected(option2);
 assertEquals("o2", group.getSelected());
 }

 // Test case for setSelected() method with null option
 @Test(expected = NullPointerException.class)
 public void testSetSelectedNull() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(null);
 }

 // Test case for setSelected() method with already selected option
 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option one");
 Option option2 = new Option("o2", "option two");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 group.setSelected(option1);
 }

 // Test case for isRequired() method
 @Test
 public void testIsRequired() {
 OptionGroup group = new OptionGroup();
 OptionGroup requiredGroup = new OptionGroup();
 requiredGroup.setRequired(true);

 assertEquals(false, group.isRequired());
 assertEquals(true, requiredGroup.isRequired());
 }
}