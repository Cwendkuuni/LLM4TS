package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

 @Test
 public void testPatternBuilder() {
 PatternBuilder pb = new PatternBuilder();

 // Test empty pattern
 pb.reset();
 Option option = pb.create();
 assertNull(option);

 // Test simple option
 pb.withPattern("a:");
 Option option1 = pb.create();
 assertEquals("a", option1.getLongOpt());
 assertNull(option1.getArgName());
 assertFalse(option1.isRequired());

 // Test option with argument
 pb.reset();
 pb.withPattern("a:s");
 Option option2 = pb.create();
 assertEquals("a", option2.getLongOpt());
 assertEquals("s", option2.getArgName());
 assertFalse(option2.isRequired());

 // Test required option
 pb.reset();
 pb.withPattern("a:s");
 pb.withPattern("!a:s");
 Option option3 = pb.create();
 assertEquals("a", option3.getLongOpt());
 assertEquals("s", option3.getArgName());
 assertTrue(option3.isRequired());

 // Test option with class validator
 pb.reset();
 pb.withPattern("@com.example.MyClass");
 Option option4 = pb.create();
 assertEquals("com.example.MyClass", option4.getLongOpt());
 assertNull(option4.getArgName());
 assertNotNull(option4.getValidator());

 // Test option with number validator
 pb.reset();
 pb.withPattern("%d");
 Option option5 = pb.create();
 assertEquals("d", option5.getLongOpt());
 assertNull(option5.getArgName());
 assertNotNull(option5.getValidator());

 // Test option with date validator
 pb.reset();
 pb.withPattern("#yyyy-MM-dd");
 Option option6 = pb.create();
 assertEquals("yyyy-MM-dd", option6.getLongOpt());
 assertNull(option6.getArgName());
 assertNotNull(option6.getValidator());

 // Test option with file validator (existing)
 pb.reset();
 pb.withPattern("<myfile.txt>");
 Option option7 = pb.create();
 assertEquals("myfile.txt", option7.getLongOpt());
 assertNull(option7.getArgName());
 assertNotNull(option7.getValidator());

 // Test option with file validator (not existing)
 pb.reset();
 pb.withPattern("!<myfile.txt>");
 Option option8 = pb.create();
 assertEquals("myfile.txt", option8.getLongOpt());
 assertNull(option8.getArgName());
 assertNotNull(option8.getValidator());

 // Test option with url validator
 pb.reset();
 pb.withPattern("/myurl");
 Option option9 = pb.create();
 assertEquals("myurl", option9.getLongOpt());
 assertNull(option9.getArgName());
 assertNotNull(option9.getValidator());

 // Test option with wildcard
 pb.reset();
 pb.withPattern("a:*");
 Option option10 = pb.create();
 assertEquals("a", option10.getLongOpt());
 assertNull(option10.getArgName());
 assertTrue(option10.isRequired());

 // Test group option
 pb.reset();
 pb.withPattern("g a:s,g b:s");
 Option groupOption = pb.create();
 assertEquals("g", groupOption.getLongOpt());
 assertNotNull(groupOption.getArgumentSpecs());
 assertEquals(2, groupOption.getArgumentSpecs().size());
 }
}