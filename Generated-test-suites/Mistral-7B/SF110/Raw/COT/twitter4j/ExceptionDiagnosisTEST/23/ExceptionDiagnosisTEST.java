package twitter4j.test;

import static org.junit.Assert.*;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
        assertNull(diagnosis.getHexString());
        assertNull(diagnosis.getTh().getCause());
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        assertEquals(stackTrace.length, diagnosis.getStackTraceElements().length);
        StackTraceElement expected = stackTrace[0];
        StackTraceElement actual = diagnosis.getStackTraceElements()[0];
        assertEquals(expected.getClassName(), actual.getClassName());
        assertEquals(expected.getMethodName(), actual.getMethodName());
        assertEquals(expected.getLineNumber(), actual.getLineNumber());
    }

    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable throwable = new TwitterException("Test error message");
        String[] filter = new String[]{"twitter4j"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(diagnosis);
        String hexString = diagnosis.asHexString();
        assertTrue(hexString.contains("-"));
        String[] parts = hexString.split("-");
        int stackLineHash = Integer.parseInt(parts[0], 16);
        int lineNumberHash = Integer.parseInt(parts[1], 16);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("twitter4j")) {
                assertEquals(element.getClassName(), parts[2]);
                assertEquals(element.getMethodName(), parts[3]);
                assertEquals(element.getLineNumber(), Integer.parseInt(parts[4], 16));
                break;
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = diagnosis.getStackLineHash();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
        int expectedHash = lastElement.getClassName().hashCode() + lastElement.getMethodName().hashCode();
        assertEquals(expectedHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = diagnosis.getLineNumberHash();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
        int expectedHash = lastElement.getLineNumber();
        assertEquals(expectedHash, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String hexString = diagnosis.asHexString();
        assertTrue(hexString.contains("-"));
        String[] parts = hexString.split("-");
        int stackLineHash = Integer.parseInt(parts[0], 16);
        int lineNumberHash = Integer.parseInt(parts[1], 16);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
        int expectedHash = lastElement.getClassName().hashCode() + lastElement.getMethodName().hashCode() + lastElement.getLineNumber();
        assertEquals(Integer.toHexString(expectedHash).substring(0, 8), parts[0]);
        assertEquals(Integer.toHexString(expectedHash).substring(8, 16), parts[1]);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new TwitterException("Test error message 1");
        Throwable throwable2 = new TwitterException("Test error message 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(diagnosis1.equals(diagnosis2));

        Throwable throwable3 = new TwitterException("Test error message 1");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"twitter4j"});
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"twitter4j"});
        assertTrue(diagnosis3.equals(diagnosis4));
        assertFalse(diagnosis1.equals(diagnosis3));

        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(diagnosis3);
        assertTrue(diagnosis5.equals(diagnosis3));
        assertTrue(diagnosis5.equals(diagnosis4));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new TwitterException("Test error message 1");
        Throwable throwable2 = new TwitterException("Test error message 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(diagnosis1.equals(diagnosis2));
        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        Throwable throwable3 = new TwitterException("Test error message 1");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"twitter4j"});
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"twitter4j"});
        assertTrue(diagnosis3.equals(diagnosis4));
        assertEquals(diagnosis3.hashCode(), diagnosis4.hashCode());

        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(diagnosis3);
        assertTrue(diagnosis5.equals(diagnosis3));
        assertEquals(diagnosis5.hashCode(), diagnosis3.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
        assertEquals(expected, diagnosis.toString());
    }

    @Test
    public void testGetStackTraceElements() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        StackTraceElement[] expected = throwable.getStackTrace();
        StackTraceElement[] actual = diagnosis.getStackTraceElements();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetThCause() {
        Throwable throwable = new TwitterException("Test error message");
        throwable.initCause(new IOException("Test cause message"));
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        Throwable cause = diagnosis.getTh().getCause();
        assertEquals(cause.getClass(), IOException.class);
        assertEquals(cause.getMessage(), "Test cause message");
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetStacktraceElementWithoutFilter() {
        Throwable throwable = new TwitterException("Test error message");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        diagnosis.getStackTraceElements()[0];
    }

    @Test
    public void testGetStacktraceElementWithFilter() {
        Throwable throwable = new TwitterException("Test error message");
        String[] filter = new String[]{"twitter4j"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement expected = null;
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("twitter4j")) {
                expected = element;
                break;
            }
        }
        StackTraceElement actual = diagnosis.getStackTraceElements()[0];
        assertEquals(expected.getClassName(), actual.getClassName());
        assertEquals(expected.getMethodName(), actual.getMethodName());
        assertEquals(expected.getLineNumber(), actual.getLineNumber());
    }
}