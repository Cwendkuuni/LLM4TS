package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExceptionDiagnosisTest {

    private final String className;
    private final String methodName;
    private final int lineNumber;
    private final String expectedHexString;

    public ExceptionDiagnosisTest(String className, String methodName, int lineNumber, String expectedHexString) {
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.expectedHexString = expectedHexString;
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return new Iterable<Object[]>() {
            @Override
            public Iterator<Object[]> iterator() {
                return new Iterator<Object[]>() {
                    int i = 0;
                    final String[] testCases = new String[] {
                            "java.lang.RuntimeException", "test", 42, "0792d3bf-7f67",
                            "twitter4j.StatusDeletionNotSupportedException", "getStatus", 45, "010b0748-8a9d"
                    };
                    @Override
                    public boolean hasNext() {
                        return i < testCases.length;
                    }

                    @Override
                    public Object[] next() {
                        return new Object[] { testCases[i++].split("\\s+") };
                    }
                };
            }
        };
    }

    @Test
    public void testExceptionDiagnosisConstruction() {
        final Throwable th = new RuntimeException("Test exception");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());

        final StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace.length > 0) {
            final StackTraceElement line = stackTrace[stackTrace.length - 1];
            final ExceptionDiagnosis diagnosisWithFilter = new ExceptionDiagnosis(th, new String[] {className});
            assertEquals(className.hashCode() + methodName.hashCode(), diagnosisWithFilter.getStackLineHash());
            assertEquals(lineNumber, diagnosisWithFilter.getLineNumberHash());
            assertEquals(expectedHexString, diagnosisWithFilter.asHexString());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructionInvalidFilter() {
        final Throwable th = new RuntimeException("Test exception");
        new ExceptionDiagnosis(th, new String[] {"foo"});
    }

    @Test
    public void testGetStackLineHash() {
        final Throwable th = new RuntimeException("Test exception");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, new String[] {className});
        assertEquals(className.hashCode() + methodName.hashCode(), diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        final Throwable th = new RuntimeException("Test exception", new IOException());
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, new String[] {className});
        assertEquals(lineNumber, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        final Throwable th = new RuntimeException("Test exception", new IOException());
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, new String[] {className});
        assertEquals(expectedHexString, diagnosis.asHexString());
    }

    @Test
    public void testEquals() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UndeclaredThrowableException {
        final Throwable th = new RuntimeException("Test exception", new IOException());
        final ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th, new String[] {className});
        final ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th, new String[] {className});
        assertEquals(diagnosis1, diagnosis2);

        final ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th, new String[] {"foo"});
        assertNotEquals(diagnosis1, diagnosis3);

        final ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(th.getCause(), new String[] {className});
        assertNotEquals(diagnosis1, diagnosis4);

        final ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(new Exception("Test exception"), new String[] {className});
        assertNotEquals(diagnosis1, diagnosis5);
    }

    @Test
    public void testHashCode() {
        final Throwable th = new RuntimeException("Test exception");
        final ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(th, new String[] {className});
        final ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(th, new String[] {className});
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        final ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(th, new String[] {"foo"});
        assertNotEquals(diagnosis1.hashCode(), diagnosis3.hashCode());

        final ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(th.getCause(), new String[] {className});
        assertNotEquals(diagnosis1.hashCode(), diagnosis4.hashCode());

        final ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(new Exception("Test exception"), new String[] {className});
        assertNotEquals(diagnosis1.hashCode(), diagnosis5.hashCode());
    }

    @Test
    public void testToString() {
        final Throwable th = new RuntimeException("Test exception");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(th, new String[] {className});
        assertEquals("ExceptionDiagnosis{stackLineHash=" + className.hashCode() + methodName.hashCode() + ", lineNumberHash=" + lineNumber + "}", diagnosis.toString());
    }
}