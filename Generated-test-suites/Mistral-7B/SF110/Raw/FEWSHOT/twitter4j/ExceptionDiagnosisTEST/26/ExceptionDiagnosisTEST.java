package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisWithEmptyFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=1811703123, lineNumberHash=-408268443}", diagnosis.toString());
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j.example.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(filter)) {
                int stackLineHash = 31 * 1811703123 + element.getClassName().hashCode() + element.getMethodName().hashCode();
                int lineNumberHash = 31 * 1811703123 + element.getLineNumber();
                String expectedHexString = toHexString(stackLineHash) + "-" + toHexString(lineNumberHash);
                assertEquals(expectedHexString, diagnosis.asHexString().substring(0, expectedHexString.length()));
                break;
            }
        }
    }

    @Test
    public void testExceptionDiagnosisWithCausedThrowable() {
        Throwable mainThrowable = new RuntimeException("Main Exception");
        Throwable causeThrowable = new IOException("Caused Exception");
        mainThrowable.initCause(causeThrowable);
        ExceptionDiagnosis mainDiagnosis = new ExceptionDiagnosis(mainThrowable);
        ExceptionDiagnosis causeDiagnosis = new ExceptionDiagnosis(causeThrowable, new String[]{"io.IOException"});
        String expectedHexString = mainDiagnosis.asHexString() + " " + causeDiagnosis.asHexString();
        assertEquals(expectedHexString, mainDiagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j.example.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(filter)) {
                int expectedHash = 31 * 1811703123 + element.getClassName().hashCode() + element.getMethodName().hashCode();
                assertEquals(expectedHash, diagnosis.getStackLineHash());
                break;
            }
        }
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j.example.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(filter)) {
                int expectedHash = 31 * 1811703123 + element.getLineNumber();
                assertEquals(expectedHash, diagnosis.getLineNumberHash());
                break;
            }
        }
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j.example.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(filter)) {
                int stackLineHash = 31 * 1811703123 + element.getClassName().hashCode() + element.getMethodName().hashCode();
                int lineNumberHash = 31 * 1811703123 + element.getLineNumber();
                String expectedHexString = toHexString(stackLineHash) + "-" + toHexString(lineNumberHash);
                String diagnosisHexString = diagnosis.asHexString();
                assertEquals(expectedHexString, diagnosisHexString.substring(0, expectedHexString.length()));
                break;
            }
        }
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(diagnosis1.equals(diagnosis2));

        Throwable throwable3 = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable3);
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable3);
        assertTrue(diagnosis3.equals(diagnosis4));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j.example.";
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable, new String[]{filter});
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, new String[]{filter});
        int expectedHash = 1811703123 * 31 + 123456789;
        assertEquals(expectedHash, diagnosis1.hashCode());
        assertEquals(expectedHash, diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j.example.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(filter)) {
                int stackLineHash = 31 * 1811703123 + element.getClassName().hashCode() + element.getMethodName().hashCode();
                int lineNumberHash = 31 * 1811703123 + element.getLineNumber();
                String expectedToString = "ExceptionDiagnosis{stackLineHash=" + stackLineHash + ", lineNumberHash=" + lineNumberHash + '}';
                assertEquals(expectedToString, diagnosis.toString());
                break;
            }
        }
    }

    @Test
    public void testNullDiagnosis() {
        assertNull(new ExceptionDiagnosis(null));
    }

    @Test
    public void testEmptyFilterArray() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{});
        assertEquals("ExceptionDiagnosis{stackLineHash=1811703123, lineNumberHash=-408268443}", diagnosis.toString());
    }

    private String toHexString(int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
}