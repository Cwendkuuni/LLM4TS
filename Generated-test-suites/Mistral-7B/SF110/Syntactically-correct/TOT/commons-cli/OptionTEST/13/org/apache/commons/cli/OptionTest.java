package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testBasicOption() throws ParseException {
 Option option = new Option("a", "alert", false, "Print an alert message");
 Options options = new Options();
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(new String[]{"-a"}));
 Assert.assertFalse("Option 'a' should not have any value", cmd.hasOption("a"));
 }

 @Test
 public void testOptionWithArg() throws ParseException {
 Option option = new Option("b", "baseDir", true, "Base directory for the test");
 options.addOption(option);
 cmd = parser.parse(options.parse(new String[]{"-b", "/tmp"}));
 Assert.assertEquals("/tmp", cmd.getOptionValue("baseDir"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Option option = new Option("c", "configFile", true, "Configuration file path");
 options.addOption(option);
 cmd = parser.parse(options.parse(new String[]{"-d"}));
 }

 @Test
 public void testLongOption() throws ParseException {
 Option option = OptionBuilder.withLongOpt("database")
 .hasArg()
 .isRequired()
 .desc("Database connection URL")
 .build();
 options.addOption(option);
 cmd = parser.parse(options.parse(new String[]{"--database", "jdbc:mysql://localhost:3306/testdb"}));
 Assert.assertEquals("jdbc:mysql://localhost:3306/testdb", cmd.getOptionValue("database"));
 }

 @Test
 public void testMultipleValues() throws ParseException {
 Option option = OptionBuilder.withLongOpt("directories")
 .valueSeparator(';')
 .hasArg()
 .isRequired()
 .desc("List of directories to search")
 .build();
 options.addOption(option);
 cmd = parser.parse(options.parse(new String[]{"--directories", "/usr/local/lib;/usr/share/java"}));
 List<String> directories = (List<String>)cmd.getValues("directories");
 Assert.assertEquals(2, directories.size());
 Assert.assertEquals("/usr/local/lib", directories.get(0));
 Assert.assertEquals("/usr/share/java", directories.get(1));
 }
}