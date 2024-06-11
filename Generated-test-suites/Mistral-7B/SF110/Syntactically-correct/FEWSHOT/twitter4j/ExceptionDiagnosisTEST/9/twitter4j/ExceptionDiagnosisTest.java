package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(Modifier.FINAL, Modifier.getClassModifiers(ExceptionDiagnosis.class), Modifier.FINAL);
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotNull(exceptionDiagnosis.getTh());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndInclusionFilter() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"java.io."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals(Modifier.FINAL, Modifier.getClassModifiers(ExceptionDiagnosis.class), Modifier.FINAL);
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotNull(exceptionDiagnosis.getTh());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        assertEquals(className.startsWith("java.io."), true);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int expectedStackLineHash = 0;
        for (int i = throwable.getStackTrace().length - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
            String className = stackTraceElement.getClassName();
            assertEquals(className.hashCode(), stackLineHash >>> 24);
            String methodName = stackTraceElement.getMethodName();
            assertEquals(methodName.hashCode(), (stackLineHash >>> 16) & 0xFFFF);
            int lineNumber = stackTraceElement.getLineNumber();
            assertEquals(lineNumber, stackLineHash & 0xFFFF);
            stackLineHash = 31 * stackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode() + lineNumber;
            expectedStackLineHash = 31 * expectedStackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode() + lineNumber;
        }
        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        int expectedLineNumberHash = 0;
        for (int i = throwable.getStackTrace().length - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = throwable.getStackTrace()[i];
            int lineNumber = stackTraceElement.getLineNumber();
            expectedLineNumberHash = 31 * expectedLineNumberHash + lineNumber;
        }
        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        String expectedHexString = toHexString(stackLineHash) + "-" + toHexString(lineNumberHash);
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
        exceptionDiagnosis1.th = new RuntimeException("Test RuntimeException");
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
        exceptionDiagnosis2.th = new RuntimeException("Test RuntimeException");
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ExceptionDiagnosis{stackLineHash=");
        stringBuilder.append(exceptionDiagnosis.getStackLineHash());
        stringBuilder.append(", lineNumberHash=");
        stringBuilder.append(exceptionDiagnosis.getLineNumberHash());
        stringBuilder.append('}');
        assertEquals(stringBuilder.toString(), exceptionDiagnosis.toString());
    }

    @Test
    public void testGetTh() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable, exceptionDiagnosis.getTh());
    }

    @Test
    public void testCause() {
        Throwable throwable = new IOException("Test IOException");
        Throwable cause = new RuntimeException("Test RuntimeException");
        throwable.initCause(cause);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis.getTh().getCause());
        String hexString = exceptionDiagnosis.asHexString();
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        String expectedHexString = toHexString(stackLineHash) + "-" + toHexString(lineNumberHash) + " " + new ExceptionDiagnosis(cause).asHexString();
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));

        String[] inclusionFilter = new String[]{"java.io."};
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        assertTrue(exceptionDiagnosis4.equals(exceptionDiagnosis5));
    }

    private String toHexString(int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
}