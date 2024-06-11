import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private static final DfpField FIELD = new Dfp128Field();

 @Test
 public void testConstructor() {
 DfpDec dfpDec1 = new DfpDec(FIELD);
 DfpDec dfpDec2 = new DfpDec(FIELD, 0x01);
 DfpDec dfpDec3 = new DfpDec(FIELD, 0x3C4C3F4227145641L);
 DfpDec dfpDec4 = new DfpDec(FIELD, 0x1.0p-100);
 DfpDec dfpDec5 = new DfpDec(FIELD, new DfpDec(FIELD, 0x1.5p6));

 assertEquals(0, dfpDec1.getDecimalDigits());
 assertEquals(1, dfpDec2.signum());
 assertEquals(0x01, dfpDec2.getDecimalDigits());
 assertEquals(0x3C4C3F4227145641L, dfpDec3.doubleValueExact());
 assertEquals(-100, dfpDec4.getExponent());
 assertEquals(6, dfpDec5.getExponent());
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(FIELD, 0x1.5p6);
 DfpDec newInstance = dfpDec.newInstance();
 DfpDec newInstance2 = dfpDec.newInstance(0x01);

 assertEquals(dfpDec, newInstance);
 assertEquals(0x01, newInstance2.signum());
 }

 @Test
 public void testRound() {
 DfpDec dfpDec = new DfpDec(FIELD, 0x1.5p6);
 DfpDec expected = new DfpDec(FIELD, 0x1.6p6);

 assertEquals(0, dfpDec.round(0));
 assertEquals(1, dfpDec.round(1));
 assertEquals(1, dfpDec.round(15));
 assertEquals(15, dfpDec.round(16));
 assertEquals(15, dfpDec.round(-1));
 assertEquals(14, dfpDec.round(-2));
 assertEquals(14, dfpDec.round(-15));
 assertEquals(1, dfpDec.round(-16));

 DfpDec result = new DfpDec(dfpDec);
 assertEquals(expected, result.round(1));
 }

 @Test
 public void testNextAfter() {
 DfpDec dfpDec1 = new DfpDec(FIELD, 0x1.5p6);
 DfpDec dfpDec2 = new DfpDec(FIELD, 0x1.6p6);
 DfpDec dfpDec3 = new DfpDec(FIELD, 0x1.4p-23);
 DfpDec dfpDec4 = new DfpDec(FIELD, 0x1.0p-308);
 DfpDec dfpDec5 = new DfpDec(FIELD, 0x1.0p-308);

 assertEquals(dfpDec2, dfpDec1.nextAfter(dfpDec2));
 assertEquals(dfpDec1, dfpDec1.nextAfter(dfpDec1));
 assertEquals(dfpDec3, dfpDec3.nextAfter(dfpDec4));
 assertEquals(dfpDec5, dfpDec5.nextAfter(dfpDec5));
 }
}