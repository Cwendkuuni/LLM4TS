package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.period.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

 @BeforeClass
 static public void beforeClass() {
 // Set up any static variables or initialize any static objects here
 }

 @AfterClass
 static public void afterClass() {
 // Perform any necessary cleanup after all tests have completed
 }

 @Before
 static public void before() {
 // Set up any test data or initialize any test objects here
 }

 @After
 static public void after() {
 // Perform any necessary cleanup after each test method has completed
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 assertSame("Getting field type should return the correct instance", MySingleFieldType.INSTANCE, testObject.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 assertSame("Getting period type should return the correct instance", MySingleFieldPeriod.TYPE, testObject.getPeriodType());
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 assertEquals("Size should return 1", 1, testObject.size());
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 assertEquals("Getting value should return the correct value", 1, testObject.getValue());
 }

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 testObject.setValue(2);
 assertEquals("Setting value should update the test object", 2, testObject.getValue());
 }

 @Test
 public void testGet() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 DurationFieldType fieldType = MySingleFieldType.INSTANCE;
 assertEquals("Getting value through get method should return the correct value", 1, testObject.get(fieldType));
 }

 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 DurationFieldType fieldType = MySingleFieldType.INSTANCE;
 assertTrue("Getting field type should be supported", testObject.isSupported(fieldType));
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 Period expected = Period.withFields(Field.instance(MySingleFieldType.INSTANCE, 1));
 Period actual = testObject.toPeriod();
 assertEquals("Converting BaseSingleFieldPeriod to Period should return the expected Period", expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 MutablePeriod expected = new MutablePeriod().withField(MySingleFieldType.INSTANCE, 1);
 MutablePeriod actual = testObject.toMutablePeriod();
 assertEquals("Converting BaseSingleFieldPeriod to MutablePeriod should return the expected MutablePeriod", expected, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod testObject1 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testObject2 = new MySingleFieldPeriod(1);
 assertTrue("Two instances of BaseSingleFieldPeriod with the same value should be equal", testObject1.equals(testObject2));
 BaseSingleFieldPeriod testObject3 = new MySingleFieldPeriod(2);
 assertFalse("Two instances of BaseSingleFieldPeriod with different values should not be equal", testObject1.equals(testObject3));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod testObject1 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testObject2 = new MySingleFieldPeriod(1);
 int expectedHashCode1 = 27 * (27 * 17 + 1) + MySingleFieldType.INSTANCE.hashCode();
 int expectedHashCode2 = 27 * (27 * 17 + 1) + MySingleFieldType.INSTANCE.hashCode();
 int actualHashCode1 = testObject1.hashCode();
 int actualHashCode2 = testObject2.hashCode();
 assertEquals("Two instances of BaseSingleFieldPeriod with the same value should have the same hashcode", expectedHashCode1, actualHashCode1);
 assertEquals("Two instances of BaseSingleFieldPeriod with the same value should have the same hashcode", expectedHashCode2, actualHashCode2);
 BaseSingleFieldPeriod testObject3 = new MySingleFieldPeriod(2);
 int actualHashCode3 = testObject3.hashCode();
 assertNotEquals("Two instances of BaseSingleFieldPeriod with different values should have different hashcodes", expectedHashCode1, actualHashCode3);
 }

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod testObject1 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testObject2 = new MySingleFieldPeriod(1);
 assertEquals("Two instances of BaseSingleFieldPeriod with the same value should compare as equal", 0, testObject1.compareTo(testObject2));
 BaseSingleFieldPeriod testObject3 = new MySingleFieldPeriod(2);
 assertTrue("Two instances of BaseSingleFieldPeriod with different values should compare as greater when the first instance has a greater value", testObject1.compareTo(testObject3) < 0);
 assertTrue("Two instances of BaseSingleFieldPeriod with different values should compare as lesser when the second instance has a greater value", testObject3.compareTo(testObject1) > 0);
 }

 @Test
 public void testBetween() {
 ReadableInstant readableInstant1 = new DateTime(2015, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableInstant readableInstant2 = new DateTime(2015, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC);
 BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(1);
 int expected = 86400000; // One day in milliseconds
 int actual = BaseSingleFieldPeriod.between(readableInstant1, readableInstant2, testObject.getFieldType());
 assertEquals("The difference between two instants in days should be equal to the value of the BaseSingleFieldPeriod", expected, actual);
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod readablePeriod = Period.withFields(Field.instance(MySingleFieldType.INSTANCE, 1));
 long n = 2;
 int expected = 172800000; // Two days in milliseconds
 int actual = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals("The standard period of the given ReadablePeriod in the specified number of units should be the expected value", expected, actual);
 }
}

 class MySingleFieldPeriod extends BaseSingleFieldPeriod {
 private static final MySingleFieldType INSTANCE = new MySingleFieldType();
 private static final PeriodType TYPE = new PeriodType() {
 @Override
 public String getID() {
 return "MySingleFieldPeriodType";
 }

 @Override
 public String getName() {
 return "MySingleFieldPeriod";
 }

 @Override
 public DurationFieldType getFieldType() {
 return MySingleFieldType.INSTANCE;
 }
 };

 MySingleFieldPeriod(int iPeriod) {
 super(iPeriod);
 }

 @Override
 public DurationFieldType getFieldType() {
 return INSTANCE;
 }

 @Override
 public PeriodType getPeriodType() {
 return TYPE;
 }
 }

 class MySingleFieldType implements DurationFieldType {
 public static final MySingleFieldType INSTANCE = new MySingleFieldType();
 private static final String ID = "MyFieldID";
 private static final String NAME = "MySingleField";

 @Override
 public int getPrecision() {
 return DURATION_FIELD_ANY;
 }

 @Override
 public String getID() {
 return ID;
 }

 @Override
 public String getName() {
 return NAME;
 }

 @Override
 public long getUnitMillis() {
 return 86400000; // One day in milliseconds
 }

 @Override
 public long getUnitCount(long n) {
 return n;
 }

 @Override
 public DurationFieldType getNextFieldType() {
 return null;
 }

 @Override
 public int getValue(ReadableInstant inst) {
 return (int)(inst.getMillis() / getUnitMillis());
 }

 @Override
 public int getValue(ReadablePartial partial) {
 throw new UnsupportedOperationException("Getting value from a ReadablePartial is not supported for this field type");
 }

 @Override
 public int getValue(ReadableDateTime dateTime) {
 return (int)(dateTime.getMillis() / getUnitMillis());
 }

 @Override
 public int getValue(ReadablePeriod period) {
 return period.getValue(0);
 }
}