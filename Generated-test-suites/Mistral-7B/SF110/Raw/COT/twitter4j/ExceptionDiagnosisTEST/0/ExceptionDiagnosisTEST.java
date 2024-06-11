package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.Twthrowable; // Assuming Twthrowable is a Throwable subclass used in the actual code

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Throwable throwable = new Twthrowable("Test Error Message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.getHexString());
        Assert.assertEquals(throwable.getClass().getName(), diagnosis.getHexString().split("-")[0]);
    }

    @Test
    public void testExceptionDiagnosisConstructionWithEmptyFilter() {
        Throwable throwable = new Twthrowable("Test Error Message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.getHexString());
        Assert.assertEquals(throwable.getClass().getName(), diagnosis.getHexString().split("-")[0]);
    }

    @Test
    public void testExceptionDiagnosisConstructionWithClassFilter() {
        Throwable throwable = new Twthrowable("Test Error Message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("com.example")) {
                Assert.assertEquals(element.getClassName() + "." + element.getMethodName(), diagnosis.getHexString().split("-")[0]);
                break;
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructionWithNullFilter() {
        Throwable throwable = new Twthrowable("Test Error Message");
        new ExceptionDiagnosis(throwable, null);
    }

    @Test
    public void testExceptionDiagnosisGetStackLineHash() {
        Throwable throwable = new Twthrowable("Test Error Message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        Assert.assertEquals(stackTrace[stackTrace.length - 1].getHashCode(), diagnosis.getStackLineHash());
    }

    @Test
    public void testExceptionDiagnosisGetLineNumberHash() {
        Throwable throwable = new Twthrowable("Test Error Message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        Assert.assertEquals(stackTrace[stackTrace.length - 1].getLineNumber(), diagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisEquals() {
        Throwable throwable1 = new Twthrowable("Test Error Message 1");
        Throwable throwable2 = new Twthrowable("Test Error Message 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertFalse(diagnosis1.equals(diagnosis2));
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);
        Assert.assertTrue(diagnosis1.equals(diagnosis3));
    }

    @Test
    public void testExceptionDiagnosisHashCode() {
        Throwable throwable1 = new Twthrowable("Test Error Message 1");
        Throwable throwable2 = new Twthrowable("Test Error Message 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);
        Assert.assertEquals(diagnosis1.hashCode(), diagnosis3.hashCode());
    }

    @Test
    public void testExceptionDiagnosisToString() {
        Throwable throwable = new Twthrowable("Test Error Message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + stackTraceElementHashCode(throwable.getStackTrace()[0]) + ", lineNumberHash=" + lineNumber(throwable.getStackTrace()[0].getLineNumber()) + '}';
        Assert.assertEquals(expected, diagnosis.toString());
    }

    private int stackTraceElementHashCode(StackTraceElement element) {
        return element.getClassName().hashCode() * 31 + element.getMethodName().hashCode() * 31 + Integer.hashCode(element.getLineNumber());
    }

    private int lineNumber(int lineNumber) {
        return lineNumber;
    }
}