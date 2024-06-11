package org.apache.commons.cli.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1;
 private Option option2;
 private Option option3;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "option one", false, "o1");
 option2 = new Option("o2", "option two", false, "o2");
 option3 = new Option("o3", "option three", false, "o3");
 optionGroup.addOption(option1).addOption(option2).addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertTrue(names.contains("o1"));
 assertTrue(names.contains("o2"));
 assertTrue(names.contains("o3"));
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 assertTrue(options.contains(option3));
 }

 @Test
 public void testSetAndGetSelected() {
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 optionGroup.setSelected(option2);
 assertEquals("o2", optionGroup.getSelected());
 optionGroup.setSelected(option3);
 assertEquals("o3", optionGroup.getSelected());
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test(expected = org.apache.commons.cli.AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testIsRequired() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[-o1 option one, -o2 option two, --o3 option three]";
 assertEquals(expected, optionGroup.toString());
 }
}