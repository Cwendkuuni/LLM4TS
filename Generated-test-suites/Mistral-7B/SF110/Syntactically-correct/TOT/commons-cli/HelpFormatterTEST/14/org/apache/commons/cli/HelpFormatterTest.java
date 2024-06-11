package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit. Assert;

import java.util.Arrays;

public class HelpFormatterTest {

 @org.junit.Test
 public void testDefaultHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass [options]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "description for arg1");
 options.addOption("b", "arg2", false, "description for arg2");
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testCustomHelp() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(100);
 formatter.setLeftPadding(5);
 formatter.setDescPadding(10);
 formatter.setSyntaxPrefix("usage: mytool.sh ");
 formatter.setNewLine("\n");
 formatter.setOptPrefix("--");
 formatter.setLongOptPrefix("--");
 formatter.setArgName("ARG");
 String cmdLineSyntax = "java MyClass [options]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "description for arg1");
 options.addOption("b", "arg2", false, "description for arg2");
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testHelpWithArgs() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass arg1 arg2";
 Options options = new Options();
 options.addOption("a", "arg1", true, "description for arg1");
 options.addOption("b", "arg2", true, "description for arg2");
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testInvalidHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass [options]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "description for arg1");
 try {
 formatter.printHelp(cmdLineSyntax, options, true, null, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @org.junit.Test
 public void testHelpWithLongOptions() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass [options]";
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("group1", "Group1", "Options in group1"));
 options.addOption("g1-a", "global arg1", true, "description for global arg1 in group1");
 options.addOption("g1-b", "global arg2", false, "description for global arg2 in group1");
 options.addOptionGroup(new OptionGroup("group2", "Group2", "Options in group2"));
 options.addOption("g2-a", "group arg1", true, "description for group arg1 in group2");
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testHelpWithOptionalArgs() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass arg1 [arg2]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "description for arg1");
 options.addOption("b", "arg2", false, "description for arg2");
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testHelpWithInvalidOptions() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass [options]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "description for arg1");
 options.addOption("x", null, false, "description for x");
 try {
 formatter.printHelp(cmdLineSyntax, options);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}