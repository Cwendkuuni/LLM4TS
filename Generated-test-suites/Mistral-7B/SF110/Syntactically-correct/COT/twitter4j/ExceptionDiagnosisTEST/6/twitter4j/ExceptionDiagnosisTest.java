package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.Twthrowable; // Assuming Twthrowable is the Throwable subclass used in your codebase

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Twthrowable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345; // Add your expected hash code value here
        int expectedLineNumberHash = 5678; // Add your expected hash code value here
        String expectedHexString = "ExpectedHexString-ExpectedHexString"; // Add your expected hex string value here
        ExceptionDiagnosis expectedExceptionDiagnosis = new ExceptionDiagnosis(new Twthrowable("Test Exception"), new String[]{"ExpectedClassName"});
        String expectedAsHexString = expectedExceptionDiagnosis.asHexString();

        int actualStackLineHash = exceptionDiagnosis.getStackLineHash();
        int actualLineNumberHash = exceptionDiagnosis.getLineNumberHash();
        String actualAsHexString = exceptionDiagnosis.asHexString();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
        assertTrue(actualAsHexString.startsWith(expectedHexString));
    }

    @Test(expected = AssertionError.class)
    public void testExceptionDiagnosisConstructionWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testEqualsMethod() {
        Twthrowable throwable1 = new Twthrowable("Test Exception 1");
        Twthrowable throwable2 = new Twthrowable("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);

        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCodeMethod() {
        Twthrowable throwable1 = new Twthrowable("Test Exception 1");
        Twthrowable throwable2 = new Twthrowable("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);

        int expectedHashCode1 = 123123; // Add your expected hash code value here
        int expectedHashCode2 = 456456; // Add your expected hash code value here

        assertEquals(expectedHashCode1, exceptionDiagnosis1.hashCode());
        assertEquals(expectedHashCode2, exceptionDiagnosis2.hashCode());
        assertEquals(expectedHashCode1, exceptionDiagnosis3.hashCode());
    }

    @Test
    public void testToStringMethod() {
        Twthrowable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...,}"; // Add your expected toString value here

        String actualToString = exceptionDiagnosis.toString();

        assertTrue(actualToString.contains(expectedToString));
    }
}