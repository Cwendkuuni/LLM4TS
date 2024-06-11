import org.junit.Assert;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFields;
import org.joda.time.Instant;

public class UnsupportedDurationFieldTest {

    @org.junit.Test
    public void testUnsupportedDurationFieldGetInstance() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);

        Assert.assertSame("Unsupported Duration Fields should be the same instance", unsupportedField1, unsupportedField2);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetValue() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;

        unsupportedField.getValue(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetValueAsLong() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;

        unsupportedField.getValueAsLong(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetValueWithInstant() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        Instant instant = new Instant(1234567890123L);

        unsupportedField.getValue(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        Instant instant = new Instant(1234567890123L);

        unsupportedField.getValueAsLong(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetMillis() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 1;

        unsupportedField.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetMillisWithValue() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1;

        unsupportedField.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1;
        Instant instant = new Instant(1234567890123L);

        unsupportedField.getMillis(value, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldAdd() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        Instant instant = new Instant(1234567890123L);
        long value = 1;

        unsupportedField.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetDifference() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        Instant minuendInstant = new Instant(1234567890123L);
        Instant subtrahendInstant = new Instant(1234567880123L);

        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        Instant minuendInstant = new Instant(1234567890123L);
        Instant subtrahendInstant = new Instant(1234567880123L);

        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test
    public void testUnsupportedDurationFieldEquals() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField3 = DurationFields.seconds(1);

        Assert.assertTrue("Unsupported Duration Fields should be equal", unsupportedField1.equals(unsupportedField2));
        Assert.assertFalse("An Unsupported Duration Field should not be equal to a supported one", unsupportedField1.equals(unsupportedField3));
    }

    @org.junit.Test
    public void testUnsupportedDurationFieldHashCode() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);

        Assert.assertEquals("Unsupported Duration Fields should have the same hashcode", unsupportedField1.hashCode(), unsupportedField2.hashCode());
    }

    @org.junit.Test
    public void testUnsupportedDurationFieldToString() {
        DurationFieldType type = DurationFieldType. seconds();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

        Assert.assertEquals("Expected 'UnsupportedDurationField[seconds]'", "UnsupportedDurationField[seconds]", unsupportedField.toString());
    }

}