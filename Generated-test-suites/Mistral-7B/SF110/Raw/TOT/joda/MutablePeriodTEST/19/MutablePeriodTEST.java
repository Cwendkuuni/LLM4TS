import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.base.BasicPeriod;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParseString() {
 MutablePeriod period = MutablePeriod.parse("P1Y2M3DT4H5M6.7S"); // typical use case
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(6, period.getSeconds());
 assertEquals(700, period.getMillis());

 MutablePeriod emptyPeriod = MutablePeriod.parse("P0"); // empty period
 assertEquals(0, emptyPeriod.getYears());
 assertEquals(0, emptyPeriod.getMonths());
 assertEquals(0, emptyPeriod.getWeeks());
 assertEquals(0, emptyPeriod.getDays());
 assertEquals(0, emptyPeriod.getHours());
 assertEquals(0, emptyPeriod.getMinutes());
 assertEquals(0, emptyPeriod.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testParseStringWithInvalidFormat() {
 try {
 MutablePeriod.parse("P1Y2M3DT4H5M6.7SZ"); // invalid character
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseStringWithNull() {
 try {
 MutablePeriod.parse(null); // null string
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseStringWithEmptyString() {
 MutablePeriod emptyPeriod = MutablePeriod.parse(""); // empty string
 assertEquals(0, emptyPeriod.getYears());
 assertEquals(0, emptyPeriod.getMonths());
 assertEquals(0, emptyPeriod.getWeeks());
 assertEquals(0, emptyPeriod.getDays());
 assertEquals(0, emptyPeriod.getHours());
 assertEquals(0, emptyPeriod.getMinutes());
 assertEquals(0, emptyPeriod.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testNew() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8); // typical use case
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());

 MutablePeriod zeroPeriod = new MutablePeriod(); // default constructor
 assertEquals(0, zeroPeriod.getYears());
 assertEquals(0, zeroPeriod.getMonths());
 assertEquals(0, zeroPeriod.getWeeks());
 assertEquals(0, zeroPeriod.getDays());
 assertEquals(0, zeroPeriod.getHours());
 assertEquals(0, zeroPeriod.getMinutes());
 assertEquals(0, zeroPeriod.getSeconds());
 assertEquals(0, zeroPeriod.getMillis());
 }

 @Test
 public void testNewWithNullPeriodType() {
 try {
 new MutablePeriod(null, 1, 2, 3, 4, 5, 6, 7, 8); // null PeriodType
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNewWithInvalidArguments() {
 try {
 new MutablePeriod(-1, 2, 3, 4, 5, 6, 7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, -2, 3, 4, 5, 6, 7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, 2, -3, 4, 5, 6, 7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, 2, 3, -4, 5, 6, 7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, 2, 3, 4, -5, 6, 7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, 2, 3, 4, 5, -6, 7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, 2, 3, 4, 5, 6, -7, 8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new MutablePeriod(1, 2, 3, 4, 5, 6, 7, -8); // negative value
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetValues() {
 MutablePeriod period = new MutablePeriod(); // empty period
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
 period.setValues(values);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod(); // empty period
 period.setValue(0, 1);
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetField() {
 MutablePeriod period = new MutablePeriod(); // empty period
 period.setField(DurationFieldType.years(), 1);
 assertEquals(1, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period = new MutablePeriod(); // empty period
 BasicPeriod otherPeriod = new BasicPeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 period.setPeriod(otherPeriod);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithNull() {
 try {
 MutablePeriod period = new MutablePeriod(); // empty period
 period.setPeriod(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetPeriodWithSameInstance() {
 MutablePeriod period = new MutablePeriod(); // empty period
 MutablePeriod otherPeriod = new MutablePeriod(period);
 period.setPeriod(otherPeriod);
 assertEquals(period, otherPeriod);
 }

 @Test
 public void testSetPeriodWithDifferentInstance() {
 MutablePeriod period = new MutablePeriod(); // empty period
 MutablePeriod otherPeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setPeriod(otherPeriod);
 assertEquals(otherPeriod, period);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithValidArguments() {
 MutablePeriod period = new MutablePeriod(); // empty period
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
 period.setPeriod(values);
 assertEquals(1, period.getYears());
 assertEquals(2, period.getMonths());
 assertEquals(3, period.getWeeks());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(7, period.getSeconds());
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetPeriodWithInvalidArguments() {
 MutablePeriod period = new MutablePeriod(); // empty period
 int[] values = {-1, 2, 3, 4, 5, 6, 7, 8};
 try {
 period.setPeriod(values);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetPeriodWithNullValues() {
 MutablePeriod period = new MutablePeriod(); // empty period
 int[] values = {1, null, 3, 4, 5, 6, 7, 8};
 try {
 period.setPeriod(values);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetPeriodWithTooFewValues() {
 MutablePeriod period = new MutablePeriod(); // empty period
 int[] values = {1};
 try {
 period.setPeriod(values);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetPeriodWithTooManyValues() {
 MutablePeriod period = new MutablePeriod(); // empty period
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
 try {
 period.setPeriod(values);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testClear() {
 MutablePeriod period = new MutablePeriod(); // empty period
 period.clear();
 assertEquals(0, period.getYears());
 assertEquals(0, period.getMonths());
 assertEquals(0, period.getWeeks());
 assertEquals(0, period.getDays());
 assertEquals(0, period.getHours());
 assertEquals(0, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testClone() {
 MutablePeriod period = new MutablePeriod(); // empty period
 MutablePeriod clonedPeriod = period.copy();
 assertEquals(period, clonedPeriod);
 }
}