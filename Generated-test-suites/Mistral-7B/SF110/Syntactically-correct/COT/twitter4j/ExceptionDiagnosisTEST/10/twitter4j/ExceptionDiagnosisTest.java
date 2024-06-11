package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.ThrowableWithStackTrace;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
        Assert.assertEquals(exceptionDiagnosis.asHexString(), exceptionDiagnosis.asHexString()); // test equals method
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);

        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());

        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        Assert.assertTrue(stackTraceElement.getClassName().startsWith(filter[0]));
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();

        Assert.assertNotSame(stackLineHash, 0);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        Assert.assertNotSame(lineNumberHash, 0);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();

        Assert.assertNotNull(hexString);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsObjectNull() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new RuntimeException("Test Exception"));
        Object obj = null;

        exceptionDiagnosis.equals(obj);
    }

    @Test
    public void testEqualsObjectNotInstanceOfExceptionDiagnosis() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new RuntimeException("Test Exception"));
        Object obj = new Object();

        exceptionDiagnosis.equals(obj);
        Assert.assertFalse(exceptionDiagnosis.equals(obj));
    }

    @Test
    public void testEqualsObjectWithSameValues() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);

        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);

        int hashCode1 = exceptionDiagnosis1.hashCode();
        int hashCode2 = exceptionDiagnosis2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String toString = exceptionDiagnosis.toString();

        Assert.assertNotNull(toString);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[0];

        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
    }

    @Test
    public void testConstructorWithFilterContainingEmptyString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"", "twitter4j"};

        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);

        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        Assert.assertFalse(stackTraceElement.getClassName().isEmpty());
    }

    @Test
    public void testToStringWithCausedByException() {
        Throwable throwable = new RuntimeException("Test Exception", new IOException("Test IOException"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String toString = exceptionDiagnosis.toString();

        Assert.assertContains(toString, "ExceptionDiagnosis{stackLineHash=");
        Assert.assertContains(toString, "lineNumberHash=");
        Assert.assertContains(toString, "}");
        Assert.assertContains(toString, "ExceptionDiagnosis{stackLineHash=");
        Assert.assertContains(toString, "lineNumberHash=");
        Assert.assertContains(toString, "}");
    }
}