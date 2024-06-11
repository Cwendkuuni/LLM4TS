package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithSimpleOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option optA = new Option("a", "option A");
 Option optB = new Option("b", "option B");

 optionGroup.addOption(optA);
 optionGroup.addOption(optB);

 try {
 optionGroup.setSelected(optA);
 assertEquals("option A", optionGroup.getSelected());
 } catch (Exception e) {
 // expected - no option can be selected before adding it
 }

 try {
 optionGroup.setSelected(optB);
 assertEquals("option B", optionGroup.getSelected());
 } catch (AlreadySelectedException e) {
 // expected - an option can only be selected once
 }

 try {
 optionGroup.setSelected(optA);
 assertEquals("option A", optionGroup.getSelected());
 } catch (Exception e) {
 // expected - an option can only be selected once
 }

 try {
 optionGroup.setSelected(null);
 assertEquals(null, optionGroup.getSelected());
 } finally {
 // reset to allow further testing
 optionGroup.setSelected(optB);
 }

 assertEquals(2, optionGroup.getOptions().size());
 assertEquals(2, optionGroup.getNames().size());
 }

 @Test
 public void testOptionGroupWithRequiredOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option optA = new Option("a", true, "option A");
 Option optB = new Option("b", false, "option B");

 optionGroup.addOption(optA);
 optionGroup.addOption(optB);

 try {
 optionGroup.setSelected(optA);
 assertEquals("option A", optionGroup.getSelected());
 } catch (Exception e) {
 // expected - an option cannot be selected if it's not present
 }

 try {
 optionGroup.setSelected(optB);
 assertNull(optionGroup.getSelected());
 } finally {
 // reset to allow further testing
 optionGroup.setSelected(optA);
 }

 try {
 optionGroup.setSelected(null);
 assertEquals(null, optionGroup.getSelected());
 } finally {
 // reset to allow further testing
 optionGroup.setSelected(optA);
 }

 try {
 optionGroup.setSelected(optB);
 assertNull(optionGroup.getSelected());
 } catch (IllegalArgumentException e) {
 // expected - a required option must be selected
 }

 try {
 optionGroup.setSelected(optA);
 assertEquals("option A", optionGroup.getSelected());
 } finally {
 // reset to allow further testing
 optionGroup.setSelected(null);
 }

 assertEquals(2, optionGroup.getOptions().size());
 assertEquals(2, optionGroup.getNames().size());
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option optA = new Option("a", null);
 Option optB = new Option("b", null);
 Option optC = new Option("c", null);
 Option optD = new Option("d", null);

 optionGroup.addOption(optA);
 optionGroup.addOption(optB);
 optionGroup.addOption(optC);
 optionGroup.addOption(optD);

 assertEquals("[--a, --b, --c, --d]", optionGroup.toString());
 }
}