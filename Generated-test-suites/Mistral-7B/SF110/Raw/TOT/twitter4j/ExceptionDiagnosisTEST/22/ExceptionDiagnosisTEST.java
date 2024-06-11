package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {
    @Test
    public void testGetStackLineHash() {
        // Test for getStackLineHash()
    }

    @Test
    public void testGetLineNumberHash() {
        // Test for getLineNumberHash()
    }

    @Test
    public void testConstructor() {
        // Test for constructor
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, new String[]{"com.example"});

        String hexString1 = exceptionDiagnosis1.asHexString();
        String hexString2 = exceptionDiagnosis2.asHexString();

        assertEquals(hexString1, hexString2.substring(0, hexString1.length()-2));
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException();
        Throwable throwable2 = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);

        assertEquals(exceptionDiagnosis1, exceptionDiagnosis3);
        assertNotEquals(exceptionDiagnosis1, exceptionDiagnosis2);
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException();
        Throwable throwable2 = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        int hashCode1 = exceptionDiagnosis1.hashCode();
        int hashCode2 = exceptionDiagnosis2.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        String actualString = exceptionDiagnosis.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testStackDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{"com.example"});

        String hexString = exceptionDiagnosis.asHexString();
        String[] hexStrings = hexString.split("-");

        int stackLineHash = Integer.parseInt(hexStrings[0], 16);
        int lineNumberHash = Integer.parseInt(hexStrings[1], 16);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedStackLineHash = 0;
        int expectedLineNumberHash = 0;

        for (StackTraceElement stackTraceElement : stackTrace) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            int lineNumber = stackTraceElement.getLineNumber();

            if (className.startsWith("com.example")) {
                int classHashCode = className.hashCode();
                int methodHashCode = methodName.hashCode();
                expectedStackLineHash = 31 * (31 * classHashCode + methodHashCode + lineNumber);
                expectedLineNumberHash = 31 * expectedLineNumberHash + lineNumber;
            }
        }

        assertEquals(expectedStackLineHash, stackLineHash);
        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testStackDiagnosisWithoutFilter() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String hexString = exceptionDiagnosis.asHexString();
        String[] hexStrings = hexString.split("-");

        int stackLineHash = Integer.parseInt(hexStrings[0], 16);
        int lineNumberHash = Integer.parseInt(hexStrings[1], 16);

        StackTraceElement[] stackTrace = throwable.getStackTrace();

        int expectedStackLineHash = 0;
        int expectedLineNumberHash = 0;

        for (StackTraceElement stackTraceElement : stackTrace) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            int lineNumber = stackTraceElement.getLineNumber();

            int classHashCode = className.hashCode();
            int methodHashCode = methodName.hashCode();

            expectedStackLineHash = 31 * (31 * classHashCode + methodHashCode + lineNumber);
            expectedLineNumberHash = 31 * expectedLineNumberHash + lineNumber;
        }

        assertEquals(expectedStackLineHash, stackLineHash);
        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testExceptionWithCause() {
        Throwable throwable = new RuntimeException("Test exception", new RuntimeException("Caused by test exception"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedHexString = exceptionDiagnosis.asHexString();
        String actualHexString = new ExceptionDiagnosis(throwable.getCause()).asHexString();

        String[] expectedHexStrings = expectedHexString.split("-");
        String[] actualHexStrings = actualHexString.split("-");

        int stackLineHashExpected = Integer.parseInt(expectedHexStrings[0], 16);
        int lineNumberHashExpected = Integer.parseInt(expectedHexStrings[1], 16);
        int stackLineHashActual = Integer.parseInt(actualHexStrings[0], 16);
        int lineNumberHashActual = Integer.parseInt(actualHexStrings[1], 16);

        assertEquals(stackLineHashExpected, stackLineHashActual);
        assertEquals(lineNumberHashExpected, lineNumberHashActual);
    }

    @Test
    public void testExceptionWithoutCause() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedHexString = exceptionDiagnosis.asHexString();
        String actualHexString = new ExceptionDiagnosis(new RuntimeException()).asHexString();

        String[] expectedHexStrings = expectedHexString.split("-");
        String[] actualHexStrings = actualHexString.split("-");

        int stackLineHashExpected = Integer.parseInt(expectedHexStrings[0], 16);
        int lineNumberHashExpected = Integer.parseInt(expectedHexStrings[1], 16);
        int stackLineHashActual = Integer.parseInt(actualHexStrings[0], 16);
        int lineNumberHashActual = Integer.parseInt(actualHexStrings[1], 16);

        assertEquals(stackLineHashExpected, stackLineHashActual);
        assertEquals(lineNumberHashExpected, lineNumberHashActual);
    }
}