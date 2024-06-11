package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

 private static final int DEFAULT_WIDTH = 74;
 private static final int DEFAULT_LEFT_PAD = 1;
 private static final int DEFAULT_DESC_PAD = 3;

 @Test
 public void testPrintHelp() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCmdLineSyntax() {
  String cmdLineSyntax = null;
  String header = "Options:";
  Options options = new Options();

  HelpFormatter formatter = new HelpFormatter();
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCmdLineSyntax() {
  String cmdLineSyntax = "";
  String header = "Options:";
  Options options = new Options();

  HelpFormatter formatter = new HelpFormatter();
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD, null, false);
 }

 @Test
 public void testPrintHelpWithAutoUsage() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, null, null, null, true);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addInvalidOption("x", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD, null, false);
 }

 @Test
 public void testPrintHelpWithCustomWidth() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setWidth(50);
  formatter.printHelp(System.out, 50, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomLeftPadding() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setLeftPadding(10);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, 10, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomDescPadding() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setDescPadding(10);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, 10);
 }

 @Test
 public void testPrintHelpWithCustomSyntaxPrefix() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  String syntaxPrefix = "usage: org.apache.commons.cli.HelpFormatter -h [options]\n";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setSyntaxPrefix(syntaxPrefix);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomNewLine() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  String newLine = "\n";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setNewLine(newLine);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomOptPrefix() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  String optPrefix = "--";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setOptPrefix(optPrefix);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomLongOptPrefix() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  String longOptPrefix = "--";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setLongOptPrefix(longOptPrefix);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomArgName() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  String argName = "filename";
  Options options = new Options();
  options.addRequiredOption("h", "help", false, "Print this help message.");
  options.addRequiredOption("v", "verbose", false, "Print verbose output.");
  options.addOption("i", "inputFile", true, "Input file.");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setArgName(argName);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }

 @Test
 public void testPrintHelpWithCustomOptionComparator() throws Exception {
  String cmdLineSyntax = "org.apache.commons.cli.HelpFormatter -h [options]";
  String header = "Options:";
  String[] optionsArray = {"optionA", "optionB", "optionC"};
  Options options = new Options();
  for (String option : optionsArray) {
      options.addOption(option, false, false, "Default option " + option);
  }
  HelpFormatter formatter = new HelpFormatter();
  Comparator comparator = (o1, o2) -> ((Option)o1).getLongOpt().compareToIgnoreCase((Option)o2.getLongOpt());
  formatter.setOptionComparator(comparator);
  formatter.printHelp(System.out, DEFAULT_WIDTH, cmdLineSyntax, header, options, DEFAULT_LEFT_PAD, DEFAULT_DESC_PAD);
 }
}