import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return new Dfp128Field();
 }

 @Test
 public void testDfpDecConstruction() {
 DfpDec dfpDec1 = new DfpDec(getField());
 DfpDec dfpDec2 = new DfpDec(getField(), 123);
 DfpDec dfpDec3 = new DfpDec(getField(), 12345L);
 DfpDec dfpDec4 = new DfpDec(getField(), 123456789.0);
 DfpDec dfpDec5 = new DfpDec(getField(), new DfpDec(getField(), 98));
 DfpDec dfpDec6 = new DfpDec(getField(), "123.45");
 DfpDec dfpDec7 = new DfpDec(getField(), (byte) 1, (byte) 3);

 Assert.assertEquals("Test constructor with no arguments", dfpDec1.toString(), "0.0");
 Assert.assertEquals("Test constructor with byte argument", dfpDec2.toString(), "123.0");
 Assert.assertEquals("Test constructor with int argument", dfpDec3.toString(), "12345.0");
 Assert.assertEquals("Test constructor with long argument", dfpDec4.toString(), "123456789.0");
 Assert.assertEquals("Test constructor with DfpDec argument", dfpDec5.toString(), "98.0");
 Assert.assertEquals("Test constructor with String argument", dfpDec6.toString(), "123.45");
 Assert.assertEquals("Test constructor with byte sign and nan argument", dfpDec7.toString(), "NaN");
 }

 @Test
 public void testDfpDecNewInstance() {
 DfpDec dfpDec = new DfpDec(getField());
 DfpDec newInstance = dfpDec.newInstance();

 Assert.assertEquals("Test newInstance method with no arguments", newInstance.toString(), "0.0");
 }

 @Test
 public void testDfpDecNewInstanceWithArguments() {
 DfpDec dfpDec = new DfpDec(getField());
 DfpDec newInstanceByte = dfpDec.newInstance((byte) 1);
 DfpDec newInstanceInt = dfpDec.newInstance(123);
 DfpDec newInstanceLong = dfpDec.newInstance(12345L);
 DfpDec newInstanceDouble = dfpDec.newInstance(123456789.0);
 DfpDec newInstanceDfp = dfpDec.newInstance(new DfpDec(getField(), 98));
 DfpDec newInstanceString = dfpDec.newInstance("123.45");
 DfpDec newInstanceByteSign = dfpDec.newInstance((byte) 1, (byte) 3);

 Assert.assertEquals("Test newInstance method with byte argument", newInstanceByte.toString(), "0.1");
 Assert.assertEquals("Test newInstance method with int argument", newInstanceInt.toString(), "123.0");
 Assert.assertEquals("Test newInstance method with long argument", newInstanceLong.toString(), "12345.0");
 Assert.assertEquals("Test newInstance method with double argument", newInstanceDouble.toString(), "123456789.0");
 Assert.assertEquals("Test newInstance method with DfpDec argument", newInstanceDfp.toString(), "98.0");
 Assert.assertEquals("Test newInstance method with String argument", newInstanceString.toString(), "123.45");
 Assert.assertEquals("Test newInstance method with byte sign and nan argument", newInstanceByteSign.toString(), "NaN");
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(getField());
 int decimalDigits = dfpDec.getDecimalDigits();

 Assert.assertEquals("Test getDecimalDigits method", decimalDigits, 48);
 }

 @Test
 public void testRound() {
 DfpDec dfpDec = new DfpDec(getField());
 DfpDec roundResult;

 // Test rounding down
 roundResult = dfpDec.round(0);
 Assert.assertEquals("Test rounding down", roundResult.toString(), "0.0");

 // Test rounding up
 roundResult = dfpDec.round(1);
 Assert.assertEquals("Test rounding up", roundResult.toString(), "0.0");
 roundResult = dfpDec.round(4);
 Assert.assertEquals("Test rounding up", roundResult.toString(), "0.0001");

 // Test rounding half up
 roundResult = dfpDec.round(2);
 Assert.assertEquals("Test rounding half up", roundResult.toString(), "0.0");
 roundResult = dfpDec.round(5);
 Assert.assertEquals("Test rounding half up", roundResult.toString(), "0.0001");
 roundResult = dfpDec.round(6);
 Assert.assertEquals("Test rounding half up", roundResult.toString(), "0.0002");

 // Test rounding half down
 roundResult = dfpDec.round(3);
 Assert.assertEquals("Test rounding half down", roundResult.toString(), "0.0");
 roundResult = dfpDec.round(5);
 Assert.assertEquals("Test rounding half down", roundResult.toString(), "0.0001");
 roundResult = dfpDec.round(7);
 Assert.assertEquals("Test rounding half down", roundResult.toString(), "0.0002");

 // Test rounding half even
 roundResult = dfpDec.round(3);
 roundResult = dfpDec.round(5);
 Assert.assertEquals("Test rounding half even", roundResult.toString(), "0.0001");
 roundResult = dfpDec.round(6);
 Assert.assertEquals("Test rounding half even", roundResult.toString(), "0.0002");
 roundResult = dfpDec.round(5.5);
 Assert.assertEquals("Test rounding half even", roundResult.toString(), "0.0002");

 // Test rounding half odd
 roundResult = dfpDec.round(3);
 roundResult = dfpDec.round(5);
 Assert.assertEquals("Test rounding half odd", roundResult.toString(), "0.0001");
 roundResult = dfpDec.round(6);
 Assert.assertEquals("Test rounding half odd", roundResult.toString(), "0.0002");
 roundResult = dfpDec.round(5.5);
 Assert.assertEquals("Test rounding half odd", roundResult.toString(), "0.0002");
 roundResult = dfpDec.round(5.5, 1);
 Assert.assertEquals("Test rounding half odd with precision", roundResult.toString(), "0.001");
 }

 @Test
 public void testNextAfter() {
 DfpDec dfpDec1 = new DfpDec(getField());
 DfpDec dfpDec2 = new DfpDec(getField()).setValue(1.23456789);
 DfpDec dfpDec3 = new DfpDec(getField()).setExp(-32767).setMant(new long[] { 1 });
 DfpDec dfpDec4 = new DfpDec(getField()).setExp(32768).setMant(new long[] { 1 });
 DfpDec dfpDec5 = new DfpDec(getField()).setExp(-32767).setMant(new long[] { 0, 0 });
 DfpDec nextAfterResult1;
 DfpDec nextAfterResult2;
 DfpDec nextAfterResult3;
 DfpDec nextAfterResult4;
 DfpDec nextAfterResult5;

 // Test nextAfter method with dfpDec1 < dfpDec2
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < dfpDec2", nextAfterResult1.toString(), "1.23456789");

 // Test nextAfter method with dfpDec1 == dfpDec2
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 nextAfterResult2 = dfpDec2.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 == dfpDec2", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 < 0 and dfpDec2 > 0
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < 0 and dfpDec2 > 0", nextAfterResult1.toString(), "1.23456789");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 nextAfterResult2 = dfpDec2.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < 0 and dfpDec2 > 0", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 = 0 and dfpDec2 > 0
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = 0 and dfpDec2 > 0", nextAfterResult1.toString(), "1.23456789");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 nextAfterResult2 = dfpDec2.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = 0 and dfpDec2 > 0", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 < 0 and dfpDec2 < 0
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < 0 and dfpDec2 < 0", nextAfterResult1.toString(), "1.23456789");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 nextAfterResult2 = dfpDec2.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < 0 and dfpDec2 < 0", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 = -Inf and dfpDec2 > 0
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = -Inf and dfpDec2 > 0", nextAfterResult1.toString(), "1.23456789");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 nextAfterResult2 = dfpDec2.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = -Inf and dfpDec2 > 0", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 = Inf and dfpDec2 > 0
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = Inf and dfpDec2 > 0", nextAfterResult1.toString(), "Inf");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec2);
 nextAfterResult2 = dfpDec2.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = Inf and dfpDec2 > 0", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 < 0 and dfpDec2 = NaN
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec3);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < 0 and dfpDec2 = NaN", nextAfterResult1.toString(), "NaN");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec3);
 nextAfterResult2 = dfpDec3.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 < 0 and dfpDec2 = NaN", nextAfterResult1.toString(), nextAfterResult2.toString());

 // Test nextAfter method with dfpDec1 = 0 and dfpDec2 = NaN
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec3);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = 0 and dfpDec2 = NaN", nextAfterResult1.toString(), "NaN");
 nextAfterResult1 = dfpDec1.nextAfter(dfpDec3);
 nextAfterResult2 = dfpDec3.nextAfter(dfpDec1);
 Assert.assertEquals("Test nextAfter method with dfpDec1 = 0 and dfpDec2 = NaN", nextAfterResult1.toString(), nextAfterResult2.toString());
 }

}