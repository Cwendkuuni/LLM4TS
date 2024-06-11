package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.Twthrowable; // Assuming Twthrowable is the Throwable class used in the project

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        assertNotNull(stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertNotNull(lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();
        assertNotNull(hexString);
        assertTrue(hexString.matches("^[0-9A-F]{32}$"));
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new Twthrowable("Test Exception 1");
        Throwable throwable2 = new Twthrowable("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new Twthrowable("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"testPackage"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"testPackage"});

        assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));
        assertTrue(exceptionDiagnosis3.hashCode() == exceptionDiagnosis4.hashCode());
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);

        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new ExceptionDiagnosis(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullIncludeFilter() {
        Throwable throwable = new Twthrowable("Test Exception");
        new ExceptionDiagnosis(throwable, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testToString() {
        Throwable throwable = new Twthrowable("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String toString = exceptionDiagnosis.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
    }

    @Test
    public void testGetStackTrace() {
        Throwable throwable = new Twthrowable("Test Exception");
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{stackTrace[0].getClassName()});
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        assertNotEquals(stackLineHash, 0);
        assertNotEquals(lineNumberHash, 0);

        StackTraceElement stackTraceElement = stackTrace[0];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();

        assertTrue(className.startsWith("testPackage"));
        assertTrue(methodName.contains("testMethod")); // Replace "testMethod" with the actual method name
    }

    @Test
    public void testCause() {
        Throwable throwable1 = new Twthrowable("Test Exception 1");
        Throwable throwable2 = new Twthrowable("Test Exception 2");
        throwable1.initCause(throwable2);
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);

        String hexString = exceptionDiagnosis1.asHexString();
        assertTrue(hexString.contains(" "));

        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        String hexString2 = exceptionDiagnosis2.asHexString();

        assertEquals(hexString.substring(0, hexString.indexOf(" ")), hexString2);
    }
}