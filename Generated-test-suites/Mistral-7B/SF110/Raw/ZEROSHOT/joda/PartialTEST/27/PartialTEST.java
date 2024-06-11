package org.junit.jupiter.api;
import org.joda.time.Partial;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartialTest {

 private Partial partial;
 private Partial emptyPartial;
 private DateTimeFieldType fieldType;
 private DateTimeFieldType rangeFieldType;
 private Chronology chronology;

 @BeforeAll
 public void setup() {
 this.fieldType = new LocalDateField(DateTimeFields.YEAR_LOCAL);
 this.rangeFieldType = new LocalDateField(DateTimeFields.DAY_OF_MONTH);
 this.chronology = GregorianChronology.getInstance();

 this.emptyPartial = new Partial(this.chronology);
 this.partial = new Partial(this.fieldType, 2022, this.chronology);
 }

 @Test
 public void testConstructorWithNullChronology() {
 assertThrows(IllegalArgumentException.class, () -> new Partial(null));
 }

 @Test
 public void testConstructorWithNullDateTimeFieldType() {
 assertThrows(IllegalArgumentException.class, () -> new Partial(null, 2022, this.chronology));
 }

 @Test
 public void testSize() {
 assertEquals(1, this.partial.size());
 }

 @Test
 public void testGetChronology() {
 assertEquals(this.chronology, this.partial.getChronology());
 }

 @Test
 public void testGetValues() {
 assertArrayEquals(new int[]{2022}, this.partial.getValues());
 }

 @Test
 public void testGetFieldTypes() {
 DateTimeFieldType[] fieldTypes = this.partial.getFieldTypes();
 assertEquals(this.fieldType, fieldTypes[0]);
 assertArrayEquals(fieldTypes, this.partial.getFieldTypes());
 }

 @Test
 public void testGetField() {
 assertEquals(this.fieldType.getField(this.chronology), this.partial.getField(0, this.chronology));
 }

 @Test
 public void testGetFieldType() {
 assertEquals(this.fieldType, this.partial.getFieldType(0));
 }

 @Test
 public void testWith() {
 Partial newPartial = this.partial.with(this.fieldType, 2023);
 assertEquals(newPartial.size(), this.partial.size() + 1);
 assertEquals(2023, newPartial.getValue(newPartial.size() - 1));
 }

 @Test
 public void testWithout() {
 Partial newPartial = this.partial.without(this.fieldType);
 assertEquals(newPartial.size(), this.partial.size() - 1);
 }

 @Test
 public void testWithField() {
 Partial newPartial = this.partial.withField(this.fieldType, 2023);
 assertEquals(this.partial.size(), newPartial.size());
 assertEquals(2023, newPartial.getValue(0));
 }

 @Test
 public void testWithFieldAdded() {
 Partial newPartial = this.partial.withFieldAdded(this.rangeFieldType, 31);
 assertEquals(this.partial.size() + 1, newPartial.size());
 assertEquals(31, newPartial.getValue(this.partial.size()));
 }

 @Test
 public void testWithFieldAddWrapped() {
 Partial newPartial = this.partial.withFieldAddWrapped(this.rangeFieldType, 30);
 assertEquals(this.partial.size(), newPartial.size());
 }

 @Test
 public void testWithPeriodAdded() {
 LocalDate date = new LocalDate(2022, 12, 31);
 Period period = new Period(1, 0, 0, 0, 0, 0, 0);
 Partial newPartial = this.partial.withPeriodAdded(date, period.getYears());
 assertEquals(this.partial.size() + period.getYears(), newPartial.size());
 assertEquals(2023, newPartial.getValue(0));
 }

 @Test
 public void testIsMatch() {
 LocalDateTime dateTime = new LocalDateTime(2022, 12, 31, 0, 0, 0);
 assertTrue(this.partial.isMatch(dateTime));
 }

 @Test
 public void testIsMatchWithDifferentChronology() {
 LocalDateTime dateTime = new LocalDateTime(2022, 12, 31, 0, 0, 0).withChronology(UTC);
 assertFalse(this.partial.isMatch(dateTime));
 }

 @Test
 public void testIsMatchWithDifferentFieldType() {
 LocalDateTime dateTime = new LocalDateTime(2022, 12, 31, 0, 0, 0).withFieldAdded(LocalFieldType.HOUR_OF_DAY, 1);
 assertFalse(this.partial.isMatch(dateTime));
 }

 @Test
 public void testToString() {
 String expected = "[year=2022]";
 assertEquals(expected, this.partial.toString());
 }

 @Test
 public void testToStringList() {
 String expected = "[year=2022]";
 assertEquals(expected, this.partial.toStringList());
 }

 @Test
 public void testToStringWithFormatter() {
 DateTimeFormatter formatter = ISODateTimeFormat.forFields(new DateTimeFieldType[]{this.fieldType}, true, false);
 String expected = "[year=2022]";
 assertEquals(expected, formatter.print(this.partial));
 }

 @Test
 public void testToStringWithCustomFormatter() {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy");
 String expected = "2022";
 assertEquals(expected, formatter.print(this.partial.getField(0, this.chronology)));
 assertEquals(expected, this.partial.toString(formatter));
 }

 @Test
 public void testToStringWithCustomFormatterAndLocale() {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy").withLocale(Locale.CHINESE);
 String expected = "二 thousand twenty two";
 assertEquals(expected, this.partial.toString(formatter));
 }

 @Test
 public void testEmptyPartial() {
 assertTrue(this.emptyPartial.isEmpty());
 assertEquals(0, this.emptyPartial.size());
 assertThrows(UnsupportedOperationException.class, () -> this.emptyPartial.getValue(0));
 assertThrows(UnsupportedOperationException.class, () -> this.emptyPartial.getField(0, this.chronology));
 assertThrows(UnsupportedOperationException.class, () -> this.emptyPartial.getFieldTypes());
 assertThrows(UnsupportedOperationException.class, () -> this.emptyPartial.set(this.fieldType, 2022));
 }

}