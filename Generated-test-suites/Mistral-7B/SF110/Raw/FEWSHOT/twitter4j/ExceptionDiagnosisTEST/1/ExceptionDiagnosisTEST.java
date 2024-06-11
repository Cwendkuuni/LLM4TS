package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        final Throwable throwable = mock(Throwable.class);
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNull(exceptionDiagnosis.getClassNameFilter());
        assertEquals(throwable, exceptionDiagnosis.getThrownException());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        final Throwable throwable = mock(Throwable.class);
        final String[] inclusionFilter = new String[]{"com.example"};
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals(inclusionFilter, exceptionDiagnosis.getClassNameFilter());
        assertEquals(throwable, exceptionDiagnosis.getThrownException());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testGetStackLineHash() {
        final Throwable throwable = mock(Throwable.class);
        final StackTraceElement stackTraceElement = mock(StackTraceElement.class);
        when(throwable.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement});
        final String className = "com.example";
        when(stackTraceElement.getClassName()).thenReturn(className);
        final String methodName = "testMethod";
        when(stackTraceElement.getMethodName()).thenReturn(methodName);

        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        final int stackLineHash = exceptionDiagnosis.getStackLineHash();
        final int expectedStackLineHash = 31 * (31 * className.hashCode() + methodName.hashCode()) + stackTraceElement.getLineNumber();
        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        final Throwable throwable = mock(Throwable.class);
        final StackTraceElement stackTraceElement = mock(StackTraceElement.class);
        when(throwable.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement});
        final int lineNumber = 123;

        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        final int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(lineNumber, stackTraceElement.getLineNumber());
        assertEquals(lineNumberHash, 31 * lineNumber);
    }

    @Test
    public void testAsHexString() {
        final Throwable throwable = mock(Throwable.class);
        final String[] inclusionFilter = new String[]{"com.example"};
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        final String expectedHexString = "0000000" + Integer.toHexString(exceptionDiagnosis.getStackLineHash()) + "-"
                + Integer.toHexString(exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testToString() {
        final Throwable throwable = mock(Throwable.class);
        final StackTraceElement stackTraceElement = mock(StackTraceElement.class);
        when(throwable.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement});
        final String className = "com.example";
        when(stackTraceElement.getClassName()).thenReturn(className);
        final String methodName = "testMethod";
        when(stackTraceElement.getMethodName()).thenReturn(methodName);

        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        final String expectedToString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash()
                + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        assertEquals(expectedToString, exceptionDiagnosis.toString());
    }

    @Test
    public void testEquals() {
        final Throwable throwable1 = mock(Throwable.class);
        final Throwable throwable2 = mock(Throwable.class);
        final StackTraceElement stackTraceElement1 = mock(StackTraceElement.class);
        final StackTraceElement stackTraceElement2 = mock(StackTraceElement.class);
        when(throwable1.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement1});
        when(throwable2.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement2});
        when(stackTraceElement1.getClassName()).thenReturn("com.example1");
        when(stackTraceElement2.getClassName()).thenReturn("com.example2");

        final ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
        final ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);

        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        final ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"com.example1"});
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));

        final ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"com.example2"});
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis4));

        final ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis5));

        final ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable1, new String[]{"com.example1"});
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis6));
    }

    @Test
    public void testHashCode() {
        final Throwable throwable = mock(Throwable.class);
        final StackTraceElement stackTraceElement = mock(StackTraceElement.class);
        when(throwable.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement});
        final String className = "com.example";
        when(stackTraceElement.getClassName()).thenReturn(className);
        final String methodName = "testMethod";
        when(stackTraceElement.getMethodName()).thenReturn(methodName);

        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        final int expectedHashCode = 31 * (31 * className.hashCode() + methodName.hashCode()) + stackTraceElement.getLineNumber();
        assertEquals(expectedHashCode, exceptionDiagnosis.hashCode());
    }

    @Test
    public void testGetThrownException() {
        final Throwable throwable = mock(Throwable.class);
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertEquals(throwable, exceptionDiagnosis.getThrownException());
    }

    @Test
    public void testGetClassNameFilter() {
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable(), new String[0]);
        assertNull(exceptionDiagnosis.getClassNameFilter());
    }

    @Test
    public void testGetClassNameFilterWithFilter() {
        final Throwable throwable = mock(Throwable.class);
        final String[] inclusionFilter = new String[]{"com.example"};
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals(inclusionFilter, exceptionDiagnosis.getClassNameFilter());
    }

    @Test
    public void testToHexString() {
        final int value = 123456789;
        final String expectedHexString = "2a30313233343536373839";
        assertEquals(expectedHexString, ExceptionDiagnosis.toHexString(value));
    }

    @Test
    public void testGetCause() {
        final Throwable throwable = mock(Throwable.class);
        final Throwable cause = mock(Throwable.class);
        when(throwable.getCause()).thenReturn(cause);
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        final ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(cause, new String[0]);
        final String expectedHexString = ExceptionDiagnosis.toHexString(throwable.getStackTrace()[0].getHashCode()) + "-"
                + Integer.toHexString(throwable.getStackTrace()[0].getLineNumber());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
        assertEquals(cause, exceptionDiagnosis.getThrownException().getCause());
        assertEquals(causeExceptionDiagnosis, exceptionDiagnosis.getThrownException());
    }

    @Test
    public void testAccessPrivateFields() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Throwable throwable = mock(Throwable.class);
        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);

        final Field stackLineHashField = ExceptionDiagnosis.class.getDeclaredField("stackLineHash");
        stackLineHashField.setAccessible(true);
        assertEquals(exceptionDiagnosis.getStackLineHash(), stackLineHashField.get(exceptionDiagnosis));

        final Field lineNumberHashField = ExceptionDiagnosis.class.getDeclaredField("lineNumberHash");
        lineNumberHashField.setAccessible(true);
        assertEquals(exceptionDiagnosis.getLineNumberHash(), lineNumberHashField.get(exceptionDiagnosis));

        final Field hexStringField = ExceptionDiagnosis.class.getDeclaredField("hexString");
        hexStringField.setAccessible(true);
        assertEquals(exceptionDiagnosis.asHexString(), hexStringField.get(exceptionDiagnosis));

        final Field thField = ExceptionDiagnosis.class.getDeclaredField("th");
        thField.setAccessible(true);
        assertEquals(exceptionDiagnosis.getThrownException(), thField.get(exceptionDiagnosis));
    }

    @Test
    public void testPrintStackTrace() {
        final Throwable throwable = new Exception("Test Exception");
        final PrintWriter printWriter = mock(PrintWriter.class);
        final StringWriter stringWriter = new StringWriter(printWriter);

        throwable.printStackTrace(printWriter);
        final String expectedStackTrace = stringWriter.toString();
        final StackTraceElement[] stackTrace = throwable.getStackTrace();
        final String expectedStackTraceLine = stackTrace[stackTrace.length - 1].toString();

        final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        exceptionDiagnosis.printStackTrace(new PrintWriter(System.out));

        final String actualStackTrace = System.out.getBuffer().toString();
        assertEquals(expectedStackTrace, actualStackTrace);
        assertTrue(actualStackTrace.endsWith(expectedStackTraceLine));
    }
}