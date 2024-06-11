package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisWithNullThrowable() {
        assertNull(new ExceptionDiagnosis(null));
    }

    @Test
    public void testExceptionDiagnosisWithEmptyInclusionFilter() {
        Throwable throwable = new IOException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithClassFilter() {
        Throwable throwable = new IOException("Test Exception", new IllegalArgumentException("Test Argument"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{"java.io."});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
        int expectedHash = lastElement.getClassName().hashCode() + lastElement.getMethodName().hashCode();
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(expectedHash, stackLineHash);
        assertEquals(lastElement.getLineNumber(), lineNumberHash);
        assertEquals(exceptionDiagnosis.asHexString(), toHexString(stackLineHash) + "-" + lineNumberHash);
    }

    @Test
    public void testExceptionDiagnosisWithMultipleMatches() {
        Throwable throwable = new IOException("Test Exception", new IllegalArgumentException("Test Argument"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "test"});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedHash = stackTrace[stackTrace.length - 1].getClassName().hashCode() + stackTrace[stackTrace.length - 1].getMethodName().hashCode();
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(expectedHash, stackLineHash);
        assertEquals(stackTrace[stackTrace.length - 1].getLineNumber(), lineNumberHash);
        assertEquals(exceptionDiagnosis.asHexString(), toHexString(stackLineHash) + "-" + lineNumberHash);
    }

    @Test
    public void testExceptionDiagnosisWithRecursion() {
        Throwable throwable = new IOException("Test Exception 1", new IOException("Test Exception 2", new IOException("Test Exception 3")));
        ExceptionDiagnosis parentDiagnosis = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis childDiagnosis = new ExceptionDiagnosis(throwable.getCause());
        String parentHexString = parentDiagnosis.asHexString();
        String childHexString = childDiagnosis.asHexString();
        int parentStackLineHash = parentDiagnosis.getStackLineHash();
        int parentLineNumberHash = parentDiagnosis.getLineNumberHash();
        int childStackLineHash = childDiagnosis.getStackLineHash();
        int childLineNumberHash = childDiagnosis.getLineNumberHash();
        assertEquals(parentHexString, toHexString(parentStackLineHash) + "-" + parentLineNumberHash + " " + childHexString);
        assertNotSame(parentDiagnosis, childDiagnosis);
    }

    @Test
    public void testExceptionDiagnosisWithNullCause() {
        Throwable throwable = new IOException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNull(exceptionDiagnosis.asHexString().split(" ")[2]);
    }

    private String toHexString(int value) {
        return "0000000" + Integer.toHexString(value).substring(0, 8);
    }
}