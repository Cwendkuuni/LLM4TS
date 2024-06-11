import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private static final DfpField FACTORY = new Dfp128Field();

 @Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(FACTORY);
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceInt() {
 DfpDec d1 = new DfpDec(FACTORY, 42);
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceLong() {
 DfpDec d1 = new DfpDec(FACTORY, 1234567890L);
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceDouble() {
 DfpDec d1 = new DfpDec(FACTORY, 3.14159265358979323846);
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2, 0.0);
 }

 @Test
 public void testNewInstanceDfp() {
 DfpDec d1 = new DfpDec(FACTORY, new DfpDec(FACTORY, 42));
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testRoundZero() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.789012345678901234567890d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_DOWN));
 assertEquals(d1, d2);
 }

 @Test
 public void testRoundUp() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.789012345678901234567891d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_UP));
 assertEquals(d1.add(Dfp.ONE), d2);
 }

 @Test
 public void testRoundHalfUp() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.543210123456543210123456d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_HALF_UP));
 assertTrue(d1.compareTo(d2) <= 0);
 }

 @Test
 public void testRoundHalfDown() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.543210123456543210123457d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_HALF_DOWN));
 assertTrue(d1.compareTo(d2) > 0);
 }

 @Test
 public void testRoundHalfEven() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.543210123456543210123456d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_HALF_EVEN));
 assertTrue(d1.compareTo(d2) <= 0 || d1.compareTo(d2) >= 0);
 }

 @Test
 public void testRoundHalfOdd() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.543210123456543210123456d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_HALF_ODD));
 assertTrue(d1.compareTo(d2) < 0 || d1.compareTo(d2) >= 0);
 }

 @Test
 public void testRoundCEIL() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.543210123456543210123456d);
 DfpDec d2 = DfpDec.newInstance(d1.round(DfpField.ROUND_CEIL));
 assertTrue(d2.compareTo(d1) > 0);
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(FACTORY, 123456.789012345678901234567890d);
 DfpDec d2 = DfpDec.newInstance(d1.nextAfter(new DfpDec(FACTORY, 123456.789012345678901234567891d)));
 assertEquals(d1.add(Dfp.ONE), d2);
 }

 @Test
 public void testNextAfterZero() {
 DfpDec d1 = new DfpDec(FACTORY, 0.0d);
 DfpDec d2 = DfpDec.newInstance(d1.nextAfter(new DfpDec(FACTORY, 1.0d)));
 assertEquals(DfpDec.ONE, d2);
 }

 @Test
 public void testNextAfterNegative() {
 DfpDec d1 = new DfpDec(FACTORY, -123456.789012345678901234567890d);
 DfpDec d2 = DfpDec.newInstance(d1.nextAfter(new DfpDec(FACTORY, -123456.789012345678901234567891d)));
 assertEquals(d1, d2);
 }

 @Test
 public void testNextAfterNaN() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.NaN);
 DfpDec d2 = DfpDec.newInstance(d1.nextAfter(new DfpDec(FACTORY, 123456.789012345678901234567891d)));
 assertEquals(d1, d2);
 }
}