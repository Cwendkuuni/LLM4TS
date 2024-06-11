package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.InvalidThrowableException;
import twitter4j.ThrowableWithFilters;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis.getStackLineHash());
        assertNotNull(diagnosis.getLineNumberHash());
        assertNotNull(diagnosis.getHexString());
        assertEquals("TestRuntimeException", diagnosis.th.getMessage());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilters() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        String[] filters = new String[]{"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);
        assertNotNull(diagnosis.getStackLineHash());
        assertNotNull(diagnosis.getLineNumberHash());
        assertNotNull(diagnosis.getHexString());
        assertEquals("TestRuntimeException", diagnosis.th.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        String[] filters = new String[]{"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);
        int stackLineHash = diagnosis.getStackLineHash();
        assertNotNull(stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        String[] filters = new String[]{"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);
        int lineNumberHash = diagnosis.getLineNumberHash();
        assertNotNull(lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        String[] filters = new String[]{"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);
        assertNotNull(diagnosis.getHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test1 RuntimeException");
        Throwable throwable2 = new RuntimeException("Test2 RuntimeException");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertFalse(diagnosis1.equals(diagnosis2));

        Throwable throwable3 = new RuntimeException("Test RuntimeException");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable3, new String[0]);
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable3, new String[0]);
        assertTrue(diagnosis3.equals(diagnosis4));

        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[]{"com.test"});
        ExceptionDiagnosis diagnosis6 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[0]);
        assertFalse(diagnosis5.equals(diagnosis6));

        ExceptionDiagnosis diagnosis7 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[]{"com.test"});
        ExceptionDiagnosis diagnosis8 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[]{"com.test"});
        assertTrue(diagnosis7.equals(diagnosis8));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test1 RuntimeException");
        Throwable throwable2 = new RuntimeException("Test2 RuntimeException");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        Throwable throwable3 = new RuntimeException("Test RuntimeException");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable3, new String[0]);
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable3, new String[0]);
        assertEquals(diagnosis3.hashCode(), diagnosis4.hashCode());

        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[]{"com.test"});
        ExceptionDiagnosis diagnosis6 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[0]);
        assertNotEquals(diagnosis5.hashCode(), diagnosis6.hashCode());

        ExceptionDiagnosis diagnosis7 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[]{"com.test"});
        ExceptionDiagnosis diagnosis8 = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[]{"com.test"});
        assertEquals(diagnosis7.hashCode(), diagnosis8.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        String[] filters = new String[]{"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);
        String toString = diagnosis.toString();
        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidThrowableException() {
        new ExceptionDiagnosis(new InvalidThrowableException("Test InvalidThrowableException"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithThrowableWithFiltersNull() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        new ExceptionDiagnosis(throwable, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithThrowableWithEmptyFilters() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        new ExceptionDiagnosis(throwable, new String[0]);
    }

    @Test
    public void testConstructorWithThrowableWithFiltersNotEmpty() {
        Throwable throwable = new RuntimeException("Test RuntimeException");
        String[] filters = new String[]{"com.test"};
        new ExceptionDiagnosis(throwable, filters);
    }

    @Test
    public void testGetCause() {
        Throwable throwable = new RuntimeException("Test Throwable with Cause");
        Throwable cause = new RuntimeException("Test Cause");
        throwable.initCause(cause);
        String[] filters = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filters);
        assertEquals(cause.getMessage(), diagnosis.th.getCause().getMessage());
    }
}