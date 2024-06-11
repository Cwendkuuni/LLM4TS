import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodConstructionWithValues() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[] {3, 2, 1, 4, 5, 6, 7, 8};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithDurations() {
 BasePeriod period = new BasePeriod(Duration.standardDays(3), Duration.standardDays(2), PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[] {3, 0, 0, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithInstants() {
 Instant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 Instant end = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 1};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadablePeriods() {
 BaseLocalDate start = new LocalDate(2022, 1, 1);
 BaseLocalDate end = new LocalDate(2022, 1, 2);
 BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 1};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriodAddingDurations() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 period = period.plus(Duration.standardDays(2));
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{3, 0, 0, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriodMergingPeriods() {
 BasePeriod period1 = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(Duration.standardDays(2), PeriodType.standard());
 BasePeriod mergedPeriod = period1.merge(period2);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{3, 0, 0, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, mergedPeriod.getPeriodType());
 assertArrayEquals(expectedValues, mergedPeriod.getValues());
 }

 @Test
 public void testBasePeriodAddingPeriods() {
 BasePeriod period1 = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(Duration.standardDays(2), PeriodType.standard());
 BasePeriod sumPeriod = period1.plus(period2);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{3, 0, 0, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, sumPeriod.getPeriodType());
 assertArrayEquals(expectedValues, sumPeriod.getValues());
 }
}