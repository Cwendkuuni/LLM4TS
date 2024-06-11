package twitter4j;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis original = new ExceptionDiagnosis(throwable);
        byte[] serialized = SerializationUtils.serialize(original);
        ExceptionDiagnosis deserialized = (ExceptionDiagnosis) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
    }

    @Test
    public void testConstructor() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals(throwable.getClass(), exceptionDiagnosis.getTh().getClass());
    }

    @Test
    public void testConstructorWithFilter() {
        Throwable throwable = new RuntimeException("Test exception");
        String[] filter = {"testPackage"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(exceptionDiagnosis);
        assertEquals(throwable.getClass(), exceptionDiagnosis.getTh().getClass());
        assertEquals(Arrays.asList(filter), exceptionDiagnosis.getInclusionFilter());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedHash = calculateStackLineHash(throwable, new String[]{"testPackage"});
        int actualHash = exceptionDiagnosis.getStackLineHash();
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedHash = calculateLineNumberHash(throwable, new String[]{"testPackage"});
        int actualHash = exceptionDiagnosis.getLineNumberHash();
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedHexString = calculateHexString(exceptionDiagnosis.getStackLineHash(), exceptionDiagnosis.getLineNumberHash());
        String actualHexString = exceptionDiagnosis.asHexString();
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test exception 1");
        Throwable throwable2 = new RuntimeException("Test exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test exception 1");
        Throwable throwable2 = new RuntimeException("Test exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        int expectedHash1 = calculateHashCode(exceptionDiagnosis1);
        int expectedHash2 = calculateHashCode(exceptionDiagnosis2);
        int actualHash1 = exceptionDiagnosis1.hashCode();
        int actualHash2 = exceptionDiagnosis2.hashCode();
        assertEquals(expectedHash1, actualHash1);
        assertEquals(expectedHash2, actualHash2);
        assertNotEquals(actualHash1, actualHash2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + calculateStackLineHash(throwable, new String[]{"testPackage"}) + ", lineNumberHash=" + calculateLineNumberHash(throwable, new String[]{"testPackage"}) + '}';
        String actualString = exceptionDiagnosis.toString();
        assertEquals(expectedString, actualString);
    }

    private int calculateHashCode(ExceptionDiagnosis exceptionDiagnosis) {
        int result = exceptionDiagnosis.getStackLineHash();
        result = 31 * result + exceptionDiagnosis.getLineNumberHash();
        return result;
    }

    private int calculateLineNumberHash(Throwable throwable, String[] filter) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int lineNumberHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            for (String filterString : filter) {
                if (stackTraceElement.getClassName().startsWith(filterString)) {
                    int classNameHash = stackTraceElement.getClassName().hashCode();
                    int methodNameHash = stackTraceElement.getMethodName().hashCode();
                    lineNumberHash = 31 * lineNumberHash + (classNameHash + methodNameHash);
                    break;
                }
            }
        }
        return lineNumberHash;
    }

    private int calculateStackLineHash(Throwable throwable, String[] filter) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int stackLineHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = stackTrace[i];
            for (String filterString : filter) {
                if (stackTraceElement.getClassName().startsWith(filterString)) {
                    int classNameHash = stackTraceElement.getClassName().hashCode();
                    int methodNameHash = stackTraceElement.getMethodName().hashCode();
                    stackLineHash = 31 * stackLineHash + (classNameHash + methodNameHash);
                    int lineNumber = stackTraceElement.getLineNumber();
                    stackLineHash = 31 * stackLineHash + lineNumber;
                    break;
                }
            }
        }
        return stackLineHash;
    }

    private String calculateHexString(int stackLineHash, int lineNumberHash) {
        String stackLineHexString = toHexString(stackLineHash);
        String lineNumberHexString = toHexString(lineNumberHash);
        return stackLineHexString + "-" + lineNumberHexString;
    }

    private String toHexString(int value) {
        String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
}