package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("option1", "Option 1 Description");
 Option addedOption = optionGroup.addOption(option);
 assertEquals(option, addedOption);
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1 Description");
 Option option2 = new Option("option2", "Option 2 Description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
 }

 @Test
 public void testGetOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1 Description");
 Option option2 = new Option("option2", "Option 2 Description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertFalse(optionGroup.getOptions().isEmpty());
 }

 @Test
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1 Description");
 Option option2 = new Option("option1", "Option 1 Description");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1 Description");
 Option option2 = new Option("option2", "Option 2 Description");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 assertEquals("[--option1 Option 1 Description, --option2 Option 2 Description]", optionGroup.toString());
 }
}