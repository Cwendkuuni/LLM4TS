import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(
 new Option.Builder()
 .longOpt("optionA")
 .valueSeparator('=')
 .hasArg(true)
 .build(),
 new ArrayList()
 );

 // Test adding options
 Option optionA = new Option.Builder()
 .longOpt("optionA")
 .valueSeparator('=')
 .hasArg(true)
 .build();
 writeableCommandLine.addOption(optionA);
 assertTrue(writeableCommandLine.hasOption(optionA));

 // Test adding values
 writeableCommandLine.addValue(optionA, "value1");
 assertEquals("value1", writeableCommandLine.getValues(optionA, null).get(0));

 // Test adding switch
 writeableCommandLine.addSwitch(optionA, true);
 assertTrue(writeableCommandLine.getSwitch(optionA, false));

 // Test getting properties
 writeableCommandLine.addProperty("property1", "value1");
 assertEquals("value1", writeableCommandLine.getProperty("property1", ""));

 // Test getting options
 assertEquals(1, writeableCommandLine.getOptions().size());

 // Test looksLikeOption
 assertTrue(writeableCommandLine.looksLikeOption("--optionA"));
 assertFalse(writeableCommandLine.looksLikeOption("--noneOption"));

 // Test toString
 assertEquals("--optionA=value1", writeableCommandLine.toString());
 }
}