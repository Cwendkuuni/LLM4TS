import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.DateTimeField;
import org.joda.time.DurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 private static Partial getTestPartial(Chronology chronology, DateTimeFieldType... fieldTypes) {
 return new Partial(chronology, fieldTypes, new int[fieldTypes.length]);
 }

 @Test
 public void testEmptyPartial() {
 Partial partial = new Partial();
 assertEquals(0, partial.size());
 }

 @Test
 public void testPartialWithSingleField() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 Partial partial = getTestPartial(chronology, DateTimeFieldType.year());
 assertEquals(1, partial.size());
 assertEquals(chronology, partial.getChronology());
 assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
 assertEquals(0, partial.getValue(0));
 }

 @Test
 public void testPartialWithMultipleFields() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 assertEquals(3, partial.size());
 assertEquals(chronology, partial.getChronology());
 assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
 assertEquals(DateTimeFieldType.monthOfYear(), partial.getFieldTypes()[1]);
 assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldTypes()[2]);
 assertEquals(0, partial.getValue(0));
 assertEquals(0, partial.getValue(1));
 assertEquals(0, partial.getValue(2));
 }

 @Test
 public void testPartialWithInvalidFields() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.year(), DateTimeFieldType.dayOfMonth() };
 try {
 Partial partial = getTestPartial(chronology, fieldTypes);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithInvalidValues() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 try {
 partial.with(DateTimeFieldType.dayOfMonth(), -1);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPartialWithValidValues() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 31);
 partial = partial.with(DateTimeFieldType.year(), dateTime.getYear());
 partial = partial.with(DateTimeFieldType.monthOfYear(), dateTime.getMonthOfYear());
 partial = partial.with(DateTimeFieldType.dayOfMonth(), dateTime.getDayOfMonth());
 assertEquals(3, partial.size());
 assertEquals(2022, partial.getValue(0));
 assertEquals(1, partial.getValue(1));
 assertEquals(31, partial.getValue(2));
 }

 @Test
 public void testPartialWithValidValuesInWrongOrder() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.year() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 31);
 partial = partial.with(DateTimeFieldType.dayOfMonth(), dateTime.getDayOfMonth());
 partial = partial.with(DateTimeFieldType.monthOfYear(), dateTime.getMonthOfYear());
 partial = partial.with(DateTimeFieldType.year(), dateTime.getYear());
 assertEquals(3, partial.size());
 assertEquals(2022, partial.getValue(2));
 assertEquals(1, partial.getValue(1));
 assertEquals(31, partial.getValue(0));
 }

 @Test
 public void testPartialWithValidValuesAndChronologyChange() {
 Chronology chronology1 = DateTimeUtils.forLocale(Locale.US);
 Chronology chronology2 = DateTimeUtils.forLocale(Locale.CHINA);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology1, fieldTypes);
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 31);
 partial = partial.withChronologyRetainFields(chronology2);
 assertEquals(3, partial.size());
 assertEquals(2022, partial.getValue(0));
 assertEquals(1, partial.getValue(1));
 assertEquals(31, partial.getValue(2));
 }

 @Test
 public void testPartialToString() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 31);
 partial = partial.with(DateTimeFieldType.year(), dateTime.getYear());
 partial = partial.with(DateTimeFieldType.monthOfYear(), dateTime.getMonthOfYear());
 partial = partial.with(DateTimeFieldType.dayOfMonth(), dateTime.getDayOfMonth());
 String expectedToString = "[" + DateTimeFieldType.year().getName() + "=2022, " + DateTimeFieldType.monthOfYear().getName() + "=1, " + DateTimeFieldType.dayOfMonth().getName() + "=31]";
 assertEquals(expectedToString, partial.toString());
 }

 @Test
 public void testPartialToStringWithCustomFormat() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 31);
 partial = partial.with(DateTimeFieldType.year(), dateTime.getYear());
 partial = partial.with(DateTimeFieldType.monthOfYear(), dateTime.getMonthOfYear());
 partial = partial.with(DateTimeFieldType.dayOfMonth(), dateTime.getDayOfMonth());
 String expectedToString = "[" + DateTimeFieldType.year().getName() + "=2022, " + DateTimeFieldType.monthOfYear().getName() + "=1, " + DateTimeFieldType.dayOfMonth().getName() + "=31]";
 assertEquals(expectedToString, partial.toString("[" + DateTimeFieldType.year().getName() + "=YYYY, " + DateTimeFieldType.monthOfYear().getName() + "=MM, " + DateTimeFieldType.dayOfMonth().getName() + "=dd]"));
 }

 @Test
 public void testPartialPlus() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 31);
 LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 28);
 assertEquals(new LocalDateTime(2022, 3, 31), partial.plus(dateTime1.minus(dateTime2)));
 }

 @Test
 public void testPartialMinus() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 31);
 LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 28);
 assertEquals(new LocalDateTime(2022, 1, 3), partial.minus(dateTime1.minus(dateTime2)));
 }

 @Test
 public void testPartialWithPeriodAdded() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 ReadablePeriod period = new BasePeriod(DateTimeFieldType.years(), 2, DateTimeFieldType.months(), 1, DateTimeFieldType.days(), 1);
 assertEquals(new LocalDateTime(2024, 2, 2), partial.withPeriodAdded(period, 1));
 }

 @Test
 public void testPartialWithoutField() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 DateTimeFieldType fieldToRemove = DateTimeFieldType.monthOfYear();
 Partial partialWithoutField = partial.without(fieldToRemove);
 assertEquals(2, partialWithoutField.size());
 assertFalse(partialWithoutField.getFieldType(1) == fieldToRemove);
 }

 @Test
 public void testPartialWithFieldAdded() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 DurationFieldType durationFieldType = DateTimeFieldType.days();
 Partial partialWithFieldAdded = partial.withFieldAdded(durationFieldType, 1);
 assertEquals(4, partialWithFieldAdded.size());
 assertEquals(durationFieldType, partialWithFieldAdded.getFieldTypes()[3]);
 }

 @Test
 public void testPartialWithFieldAddWrapped() {
 Chronology chronology = DateTimeUtils.forLocale(Locale.US);
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() };
 Partial partial = getTestPartial(chronology, fieldTypes);
 DurationFieldType durationFieldType = DateTimeFieldType.days();
 Partial partialWithFieldAddWrapped = partial.withFieldAddWrapped(durationFieldType, 1);
 assertEquals(4, partialWithFieldAddWrapped.size());
 assertEquals(durationFieldType, partialWithFieldAddWrapped.getFieldTypes()[3]);
 }
}