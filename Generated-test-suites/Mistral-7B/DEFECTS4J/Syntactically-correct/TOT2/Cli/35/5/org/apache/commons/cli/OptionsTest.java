import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testOptions_BasicUsage() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--output,test.txt");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testOptions_ShortOptionWithValue() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--output,test.txt");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testOptions_LongOptionWithValue() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 options.addOption("outputfile", "of", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--outputfile,test.txt");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertEquals("test.txt", cmd.getOptionValue("outputfile"));
 }

 @Test
 public void testOptions_MissingOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--nonexistentoption");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing did not fail.");
 }

 assertTrue(cmd == null);
 }

 @Test
 public void testOptions_MixedShortAndLongOptions() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 options.addOption("outputfile", "of", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--o,test.txt --outputfile,test.txt");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertEquals("test.txt", cmd.getOptionValue("outputfile"));
 }

 @Test
 public void testOptions_MissingValueForShortOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--o");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertNull(cmd.getOptionValue("output"));
 }

 @Test
 public void testOptions_MissingValueForLongOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 options.addOption("outputfile", "of", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--outputfile");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertNull(cmd.getOptionValue("outputfile"));
 }

 @Test(expected = ParseException.class)
 public void testOptions_IllegalOption() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--illegaloption");
 } catch (ParseException e) {
 // Expected
 }

 assertTrue(cmd == null);
 }

 @Test
 public void testOptions_MultipleOptionsWithSameName() {
 Options options = new Options();
 options.addOption("o", "output", true, "Output file path");
 options.addOption("o", "overwrite", false, "Overwrite flag");
 options.addOption("outputfile", "of", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--o --o --outputfile,test.txt");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertEquals("test.txt", cmd.getOptionValue("outputfile"));
 assertFalse(cmd.hasOption("output")); // output flag is false
 assertTrue(cmd.hasOption("outputfile"));
 assertTrue(cmd.hasOption("o")); // output flag is present but has different meaning
 }

 @Test
 public void testOptions_OptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "group");
 options.addOptionGroup(group);
 group.addOption("g", "groupOption", false, "Group option");
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--g --output,test.txt");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertTrue(cmd.hasOption("groupOption"));
 assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testOptions_NestedOptionGroup() {
 Options options = new Options();
 OptionGroup group1 = new OptionGroup("g1", "group1");
 OptionGroup group2 = new OptionGroup("g2", "group2");
 group1.addOptionGroup(group2);
 options.addOptionGroup(group1);
 group2.addOption("g2", "group2Option", false, "Group option");
 options.addOption("o", "output", true, "Output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--g1 --g2");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertTrue(cmd.hasOption("group2Option"));
 }

 @Test
 public void testOptions_OptionWithValueRange() {
 Options options = new Options();
 options.addOption("n", "number", true, "Number of threads to use (range: 1-10)");
 options.addOption("n", "number", true, "Number of threads to use (range: 1-10)");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--number,5 --number,11");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertNotNull(cmd);
 assertTrue(cmd.hasOption("number"));
 assertEquals("5", cmd.getOptionValue("number"));
 }

 @Test(expected = ParseException.class)
 public void testOptions_InvalidValueRange() {
 Options options = new Options();
 options.addOption("n", "number", true, "Number of threads to use (range: 1-10)");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--number,11");
 } catch (ParseException e) {
 // Expected
 }

 assertTrue(cmd == null);
 }

 @Test
 public void testOptions_OptionWithMultiValue() {
 Options options = new Options();
 options.addOption("c", "classpath", true, "Classpath elements (multiple allowed)");
 options.addOption("c", "classpath", true, "Classpath elements (multiple allowed)");

 CommandLine cmd = null;
 try {
 cmd = options.parse("--classpath,a.jar --classpath,b.jar --classpath,c.jar");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed.");
 }

 assertNotNull(cmd);
 assertTrue(cmd.hasOption("classpath"));
 List<String> values = cmd.getOptionValues("classpath");
 assertEquals(3, values.size());
 assertEquals("a.jar", values.get(0));
 assertEquals("b.jar", values.get(1));
 assertEquals("c.jar", values.get(2));
 }
}