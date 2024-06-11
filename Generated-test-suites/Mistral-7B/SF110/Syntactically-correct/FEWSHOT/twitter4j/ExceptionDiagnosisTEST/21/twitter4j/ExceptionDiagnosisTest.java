package twitter4j;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals( Modifier.FINAL, Modifier.lookup(exceptionDiagnosis.getClass().getModifiers()) );
        assertEquals(453958937114285988L, exceptionDiagnosis.serialVersionUID, 0L);
        assertNull(exceptionDiagnosis.hexString);
        assertNull(exceptionDiagnosis.th.getCause());
        assertEquals(throwable.getClass(), exceptionDiagnosis.th.getClass());
        assertEquals(throwable.getMessage(), exceptionDiagnosis.th.getMessage());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "java.lang.RuntimeException";
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        assertNotNull(exceptionDiagnosis);
        assertEquals( Modifier.FINAL, Modifier.lookup(exceptionDiagnosis.getClass().getModifiers()) );
        assertEquals(453958937114285988L, exceptionDiagnosis.serialVersionUID, 0L);
        assertNull(exceptionDiagnosis.hexString);
        assertEquals(throwable.getClass(), exceptionDiagnosis.th.getClass());
        assertEquals(throwable.getMessage(), exceptionDiagnosis.th.getMessage());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith(filter)) {
                int expectedStackLineHash = 31 * 0 + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
                int expectedLineNumberHash = 31 * 0 + stackTraceElement.getLineNumber();
                int stackLineHash = exceptionDiagnosis.getStackLineHash();
                int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
                assertEquals(expectedStackLineHash, stackLineHash);
                assertEquals(expectedLineNumberHash, lineNumberHash);
                break;
            }
        }
    }

    @Test
    public void testHashCodeAndEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        ExceptionDiagnosis exceptionDiagnosis1Copy = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2Copy = new ExceptionDiagnosis(throwable2);

        assertEquals(exceptionDiagnosis1, exceptionDiagnosis1Copy);
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis1Copy.hashCode());

        assertNotEquals(exceptionDiagnosis1, exceptionDiagnosis2);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        StackTraceElement[] stackTrace1 = throwable1.getStackTrace();
        StackTraceElement[] stackTrace2 = throwable2.getStackTrace();

        for (StackTraceElement stackTraceElement1 : stackTrace1) {
            for (StackTraceElement stackTraceElement2 : stackTrace2) {
                if (stackTraceElement1.getClassName().equals(stackTraceElement2.getClassName())
                        && stackTraceElement1.getMethodName().equals(stackTraceElement2.getMethodName())
                        && stackTraceElement1.getLineNumber() == stackTraceElement2.getLineNumber()) {
                    int expectedStackLineHash1 = 31 * 0 + stackTraceElement1.getClassName().hashCode() + stackTraceElement1.getMethodName().hashCode();
                    int expectedLineNumberHash1 = 31 * 0 + stackTraceElement1.getLineNumber();
                    int stackLineHash1 = exceptionDiagnosis1.getStackLineHash();
                    int lineNumberHash1 = exceptionDiagnosis1.getLineNumberHash();
                    int stackLineHash2 = exceptionDiagnosis2.getStackLineHash();
                    int lineNumberHash2 = exceptionDiagnosis2.getLineNumberHash();
                    assertEquals(expectedStackLineHash1, stackLineHash1);
                    assertEquals(expectedLineNumberHash1, lineNumberHash1);
                    assertEquals(expectedStackLineHash1, stackLineHash2);
                    assertEquals(expectedLineNumberHash1, lineNumberHash2);
                    return;
                }
            }
        }

        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedToString = String.format("ExceptionDiagnosis{stackLineHash=%d, lineNumberHash=%d}", exceptionDiagnosis.getStackLineHash(), exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedToString, exceptionDiagnosis.toString());
    }

    @Test
    public void testHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedHexString = "";
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement line = stackTrace[i];
            String expectedClassName = line.getClassName();
            String expectedMethodName = line.getMethodName();
            int expectedLineNumber = line.getLineNumber();
            String expectedClassNameHex = toHexString(expectedClassName.hashCode());
            String expectedMethodNameHex = toHexString(expectedMethodName.hashCode());
            int expectedStackLineHash = 31 * (31 * expectedClassName.hashCode() + expectedMethodName.hashCode()) + expectedLineNumber;
            int expectedLineNumberHash = expectedLineNumber;

            String hexString = exceptionDiagnosis.asHexString();
            String[] hexStringParts = hexString.split("-");
            String stackLineHashHex = hexStringParts[0];
            String lineNumberHashHex = hexStringParts[1];

            String[] stackLineHashParts = stackLineHashHex.split(" ");
            int calculatedStackLineHash = Integer.parseInt(stackLineHashParts[1], 16) * 31 + Integer.parseInt(stackLineHashParts[2], 16);
            assertEquals(Integer.toHexString(expectedStackLineHash), stackLineHashHex);
            assertEquals(Integer.toHexString(expectedLineNumberHash), lineNumberHashHex);
            assertEquals(Integer.toHexString(expectedStackLineHash), stackLineHashParts[0]);
            break;
        }
    }

    @Test
    public void testHexStringWithCause() {
        Throwable throwable = new RuntimeException("Test Exception 1");
        Throwable cause = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(cause);
        String expectedHexString = "" + exceptionDiagnosis.getStackLineHash() + "-" + exceptionDiagnosis.getLineNumberHash() + " " + causeExceptionDiagnosis.asHexString();
        String hexString = exceptionDiagnosis.asHexString();
        assertEquals(expectedHexString, hexString);
    }

    private String toHexString(final int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
}