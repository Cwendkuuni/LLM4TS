package org.example.junit;

import org.joda.time.DurationFieldType;
import org.junit.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private static UnsupportedDurationField testField;

    @BeforeClass
    public static void setupClass() {
        testField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
    }

    @Test
    public void testIsSupported() {
        assertFalse(testField.isSupported());
    }

    @Test
    public void testIsSupportedWithNullType() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(null);
        assertFalse(unsupportedField.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        testField.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        testField.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithNullInstant() {
        testField.getValue(null, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithNullInstant() {
        testField.getValueAsLong(null, 1);
    }

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(field1.getName(), field2.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test
    public void testGetInstanceConcurrentAccess() {
        final DurationFieldType type = DurationFieldType.DAYS;
        Runnable task = () -> UnsupportedDurationField.getInstance(type);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<UnsupportedDurationField>> futures = executor.invokeAll(Arrays.asList(task, task, task));
        UnsupportedDurationField field1 = futures.get(0).get();
        UnsupportedDurationField field2 = futures.get(1).get();
        assertEquals(field1, field2);
    }

    @Test
    public void testName() {
        assertEquals("DAYS", testField.getName());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = testField;
        UnsupportedDurationField field2 = testField;
        assertTrue(field1.equals(field2));
        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        int code1 = testField.hashCode();
        UnsupportedDurationField duplicate = testField;
        int code2 = duplicate.hashCode();
        assertEquals(code1, code2);
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[DAYS]", testField.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField newField = (UnsupportedDurationField) testField.readResolve();
        assertEquals(testField.getName(), newField.getName());
    }

    @After
    public void tearDown() {
        testField = null;
    }

}

//