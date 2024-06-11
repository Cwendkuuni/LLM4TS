import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.*;

public class BasePeriodTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any class-level initialization here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Tear down any class-level resources here
 }

 @Before
 public void setUp() {
 // Set up any test-level initialization here
 }

 @After
 public void tearDown() {
 // Tear down any test-level resources here
 }

 @Test
 public void testBasePeriodConstructorWithInts() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period expected = new BasePeriod(PeriodType.standard()).withFields(new LocalDate(2022, 3, 5).toSerialable());
 Assert.assertEquals(expected.toString(), p.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodConstructorWithNullReadableInstant() {
 new BasePeriod(null, null, PeriodType.standard());
 }

 @Test
 public void testBasePeriodConstructorWithReadableInstant() {
 Instant instant1 = new DateTime("2022-03-05T12:34:56.789Z").toInstant();
 Instant instant2 = new DateTime("2022-03-06T13:45:67.123Z").toInstant();
 BasePeriod p = new BasePeriod(instant1, instant2, PeriodType.standard());
 BasePeriod expected = new BasePeriod(PeriodType.standard()).withFields(new LocalDate(2022, 3, 1).toSerialable()).plusDays(1).plusHours(1).plusMinutes(5).plusSeconds(6).plusMillis(7);
 Assert.assertEquals(expected.toString(), p.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodConstructorWithNullReadablePartial() {
 new BasePeriod(null, null, PeriodType.standard());
 }

 @Test
 public void testBasePeriodConstructorWithReadablePartial() {
 LocalDate date1 = new LocalDate(2022, 3, 5);
 LocalDate date2 = new LocalDate(2022, 3, 6);
 BasePeriod p = new BasePeriod(date1, date2, PeriodType.standard());
 BasePeriod expected = new BasePeriod(PeriodType.standard()).withFields(new LocalDate(2022, 3, 1).toSerialable()).plusDays(1);
 Assert.assertEquals(expected.toString(), p.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodConstructorWithNullPeriod() {
 new BasePeriod(new Object(), null, PeriodType.standard());
 }

 @Test
 public void testBasePeriodConstructorWithObject() {
 Duration d = Duration.standardDays(1).plusHours(1).plusMinutes(5).plusSeconds(6).plusMillis(7);
 BasePeriod p = new BasePeriod(d, PeriodType.standard());
 BasePeriod expected = new BasePeriod(PeriodType.standard()).withFields(new LocalDate(2022, 3, 6).toSerialable());
 Assert.assertEquals(expected.toString(), p.toString());
 }

 @Test
 public void testBasePeriodConstructorWithIntArray() {
 int[] values = {31, 2, 0, 12, 0, 30, 0, 7};
 BasePeriod p = new BasePeriod(values, PeriodType.standard());
 BasePeriod expected = new BasePeriod(PeriodType.standard()).withFields(new LocalDate(2023, 3, 7).toSerialable());
 Assert.assertEquals(expected.toString(), p.toString());
 }

 @Test
 public void testBasePeriodGetters() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Assert.assertEquals(PeriodType.standard(), p.getPeriodType());
 Assert.assertEquals(31, p.getValue(DurationFieldType.days().getLenientIndex()));
 Assert.assertEquals(2, p.getValue(DurationFieldType.months().getLenientIndex()));
 // ... add tests for other getters
 }

 @Test
 public void testBasePeriodToDurationFrom() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Instant instant = new DateTime("2022-03-05T12:34:56.789Z").toInstant();
 Duration duration = p.toDurationFrom(instant);
 Assert.assertEquals(Duration.standardDays(1).plusHours(2).plusMinutes(3).plusSeconds(4).plusMillis(5), duration);
 }

 @Test
 public void testBasePeriodToDurationTo() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Instant instant = new DateTime("2022-03-05T12:34:56.789Z").toInstant();
 Duration duration = p.toDurationTo(instant);
 Assert.assertEquals(Duration.standardDays(1).plusHours(2).plusMinutes(3).plusSeconds(4).plusMillis(5), duration.negate());
 }

 @Test
 public void testBasePeriodCheckAndUpdate() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 p.checkAndUpdate(DurationFieldType.years(), new int[] {1, 2, 3, 4, 5, 6, 7, 8}, 10);
 Assert.assertEquals(new int[] {1, 2, 10, 4, 5, 6, 7, 8}, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 }

 @Test
 public void testBasePeriodSetPeriod() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period q = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 p.setPeriod(q);
 Assert.assertEquals(new int[] {3, 4, 5, 6, 7, 8, 9, 10}, p.getValues());
 }

 @Test
 public void testBasePeriodSetPeriodInternal() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period q = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 p.setPeriodInternal(q);
 Assert.assertEquals(new int[] {3, 4, 5, 6, 7, 8, 9, 10}, p.getValues());
 }

 @Test
 public void testBasePeriodSetPeriodWithNullPeriod() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 p.setPeriod(null);
 Assert.assertArrayEquals(new int[8], p.getValues());
 }

 @Test
 public void testBasePeriodMergePeriod() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period q = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 Period r = new BasePeriod();
 p.mergePeriod(q);
 r.setValues(p.getValues());
 q.mergePeriodInto(r);
 Assert.assertEquals(new int[] {4, 6, 8, 10, 7, 9, 11, 12}, r.getValues());
 }

 @Test
 public void testBasePeriodAddPeriod() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Period q = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 Period r = new BasePeriod();
 r.setValues(p.getValues());
 p.addPeriod(q);
 r.addPeriodInto(r.getValues(), p);
 Assert.assertEquals(new int[] {4, 6, 11, 10, 12, 13, 17, 18}, r.getValues());
 }

 @Test
 public void testBasePeriodSetField() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 p.setField(DurationFieldType.years(), 10);
 Assert.assertEquals(new int[] {10, 2, 3, 4, 5, 6, 7, 8}, p.getValues());
 }

 @Test
 public void testBasePeriodSetFieldInto() {
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
 p.setFieldInto(values, DurationFieldType.years(), 10);
 Assert.assertEquals(new int[] {10, 2, 3, 4, 5, 6, 7, 8}, values);
 }

 @Test
 public void testBasePeriodAddField() {
 Period p = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 p.addField(DurationFieldType.years(), 10);
 Assert.assertEquals(new int[] {11, 2, 3, 4, 5, 6, 7, 8}, p.getValues());
 }

 @Test
 public void testBasePeriodAddFieldInto() {
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
 p.addFieldInto(values, DurationFieldType.years(), 10);
 Assert.assertEquals(new int[] {11, 2, 3, 4, 5, 6, 7, 8}, values);
 }

}