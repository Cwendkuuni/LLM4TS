import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private Fraction expectedResult;

 @Test
 public void testProperFractionFormat() {
 // Test default constructor
 properFractionFormat = new ProperFractionFormat();

 // Test valid input with no whole part
 Fraction fraction1 = new Fraction(2, 3);
 String actualResult = properFractionFormat.format(fraction1, new StringBuffer(), null).toString();
 expectedResult = new Fraction(2, 3);
 assertTrue(expectedResult.equals(new Fraction(actualResult.split(" ")[1].split("/")[0], Integer.parseInt(actualResult.split(" ")[1].split("/")[1]))));

 // Test valid input with whole part
 Fraction fraction2 = new Fraction(4, 3);
 properFractionFormat = new ProperFractionFormat(new DecimalFormat("#")); // Setting wholeFormat to DecimalFormat for testing
 String actualResult2 = properFractionFormat.format(fraction2, new StringBuffer(), null).toString();
 expectedResult = new Fraction(1, 3, 1, 1).add(new Fraction(4, 3));
 assertTrue(expectedResult.equals(new Fraction(actualResult.split(" ")[0].split(" ")[0].equals("") ? 1 : Integer.parseInt(actualResult.split(" ")[0].split(" ")[0]), actualResult.split(" ")[1].split("/")[0], actualResult.split(" ")[1].split("/")[1])));

 // Test constructor with custom number formats
 DecimalFormat numeratorFormat = new DecimalFormat("#");
 DecimalFormat denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat customFormat = new ProperFractionFormat(new DecimalFormat("#"), numeratorFormat, denominatorFormat);
 Fraction fraction3 = new Fraction(5, 7);
 String actualResult3 = customFormat.format(fraction3, new StringBuffer(), null).toString();
 expectedResult = new Fraction(5, 7);
 assertTrue(expectedResult.equals(new Fraction(actualResult.split(" ")[1].split("/")[0], Integer.parseInt(actualResult.split(" ")[1].split("/")[1]))));
 }

 @Test
 public void testParse() {
 // Test valid input with no whole part
 String input1 = "2/3";
 Fraction parsedFraction1 = properFractionFormat.parse(input1, new ParsePosition(0));
 assertEquals(new Fraction(2, 3), parsedFraction1);

 // Test valid input with whole part
 String input2 = "1 2/3";
 Fraction parsedFraction2 = properFractionFormat.parse(input2, new ParsePosition(0));
 assertEquals(new Fraction(1, 3, 1, 1).add(new Fraction(2, 3)), parsedFraction2);

 // Test invalid input
 String input3 = "abc/def";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction3 = properFractionFormat.parse(input3, pos);
 assertNull(parsedFraction3);
 assertEquals(input3.length(), pos.getIndex());
 }
}