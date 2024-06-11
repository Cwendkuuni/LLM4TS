package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.ThrowableWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndEmptyFilter() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndFilter() {
        String filter = "test";
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement stackTraceElement = new ExceptionInInitializerError("Test Error").getStackTrace()[0];
        Assert.assertTrue(stackTraceElement.getClassName().startsWith(filter));
        Assert.assertNotEquals("", exceptionDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new ThrowableWrapper("Test Throwable 1");
        Throwable throwable2 = new ThrowableWrapper("Test Throwable 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);
        Assert.assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}', exceptionDiagnosis.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFilter() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        new ExceptionDiagnosis(throwable, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyFilterArray() {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        new ExceptionDiagnosis(throwable, new String[0]);
    }

    @Test
    public void testGetCauseHexString() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Throwable throwable = new ThrowableWrapper("Test Throwable");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Method getCauseMethod = ExceptionDiagnosis.class.getDeclaredMethod("getCause");
        getCauseMethod.setAccessible(true);
        Throwable cause = (Throwable)getCauseMethod.invoke(exceptionDiagnosis);
        ExceptionDiagnosis causeDiagnosis = new ExceptionDiagnosis(cause);
        String exceptionHexString = exceptionDiagnosis.asHexString();
        String causeHexString = causeDiagnosis.asHexString();
        int index = exceptionHexString.lastIndexOf(" ");
        String exceptionHexStringWithoutCause = exceptionHexString.substring(0, index);
        String causeHexStringWithoutException = causeHexString.substring(causeHexString.indexOf(" "));
        Assert.assertNotEquals(exceptionHexStringWithoutCause, causeHexStringWithoutException);
    }

}