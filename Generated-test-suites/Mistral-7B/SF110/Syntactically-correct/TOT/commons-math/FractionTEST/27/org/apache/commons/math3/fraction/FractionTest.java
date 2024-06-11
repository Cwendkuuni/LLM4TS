import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

 @Test
 public void testFractionConstructors() {
 Fraction fraction1 = new Fraction(1.0 / 2.0);
 Fraction fraction2 = new Fraction(1.0 / 2.0, 1.0E-5, 100);
 Fraction fraction3 = new Fraction(1.0 / 3.0, 100);
 Fraction fraction4 = new Fraction(1);
 Fraction fraction5 = new Fraction(1, 2);
 Fraction fraction6 = new Fraction(-1, 1);
 Fraction fraction7 = new Fraction(2, 1);
 Fraction fraction8 = new Fraction(0);
 Fraction fraction9 = new Fraction(5, 2);
 Fraction fraction10 = new Fraction(Integer.MIN_VALUE, 1);
 Fraction fraction11 = new Fraction(5, 0);
 Fraction fraction12 = new Fraction("1/2");

 assertEquals(0.5, fraction1.doubleValue(), 0.001);
 assertEquals(0.5, fraction2.doubleValue(), 0.001);
 assertEquals(0.16666666666666663, fraction3.doubleValue(), 0.0000001);
 assertEquals(1.0, fraction4.doubleValue());
 assertEquals(0.5, fraction5.doubleValue(), 0.001);
 assertEquals(-1.0, fraction6.doubleValue(), 0.001);
 assertEquals(2.0, fraction7.doubleValue(), 0.001);
 assertEquals(0.0, fraction8.doubleValue());
 assertEquals(2.5, fraction9.doubleValue(), 0.001);

 try {
 Fraction fraction13 = new Fraction(Double.POSITIVE_INFINITY);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }

 try {
 Fraction fraction14 = new Fraction(Double.NEGATIVE_INFINITY);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }

 try {
 Fraction fraction15 = new Fraction(1.0 / 0.0);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }

 try {
 Fraction fraction16 = new Fraction(Integer.MAX_VALUE, 1);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }

 try {
 Fraction fraction17 = new Fraction(Integer.MIN_VALUE, 1);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }

 try {
 Fraction fraction18 = new Fraction(Integer.MIN_VALUE, 2);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }

 try {
 Fraction fraction19 = new Fraction("5/0");
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // expected
 }
 }

 @Test
 public void testFractionMethods() {
 Fraction fraction1 = new Fraction(1.0 / 2.0);
 Fraction fraction2 = new Fraction(1.0 / 2.0, 1.0E-5, 100);
 Fraction fraction3 = new Fraction(1.0 / 3.0, 100);
 Fraction fraction4 = new Fraction(-1, 1);
 Fraction fraction5 = new Fraction(2, 1);
 Fraction fraction6 = Fraction.ZERO;
 Fraction fraction7 = new Fraction(2, 1).abs();
 Fraction fraction8 = new Fraction(1, 2).negate();
 Fraction fraction9 = new Fraction(1, 2).reciprocal();
 Fraction fraction10 = new Fraction(1, 2).add(new Fraction(1, 3));
 Fraction fraction11 = new Fraction(1, 2).subtract(new Fraction(1, 3));

 assertEquals(0.5, fraction1.doubleValue(), 0.001);
 assertEquals(0.5, fraction2.doubleValue(), 0.001);
 assertEquals(0.16666666666666663, fraction3.doubleValue(), 0.0000001);
 assertEquals(-1.0, fraction4.doubleValue(), 0.001);
 assertEquals(2.0, fraction5.doubleValue(), 0.001);
 assertEquals(0.0, fraction6.doubleValue());
 assertEquals(1.5, fraction7.doubleValue(), 0.001);
 assertEquals(-1.0, fraction8.doubleValue(), 0.001);
 assertEquals(0.5, fraction9.doubleValue(), 0.001);
 assertEquals(1.25, fraction10.doubleValue(), 0.001);
 assertEquals(0.75, fraction11.doubleValue(), 0.001);
 }

 @Test
 public void testFractionComparison() {
 Fraction fraction1 = new Fraction(1.0 / 2.0);
 Fraction fraction2 = new Fraction(1.0 / 2.0);
 Fraction fraction3 = new Fraction(2.0 / 3.0);
 Fraction fraction4 = new Fraction(-1.0 / 2.0);
 Fraction fraction5 = new Fraction(1.0 / 2.0, 1.0E-5, 100);
 Fraction fraction6 = new Fraction(1.0 / 2.0, 1.0E-5, 100).negate();
 Fraction fraction7 = new Fraction(1.0 / 2.0, 1.0E-5, 100);
 Fraction fraction8 = new Fraction(1.0 / 2.0, 1.0E-5, 100).reciprocal();

 assertEquals(0, fraction1.compareTo(fraction2));
 assertEquals(-1, fraction1.compareTo(fraction3));
 assertEquals(1, fraction1.compareTo(fraction4));
 assertEquals(-1, fraction1.compareTo(fraction5));
 assertEquals(1, fraction1.compareTo(fraction6));
 assertEquals(0, fraction1.compareTo(fraction7));
 assertEquals(-1, fraction1.compareTo(fraction8));
 }

 @Test
 public void testFractionArithmetic() {
 Fraction fraction1 = new Fraction(1.0 / 2.0);
 Fraction fraction2 = new Fraction(1.0 / 2.0, 1.0E-5, 100);
 Fraction fraction3 = new Fraction(1.0 / 3.0, 100);
 Fraction fraction4 = new Fraction(2, 1);
 Fraction fraction5 = Fraction.ZERO;
 Fraction fraction6 = new Fraction(-1, 1);
 Fraction fraction7 = new Fraction(1, 2);
 Fraction fraction8 = new Fraction(1, 2);
 Fraction fraction9 = new Fraction(2, 1);
 Fraction fraction10 = new Fraction(1, 2).add(new Fraction(1, 3));
 Fraction fraction11 = new Fraction(1, 2).subtract(new Fraction(1, 3));
 Fraction fraction12 = new Fraction(1, 2).multiply(new Fraction(1, 2));
 Fraction fraction13 = new Fraction(1, 2).multiply(new Fraction(1, 2).negate());
 Fraction fraction14 = new Fraction(1, 2).divide(new Fraction(1, 2));
 Fraction fraction15 = new Fraction(1, 2).divide(new Fraction(1, 2).negate());
 Fraction fraction16 = new Fraction(1, 2).add(new Fraction(1, 2));
 Fraction fraction17 = new Fraction(1, 2).subtract(new Fraction(1, 2));

 assertEquals(1.0, fraction10.doubleValue(), 0.001);
 assertEquals(-1.0, fraction11.doubleValue(), 0.001);
 assertEquals(0.25, fraction12.doubleValue(), 0.001);
 assertEquals(-1.0, fraction13.doubleValue(), 0.001);
 assertEquals(1.0, fraction14.doubleValue(), 0.001);
 assertEquals(0.0, fraction15.doubleValue(), 0.001);
 assertEquals(0.0, fraction16.doubleValue(), 0.001);
 assertEquals(0.0, fraction17.doubleValue(), 0.001);
 }
}