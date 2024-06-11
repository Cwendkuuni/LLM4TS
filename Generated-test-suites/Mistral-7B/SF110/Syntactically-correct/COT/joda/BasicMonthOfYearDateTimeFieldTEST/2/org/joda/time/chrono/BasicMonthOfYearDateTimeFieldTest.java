import org.joda.time.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setup() {
 GregorianChronology chronology = new GregorianChronology();
 field = new BasicMonthOfYearDateTimeField(chronology, 2); // February is the 2nd leap month
 }

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet_validYearMonth() {
 assertEquals(2, field.get(DateTimeMath.millisOfDay(0L)));
 }

 @Test
 public void testGet_invalidYearMonth() {
 try {
 field.get(Long.MIN_VALUE);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAdd_validAmount() {
 long original = DateTimeMath.millisOfDay(0L);
 long result = field.add(original, 1);
 assertEquals(1, field.get(result));
 }

 @Test
 public void testAdd_zeroAmount() {
 long original = DateTimeMath.millisOfDay(0L);
 long result = field.add(original, 0);
 assertEquals(2, field.get(original)); // no change
 }

 @Test
 public void testAdd_negativeAmount() {
 long original = DateTimeMath.millisOfDay(DateTimeMath.millisOfDay(0, new DateTime(2022, 2, 28).toLocalDate().toDate().getTime()));
 long result = field.add(original, -1);
 assertEquals(1, field.get(result));
 }

 @Test
 public void testAdd_largeAmount() {
 long original = DateTimeMath.millisOfDay(DateTimeMath.millisOfDay(0, new DateTime(2022, 2, 28).toLocalDate().toDate().getTime()));
 long result = field.add(original, Long.MAX_VALUE);
 fail("Adding too large amount should throw IllegalArgumentException");
 }

 @Test
 public void testAddWrapField() {
 long original = DateTimeMath.millisOfDay(0L);
 long result = field.addWrapField(original, 13);
 assertEquals(2, field.get(result));
 }

 @Test
 public void testAdd_invalidReadablePartial() {
 try {
 field.add(new DateTime(), 1, new int[0], 0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAdd_partialsWithDifferentFieldTypes() {
 try {
 field.add(new DateTime(), 1, new int[] { 1, 2 }, 0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAdd_partialsWithSameFieldTypeDifferentValues() {
 long original = DateTimeMath.millisOfDay(0L);
 int[] expectedResult = { 3, 1 };
 int[] result = field.add(new DateTime(), 1, new int[] { 1, 2 }, 0);
 assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testAdd_contiguousPartials() {
 long original = DateTimeMath.millisOfDay(0L);
 long expectedResult = DateTimeMath.millisOfDay(new DateTime(2022, 3, 1).toLocalDate().toDate().getTime());
 long result = field.add(new DateTime(), 1, new int[] { 1, 12 }, 0).getTime();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 long later = DateTimeMath.millisOfDay(DateTimeMath.millisOfDay(0, new DateTime(2022, 3, 1).toLocalDate().toDate().getTime()));
 long difference = field.getDifferenceAsLong(later, DateTimeMath.millisOfDay(0L));
 assertEquals(30, difference);
 }

 @Test
 public void testSet() {
 long original = DateTimeMath.millisOfDay(0L);
 long expectedResult = DateTimeMath.millisOfDay(new DateTime(2022, 3, 1).toLocalDate().toDate().getTime());
 long result = field.set(original, 3);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField rangeDurationField = field.getRangeDurationField();
 assertEquals(DurationFieldType.years(), rangeDurationField.getType());
 }

 @Test
 public void testIsLeap_validDate() {
 assertTrue(field.isLeap(DateTimeMath.millisOfDay(new DateTime(2022, 2, 29).toLocalDate().toDate().getTime())));
 }

 @Test
 public void testIsLeap_invalidDate() {
 assertFalse(field.isLeap(DateTimeMath.millisOfDay(0L)));
 }

 @Test
 public void testGetLeapAmount_validDate() {
 assertEquals(1, field.getLeapAmount(DateTimeMath.millisOfDay(new DateTime(2022, 2, 29).toLocalDate().toDate().getTime())));
 }

 @Test
 public void testGetLeapAmount_invalidDate() {
 assertEquals(0, field.getLeapAmount(DateTimeMath.millisOfDay(0L)));
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField leapDurationField = field.getLeapDurationField();
 assertEquals(DurationFieldType.days(), leapDurationField.getType());
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 long original = DateTimeMath.millisOfDay(DateTimeMath.millisOfDay(0, new DateTime(2022, 2, 28).toLocalDate().toDate().getTime()));
 long floor = field.roundFloor(original);
 assertEquals(DateTimeMath.millisOfDay(0, new DateTime(2022, 2, 28).toLocalDate().toDate().getTime()), floor);
 }

 @Test
 public void testRemainder() {
 long original = DateTimeMath.millisOfDay(DateTimeMath.millisOfDay(0, new DateTime(2022, 2, 29).toLocalDate().toDate().getTime()));
 long remainder = field.remainder(original);
 assertEquals(0, remainder);
 }

 @Test
 public void testSet_invalidValue() {
 try {
 field.set(Long.MIN_VALUE, 1);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}