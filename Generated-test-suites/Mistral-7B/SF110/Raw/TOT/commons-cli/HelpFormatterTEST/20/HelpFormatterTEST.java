package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class HelpFormatterTest {

 private HelpFormatter formatter;
 private Options options;

 @BeforeEach
 public void setup() {
 formatter = new HelpFormatter();
 options = new Options();
 }

 @Test
 public void testDefaultPrintHelp() {
 final String cmdLineSyntax = "java MyClass [options]";
 options.addOption("a", "arg1", true, "An argument");
 options.addOption("b", "arg2", false, "Another argument");
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithPrefix() {
 final String cmdLineSyntax = "java MyClass [options]";
 final String prefix = "Usage: MyClass [options]";
 formatter.setSyntaxPrefix(prefix);
 options.addOption("a", "arg1", true, "An argument");
 options.addOption("b", "arg2", false, "Another argument");
 formatter.printHelp(cmdLineSyntax, options);
 assertEquals(prefix, formatter.getSyntaxPrefix());
 }

 @Test
 public void testPrintHelpWithWidth() {
 final String cmdLineSyntax = "java MyClass [options]";
 final int width = 50;
 formatter.setWidth(width);
 options.addOption("a", "arg1", true, "An argument");
 options.addOption("b", "arg2", false, "Another argument");
 formatter.printHelp(cmdLineSyntax, options);
 assertEquals(width, formatter.getWidth());
 }

 @Test
 public void testPrintHelpWithLeftPadding() {
 final String cmdLineSyntax = "java MyClass [options]";
 final int leftPad = 5;
 formatter.setLeftPadding(leftPad);
 options.addOption("a", "arg1", true, "An argument");
 options.addOption("b", "arg2", false, "Another argument");
 formatter.printHelp(cmdLineSyntax, options);
 assertEquals(leftPad, formatter.getLeftPadding());
 }

 @Test
 public void testPrintHelpWithDescPadding() {
 final String cmdLineSyntax = "java MyClass [options]";
 final int descPad = 5;
 formatter.setDescPadding(descPad);
 options.addOption("a", "arg1", true, "An argument");
 options.addOption("b", "arg2", false, "Another argument");
 formatter.printHelp(cmdLineSyntax, options);
 assertEquals(descPad, formatter.getDescPadding());
 }

 @Test
 public void testPrintHelpWithCustomOptionComparator() {
 final String cmdLineSyntax = "java MyClass [options]";
 options.addOption("z", "zarg", true, "A custom argument");
 options.addOption("a", "arg1", true, "An argument");
 formatter.setOptionComparator(new OptionComparator());
 formatter.printHelp(cmdLineSyntax, options);
 List<Option> optionsList = formatter.getOptions();
 Option zOption = null;
 Option aOption = null;
 for (Option option : optionsList) {
 if (option.getLongOpt().equals("zarg")) {
 zOption = option;
 }
 if (option.getLongOpt().equals("arg1")) {
 aOption = option;
 }
 }
 assertTrue(zOption.isAllowed(null));
 assertTrue(aOption.isAllowed(null));
 assertTrue(zOption.getOpt().compareTo(aOption.getOpt()) < 0);
 }

 @Test
 public void testPrintHelpWithArgName() {
 final String cmdLineSyntax = "java MyClass [options] [arg]";
 final String argName = "param";
 formatter.setArgName(argName);
 options.addOption("a", "arg1", true, "An argument");
 formatter.printHelp(cmdLineSyntax, options);
 assertEquals(argName, formatter.getArgName());
 }

 @Test
 public void testPrintHelpWithFooter() {
 final String cmdLineSyntax = "java MyClass [options]";
 final String footer = "MyFooter";
 formatter.printHelp(cmdLineSyntax, options, footer);
 assertEquals(footer, formatter.getLastPrintedMessage());
 }

 @Test
 public void testPrintHelpWithAutoUsage() {
 final String cmdLineSyntax = "java MyClass [options]";
 final String argName = "param";
 formatter.setArgName(argName);
 options.addOption("a", "arg1", true, "An argument");
 final CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options, null);
 }
 catch (ParseException e) {
 //Expected
 }
 formatter.printHelp(cmdLineSyntax, options, true);
 assertEquals(cmdLineSyntax, formatter.getLastPrintedMessage().substring(0, cmdLineSyntax.length()));
 }
}