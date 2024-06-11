import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = {"-a", "--optionA=valueA", "--optionB=valueB", "-c", "--"};

 String[] expected = {"-a", "--optionA", "valueA", "--optionB", "valueB", "-c"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = {"--invalid", "-a", "--optionA", "valueA", "--optionB=valueB", "-c"};

 parser.flatten(options, args, true);
 }

 @Test
 public void testFlattenBursting() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] args = {"-abc", "--optionA", "--longOptionName=value", "-d"};

 String[] expected = {"-a", "--optionA", "--optionA", "longOptionName", "value", "-d"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }
}