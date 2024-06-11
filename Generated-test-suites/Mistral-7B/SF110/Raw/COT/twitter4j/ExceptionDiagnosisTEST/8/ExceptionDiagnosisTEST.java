package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.twitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        Assert.assertNull(new ExceptionDiagnosis(null));
    }

    @Test
    public void testExceptionDiagnosisConstructorWithValidThrowable() {
        Throwable throwable = new twitterException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis);
        Assert.assertNotNull(exceptionDiagnosis.th);
        Assert.assertEquals(exceptionDiagnosis.getStackLineHash(), getHashCode(throwable.getStackTrace()[0]));
        Assert.assertEquals(exceptionDiagnosis.getLineNumberHash(), throwable.getStackTrace()[0].getLineNumber());
        Assert.assertEquals(exceptionDiagnosis.asHexString(), exceptionDiagnosis.toHexString(exceptionDiagnosis.getStackLineHash()) + "-" + exceptionDiagnosis.toHexString(exceptionDiagnosis.getLineNumberHash()));
    }

    @Test
    public void testExceptionDiagnosisConstructorWithEmptyStringArray() {
        Throwable throwable = new twitterException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        Assert.assertNotNull(exceptionDiagnosis);
        Assert.assertNotNull(exceptionDiagnosis.th);
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        Assert.assertEquals(exceptionDiagnosis.getStackLineHash(), getHashCode(stackTraceElement));
        Assert.assertEquals(exceptionDiagnosis.getLineNumberHash(), stackTraceElement.getLineNumber());
        Assert.assertEquals(exceptionDiagnosis.asHexString(), exceptionDiagnosis.toHexString(exceptionDiagnosis.getStackLineHash()) + "-" + exceptionDiagnosis.toHexString(exceptionDiagnosis.getLineNumberHash()));
    }

    @Test
    public void testExceptionDiagnosisConstructorWithValidThrowableAndStringArray() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = {"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(exceptionDiagnosis);
        Assert.assertNotNull(exceptionDiagnosis.th);
        int expectedStackLineHash = getHashCode(findMatchingStackTraceElement(throwable.getStackTrace(), inclusionFilter)[0]);
        Assert.assertEquals(exceptionDiagnosis.getStackLineHash(), expectedStackLineHash);
        Assert.assertEquals(exceptionDiagnosis.getLineNumberHash(), findMatchingStackTraceElement(throwable.getStackTrace(), inclusionFilter)[0].getLineNumber());
        Assert.assertEquals(exceptionDiagnosis.asHexString(), exceptionDiagnosis.toHexString(exceptionDiagnosis.getStackLineHash()) + "-" + exceptionDiagnosis.toHexString(exceptionDiagnosis.getLineNumberHash()));
    }

    @Test
    public void testEqualsMethod() {
        Throwable throwable1 = new twitterException("Test Exception 1");
        Throwable throwable2 = new twitterException("Test Exception 2");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);

        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCodeMethod() {
        Throwable throwable1 = new twitterException("Test Exception 1");
        Throwable throwable2 = new twitterException("Test Exception 2");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);

        Assert.assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());
        Assert.neq(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test(expected = AssertionError.class)
    public void testToStringMethod() {
        Throwable throwable = new twitterException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertEquals(exceptionDiagnosis.toString(), "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}');
    }

    private int getHashCode(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode() + stackTraceElement.getLineNumber();
    }

    private StackTraceElement findMatchingStackTraceElement(StackTraceElement[] stackTrace, String[] inclusionFilter) {
        for (StackTraceElement stackTraceElement : stackTrace) {
            for (String filter : inclusionFilter) {
                if (stackTraceElement.getClassName().startsWith(filter)) {
                    return stackTraceElement;
                }
            }
        }
        return null;
    }

}