package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.format.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartialTest {

 private Partial partial;
 private Partial partialWithUTC;
 private Partial partialWithTypes;
 private Partial partialWithValues;
 private ReadablePartial readablePartial;

 @BeforeAll
 public void setUp() {
 // Creating an instance with default values
 partial = new Partial();

 // Creating an instance with specified Chronology
 partialWithUTC = new Partial(DateTimeUtils.UTC);

 // Creating an instance with specified DateTimeFieldType and value
 partialWithTypes = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);

 // Creating an instance with specified DateTimeFieldType, value and Chronology
 partialWithValues = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12, DateTimeUtils.UTC);

 // Creating an instance from a ReadablePartial
 readablePartial = new LocalDateTime().with(DateTimeFields.HOUR_OF_DAY, 12).toLocalDate().toPartial();
 }

 @Test
 public void testSize() {
 // Testing size method of Partial class
 assertEquals(0, partial.size());
 assertEquals(1, partialWithTypes.size());
 }

 @Test
 public void testGetChronology() {
 // Testing getChronology method of Partial class
 assertEquals(DateTimeUtils.getDefault(), partial.getChronology());
 assertEquals(DateTimeUtils.UTC, partialWithUTC.getChronology());
 }

 @Test
 public void testGetField() {
 // Testing getField method of Partial class
 assertEquals(DateTimeField.HOUR_OF_DAY, partialWithTypes.getField(0));
 }

 @Test
 public void testGetFieldType() {
 // Testing getFieldType method of Partial class
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithTypes.getFieldType(0));
 }

 @Test
 public void testGetFieldTypes() {
 // Testing getFieldTypes method of Partial class
 assertArrayEquals(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY}, partialWithTypes.getFieldTypes());
 }

 @Test
 public void testGetValue() {
 // Testing getValue method of Partial class
 assertEquals(12, partialWithTypes.getValue(0));
 }

 @Test
 public void testGetValues() {
 // Testing getValues method of Partial class
 assertArrayEquals(new int[]{12}, partialWithTypes.getValues());
 }

 @Test
 public void testWithChronologyRetainFields() {
 // Testing withChronologyRetainFields method of Partial class
 Partial partialWithNewChronology = partialWithUTC.withChronologyRetainFields(DateTimeUtils.UTC);
 assertEquals(partialWithUTC, partialWithNewChronology);
 }

 @Test
 public void testWith() {
 // Testing with method of Partial class
 Partial partialWithNewValue = partialWithTypes.with(DateTimeFieldType.MINUTE_OF_HOUR, 30);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithNewValue.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithNewValue.getFieldType(1));
 assertArrayEquals(new int[]{12, 30}, partialWithNewValue.getValues());
 }

 @Test
 public void testWithout() {
 // Testing without method of Partial class
 Partial partialWithoutHour = partialWithTypes.without(DateTimeFieldType.HOUR_OF_DAY);
 assertArrayEquals(new DateTimeFieldType[]{DateTimeFieldType.MINUTE_OF_HOUR}, partialWithoutHour.getFieldTypes());
 assertArrayEquals(new int[]{0}, partialWithoutHour.getValues());
 }

 @Test
 public void testWithField() {
 // Testing withField method of Partial class
 Partial partialWithNewValueField = partialWithValues.withField(DateTimeFieldType.MINUTE_OF_HOUR, 30);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithNewValueField.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithNewValueField.getFieldType(1));
 assertArrayEquals(new int[]{12, 30}, partialWithNewValueField.getValues());
 }

 @Test
 public void testWithFieldAdded() {
 // Testing withFieldAdded method of Partial class
 Partial partialWithAddedField = partialWithTypes.withFieldAdded(DateTimeFieldType.SECOND_OF_MINUTE, 30);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithAddedField.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithAddedField.getFieldType(1));
 assertEquals(DateTimeFieldType.SECOND_OF_MINUTE, partialWithAddedField.getFieldType(2));
 assertArrayEquals(new int[]{12, 0, 30}, partialWithAddedField.getValues());
 }

 @Test
 public void testWithFieldAddWrapped() {
 // Testing withFieldAddWrapped method of Partial class
 Partial partialWithAddWrappedField = partialWithTypes.withFieldAddWrapped(DateTimeFieldType.SECOND_OF_MINUTE, 60);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithAddWrappedField.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithAddWrappedField.getFieldType(1));
 assertEquals(DateTimeFieldType.SECOND_OF_MINUTE, partialWithAddWrappedField.getFieldType(2));
 assertArrayEquals(new int[]{13, 0, 0}, partialWithAddWrappedField.getValues());
 }

 @Test
 public void testWithPeriodAdded() {
 // Testing withPeriodAdded method of Partial class
 ReadablePeriod readablePeriod = new GeneralDuration(new Date(), new Date(System.currentTimeMillis() + 1000L), true);
 Partial partialWithAddedPeriod = partialWithTypes.withPeriodAdded(readablePeriod, 1);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithAddedPeriod.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithAddedPeriod.getFieldType(1));
 assertArrayEquals(new int[]{12, 31}, partialWithAddedPeriod.getValues());
 }

 @Test
 public void testPlus() {
 // Testing plus method of Partial class
 Partial partialWithAddedPeriod = partialWithTypes.plus(new GeneralDuration(new Date(), new Date(System.currentTimeMillis() + 1000L), true));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithAddedPeriod.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithAddedPeriod.getFieldType(1));
 assertArrayEquals(new int[]{13, 1}, partialWithAddedPeriod.getValues());
 }

 @Test
 public void testMinus() {
 // Testing minus method of Partial class
 Partial partialWithSubtractedPeriod = partialWithTypes.minus(new GeneralDuration(new Date(), new Date(System.currentTimeMillis() - 1000L), true));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithSubtractedPeriod.getFieldType(0));
 assertEquals(DateTimeFieldType.MINUTE_OF_HOUR, partialWithSubtractedPeriod.getFieldType(1));
 assertArrayEquals(new int[]{11, 59}, partialWithSubtractedPeriod.getValues());
 }

 @Test
 public void testProperty() {
 // Testing property method of Partial class
 Property propertyHour = partialWithTypes.property(DateTimeFieldType.HOUR_OF_DAY);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, propertyHour.getField().getClass());
 assertEquals(12, propertyHour.get());
 Partial partialWithNewValueProperty = propertyHour.set(13);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partialWithNewValueProperty.getFieldType(0));
 assertEquals(13, partialWithNewValueProperty.getValue(0));
 }

 @Test
 public void testIsMatch() {
 // Testing isMatch method of Partial class
 LocalDateTime localDateTime1 = new LocalDateTime(2022, 1, 1, 12, 30, 0);
 LocalDateTime localDateTime2 = new LocalDateTime(2022, 1, 1, 13, 0, 0);
 assertTrue(partialWithTypes.isMatch(localDateTime1));
 assertFalse(partialWithTypes.isMatch(localDateTime2));
 }

 @Test
 public void testIsMatchWithReadablePartial() {
 // Testing isMatch method of Partial class with ReadablePartial
 assertTrue(partialWithTypes.isMatch(readablePartial));
 }

 @Test
 public void testGetFormatter() {
 // Testing getFormatter method of Partial class
 DateTimeFormatter formatter = partialWithTypes.getFormatter();
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formatter.pattern());
 }

 @Test
 public void testToString() {
 // Testing toString method of Partial class
 assertEquals("[hour-of-day=12]", partialWithTypes.toString());
 }

 @Test
 public void testToStringList() {
 // Testing toStringList method of Partial class
 assertEquals("[hour-of-day=12]", partialWithTypes.toStringList());
 }

 @Test
 public void testToStringWithPattern() {
 // Testing toString method of Partial class with custom pattern
 assertEquals("12:00:00", partialWithTypes.toString("HH:mm:ss"));
 }

 @Test
 public void testToStringWithLocale() {
 // Testing toString method of Partial class with custom locale
 assertEquals("12:00:00", partialWithTypes.toString("HH:mm:ss", Locale.FRANCE));
 }
}