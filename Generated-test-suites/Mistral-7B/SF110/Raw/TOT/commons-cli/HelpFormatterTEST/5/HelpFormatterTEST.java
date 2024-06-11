package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HelpFormatterTest {

 private HelpFormatter formatter;

 @Before
 public void setup() {
 formatter = new HelpFormatter();
 }

 @Test
 public void testPrintHelpWithDefaults() {
 final String cmdLineSyntax = "java MyApp";
 final Options options = new Options();
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedWidth() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final Options options = new Options();
 formatter.setWidth(80);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedLeftPadding() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final Options options = new Options();
 formatter.setLeftPadding(3);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedDescPadding() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final Options options = new Options();
 formatter.setDescPadding(5);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedSyntaxPrefix() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String syntaxPrefix = "myApp: ";
 final Options options = new Options();
 formatter.setSyntaxPrefix(syntaxPrefix);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedNewLine() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String newLine = "\n------------------\n";
 final Options options = new Options();
 formatter.setNewLine(newLine);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedOptPrefix() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String optPrefix = "-h";
 final Options options = new Options();
 formatter.setOptPrefix(optPrefix);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedLongOptPrefix() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String longOptPrefix = "--help";
 final Options options = new Options();
 formatter.setLongOptPrefix(longOptPrefix);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedArgName() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String argName = "param";
 final Options options = new Options();
 formatter.setArgName(argName);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testPrintHelpWithCustomizedOptionComparator() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final Options options = new Options();
 final HelpFormatter formatter2 = new HelpFormatter(new OptionComparator());
 formatter2.printHelp(cmdLineSyntax, options);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgWhenNullCmdLineSyntax() {
 final Options options = new Options();
 formatter.printHelp(null, options);
 }

 @Test
 public void testPrintHelpWithCustomizedHeader() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.printHelp(cmdLineSyntax, header, options);
 }

 @Test
 public void testPrintHelpWithCustomizedFooter() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String footer = "Copyright (C) 2022 My Company";
 final Options options = new Options();
 formatter.printHelp(cmdLineSyntax, null, options, footer);
 }

 @Test
 public void testPrintHelpWithCustomizedOptions() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final Options options = new Options();
 options.addOption("input", true, "Input file");
 options.addOption("output", true, "Output file");
 options.addOption("help", false, "Print this help message");
 formatter.printHelp(cmdLineSyntax, null, options);
 }

 @Test
 public void testPrintHelpWithOptionsGroup() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final Options options = new Options();
 final OptionGroup group = new OptionGroup("mygroup", "Group");
 group.setRequired(true);
 group.addOption("input", true, "Input file");
 group.addOption("output", true, "Output file");
 options.addOptionGroup(group);
 formatter.printHelp(cmdLineSyntax, null, options);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgWhenNullOptions() {
 formatter.printHelp("java MyApp arg1 arg2", null);
 }

 @Test
 public void testPrintHelpWithAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedWidthAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final int width = 60;
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setWidth(width);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedLeftPaddingAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final int leftPad = 5;
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setLeftPadding(leftPad);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedDescPaddingAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final int descPad = 7;
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setDescPadding(descPad);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedSyntaxPrefixAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String syntaxPrefix = "myApp: ";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setSyntaxPrefix(syntaxPrefix);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedNewLineAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String newLine = "\n------------------\n";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setNewLine(newLine);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedOptPrefixAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String optPrefix = "-h";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setOptPrefix(optPrefix);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedLongOptPrefixAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String longOptPrefix = "--help";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setLongOptPrefix(longOptPrefix);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }

 @Test
 public void testPrintHelpWithCustomizedArgNameAndAutoUsage() {
 final String cmdLineSyntax = "java MyApp arg1 arg2";
 final String argName = "param";
 final String header = "My App Help";
 final Options options = new Options();
 formatter.setArgName(argName);
 formatter.printHelp(cmdLineSyntax, header, options, null, true);
 }
}