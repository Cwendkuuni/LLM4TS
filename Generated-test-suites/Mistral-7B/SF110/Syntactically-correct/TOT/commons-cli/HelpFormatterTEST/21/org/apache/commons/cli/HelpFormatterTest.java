package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class HelpFormatterTest {

 @Test
 public void testDefaultHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "MyCommand [-a] [-b arg] [--long-option]";
 Options options = new Options();
 options.addOption("a", "option a", false);
 options.addOption("b", "option b with argument", true);
 options.addOption("long-option", "long option");

 formatter.printHelp(System.out, cmdLineSyntax, options);
 }

 @Test
 public void testCustomHelp() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(50);
 formatter.setLeftPadding(5);
 formatter.setDescPadding(10);
 formatter.setSyntaxPrefix("MyCommand: ");
 formatter.setOptPrefix("--");
 formatter.setLongOptPrefix("--");
 formatter.setArgName("ARG");

 String cmdLineSyntax = "MyCommand [OPTIONS]";
 Options options = new Options();
 options.addOption("a", "option a", false);
 options.addOption("b", "option b with argument", true);
 options.addOption("long-option", "long option");
 options.addOptionGroup(new OptionGroup("group", "group options", Arrays.asList(
 new Option("g", "group option", false),
 new Option("h", "help", true, "show this help message and exit"))));

 formatter.printHelp(System.out, cmdLineSyntax, options, "Custom Help");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOptions() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "MyCommand [OPTIONS]";
 Options options = null;
 formatter.printHelp(System.out, cmdLineSyntax, options);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCmdLineSyntax() {
 HelpFormatter formatter = new HelpFormatter();
 Options options = new Options();
 formatter.printHelp(System.out, null, options);
 }
}