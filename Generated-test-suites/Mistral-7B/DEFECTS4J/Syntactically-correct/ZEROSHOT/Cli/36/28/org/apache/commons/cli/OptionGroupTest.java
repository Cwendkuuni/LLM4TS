package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testCreateOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 assertNotNull(optionGroup);
 }

 @Test
 public void testAddOptionToOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("test", "testOption", true, "test description");
 optionGroup.addOption(option);
 assertEquals(1, optionGroup.getOptions().size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullOptionToOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = null;
 optionGroup.addOption(option);
 }

 @Test
 public void testGetNamesOfOptionsInOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 Collection<String> names = optionGroup.getNames();
 assertEquals(2, names.size());
 assertTrue(names.contains("option1"));
 assertTrue(names.contains("option2"));
 }

 @Test
 public void testGetOptionsInOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(2, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 }

 @Test
 public void testSetSelectOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedOptionAlreadySelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testIsRequiredOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());

 OptionGroup requiredOptionGroup = new OptionGroup();
 requiredOptionGroup.setRequired(true);
 assertTrue(requiredOptionGroup.isRequired());
 }

 @Test
 public void testToStringOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals("[--option1 option1 description, --option2 option2 description]", optionGroup.toString());
 }
}