package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {

 private PatternBuilder pb;

 @Test
 public void testCreateOption() {
  pb = new PatternBuilder();
 pb.withPattern("@myclass:arg1:/ arg2:+arg3:#date:<file:existing>:*arg4");

  Option option = pb.create();

  assertNotNull(option);

  Argument arg1 = option.getArgument();
  assertNotNull(arg1);
  assertNull(arg1.getValue());
  assertTrue(arg1.getMetavar().equals("arg1"));
  assertTrue(arg1.getValidator() instanceof ClassValidator);

  Argument arg2 = option.getArgument();
  assertNotNull(arg2);
  assertNull(arg2.getValue());
  assertTrue(arg2.getMetavar().equals("arg2"));
  assertNull(arg2.getValidator());

  Argument arg3 = option.getArgument();
  assertNotNull(arg3);
  assertNull(arg3.getValue());
  assertTrue(arg3.getMetavar().equals("arg3"));
  assertTrue(arg3.getValidator() instanceof NumberValidator);

  Argument arg4 = option.getArgument();
  assertNotNull(arg4);
  assertNull(arg4.getValue());
  assertTrue(arg4.getMetavar().equals("arg4"));
  assertNull(arg4.getValidator());

  Option myOption = (Option)options.iterator().next();
  assertTrue(myOption.getShortName() == 'm');
  assertTrue(myOption.getLongOpt().equals("myclass"));
  assertNotNull(myOption.getArgument());

  Option arg1Opt = (Option)options.iterator().next();
  assertTrue(arg1Opt.getShortName() == ' ');
  assertTrue(arg1Opt.getLongOpt().equals("arg1"));
  assertNotNull(arg1Opt.getArgument());

  Option arg2Opt = (Option)options.iterator().next();
  assertTrue(arg2Opt.getShortName() == ' ');
  assertTrue(arg2Opt.getLongOpt().equals("arg2"));
  assertNull(arg2Opt.getArgument());

  Option arg3Opt = (Option)options.iterator().next();
  assertTrue(arg3Opt.getShortName() == '+');
  assertTrue(arg3Opt.getLongOpt().equals("arg3"));
  assertNotNull(arg3Opt.getArgument());

  Option arg4Opt = (Option)options.iterator().next();
  assertTrue(arg4Opt.getShortName() == ' ');
  assertTrue(arg4Opt.getLongOpt().equals("arg4"));
  assertNull(arg4Opt.getArgument());
 }

 @Test
 public void testCreateOptionWithInvalidPattern() {
  pb = new PatternBuilder();
  pb.withPattern("@myclass:arg1:/ arg2:/arg3:#date:<file:existing>:*arg4");

  try {
   pb.create();
   fail("An IllegalArgumentException should have been thrown");
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test
 public void testCreateOptionWithEmptyPattern() {
  pb = new PatternBuilder();
  pb.withPattern("");

  try {
   pb.create();
   fail("An IllegalArgumentException should have been thrown");
  } catch (IllegalArgumentException e) {
   //expected
  }
 }

 @Test
 public void testCreateOptionWithMissingPattern() {
  pb = new PatternBuilder();

  try {
   pb.create();
   fail("An IllegalStateException should have been thrown");
  } catch (IllegalStateException e) {
   //expected
  }
 }
}