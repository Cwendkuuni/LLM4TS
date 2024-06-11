package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithSingleOption() {
 OptionGroup group = new OptionGroup();
 Option option = new Option("o", "opt", true, "Option with -o or --opt");
 group.addOption(option);

 group.setSelected(option);
 Collection<Option> options = group.getOptions();

 assertEquals(1, options.size());
 assertEquals(option, options.iterator().next());
 assertEquals("opt", group.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupWithSameOptionSelectedTwice() {
 OptionGroup group = new OptionGroup();
 Option option = new Option("o", "opt", true, "Option with -o or --opt");
 group.addOption(option);
 group.setSelected(option);
 group.setSelected(option);
 }

 @Test
 public void testOptionGroupWithMultipleOptions() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option with -o1 or --opt1");
 Option option2 = new Option("o2", "opt2", true, "Option with -o2 or --opt2");
 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 Collection<Option> options = group.getOptions();

 assertEquals(2, options.size());
 assertEquals(option1, options.iterator().next());
 assertEquals("opt1", group.getSelected());
 }

 @Test
 public void testOptionGroupWithRequiredOption() {
 OptionGroup group = new OptionGroup();
 Option option = new Option("o", "opt", true, "Required Option with -o or --opt");
 group.setRequired(true);
 group.addOption(option);

 Collection<Option> options = group.getOptions();
 assertEquals(1, options.size());
 assertEquals(option, options.iterator().next());
 assertNull(group.getSelected());
 }

 @Test(expected = IllegalStateException.class)
 public void testOptionGroupWithNullOption() {
 OptionGroup group = new OptionGroup();
 Option option = null;
 group.addOption(option);
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option with -o1 or --opt1");
 Option option2 = new Option("o2", "opt2", true, "Option with -o2 or --opt2");
 group.addOption(option1);
 group.addOption(option2);
 group.setSelected(option1);

 String expected = "[--opt1]";
 assertEquals(expected, group.toString());
 }
}