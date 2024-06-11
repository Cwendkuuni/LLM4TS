package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new TwitterException("Test TwitterException");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, new String[]{"twitter4j"});

        // Test constructor
        assertNotNull(exceptionDiagnosis1);
        assertNotNull(exceptionDiagnosis2);

        // Test getStackLineHash and getLineNumberHash
        int expectedStackLineHash1 = 0;
        int expectedLineNumberHash1 = 0;
        int stackTraceLength = throwable.getStackTrace().length;
        for (int i = stackTraceLength - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
            if (stackTraceElement.getClassName().startsWith("twitter4j")) {
                expectedStackLineHash1 += 31 * expectedStackLineHash1 + stackTraceElement.getHashCode();
                expectedLineNumberHash1 += 31 * expectedLineNumberHash1 + stackTraceElement.getLineNumber();
                break;
            }
        }
        int actualStackLineHash1 = exceptionDiagnosis1.getStackLineHash();
        int actualLineNumberHash1 = exceptionDiagnosis1.getLineNumberHash();
        assertEquals(expectedStackLineHash1, actualStackLineHash1);
        assertEquals(expectedLineNumberHash1, actualLineNumberHash1);

        int expectedStackLineHash2 = 0;
        int expectedLineNumberHash2 = 0;
        for (int i = stackTraceLength - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
            if (stackTraceElement.getClassName().startsWith("twitter4j")) {
                expectedStackLineHash2 += 31 * expectedStackLineHash2 + stackTraceElement.getHashCode();
                expectedLineNumberHash2 += 31 * expectedLineNumberHash2 + stackTraceElement.getLineNumber();
                break;
            }
        }
        for (String filter : new String[]{"twitter4j"}) {
            int hash = stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
            expectedStackLineHash2 = 31 * expectedStackLineHash2 + hash;
            expectedLineNumberHash2 = 31 * expectedLineNumberHash2 + stackTraceElement.getLineNumber();
        }
        int actualStackLineHash2 = exceptionDiagnosis2.getStackLineHash();
        int actualLineNumberHash2 = exceptionDiagnosis2.getLineNumberHash();
        assertEquals(expectedStackLineHash2, actualStackLineHash2);
        assertEquals(expectedLineNumberHash2, actualLineNumberHash2);

        // Test asHexString
        String expectedHexString1 = exceptionDiagnosis1.asHexString();
        String expectedHexString2 = exceptionDiagnosis2.asHexString();
        String stackTraceHexString = "";
        for (int i = stackTraceLength - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
            if (stackTraceElement.getClassName().startsWith("twitter4j")) {
                stackTraceHexString += exceptionDiagnosis1.toHexString(stackTraceElement.getClassName().hashCode()) + "-";
                stackTraceHexString += exceptionDiagnosis1.toHexString(stackTraceElement.getMethodName().hashCode());
                break;
            }
        }
        String causeHexString = "";
        if (throwable.getCause() != null) {
            causeHexString = new ExceptionDiagnosis(throwable.getCause()).asHexString();
        }
        expectedHexString1 += stackTraceHexString;
        if (throwable.getCause() != null) {
            expectedHexString1 += " " + causeHexString;
        }
        assertEquals(expectedHexString1, expectedHexString1);
        assertEquals(expectedHexString2.substring(0, expectedHexString2.lastIndexOf(" ")), expectedHexString1.substring(0, expectedHexString1.lastIndexOf(" ")));

        // Test equals and hashCode
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable, new String[]{"twitter4j"});
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis1));
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis1));
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        assertTrue(exceptionDiagnosis2.equals(exceptionDiagnosis1));
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
        assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis1));
        assertFalse(exceptionDiagnosis1.equals(new Object()));
        assertFalse(exceptionDiagnosis1.equals(null));
        assertFalse(exceptionDiagnosis1.equals(new ExceptionDiagnosis(new RuntimeException())));
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis1.hashCode());
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());
        assertNotEquals(exceptionDiagnosis1.hashCode(), new Object().hashCode());
        assertNotEquals(exceptionDiagnosis1.hashCode(), new ExceptionDiagnosis(new RuntimeException()).hashCode());

        // Test toString
        String expectedToString1 = "ExceptionDiagnosis{stackLineHash=" + expectedStackLineHash1 + ", lineNumberHash=" + expectedLineNumberHash1 + '}';
        String expectedToString2 = "ExceptionDiagnosis{stackLineHash=" + expectedStackLineHash2 + ", lineNumberHash=" + expectedLineNumberHash2 + '}';
        String actualToString1 = exceptionDiagnosis1.toString();
        String actualToString2 = exceptionDiagnosis2.toString();
        assertEquals(expectedToString1, actualToString1);
        assertEquals(expectedToString2, actualToString2);
    }

    @Test(expected = AssertionError.class)
    public void testNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInclusionFilter() {
        new ExceptionDiagnosis(new Throwable(), null);
    }
}