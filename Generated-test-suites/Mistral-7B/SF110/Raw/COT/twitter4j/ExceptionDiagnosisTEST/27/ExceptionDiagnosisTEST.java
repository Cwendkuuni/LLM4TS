package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwowиткиException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Throwable twowитkiException = new TwowитkiException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowитkiException);
        assertNotNull(exceptionDiagnosis);
        assertNotNull(exceptionDiagnosis.getTh());
        assertNotNull(exceptionDiagnosis.getStackTraceElementAt(0));
    }

    @Test
    public void testExceptionDiagnosisConstructionWithThrowableAndFilter() {
        Throwable twowитkiException = new TwowиткиException("Error reading tweets");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowитkiException, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertNotNull(exceptionDiagnosis.getTh());
        StackTraceElement stackTraceElement = exceptionDiagnosis.getStackTraceElementAt(0);
        assertTrue(stackTraceElement.getClassName().startsWith("twitter4j"));
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructionWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable twowитkiException = new TwowиткиException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowиткиException);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        assertNotSame(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable twowиткиException = new TwowиткиException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowиткиException);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertNotSame(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable twowитkiException = new TwowиткиException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowиткиException);
        String hexString = exceptionDiagnosis.asHexString();
        assertNotNull(hexString);
        assertFalse(hexString.isEmpty());
    }

    @Test
    public void testEquals() {
        Throwable twowиткиException1 = new TwowиткиException("Error reading tweets 1");
        Throwable twowиткиException2 = new TwowиткиException("Error reading tweets 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(twowиткиException1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(twowиткиException2);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(twowиткиException1);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));

        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(twowиткиException1, new String[]{"twitter4j"});
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis4));
    }

    @Test
    public void testHashCode() {
        Throwable twowиткиException = new TwowиткиException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(twowиткиException);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(twowиткиException);
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable twowитkiException = new TwowиткиException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowиткиException);
        String toString = exceptionDiagnosis.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetStackTraceElementAtWithNegativeIndex() {
        Throwable twowитkiException = new TwowиткиException("Error reading tweets");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twowиткиException);
        exceptionDiagnosis.getStackTraceElementAt(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStackTraceElementAtWithNullExceptionDiagnosis() {
        ExceptionDiagnosis exceptionDiagnosis = null;
        try {
            exceptionDiagnosis.getStackTraceElementAt(0);
        } catch (NullPointerException e) {
            // expected
        }
    }
}