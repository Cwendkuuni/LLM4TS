import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @BeforeClass
 public static void setupClass() {
 // Set up any static variables or objects here if necessary
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static variables or objects here if necessary
 }

 @Before
 public void setUp() {
 // Set up any instance variables or objects here if necessary
 }

 @After
 public void tearDown() {
 // Clean up any instance variables or objects here if necessary
 }

 @Test
 public void testConstructorWithInts() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithLongAndPeriodType() {
 BasePeriod period = new BasePeriod(1234567890123L, PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, 1234567890123L);
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithReadableInstants() {
 BasePeriod period = new BasePeriod(new DateTime(2010, 1, 1, 0, 0), new DateTime(2010, 1, 2, 0, 0), PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 31, 0, 0, 0, 0, 0, 0};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithReadablePartials() {
 BasePeriod period = new BasePeriod(new LocalDateTime(2010, 1, 1, 12, 30), new LocalDateTime(2010, 1, 2, 14, 15), PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{60 * 60 * 24 * 31, -60 * 30, 0, 0, 0, 0, 0, 1 * 60 * 60 + 15 * 60};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithReadableInstantAndReadableDuration() {
 BasePeriod period = new BasePeriod(new DateTime(2010, 1, 1, 0, 0), new Duration(1, TimeUnit.DAYS), PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 0};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithReadableDurationAndReadableInstant() {
 BasePeriod period = new BasePeriod(new Duration(1, TimeUnit.DAYS), new DateTime(2010, 1, 1, 0, 0), PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 0, 0, -1};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithLong() {
 BasePeriod period = new BasePeriod(1234567890123L);
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.time();
 int[] expectedValues = ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, 1234567890123L);
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithLongPeriodTypeAndChronology() {
 BasePeriod period = new BasePeriod(1234567890123L, PeriodType.standard(), ISOChronology.getInstanceUTC());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 0};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithObjectPeriodTypeAndChronology() {
 BasePeriod period = new BasePeriod(new DateTime(2010, 1, 1, 0, 0), PeriodType.standard(), ISOChronology.getInstanceUTC());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 31, 0, 0, 0, 0, 0, 0};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithIntsAndPeriodType() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testConstructorWithLongLongPeriodTypeAndChronology() {
 BasePeriod period = new BasePeriod(1234567890123L, 1234567890123L, PeriodType.standard(), ISOChronology.getInstanceUTC());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[8];
 System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 0};
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testGetPeriodType() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 PeriodType expectedPeriodType = PeriodType.standard();
 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 }

 @Test
 public void testGetValue() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 int actualValue1 = period.getValue(0);
 int actualValue2 = period.getValue(2);
 int expectedValue1 = 1;
 int expectedValue2 = 3;
 assertEquals("Incorrect value for index 0", expectedValue1, actualValue1);
 assertEquals("Incorrect value for index 2", expectedValue2, actualValue2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithNullReadablePartial() {
 new BasePeriod(null, new LocalDateTime(2010, 1, 1, 12, 30), PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithNullReadablePartial2() {
 new BasePeriod(new LocalDateTime(2010, 1, 1, 12, 30), null, PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithDifferentReadablePartials() {
 new BasePeriod(new LocalDateTime(2010, 1, 1, 12, 30), new LocalDateTime(2010, 1, 2, 12, 30), PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithInvalidPeriodType() {
 new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, null);
 }

 @Test
 public void testToDurationFrom() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration actualDuration = period.toDurationFrom(new DateTime(2010, 1, 1, 0, 0));
 Duration expectedDuration = new Duration(3, TimeUnit.DAYS);
 assertEquals("Incorrect duration", expectedDuration, actualDuration);
 }

 @Test
 public void testToDurationTo() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration actualDuration = period.toDurationTo(new DateTime(2010, 1, 1, 0, 0));
 Duration expectedDuration = new Duration(-3, TimeUnit.DAYS);
 assertEquals("Incorrect duration", expectedDuration, actualDuration);
 }
}