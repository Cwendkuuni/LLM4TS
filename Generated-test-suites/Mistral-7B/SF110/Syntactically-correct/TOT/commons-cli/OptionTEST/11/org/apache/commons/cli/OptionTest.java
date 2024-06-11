import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreationWithNoArgsAndNoDescription() {
 Option option = new Option("a");
 assertEquals("a", option.getOpt());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertNull(option.getType());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithOneArgAndDescription() {
 Option option = new Option("b", "B", true, "Description for option B");
 assertEquals("b", option.getOpt());
 assertEquals("B", option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.hasArg());
 assertEquals("Description for option B", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertNull(option.getType());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithOneArgAndLongOptAndDescription() {
 Option option = new Option("c", "mylongopt", true, "Description for option C");
 assertEquals("c", option.getOpt());
 assertEquals("mylongopt", option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertTrue(option.hasArg());
 assertEquals("Description for option C", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertNull(option.getType());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithNoArgsAndDescriptionAndHasArgSetFalse() {
 Option option = new Option("d", "Description for option D");
 assertEquals("d", option.getOpt());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArg());
 assertEquals("Description for option D", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertNull(option.getType());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCreationWithInvalidArgName() {
 Option option = new Option("e", null, false, "Description for option E");
 }

 @Test
 public void testOptionIdGetters() {
 Option option = new Option("f", null, false, "Description for option F");
 assertEquals('f', option.getId());
 }

 @Test
 public void testOptionGetterAndSetterMethods() {
 Option option = new Option("g", null, false, "Description for option G");
 option.setLongOpt("longoptforoptiong");
 assertEquals("longoptforoptiong", option.getLongOpt());
 option.setDescription("New description for option G");
 assertEquals("New description for option G", option.getDescription());
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 option.setOptionalArg(false);
 assertFalse(option.hasOptionalArg());
 option.setArgName("argnameforoptiong");
 assertEquals("argnameforoptiong", option.getArgName());
 option.setType(Integer.class);
 assertEquals(Integer.class, option.getType());
 option.setValues(Arrays.asList("value1", "value2"));
 assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
 option.setValueSeparator(';');
 assertTrue(option.hasValueSeparator());
 option.setValueSeparator('\t');
 assertFalse(option.hasValueSeparator());
 }
}