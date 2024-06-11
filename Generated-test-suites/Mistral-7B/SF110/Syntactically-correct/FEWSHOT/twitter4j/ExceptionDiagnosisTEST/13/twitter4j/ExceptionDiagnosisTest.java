package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals(Modifier.FINAL, Modifier.isFinal(exceptionDiagnosis.getClass().getDeclaredClass()));
        assertNull(exceptionDiagnosis.th.getCause());
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
    }

    @Test
    public void testConstructorWithThrowableAndEmptyFilter() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(Modifier.FINAL, Modifier.isFinal(exceptionDiagnosis.getClass().getDeclaredClass()));
        assertNull(exceptionDiagnosis.th.getCause());
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());
    }

    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = {"java.io."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(Modifier.FINAL, Modifier.isFinal(exceptionDiagnosis.getClass().getDeclaredClass()));
        assertNull(exceptionDiagnosis.th.getCause());
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        assertTrue(className.startsWith(inclusionFilter[0]));
        assertEquals("ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}', exceptionDiagnosis.toString());
    }

    @Test
    public void testConstructorWithThrowableAndFilterWithCause() {
        Throwable throwable = new IOException("Test IOException");
        Throwable cause = new NullPointerException("Test NullPointerException");
        throwable.initCause(cause);
        String[] inclusionFilter = {"java.io."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(Modifier.FINAL, Modifier.isFinal(exceptionDiagnosis.getClass().getDeclaredClass()));
        assertNull(exceptionDiagnosis.th.getCause());
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        assertTrue(className.startsWith(inclusionFilter[0]));
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(cause, inclusionFilter);
        String hexString = exceptionDiagnosis.asHexString();
        assertTrue(hexString.contains(causeExceptionDiagnosis.asHexString()));
        assertEquals("ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}', exceptionDiagnosis.toString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IOException("Test IOException");
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        String methodName = stackTrace[0].getMethodName();
        String[] inclusionFilter = {"java.io." + className.substring(className.lastIndexOf("."))};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        int expectedStackLineHash = 31 * (31 * hashCode(className.hashCode()) + hashCode(methodName.hashCode())) + stackTrace[0].getLineNumber();
        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IOException("Test IOException");
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        String[] inclusionFilter = {"java.io." + stackTraceElement.getClassName().substring(stackTraceElement.getClassName().lastIndexOf("."))};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(stackTraceElement.getLineNumber(), lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = {"java.io."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String hexString = exceptionDiagnosis.asHexString();
        String expectedHexString = exceptionDiagnosis.toHexString(exceptionDiagnosis.getStackLineHash()) + "-" + exceptionDiagnosis.toHexString(exceptionDiagnosis.getLineNumberHash());
        assertTrue(hexString.contains(expectedHexString));
    }

    @Test
    public void testEquals() {
        Throwable throwable = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new IOException("Test IOException 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable3, new String[0]);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));

        Throwable throwable4 = new IOException("Test IOException 1");
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable4, new String[]{"java.io."});
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis4));

        Throwable throwable5 = new IOException("Test IOException 1");
        StackTraceElement stackTraceElement = throwable5.getStackTrace()[0];
        String[] inclusionFilter = {"java.io." + stackTraceElement.getClassName().substring(stackTraceElement.getClassName().lastIndexOf("."))};
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable5, inclusionFilter);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis5));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = {"java.io."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int expectedHashCode = 31 * (31 * exceptionDiagnosis.getStackLineHash() + exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHashCode, exceptionDiagnosis.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = {"java.io."};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        assertEquals(expectedToString, exceptionDiagnosis.toString());
    }

    private int hashCode(int value) {
        return value;
    }
}