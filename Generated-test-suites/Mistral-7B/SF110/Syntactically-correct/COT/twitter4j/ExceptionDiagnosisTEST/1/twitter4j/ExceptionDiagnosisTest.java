package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.twitterVoid; // Assuming twitterVoid is a valid class and package name

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable throwable = new NullPointerException(); // Basic test with nullpointerexception
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithFilter() {
        Throwable throwable = new NullPointerException();
        String[] filter = new String[]{"twitter4j"}; // Filter test
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullThrowableConstructor() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null); // Null throwable test
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        Assert.assertNotNull(stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        Assert.assertNotNull(lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();
        Assert.assertNotNull(hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new NullPointerException();
        Throwable throwable2 = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int hashCode = exceptionDiagnosis.hashCode();
        Assert.assertNotSame(hashCode, 0);
    }

    @Test
    public void testToString() {
        Throwable throwable = new NullPointerException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String toString = exceptionDiagnosis.toString();
        Assert.assertNotNull(toString);
    }

    @Test
    public void testCause() {
        Throwable throwable = new NullPointerException();
        Throwable cause = new IOException();
        throwable.initCause(cause);
        String[] filter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        String hexString = exceptionDiagnosis.asHexString();
        int indexOfSpace = hexString.indexOf(" ");
        Assert.assertTrue(indexOfSpace != -1);
        String substring = hexString.substring(0, indexOfSpace);
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(cause, filter);
        String causeHexString = causeExceptionDiagnosis.asHexString();
        Assert.assertEquals(substring, causeHexString);
    }

    @Test
    public void testEmptyFilter() {
        Throwable throwable = new NullPointerException();
        String[] filter = new String[0];
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        Assert.assertEquals("", exceptionDiagnosis.asHexString());
    }
}