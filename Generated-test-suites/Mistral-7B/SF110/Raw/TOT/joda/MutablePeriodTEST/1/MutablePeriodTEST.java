import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.base.Defaults;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParseString() {
 MutablePeriod period = MutablePeriod.parse("P3Y2M4D1W1H1M1S1");
 assertEquals("Three years, two months, four days, one week, one hour, one minute, one second", period.toString());
 }

 @Test
 public void testParseStringWithFormat() {
 MutablePeriod period = MutablePeriod.parse("P3Y2M4D1W1H1M1S1", ISOPeriodFormat.standard());
 assertEquals("Three years, two months, four days, one week, one hour, one minute, one second", period.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStringInvalidFormat() {
 try {
 MutablePeriod.parse("PX3Y2M4D1W1H1M1S1"); // X is an invalid character
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateWithDefaults() {
 MutablePeriod period = new MutablePeriod();
 assertEquals("PT0S", period.toString());
 }

 @Test
 public void testCreateWithPeriodType() {
 MutablePeriod period = new MutablePeriod(PeriodType.dayTime());
 assertEquals("PT0S", period.toString());
 }

 @Test
 public void testCreateWithBasicValues() {
 MutablePeriod period = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1);
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testCreateWithBasicValuesAndPeriodType() {
 MutablePeriod period = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1, PeriodType.dayTime());
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testCreateWithValuesAndPeriodTypeAndChronology() {
 MutablePeriod period = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1, PeriodType.dayTime(), ISOChronology.getInstanceUTC());
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testCreateWithDurations() {
 MutablePeriod period = new MutablePeriod(new Period(3, 2, 4, 1, 1, 1, 1, 1));
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testCreateWithInstantValues() {
 MutablePeriod period = new MutablePeriod(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parseLocalDate("2000-01-01T00:00:00.000Z"), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parseLocalDate("2000-01-02T00:00:00.001Z"), Defaults.UTC);
 assertEquals("P1D1S1MS1", period.toString());
 }

 @Test
 public void testCreateWithReadableInstantValues() {
 MutablePeriod period = new MutablePeriod(Defaults.INSTANT.plus(1L), Defaults.INSTANT.plus(2L), null);
 assertEquals("P1D1S1MS1", period.toString());
 }

 @Test
 public void testCreateWithReadableDuration() {
 MutablePeriod period = new MutablePeriod(Duration.standardMilliseconds(1234567L), null);
 assertEquals("P1.234567DT1.234567S", period.toString());
 }

 @Test
 public void testCreateWithReadableDurationAndChronology() {
 MutablePeriod period = new MutablePeriod(Duration.standardMillis(1234567L), ISOChronology.getInstanceUTC());
 assertEquals("P1.234567DT1.234567S", period.toString());
 }

 @Test
 public void testSetValues() {
 MutablePeriod period = new MutablePeriod();
 period.setValues(new int[] {3, 2, 4, 1, 1, 1, 1, 1});
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod();
 period.setValue(PeriodType.YEAR_INDEX, 3);
 assertEquals("P3Y", period.toString());
 }

 @Test
 public void testSetField() {
 MutablePeriod period = new MutablePeriod();
 period.setField(PeriodType.MONTH_INDEX, 2);
 assertEquals("P2M", period.toString());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period = new MutablePeriod();
 MutablePeriod otherPeriod = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1);
 period.setPeriod(otherPeriod);
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testSetPeriodWithInvalidArgument() {
 try {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetPeriodWithInvalidPeriodType() {
 MutablePeriod period = new MutablePeriod();
 try {
 period.setPeriod(new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1, PeriodType.secondTime()));
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddField() {
 MutablePeriod period = new MutablePeriod();
 period.addField(PeriodType.YEAR_INDEX, 3);
 assertEquals("P3Y", period.toString());
 }

 @Test
 public void testAddPeriod() {
 MutablePeriod period = new MutablePeriod();
 MutablePeriod otherPeriod = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1);
 period.addPeriod(otherPeriod);
 assertEquals("P6Y2M4DT1H1M1S1", period.toString());
 }

 @Test
 public void testAddPeriodWithInvalidPeriodType() {
 MutablePeriod period = new MutablePeriod();
 MutablePeriod otherPeriod = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1, PeriodType.secondTime());
 try {
 period.addPeriod(otherPeriod);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddPeriodWithDifferentPeriodTypes() {
 MutablePeriod period = new MutablePeriod(PeriodType.dayTime());
 MutablePeriod otherPeriod = new MutablePeriod(PeriodType.weeks());
 period.addPeriod(otherPeriod);
 assertEquals("P1W1DT23H59M59.9999999S", period.toString());
 }

 @Test
 public void testAddPeriodWithInvalidArgument() {
 try {
 MutablePeriod period = new MutablePeriod();
 period.addPeriod(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddPeriodWithSelf() {
 MutablePeriod period = new MutablePeriod();
 period.addPeriod(period);
 assertEquals("P2PT0S", period.toString());
 }

 @Test
 public void testMergePeriod() {
 MutablePeriod period1 = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1);
 MutablePeriod period2 = new MutablePeriod(1, 1, 1, 1, 1, 1, 1, 1);
 period1.mergePeriod(period2);
 assertEquals("P4Y2M5DT1H1M1S1", period1.toString());
 }

 @Test
 public void testMergePeriodWithInvalidPeriodType() {
 MutablePeriod period1 = new MutablePeriod(PeriodType.dayTime());
 MutablePeriod period2 = new MutablePeriod(PeriodType.weeks());
 try {
 period1.mergePeriod(period2);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testMergePeriodWithNullArgument() {
 MutablePeriod period = new MutablePeriod();
 try {
 period.mergePeriod(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testClear() {
 MutablePeriod period = new MutablePeriod();
 period.clear();
 assertEquals("PT0S", period.toString());
 }

 @Test
 public void testClone() {
 MutablePeriod period = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1);
 MutablePeriod clonedPeriod = period.copy();
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 assertEquals("P3Y2M4DT1H1M1S1", clonedPeriod.toString());
 }

 @Test
 public void testToString() {
 MutablePeriod period = new MutablePeriod(3, 2, 4, 1, 1, 1, 1, 1);
 assertEquals("P3Y2M4DT1H1M1S1", period.toString());
 }
}