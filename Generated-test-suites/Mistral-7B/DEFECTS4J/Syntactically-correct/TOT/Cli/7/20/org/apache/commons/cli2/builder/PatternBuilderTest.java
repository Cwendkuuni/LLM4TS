import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

 @Test
 public void testSimpleOption() {
 Argument arg;
 Option option;
 PatternBuilder builder = new PatternBuilder();

 builder.withPattern("a:");
 option = builder.create();
 assertFalse(option.isRequired());
 assertNull(arg = option.getArg());

 builder.reset();
 builder.withPattern("a:s");
 option = builder.create();
 arg = option.getArg();
 assertNotNull(arg);
 assertNull(arg.getName());
 assertNull(arg.getValue());
 assertFalse(option.isRequired());
 }

 @Test
 public void testRequiredOption() {
 Argument arg;
 Option option;
 PatternBuilder builder = new PatternBuilder();

 builder.withPattern("a:");
 option = builder.create();
 assertFalse(option.isRequired());

 builder.reset();
 builder.withPattern("a:s");
 option = builder.create();
 assertNotNull(option.getArg());
 assertNull(option.getArg().getName());
 assertNull(option.getArg().getValue());
 assertFalse(option.isRequired());

 builder.reset();
 builder.withPattern("a:s{1,1}");
 option = builder.create();
 assertNotNull(option.getArg());
 assertNotNull(option.getArg().getName());
 assertNull(option.getArg().getValue());
 assertTrue(option.isRequired());

 builder.reset();
 builder.withPattern("a:s{1,}");
 option = builder.create();
 assertNotNull(option.getArg());
 assertNotNull(option.getArg().getName());
 assertNull(option.getArg().getValue());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionalMultiArgOption() {
 Argument arg;
 Option option;
 PatternBuilder builder = new PatternBuilder();

 builder.withPattern("a:*");
 option = builder.create();
 arg = option.getArg();
 assertNotNull(arg);
 assertNull(arg.getName());
 assertNull(arg.getValue());
 assertFalse(option.isRequired());

 builder.reset();
 builder.withPattern("a:s*");
 option = builder.create();
 arg = option.getArg();
 assertNotNull(arg);
 assertNull(arg.getName());
 assertNull(arg.getValue());
 assertFalse(option.isRequired());

 builder.reset();
 builder.withPattern("a:s{1,}*");
 option = builder.create();
 arg = option.getArg();
 assertNotNull(arg);
 assertNull(arg.getName());
 assertNull(arg.getValue());
 assertFalse(option.isRequired());

 builder.reset();
 builder.withPattern("a:s{1,2}*");
 option = builder.create();
 arg = option.getArg();
 assertNotNull(arg);
 assertNull(arg.getName());
 assertNull(arg.getValue());
 assertFalse(option.isRequired());
 }

 @Test
 public void testErrorWhenNoPattern() {
 PatternBuilder builder = new PatternBuilder();
 try {
 builder.create();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testErrorWhenInvalidOption() {
 PatternBuilder builder = new PatternBuilder();
 try {
 builder.withPattern("x:");
 builder.create();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}