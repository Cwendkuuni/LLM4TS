import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testCreateWithDefaultOptions() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.create('-d');

 assertEquals("Option created with no description, no argument, not required, no type.", null, option.getDescription());
 assertEquals("Option created with no description, no argument, not required, no type.", null, option.getLongOpt());
 assertEquals("Option created with no description, no argument, not required, no type.", false, option.isRequired());
 assertEquals("Option created with no description, no argument, not required, no type.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no description, no argument, not required, no type.", null, option.getType());
 assertEquals("Option created with no description, no argument, not required, no type.", '\0', option.getValueSeparator());
 assertEquals("Option created with no description, no argument, not required, no type.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithDescription() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withDescription("Description for test option").create('-d');

 assertEquals("Option created with description.", "Description for test option", option.getDescription());
 assertEquals("Option created with no long option.", null, option.getLongOpt());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with no arguments.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithLongOption() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("testLongOpt").create('-d');

 assertEquals("Option created with long option.", "testLongOpt", option.getLongOpt());
 assertEquals("Option created with no description.", null, option.getDescription());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with no arguments.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithArgName() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withArgName("testArgName").create('-d');

 assertEquals("Option created with arg name.", "testArgName", option.getArgName());
 assertEquals("Option created with no description.", null, option.getDescription());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with no arguments.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default long opt.", null, option.getLongOpt());
 }

 @Test
 public void testCreateWithArg() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.hasArg().create('-d');

 assertEquals("Option created with arg.", true, option.isRequired());
 assertEquals("Option created with one argument.", 1, option.getArgs());
 assertEquals("Option created with no description.", null, option.getDescription());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithOptionalArg() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.hasOptionalArg().create('-d');

 assertEquals("Option created with optional arg.", true, option.isOptional());
 assertEquals("Option created with one optional argument.", 1, option.getArgs());
 assertEquals("Option created with no description.", null, option.getDescription());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithValueSeparator() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withValueSeparator(':').create('-d');

 assertEquals("Option created with value separator.", ':', option.getValueSeparator());
 assertEquals("Option created with no description.", null, option.getDescription());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with no arguments.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithType() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withType(String.class).create('-d');

 assertEquals("Option created with type.", String.class, option.getType());
 assertEquals("Option created with no description.", null, option.getDescription());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with no arguments.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithDescriptionAndLongOpt() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withDescription("Description for test option").withLongOpt("testLongOpt").create('-d');

 assertEquals("Option created with description and long opt.", "testLongOpt", option.getLongOpt());
 assertEquals("Option created with description.", "Description for test option", option.getDescription());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with no arguments.", Option.UNINITIALIZED, option.getArgs());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 assertEquals("Option created with default arg name.", "arg", option.getArgName());
 }

 @Test
 public void testCreateWithDescriptionArgNameAndLongOpt() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withDescription("Description for test option").withArgName("testArgName").withLongOpt("testLongOpt").create('-d');

 assertEquals("Option created with description, long opt and arg name.", "testLongOpt", option.getLongOpt());
 assertEquals("Option created with description.", "Description for test option", option.getDescription());
 assertEquals("Option created with arg name.", "testArgName", option.getArgName());
 assertEquals("Option created without arg.", false, option.isRequired());
 assertEquals("Option created with one argument.", 1, option.getArgs());
 assertEquals("Option created with no type.", null, option.getType());
 assertEquals("Option created with no value separator.", '\0', option.getValueSeparator());
 }

 @Test
 public void testCreateWithDescriptionArgNameLongOptAndValueSeparator() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withDescription("Description for test option").withArgName("testArgName").withLongOpt("testLongOpt").withValueSeparator(':').create('-d');

 assertEquals("Option created with description, long opt, arg name and value separator.", "testLongOpt", option.getLongOpt());
 assertEquals("Option created with description.", "Description for test option", option.getDescription());
 assertEquals("Option created with arg name.", "testArgName", option.getArgName());
 assertEquals("Option created with one argument.", 1, option.getArgs());
 assertEquals("Option created with value separator.", ':', option.getValueSeparator());
 assertEquals("Option created with no type.", null, option.getType());
 }

 @Test
 public void testCreateWithDescriptionArgNameLongOptValueSeparatorAndType() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withDescription("Description for test option").withArgName("testArgName").withLongOpt("testLongOpt").withValueSeparator(':').withType(String.class).create('-d');

 assertEquals("Option created with description, long opt, arg name, value separator and type.", "testLongOpt", option.getLongOpt());
 assertEquals("Option created with description.", "Description for test option", option.getDescription());
 assertEquals("Option created with arg name.", "testArgName", option.getArgName());
 assertEquals("Option created with one argument.", 1, option.getArgs());
 assertEquals("Option created with value separator.", ':', option.getValueSeparator());
 assertEquals("Option created with string type.", String.class, option.getType());
 }
}