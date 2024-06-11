package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

 @Test
 public void testDefaultHelpFormatter() {
 HelpFormatter formatter = new HelpFormatter();

 String cmdLineSyntax = "java MyClass [args1] [args2]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 options.addOption("b", "arg2", false, "another argument");
 options.addOptionGroup(new OptionGroup("group", "Group Options") {{
 setRequired(false);
 addOption("g", "groupArg", false, "a group argument");
 }});

 formatter.printHelp("MyClass", options);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCmdLineSyntax() {
 HelpFormatter formatter = new HelpFormatter();
 Options options = new Options();
 formatter.printHelp(null, options);
 }

 @Test
 public void testCustomHelpFormatter() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setLeftPadding(5);
 formatter.setDescPadding(10);
 formatter.setSyntaxPrefix("myprefix ");
 formatter.setOptPrefix("myopt-");
 formatter.setLongOptPrefix("--mylongopt-");
 formatter.setArgName("argname");

 String cmdLineSyntax = "java MyClass [args1] [args2]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 options.addOption("b", "arg2", false, "another argument");
 options.addOptionGroup(new OptionGroup("group", "Group Options") {{
 setRequired(false);
 addOption("g", "groupArg", false, "a group argument");
 }});

 formatter.printHelp("MyClass", cmdLineSyntax, options);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalWidth() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(-5);
 }

 @Test
 public void testPrintHelpWithEmptyOptions() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass";
 formatter.printHelp(cmdLineSyntax, new Options());
 }

 @Test
 public void testPrintHelpWithAutoUsage() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass [args1] [args2]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 options.addOption("b", "arg2", false, "another argument");
 options.addOptionGroup(new OptionGroup("group", "Group Options") {{
 setRequired(false);
 addOption("g", "groupArg", false, "a group argument");
 }});
 formatter.printHelp(cmdLineSyntax, options, true);
 }

 @Test
 public void testPrintHelpWithHeaderAndFooter() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyClass [args1] [args2]";
 Options options = new Options();
 options.addOption("a", "arg1", true, "an argument");
 options.addOption("b", "arg2", false, "another argument");
 options.addOptionGroup(new OptionGroup("group", "Group Options") {{
 setRequired(false);
 addOption("g", "groupArg", false, "a group argument");
 }});
 formatter.setSyntaxPrefix("myprefix ");
 formatter.setHeadFirst(true);
 formatter.setHeader("Welcome to my application\n");
 formatter.setFooter("For more information visit www.myapp.com");
 formatter.printHelp("MyClass", cmdLineSyntax, options, "My Application\n");
 }
}