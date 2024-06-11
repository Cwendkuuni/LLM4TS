package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosisWithSingleThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345678; // Replace with the expected value from testing
        int expectedLineNumberHash = 98765432; // Replace with the expected value from testing
        String expectedHexString = "ABCDEF01-23456789"; // Replace with the expected value from testing
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        int expectedStackLineHash = 12345678; // Replace with the expected value from testing
        int expectedLineNumberHash = 98765432; // Replace with the expected value from testing
        String expectedHexString = "ABCDEF01-23456789"; // Replace with the expected value from testing
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345678; // Replace with the expected value from testing
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedLineNumberHash = 98765432; // Replace with the expected value from testing
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedHexString = "ABCDEF01-23456789"; // Replace with the expected value from testing
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new RuntimeException("Test Exception 1");
        String[] filter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, filter);
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, filter);
        assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        Throwable throwable3 = new RuntimeException("Test Exception 1");
        String[] filter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, filter);
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, filter);
        assertEquals(exceptionDiagnosis3.hashCode(), exceptionDiagnosis4.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        assertEquals(expectedToString, exceptionDiagnosis.toString());
    }
}