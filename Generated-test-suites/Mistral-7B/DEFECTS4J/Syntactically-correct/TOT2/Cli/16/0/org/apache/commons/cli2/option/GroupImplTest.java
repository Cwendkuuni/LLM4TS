package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

 private Options options;
 private HelpFormatter formatter;

 @Before
 public void setUp() {
 List optionsList = new ArrayList();
 optionsList.add(new Option("g", "group", false, "a group option"));
 optionsList.add(new Option("a", "anonymous", 1, "an anonymous option"));
 optionsList.add(new Option("o", "option", 2, "an option with value"));
 GroupImpl group = new GroupImpl(optionsList, "TestGroup", "test group", 0, 1);
 this.options = new Options();
 this.options.addOption(group);
 this.formatter = new HelpFormatter();
 }

 @Test
 public void testBasicUsage() throws ParseException {
 CommandLine cmd = this.options.parse("--group");
 assertTrue(cmd.isSet("group"));
 }

 @Test
 public void testAnonymousUsage() throws ParseException {
 CommandLine cmd = this.options.parse("--anonymous");
 assertEquals(1, cmd.getOptions().size());
 assertTrue(cmd.getOptionValues("anonymous").length > 0);
 }

 @Test
 public void testOptionUsage() throws ParseException {
 CommandLine cmd = this.options.parse("--group --option value1 value2");
 assertTrue(cmd.isSet("group"));
 assertTrue(cmd.isSet("option"));
 assertEquals(2, cmd.getOptionValues("option").length);
 assertEquals("value1", cmd.getOptionValues("option")[0]);
 assertEquals("value2", cmd.getOptionValues("option")[1]);
 }

 @Test(expected = ParseException.class)
 public void testMissingGroup() throws ParseException {
 CommandLine cmd = this.options.parse("--missing");
 fail("parse should have thrown an exception");
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 CommandLine cmd = this.options.parse("--group");
 cmd.parseArgument(0, "missing");
 fail("parse should have thrown an exception");
 }

 @Test(expected = ParseException.class)
 public void testTooManyOptions() throws ParseException {
 CommandLine cmd = this.options.parse("--group --option value1 value2 value3");
 fail("parse should have thrown an exception");
 }

 @Test
 public void testHelp() {
 StringWriter sw = new StringWriter();
 this.formatter.printHelp("test", this.options);
 String help = sw.toString();
 assertTrue(help.contains("TestGroup (test group)"));
 assertTrue(help.contains("--group"));
 assertTrue(help.contains("--anonymous <anonymous>"));
 assertTrue(help.contains("--option <value>"));
 }
}