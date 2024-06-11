import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.*;

public class BasePeriodTest {

 @BeforeClass
 public static void setup() {
 // Initialize any static variables or objects here
 BasePeriod.DUMMY_PERIOD = new AbstractPeriod() {
 @Override
 public int getValue(int field) {
 return 0;
 }

 @Override
 public PeriodType getPeriodType() {
 return PeriodType.time();
 }
 };
 }

 @Test
 public void testEmptyConstructor() {
 BasePeriod period = new BasePeriod();
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[8], period.iValues);
 }

 @Test
 public void testConstructorWithValues() {
 BasePeriod period = new BasePeriod(PeriodType.standard(), new int[]{1, 2, 3, 4, 5, 6, 7, 8});
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
 }

 @Test
 public void testConstructorWithValidDates() {
 DateTime date1 = new DateTime(1432345678000L);
 DateTime date2 = new DateTime(1432345688000L);
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 1, 0}, period.iValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullDates() {
 new BasePeriod(null, null, PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidDates() {
 DateTime date1 = new DateTime(1432345678000L);
 DateTime date2 = new DateTime(1432345677000L); // Past date
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 1, -1}, period.iValues);
 }

 @Test
 public void testConstructorWithValidReadableDuration() {
 Duration duration = Duration.standardDays(3);
 BasePeriod period = new BasePeriod(duration, new DateTime(), PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 0, 3, 0, 0, 0, 0, 0}, period.iValues);
 }

 @Test
 public void testConstructorWithNullReadableDuration() {
 try {
 new BasePeriod(null, new DateTime(), PeriodType.standard());
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConstructorWithInvalidReadableDuration() {
 Duration duration = Duration.standardDays(-3);
 BasePeriod period = new BasePeriod(duration, new DateTime(), PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 0, -3, 0, 0, 0, 0, 0}, period.iValues);
 }

 @Test
 public void testConstructorWithReadablePartial() {
 BaseLocalDate date1 = new LocalDate(2015, 1, 1);
 BaseLocalDate date2 = new LocalDate(2015, 1, 3);
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 0, 2, 0, 0, 0, 0, 0}, period.iValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidReadablePartial() {
 BaseLocalDate date1 = new LocalDate(2015, 1, 1);
 BaseLocalDate date2 = new LocalDate(2015, 1, 0); // Invalid date
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 }

 @Test
 public void testConstructorWithValidReadableInstant() {
 DateTime date1 = new DateTime(1432345678000L);
 DateTime date2 = new DateTime(1432345688000L);
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 1, 0}, period.iValues);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidReadableInstant() {
 DateTime date1 = new DateTime(1432345678000L);
 DateTime date2 = new DateTime(1432345677000L); // Past date
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 1, -1}, period.iValues);
 }

 @Test
 public void testSetPeriodInternal() {
 BasePeriod period = new BasePeriod();
 period.setPeriodInternal(Period.standardDays(3));
 Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
 Assert.assertArrayEquals(new int[]{0, 0, 3, 0, 0, 0, 0, 0}, period.iValues);
 }

 @Test
 public void testSetPeriodInternalWithNullPeriod() {
 BasePeriod period = new BasePeriod();
 period.setPeriodInternal(null);
 Assert.assertArrayEquals(new int[8], period.iValues);
 }

 @Test
 public void testSetPeriodInternalWithInvalidPeriod() {
 BasePeriod period = new BasePeriod();
 try {
 period.setPeriodInternal(new Period("P1Y-1M"));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 Assert.assertArrayEquals(new int[8], period.iValues);
 }

 @Test
 public void testSetPeriod() {
 BasePeriod period1 = new BasePeriod(PeriodType.standard(), new int[]{1, 2, 3, 4, 5, 6, 7, 8});
 BasePeriod period2 = Period.standardDays(3);
 period1.setPeriod(period2);
 Assert.assertEquals(PeriodType.standard(), period1.getPeriodType());
 Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 11}, period1.iValues);
 }

 @Test
 public void testSetPeriodWithNullPeriod() {
 BasePeriod period = new BasePeriod(PeriodType.standard(), new int[]{1, 2, 3, 4, 5, 6, 7, 8});
 period.setPeriod(null);
 Assert.assertArrayEquals(new int[8], period.iValues);
 }
}