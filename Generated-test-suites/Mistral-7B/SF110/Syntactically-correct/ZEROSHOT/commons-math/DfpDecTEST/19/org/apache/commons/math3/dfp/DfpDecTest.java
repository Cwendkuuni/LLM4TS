import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private DfpField field = new Dfp128Field();

 @Test
 public void testDfpDecConstruction() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = new DfpDec(field, "123.45E6");
 DfpDec d3 = new DfpDec(field, new DfpDec(field, 456));
 DfpDec d4 = new DfpDec(field, new DfpDec(field, 1));
 DfpDec d5 = new DfpDec(field, new DfpDec(field, -1));
 DfpDec d6 = new DfpDec(field, new DfpDec(field, 0));
 DfpDec d7 = new DfpDec(field, new DfpDec(field, Double.NaN));

 assertEquals(12345.6789, d1.doubleValueExact(), 0.0001);
 assertEquals(12345678.9, d2.doubleValueExact(), 0.0001);
 assertEquals(456, d3.doubleValueExact(), 0.0001);
 assertEquals(1, d4.doubleValueExact(), 0.0001);
 assertEquals(-1, d5.doubleValueExact(), 0.0001);
 assertEquals(0, d6.doubleValueExact(), 0.0001);
 assertEquals(Double.NaN, d7.doubleValueExact());
 }

 @Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = d1.newInstance();
 assertEquals(12345.6789, d1.doubleValueExact(), 0.0001);
 assertEquals(12345.6789, d2.doubleValueExact(), 0.0001);

 DfpDec d3 = new DfpDec(field, new DfpDec(field, 456));
 DfpDec d4 = d3.newInstance();
 assertEquals(456, d3.doubleValueExact(), 0.0001);
 assertEquals(456, d4.doubleValueExact(), 0.0001);
 }

 @Test
 public void testRounding() {
 DfpDec d1 = new DfpDec(field, 123.5);
 DfpDec d2 = d1.newInstance(-1);
 DfpDec d3 = new DfpDec(field, 123.6);
 DfpDec d4 = new DfpDec(field, 124.5);
 DfpDec d5 = new DfpDec(field, 123.5);
 DfpDec d6 = new DfpDec(field, 123.5);
 DfpDec d7 = new DfpDec(field, 123.5);
 DfpDec d8 = new DfpDec(field, 123.5);
 DfpDec d9 = new DfpDec(field, 123.5);
 DfpDec d10 = new DfpDec(field, 123.5);
 DfpDec d11 = new DfpDec(field, 123.5);
 DfpDec d12 = new DfpDec(field, 123.5);

 d5.round(Dfp.ROUND_DOWN);
 d6.round(Dfp.ROUND_UP);
 d7.round(Dfp.ROUND_HALF_UP);
 d8.round(Dfp.ROUND_HALF_DOWN);
 d9.round(Dfp.ROUND_HALF_EVEN);
 d10.round(Dfp.ROUND_HALF_ODD);
 d11.round(Dfp.ROUND_CEIL);
 d12.round(Dfp.ROUND_FLOOR);

 assertEquals(123.5, d1.doubleValueExact(), 0.0001);
 assertEquals(123.0, d2.doubleValueExact(), 0.0001);
 assertEquals(124.0, d3.doubleValueExact(), 0.0001);
 assertEquals(123.5, d5.doubleValueExact(), 0.0001);
 assertEquals(124.0, d6.doubleValueExact(), 0.0001);
 assertEquals(123.5, d7.doubleValueExact(), 0.0001);
 assertEquals(124.0, d8.doubleValueExact(), 0.0001);
 assertEquals(124.0, d9.doubleValueExact(), 0.0001);
 assertEquals(123.0, d10.doubleValueExact(), 0.0001);
 assertEquals(123.0, d11.doubleValueExact(), 0.0001);
 assertEquals(123.0, d12.doubleValueExact(), 0.0001);
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(field, 123.5);
 DfpDec d2 = new DfpDec(field, 123.6);
 DfpDec d3 = new DfpDec(field, -123.5);
 DfpDec d4 = new DfpDec(field, -123.6);
 DfpDec d5 = new DfpDec(field, 0.5);
 DfpDec d6 = new DfpDec(field, -0.5);
 DfpDec d7 = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec d8 = new DfpDec(field, Double.NEGATIVE_INFINITY);
 DfpDec d9 = new DfpDec(field, Double.NaN);

 assertEquals(123.5, d1.doubleValueExact(), 0.0001);
 assertEquals(124.0, d2.nextAfter(d1).doubleValueExact(), 0.0001);
 assertEquals(-123.5, d3.doubleValueExact(), 0.0001);
 assertEquals(-123.4, d4.nextAfter(d3).doubleValueExact(), 0.0001);
 assertEquals(0.6, d5.nextAfter(d5).doubleValueExact(), 0.0001);
 assertEquals(-0.6, d6.nextAfter(d6).doubleValueExact(), 0.0001);
 assertEquals(Double.POSITIVE_INFINITY, d7.nextAfter(d7).doubleValueExact());
 assertEquals(Double.NEGATIVE_INFINITY, d8.nextAfter(d8).doubleValueExact());
 assertEquals(Double.NaN, d9.nextAfter(d9).doubleValueExact());
 }
}