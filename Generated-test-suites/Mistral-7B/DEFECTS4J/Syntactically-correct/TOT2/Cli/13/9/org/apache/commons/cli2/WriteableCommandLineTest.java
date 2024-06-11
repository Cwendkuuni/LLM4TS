package org.apache.commons.cli2;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class WriteableCommandLineTest {
   // Test cases for addOption method
   // Test case for adding an option
   @Test
   public void testAddOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock(); // Assuming a mock implementation is available
      Option option = new Option("input", "i", false, "Input file path");
      commandLine.addOption(option);
      Assert.assertEquals(option, commandLine.getOptions().get(0));
   }

   // Edge case: Adding an existing option
   @Test(expected = IllegalStateException.class)
   public void testAddOptionWithExistingOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("input", "i", false, "Input file path");
      commandLine.addOption(option);
      commandLine.addOption(option);
   }

   // Test cases for addValue method
   // Test case for adding a value to an option
   @Test
   public void testAddValue() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("input", "i", false, "Input file path");
      commandLine.addOption(option);
      commandLine.addValue(option, "input.txt");
      List<String> values = commandLine.getOptionValue("input");
      Assert.assertEquals("input.txt", values.get(0));
   }

   // Edge case: Adding a value to an option without an option
   @Test(expected = IllegalArgumentException.class)
   public void testAddValueWithoutOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      commandLine.addValue(null, "input.txt");
   }

   // Test cases for getValues method
   // Test case for getting values for an existing option
   @Test
   public void testGetValues() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("input", "i", false, "Input file path");
      commandLine.addOption(option);
      commandLine.addValue(option, "input.txt");
      List<String> values = commandLine.getValues("input");
      Assert.assertEquals("input.txt", values.get(0));
      Assert.assertEquals(1, values.size());
   }

   // Edge case: Getting values for a non-existent option
   @Test
   public void testGetValuesForNonExistentOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      List<String> values = commandLine.getValues("output");
      Assert.assertTrue(values.isEmpty());
   }

   // Test cases for setDefaultValues method
   // Test case for setting default values for an option
   @Test
   public void testSetDefaultValues() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("input", "i", false, "Input file path");
      List<String> defaultValues = Arrays.asList("input1.txt", "input2.txt");
      commandLine.setDefaultValues(option, defaultValues);
      List<String> values = commandLine.getValues("input");
      Assert.assertEquals(defaultValues, values);
   }

   // Edge case: Setting default values for an existing option
   @Test
   public void testSetDefaultValuesForExistingOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("input", "i", false, "Input file path");
      commandLine.addOption(option);
      List<String> defaultValues = Arrays.asList("input1.txt", "input2.txt");
      commandLine.setDefaultValues(option, defaultValues);
      List<String> values = commandLine.getValues("input");
      Assert.assertEquals(defaultValues, values);
   }

   // Edge case: Setting default values for a non-existent option
   @Test(expected = IllegalArgumentException.class)
   public void testSetDefaultValuesForNonExistentOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("output", "o", false, "Output file path");
      List<String> defaultValues = Arrays.asList("output1.txt", "output2.txt");
      commandLine.setDefaultValues(option, defaultValues);
      List<String> values = commandLine.getValues("output");
      Assert.assertTrue(values.isEmpty());
   }

   // Test cases for addSwitch method
   // Test case for adding a switch value to an option
   @Test
   public void testAddSwitch() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("verbose", "v", false, "Verbose output");
      commandLine.addSwitch(option, true);
      Assert.assertEquals(true, commandLine.getValue("verbose"));
   }

   // Edge case: Adding a switch value to an option that already has a value
   @Test(expected = IllegalStateException.class)
   public void testAddSwitchWithExistingSwitch() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("verbose", "v", false, "Verbose output");
      commandLine.addSwitch(option, true);
      commandLine.addSwitch(option, true);
   }

   // Test cases for setDefaultSwitch method
   // Test case for setting the default state for a switch in the command line
   @Test
   public void testSetDefaultSwitch() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("verbose", "v", false, "Verbose output");
      commandLine.setDefaultSwitch(option, true);
      Assert.assertEquals(true, commandLine.getDefaultSwitch("verbose"));
   }

   // Edge case: Setting the default state for a switch in the command line that already has a value
   @Test(expected = IllegalStateException.class)
   public void testSetDefaultSwitchWithExistingSwitch() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Option option = new Option("verbose", "v", false, "Verbose output");
      commandLine.addSwitch(option, true);
      commandLine.setDefaultSwitch(option, true);
   }

   // Test cases for addProperty method
   // Test case for adding a property value to a name in the command line
   @Test
   public void testAddProperty() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      commandLine.addProperty("property1", "value1");
      Assert.assertEquals("value1", commandLine.getProperty("property1"));
   }

   // Edge case: Adding a property value to a non-existent name in the command line
   @Test(expected = IllegalArgumentException.class)
   public void testAddPropertyForNonExistentName() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      commandLine.addProperty("property2", "value2");
      Assert.assertNull(commandLine.getProperty("property1"));
   }

   // Test case for looking like an option
   @Test
   public void testLooksLikeOption() {
      WriteableCommandLine commandLine = new Cli2WriterMock();
      Assert.assertFalse(commandLine.looksLikeOption("--input"));
      Assert.assertTrue(commandLine.looksLikeOption("--input="));
      Assert.assertTrue(commandLine.looksLikeOption("input="));
   }
}