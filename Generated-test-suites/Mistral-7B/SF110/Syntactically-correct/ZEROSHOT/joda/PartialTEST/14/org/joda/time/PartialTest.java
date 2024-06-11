import org.joda.time.*;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.Chronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testPartialDefaultConstructor() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
    }

    @Test
    public void testPartialConstructorWithChronology() {
        Partial partial = new Partial(DateTimeUtils.UTC);
        assertEquals(0, partial.size());
    }

    @Test
    public void testPartialConstructorWithDateTimeFieldTypeAndInt() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
    }

    @Test
    public void testPartialConstructorWithDateTimeFieldTypeArrayAndIntArray() {
        DateTimeFieldType[] types = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};
        int[] values = {2022, 12, 31};
        Partial partial = new Partial(types, values);
        assertEquals(3, partial.size());
        for (int i = 0; i < partial.size(); i++) {
            assertEquals(types[i], partial.getFieldType(i));
            assertEquals(values[i], partial.getValue(i));
        }
    }

    @Test
    public void testPartialConstructorWithReadablePartial() {
        Partial partial = new Partial(new Partial(DateTimeFieldType.year(), 2022));
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
    }

    @Test
    public void testPartialWithChronologyRetainFields() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.UTC);
        assertEquals(1, partial.size());
        Partial partialNew = partial.withChronologyRetainFields(DateTimeUtils.ISO);
        assertEquals(1, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
    }

    @Test
    public void testPartialWith() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial partialNew = partial.with(DateTimeFieldType.monthOfYear(), 12);
        assertEquals(2, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
    }

    @Test
    public void testPartialWithout() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        Partial partialNew = partial.without(DateTimeFieldType.monthOfYear());
        assertEquals(1, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
    }

    @Test
    public void testPartialWithField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        Partial partialNew = partial.withField(DateTimeFieldType.dayOfMonth(), 31);
        assertEquals(3, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
        assertEquals(DateTimeFieldType.dayOfMonth(), partialNew.getFieldType(2));
        assertEquals(31, partialNew.getValue(2));
    }

    @Test
    public void testPartialWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        Partial partialNew = partial.withFieldAdded(DateTimeFieldType.dayOfYear(), 365);
        assertEquals(3, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
        assertEquals(DateTimeFieldType.dayOfYear(), partialNew.getFieldType(2));
        assertEquals(365, partialNew.getValue(2));
    }

    @Test
    public void testPartialWithFieldAddWrapped() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        Partial partialNew = partial.withFieldAddWrapped(DateTimeFieldType.dayOfYear(), 366);
        assertEquals(3, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
        assertEquals(DateTimeFieldType.dayOfYear(), partialNew.getFieldType(2));
        assertEquals(1, partialNew.getValue(2));
    }

    @Test
    public void testPartialWithPeriodAdded() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        ReadablePeriod period = new GeneralMillisPeriod(365 * 24 * 60 * 60 * 1000); // one year
        Partial partialNew = partial.withPeriodAdded(period, 1);
        assertEquals(3, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2023, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
    }

    @Test
    public void testPartialPlus() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        ReadablePeriod period = new GeneralMillisPeriod(365 * 24 * 60 * 60 * 1000); // one year
        Partial partialNew = partial.plus(period);
        assertEquals(3, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2023, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
    }

    @Test
    public void testPartialMinus() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023, DateTimeFieldType.monthOfYear(), 12);
        ReadablePeriod period = new GeneralMillisPeriod(365 * 24 * 60 * 60 * 1000); // one year
        Partial partialNew = partial.minus(period);
        assertEquals(3, partialNew.size());
        assertEquals(DateTimeFieldType.year(), partialNew.getFieldType(0));
        assertEquals(2022, partialNew.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partialNew.getFieldType(1));
        assertEquals(12, partialNew.getValue(1));
    }

    @Test
    public void testPartialToString() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        assertEquals("[year=2022, monthOfYear=12]", partial.toString());
    }

    @Test
    public void testPartialToStringList() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        assertEquals("[year=2022, monthOfYear=12]", partial.toStringList());
    }

    @Test
    public void testPartialToStringWithLocale() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        assertEquals("[year=2022, monthOfYear=12]", partial.toString("", null));
        assertEquals("[year=2022, monthOfYear=December]", partial.toString("MMM", Locale.ENGLISH));
        assertEquals("[year=2022, monthOfYear=12]", partial.toString("dd.MM.yyyy", Locale.ENGLISH));
    }

    @Test
    public void testPartialToStringWithFormat() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        assertEquals("[year=2022, monthOfYear=12]", partial.toString("%1$s=%2$d", null));
        assertEquals("[year=2022, monthOfYear=12]", partial.toString("%1$s=%2$d", new Object[]{"Year", "12"}));
    }

    @Test
    public void testPartialGetFormatter() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 12);
        DateTimeFormatter formatter = partial.getFormatter();
        assertTrue(formatter instanceof ISODateTimeFormat);
        assertEquals(DateTimeFieldTypeType.instance(), formatter.getFieldTypes());
    }
}