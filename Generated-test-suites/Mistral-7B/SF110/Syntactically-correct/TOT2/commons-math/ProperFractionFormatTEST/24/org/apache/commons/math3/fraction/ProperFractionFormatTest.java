import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.FieldPosition;
import org.apache.commons.math3.util.MathUtils;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.DecimalFormat;

public class ProperFractionFormatTest {

   @Test
   public void defaultConstructorTest() {
      ProperFractionFormat pff = new ProperFractionFormat();
      assertNotNull(pff);
   }

   @Test
   public void constructorWithFormatTest() {
      NumberFormat numberFormat = new DecimalFormat();
      ProperFractionFormat pff = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
      assertEquals(numberFormat, pff.getWholeFormat());
   }

   @Test
   public void formatTest() {
      Fraction fraction = new Fraction(2, 3);
      StringBuffer toAppendTo = new StringBuffer();
      ProperFractionFormat pff = new ProperFractionFormat();
      pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
      assertEquals("2 1/3", toAppendTo.toString());
   }

   @Test
   public void negativeFractionTest() {
      Fraction fraction = new Fraction(-1, 2);
      StringBuffer toAppendTo = new StringBuffer();
      ProperFractionFormat pff = new ProperFractionFormat();
      pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
      assertEquals("-1 1/2", toAppendTo.toString());
   }

   @Test
   public void parseTest() {
      String input = "2 1/3";
      ParsePosition pos = new ParsePosition(0);
      Fraction fraction = new ProperFractionFormat().parse(input, pos);
      assertEquals(new Fraction(2, 3), fraction);
      assertEquals(input.length(), pos.getIndex());
   }

   @Test
   public void invalidInputTest() {
      String input = "2 a/b";
      ParsePosition pos = new ParsePosition(0);
      Fraction fraction = new ProperFractionFormat().parse(input, pos);
      assertNull(fraction);
      assertEquals(0, pos.getIndex());
   }

   @Test
   public void setWholeFormatTest() {
      ProperFractionFormat pff = new ProperFractionFormat();
      NumberFormat numberFormat = new DecimalFormat();
      pff.setWholeFormat(numberFormat);
      assertEquals(numberFormat, pff.getWholeFormat());
   }

   @Test
   public void nullSetWholeFormatTest() {
      try {
         ProperFractionFormat pff = new ProperFractionFormat();
         pff.setWholeFormat(null);
         fail("Expected NullArgumentException with null WholeFormat");
      } catch (NullArgumentException e) {
         // expected
      }
   }

   @Test
   public void nullFormatTest() {
      try {
         new ProperFractionFormat(null, new DecimalFormat(), new DecimalFormat());
         fail("Expected IllegalArgumentException with null NumberFormat");
      } catch (IllegalArgumentException e) {
         // expected
      }
   }

   @Test
   public void illegalInputTest() {
      try {
         new ProperFractionFormat("invalidInput", new DecimalFormat(), new DecimalFormat());
         fail("Expected IllegalArgumentException with invalid input");
      } catch (IllegalArgumentException e) {
         // expected
      }
   }
}
