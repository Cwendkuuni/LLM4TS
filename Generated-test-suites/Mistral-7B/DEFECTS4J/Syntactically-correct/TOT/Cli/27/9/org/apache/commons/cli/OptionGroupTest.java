package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");
 Option option3 = new Option("o3", "opt3", false, "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals(3, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");
 Option option3 = new Option("o3", "opt3", false, "Option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertTrue(optionGroup.getNames().contains("o1"));
 assertTrue(optionGroup.getNames().contains("o2"));
 assertTrue(optionGroup.getNames().contains("o3"));
 }

 @Test
 public void testSetSelectedNull() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);

 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedAlreadySelected() throws Exception {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 try {
 optionGroup.setSelected(option1);
 } catch (Exception e) {
 // expected exception
 }

 assertEquals(option1.getOpt(), optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedDifferentOption() throws Exception {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 try {
 optionGroup.setSelected(option2);
 } catch (Exception e) {
 // expected exception
 }

 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "opt1", true, "Option 1");
 Option option2 = new Option("o2", "opt2", false, "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setRequired(true);

 assertTrue(optionGroup.isRequired());
 }
}