import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DfpDecTest {

 private DfpField field = new Dfp128Field();

 @Test
 public void testDfpDecConstruction() {
 DfpDec d1 = new DfpDec(field, 12345);
 DfpDec d2 = new DfpDec(field, 67890.12345);
 DfpDec d3 = new DfpDec(field, new DfpDec(field, 12345));
 DfpDec d4 = new DfpDec(field, new String("12345.6789"));
 DfpDec d5 = new DfpDec(field, new DfpDec(field, 12345), (byte) 1);
 DfpDec d6 = new DfpDec(field, new DfpDec(field, 12345), (byte) 0);

 assertEquals(12345, d1.getDoubleValue(), 0.0001);
 assertEquals(67890.12345, d2.getDoubleValue(), 0.00001);
 assertEquals(12345, d3.getDoubleValue(), 0.0001);
 assertEquals(12345.6789, d4.getDoubleValue(), 0.00001);
 assertEquals(12345, d5.getDoubleValue(), 0.0001);
 assertEquals(-12345, d6.getDoubleValue(), 0.0001);
 }

 @Test
 public void testDfpDecNewInstance() {
 DfpDec d1 = new DfpDec(field, 12345);
 DfpDec d2 = d1.newInstance();

 assertEquals(12345, d1.getDoubleValue(), 0.0001);
 assertEquals(12345, d2.getDoubleValue(), 0.0001);
 }

 @Test
 public void testDfpDecRound() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 int roundingMode = DfpField.ROUND_HALF_UP;
 int precision = 2;

 DfpDec result = new DfpDec(field, d1.round(roundingMode, precision));

 assertEquals(12345.68, result.getDoubleValue(), 0.00001);
 }

 @Test
 public void testDfpDecNextAfter() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec x = new DfpDec(field, 12345.67899);

 DfpDec result = new DfpDec(field, d1.nextAfter(x));

 assertEquals(12345.679, result.getDoubleValue(), 0.00001);
 }

 @Test
 public void testDfpDecComparison() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = new DfpDec(field, 12345.6789);
 DfpDec d3 = new DfpDec(field, 12345.6788);
 DfpDec d4 = new DfpDec(field, 12345.679);
 DfpDec d5 = new DfpDec(field, 12345.6790);
 DfpDec d6 = new DfpDec(field, new DfpDec(field, 12345.6789).negate());

 assertTrue(d1.equals(d2));
 assertFalse(d1.equals(d3));
 assertFalse(d1.equals(d4));
 assertFalse(d1.equals(d5));
 assertFalse(d1.equals(d6));

 assertFalse(d2.lessThan(d1));
 assertFalse(d1.lessThan(d2));
 assertTrue(d1.lessThan(d3));
 assertTrue(d3.lessThan(d1));
 assertTrue(d1.lessThan(d4));
 assertTrue(d4.lessThan(d1));
 assertTrue(d1.lessThan(d5));
 assertTrue(d5.lessThan(d1));

 assertFalse(d1.greaterThan(d2));
 assertFalse(d2.greaterThan(d1));
 assertTrue(d1.greaterThan(d3));
 assertTrue(d3.lessThan(d1));
 assertFalse(d1.greaterThan(d4));
 assertFalse(d4.greaterThan(d1));
 assertFalse(d1.greaterThan(d5));
 assertFalse(d5.greaterThan(d1));

 assertTrue(d1.equalTo(d1));
 assertTrue(d1.equalTo(d2));
 assertFalse(d1.equalTo(d3));
 assertFalse(d1.equalTo(d4));
 assertFalse(d1.equalTo(d5));
 assertFalse(d1.equalTo(d6));

 assertFalse(d1.isNaN());
 assertFalse(d2.isNaN());
 assertFalse(d3.isNaN());
 assertFalse(d4.isNaN());
 assertFalse(d5.isNaN());
 assertFalse(d6.isNaN());
 }
}