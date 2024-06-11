import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenNormalCase() {
 Options options = new Options();
 String[] args = new String[]{"-a", "--optionB", "valueB", "-c", "valueC"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "-c", "valueC"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithDOption() {
 Options options = new Options();
 String[] args = new String[]{"-DmyProperty=value", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-DmyProperty", "value", "arg1", "arg2"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithDoubleHyphen() {
 Options options = new Options();
 String[] args = new String[]{"--", "-optionD"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--", "--optionD"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 String[] args = new String[]{"-a", "arg1", "-b", "arg2", "nonOption"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"-a", "arg1", "-b", "arg2"}, flattenedArgs);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() {
 Options options = new Options();
 String[] args = new String[]{"-x", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenWithLongOption() {
 Options options = new Options();
 String[] args = new String[]{"--longOptionWithValueLongerThanOneCharacter", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--longOptionWithValueLongerThanOneCharacter"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithMultipleLongOptions() {
 Options options = new Options();
 String[] args = new String[]{"--longOption1", "--longOption2", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--longOption1", "--longOption2"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithLongAndShortOptions() {
 Options options = new Options();
 String[] args = new String[]{"-a", "--longOptionB", "valueB", "-c", "arg1", "--longOptionC", "valueC"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-a", "--longOptionB", "valueB", "-c", "--longOptionC", "valueC"}, flattenedArgs);
 }
}