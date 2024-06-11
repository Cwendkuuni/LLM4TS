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
        Assert.assertEquals(throwable.getMessage(), exceptionDiagnosis.th.getMessage());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilterArray() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filterArray = new String[]{"test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filterArray);

        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement currentElement = stackTrace[i];
            if (currentElement.getClassName().startsWith("test")) {
                Assert.assertEquals(currentElement.getClassName(), exceptionDiagnosis.th.getStackTrace()[0].getClassName());
                Assert.assertEquals(currentElement.getMethodName(), exceptionDiagnosis.th.getStackTrace()[0].getMethodName());
                Assert.assertEquals(currentElement.getLineNumber(), exceptionDiagnosis.getLineNumberHash() >> 32);
                break;
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testExceptionDiagnosisGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int stackLineHashExpected = calculateStackLineHash(stackTrace);

        Assert.assertEquals(stackLineHashExpected, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testExceptionDiagnosisGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int lineNumberHashExpected = calculateLineNumberHash(stackTrace);

        Assert.assertEquals(lineNumberHashExpected, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        ExceptionDiagnosis sameException = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis differentException = new ExceptionDiagnosis(throwable2);

        Assert.assertTrue(exceptionDiagnosis1.equals(sameException));
        Assert.assertFalse(exceptionDiagnosis1.equals(differentException));
    }

    @Test
    public void testExceptionDiagnosisHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        int expectedHashCode = calculateHashCode(exceptionDiagnosis);

        Assert.assertEquals(expectedHashCode, exceptionDiagnosis.hashCode());
    }

    @Test
    public void testExceptionDiagnosisToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + calculateStackLineHash(throwable.getStackTrace()) + ", lineNumberHash=" + calculateLineNumberHash(throwable.getStackTrace()) + '}';

        Assert.assertEquals(expectedToString, exceptionDiagnosis.toString());
    }

    private int calculateStackLineHash(StackTraceElement[] stackTrace) {
        int stackLineHash = 0;

        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement line = stackTrace[i];
            String className = line.getClassName();
            String methodName = line.getMethodName();
            int lineNumber = line.getLineNumber();

            int hash = className.hashCode() + methodName.hashCode();
            stackLineHash = 31 * stackLineHash + hash;
            stackLineHash = 31 * stackLineHash + lineNumber;
        }

        return stackLineHash;
    }

    private int calculateLineNumberHash(StackTraceElement[] stackTrace) {
        int lineNumberHash = 0;

        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement line = stackTrace[i];
            int lineNumber = line.getLineNumber();

            lineNumberHash = 31 * lineNumberHash + lineNumber;
        }

        return lineNumberHash;
    }

    private int calculateHashCode(ExceptionDiagnosis exceptionDiagnosis) {
        return exceptionDiagnosis.stackLineHash * 31 + exceptionDiagnosis.lineNumberHash;
    }

    @Test
    public void testExceptionDiagnosisWithCause() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        throwable2.initCause(throwable1);
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        Assert.assertNotNull(exceptionDiagnosis2.asHexString().split(" ")[2]);
        Assert.assertEquals(exceptionDiagnosis1.asHexString(), exceptionDiagnosis2.asHexString().split(" ")[2]);
    }
}