import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.base.Defaults;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParsingString() {
 String input = "P1Y2M3D4H5M6S";
 MutablePeriod period = MutablePeriod.parse(input);
 assertEquals(2, period.getYears());
 assertEquals(3, period.getMonths());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testParsingStringWithFormatter() {
 String input = "P1Y2M3D4H5M6S";
 PeriodFormatter formatter = ISOPeriodFormat.standard();
 MutablePeriod period = MutablePeriod.parse(input, formatter);
 assertEquals(2, period.getYears());
 assertEquals(3, period.getMonths());
 assertEquals(4, period.getDays());
 assertEquals(5, period.getHours());
 assertEquals(6, period.getMinutes());
 assertEquals(0, period.getSeconds());
 assertEquals(0, period.getMillis());
 }

 @Test
 public void testCreatingNewInstances() {
 // Default constructor
 MutablePeriod periodDefault = new MutablePeriod();
 assertEquals(0, periodDefault.getYears());
 assertEquals(0, periodDefault.getMonths());
 assertEquals(0, periodDefault.getWeeks());
 assertEquals(0, periodDefault.getDays());
 assertEquals(0, periodDefault.getHours());
 assertEquals(0, periodDefault.getMinutes());
 assertEquals(0, periodDefault.getSeconds());
 assertEquals(0, periodDefault.getMillis());

 // Parameterized constructor
 MutablePeriod periodParam = new MutablePeriod(PeriodType.standard());
 assertEquals(0, periodParam.getYears());
 assertEquals(0, periodParam.getMonths());
 assertEquals(0, periodParam.getWeeks());
 assertEquals(0, periodParam.getDays());
 assertEquals(0, periodParam.getHours());
 assertEquals(0, periodParam.getMinutes());
 assertEquals(0, periodParam.getSeconds());
 assertEquals(0, periodParam.getMillis());

 // Constructing with multiple arguments
 MutablePeriod periodArgs = new MutablePeriod(3, 2, 1, 4, 5, 6, 7, 8);
 assertEquals(3, periodArgs.getYears());
 assertEquals(2, periodArgs.getMonths());
 assertEquals(1, periodArgs.getWeeks());
 assertEquals(4, periodArgs.getDays());
 assertEquals(5, periodArgs.getHours());
 assertEquals(6, periodArgs.getMinutes());
 assertEquals(7, periodArgs.getSeconds());
 assertEquals(8, periodArgs.getMillis());
 }

 @Test
 public void testSettingValues() {
 MutablePeriod period = new MutablePeriod();
 period.setValue(0, 1);
 period.setValue(1, 2);
 period.setValue(2, 3);
 period.setValue(3, 4);
 period.setValue(4, 5);
 period.setValue(5, 6);
 period.setValue(6, 7);
 period.setValue(7, 8);

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
 public void testSettingPeriod() {
 MutablePeriod period = new MutablePeriod();
 ReadablePeriod readablePeriod = new GeneralBasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.setPeriod(readablePeriod);

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
 public void testSettingWithInvalidValues() {
 MutablePeriod period = new MutablePeriod();
 try {
 period.setValue(-1, Defaults.DECEMBER);
 fail("Expected an exception to be thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSettingPeriodWithNull() {
 MutablePeriod period = new MutablePeriod();
 try {
 period.setPeriod(null);
 fail("Expected an exception to be thrown");
 } catch (NullPointerException e) {
 // expected
 }
 }

 // Adding tests here

 // Getters tests here

 // Cloning tests here
}