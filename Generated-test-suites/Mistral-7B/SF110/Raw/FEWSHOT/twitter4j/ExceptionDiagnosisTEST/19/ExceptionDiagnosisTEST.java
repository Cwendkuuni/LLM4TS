package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getMessage(), exceptionDiagnosis.th.getMessage());
        assertEquals(throwable.getClass(), exceptionDiagnosis.th.getClass());
        assertNull(exceptionDiagnosis.getClassNameFilter());
        assertNull(exceptionDiagnosis.getMethodNameFilter());
        assertEquals(exceptionDiagnosis.getStackLineHash(), getStackLineHash(throwable));
        assertEquals(exceptionDiagnosis.getLineNumberHash(), getLineNumber(throwable));
        assertEquals(exceptionDiagnosis.asHexString(), getHexString(exceptionDiagnosis));
    }

    @Test
    public void testExceptionDiagnosisConstructionWithThrowableAndFilter() {
        Throwable throwable = new RuntimeException("Test exception");
        String[] filter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        assertEquals(throwable.getMessage(), exceptionDiagnosis.th.getMessage());
        assertEquals(throwable.getClass(), exceptionDiagnosis.th.getClass());
        assertArrayEquals(filter, exceptionDiagnosis.getClassNameFilter());
        assertArrayEquals(new String[0], exceptionDiagnosis.getMethodNameFilter());
        assertEquals(exceptionDiagnosis.getStackLineHash(), getStackLineHash(throwable, filter));
        assertEquals(exceptionDiagnosis.getLineNumberHash(), getLineNumber(throwable, filter));
        assertEquals(exceptionDiagnosis.asHexString(), getHexString(exceptionDiagnosis));
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructionWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test exception");
        int expectedStackLineHash = getStackLineHash(throwable);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test exception");
        int expectedLineNumberHash = getLineNumber(throwable);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test exception");
        String expectedHexString = getHexString(throwable);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test exception 1");
        Throwable throwable2 = new RuntimeException("Test exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable3);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis3));

        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis4));

        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"com.example", "com.example2"});
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis5));

        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis6));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test exception 1");
        Throwable throwable2 = new RuntimeException("Test exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        Throwable throwable3 = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable3);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());

        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis4.hashCode());

        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"com.example2"});
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis5.hashCode());

        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis6.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedString = String.format("ExceptionDiagnosis{stackLineHash=%d, lineNumberHash=%d}",
                exceptionDiagnosis.getStackLineHash(), exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedString, exceptionDiagnosis.toString());
    }

    @Test
    public void testGetClassNameFilter() {
        Throwable throwable = new RuntimeException("Test exception");
        String[] filter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        assertArrayEquals(filter, exceptionDiagnosis.getClassNameFilter());
        assertNull(exceptionDiagnosis.getMethodNameFilter());
    }

    @Test
    public void testGetMethodNameFilter() {
        Throwable throwable = new RuntimeException("Test exception");
        String[] filter = {"com.example", "com.example.MyClass:methodName"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        assertArrayEquals(new String[0], exceptionDiagnosis.getClassNameFilter());
        assertEquals("methodName", exceptionDiagnosis.getMethodNameFilter()[0]);
    }

    @Test
    public void testGetCause() {
        Throwable throwable = new RuntimeException("Test exception", new IllegalStateException("Caused by illegal state"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNull(exceptionDiagnosis.getClassNameFilter());
        assertNull(exceptionDiagnosis.getMethodNameFilter());
        assertEquals(throwable.getMessage(), exceptionDiagnosis.th.getMessage());
        assertEquals(throwable.getClass(), exceptionDiagnosis.th.getClass());
        assertNull(exceptionDiagnosis.getCause());

        ExceptionDiagnosis exceptionDiagnosisCaused = new ExceptionDiagnosis(throwable.getCause());
        assertArrayEquals(filter, exceptionDiagnosisCaused.getClassNameFilter());
        assertArrayEquals(new String[0], exceptionDiagnosisCaused.getMethodNameFilter());
        assertEquals(throwable.getCause().getMessage(), exceptionDiagnosisCaused.th.getMessage());
        assertEquals(throwable.getCause().getClass(), exceptionDiagnosisCaused.th.getClass());
        assertNotNull(exceptionDiagnosisCaused.getCause());
    }

    @Test
    public void testGetCauseWithNullCause() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNull(exceptionDiagnosis.getCause());
    }

    @Test
    public void testGetCauseWithNestedException() {
        Throwable throwable = new RuntimeException("Test exception", new IOException("Caused by I/O error"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();
        String[] filters = hexString.split("-");
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(new IOException(filters[1]));
        assertEquals(exceptionDiagnosis.th.getClass(), throwable.getClass());
        assertEquals(causeExceptionDiagnosis.th.getClass(), IOException.class);
    }

    private int getStackLineHash(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int hash = 0;
        for (StackTraceElement traceElement : stackTrace) {
            String className = traceElement.getClassName();
            hash = 31 * hash + (className != null ? className.hashCode() : 0) + traceElement.getMethodName().hashCode();
        }
        return hash;
    }

    private int getLineNumber(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        return stackTraceElement.getLineNumber();
    }

    private String getHexString(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int stackLineHash = getStackLineHash(throwable);
        int lineNumberHash = stackTrace[stackTrace.length - 1].getLineNumber();
        return Integer.toHexString(stackLineHash).substring(2) + "-" + Integer.toHexString(lineNumberHash).substring(2);
    }

    private String[] getClassNameFilter(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int stackLineHash = getStackLineHash(throwable);
        String className;
        for (StackTraceElement traceElement : stackTrace) {
            className = traceElement.getClassName();
            if (className.startsWith("com.example")) {
                return new String[]{className};
            }
        }
        return new String[0];
    }

    private String[] getMethodNameFilter(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int stackLineHash = getStackLineHash(throwable);
        String className = stackTrace[stackTrace.length - 1].getClassName();
        String methodName;
        for (StackTraceElement traceElement : stackTrace) {
            className = traceElement.getClassName();
            if (className.startsWith("com.example")) {
                methodName = traceElement.getMethodName();
                return new String[]{methodName};
            }
        }
        throw new AssertionError("Couldn't find method name filter");
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (!expected.getClass().isArray() || !actual.getClass().isArray()) {
            super.assertArrayEquals(expected, actual);
            return;
        }

        if (expected.getClass().getComponentType() != actual.getClass().getComponentType()) {
            fail("Expected and actual arrays have different element types");
            return;
        }

        if (expected.length != actual.length) {
            fail("Expected and actual arrays have different lengths");
            return;
        }

        for (int i = 0; i < expected.length; ++i) {
            if (!expected[i].equals(actual[i])) {
                fail("Expected and actual arrays have different elements at index " + i);
                return;
            }
        }
    }
}