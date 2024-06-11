package twitter4j;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.hexString);
        assertNotNull(exceptionDiagnosis.th);
    }

    @Test
    public void testExceptionDiagnosisConstructorWithFilter() {
        Throwable throwable = new RuntimeException("Test message");
        String[] inclusionFilter = {"MyPackage"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.hexString);
        assertNotNull(exceptionDiagnosis.th);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement filterElement = stackTrace[0];
        String filterClassName = filterElement.getClassName();
        String filterMethodName = filterElement.getMethodName();

        assertTrue(filterClassName.startsWith(inclusionFilter[0]));
        assertTrue(filterDiagnosisMatches(exceptionDiagnosis, filterClassName, filterMethodName));
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedStackLineHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement line = stackTrace[i];
            expectedStackLineHash = 31 * expectedStackLineHash + line.getClassName().hashCode() + line.getMethodName().hashCode();
        }

        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedLineNumberHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement line = stackTrace[i];
            expectedLineNumberHash = 31 * expectedLineNumberHash + line.getLineNumber();
        }

        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int stackLineHash = 0;
        int lineNumberHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement line = stackTrace[i];
            String className = line.getClassName();
            String methodName = line.getMethodName();
            stackLineHash = 31 * stackLineHash + className.hashCode() + methodName.hashCode();
            lineNumberHash = 31 * lineNumberHash + line.getLineNumber();
        }

        String expectedHexString = toHexString(stackLineHash) + "-" + toHexString(lineNumberHash);
        if (throwable.getCause() != null) {
            ExceptionDiagnosis causeDiagnosis = new ExceptionDiagnosis(throwable.getCause());
            expectedHexString += " " + causeDiagnosis.asHexString();
        }

        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test message 1");
        Throwable throwable2 = new RuntimeException("Test message 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new RuntimeException("Test message 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"MyPackage"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"MyPackage"});

        assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        int expectedHashCode = 31 * (31 * (31 * (31 * 5 + "Twitter4j".hashCode()) + "ExceptionDiagnosis".hashCode()) + exceptionDiagnosis.stackLineHash) + exceptionDiagnosis.lineNumberHash;
        assertEquals(expectedHashCode, exceptionDiagnosis.hashCode());
    }

    private boolean filterDiagnosisMatches(ExceptionDiagnosis diagnosis, String className, String methodName) {
        StackTraceElement[] stackTrace = diagnosis.th.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().equals(className) && stackTraceElement.getMethodName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    private static String toHexString(int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        assertEquals(expectedString, exceptionDiagnosis.toString());
    }

    @Test
    public void testToStringWithCause() {
        Throwable throwable = new RuntimeException("Test message");
        Throwable cause = new IllegalArgumentException("Test cause");
        throwable.initCause(cause);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        String causeString = "ExceptionDiagnosis{stackLineHash=" + cause.getStackTrace()[0].getClassName().hashCode() + ":" + cause.getStackTrace()[0].getMethodName().hashCode() + ", lineNumberHash=" + cause.getStackTrace()[0].getLineNumber() + '}';
        expectedString += " " + causeString;
        assertEquals(expectedString, exceptionDiagnosis.toString());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(exceptionDiagnosis);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ExceptionDiagnosis deserializedExceptionDiagnosis = (ExceptionDiagnosis) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(exceptionDiagnosis.getStackLineHash(), deserializedExceptionDiagnosis.getStackLineHash());
        assertEquals(exceptionDiagnosis.getLineNumberHash(), deserializedExceptionDiagnosis.getLineNumberHash());
        assertEquals(exceptionDiagnosis.hexString, deserializedExceptionDiagnosis.hexString);
        assertEquals(exceptionDiagnosis.th.getClass(), deserializedExceptionDiagnosis.th.getClass());
        assertEquals(exceptionDiagnosis.th.getMessage(), deserializedExceptionDiagnosis.th.getMessage());
    }

    @Test
    public void testReflectionToString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        Method toHexStringMethod = ExceptionDiagnosis.class.getDeclaredMethod("toHexString", int.class);
        toHexStringMethod.setAccessible(true);

        String stackLineHashHexString = (String) toHexStringMethod.invoke(exceptionDiagnosis, exceptionDiagnosis.stackLineHash);
        String lineNumberHashHexString = (String) toHexStringMethod.invoke(exceptionDiagnosis, exceptionDiagnosis.lineNumberHash);

        assertEquals(stackLineHashHexString, exceptionDiagnosis.hexString.substring(0, stackLineHashHexString.length()));
        assertEquals(lineNumberHashHexString, exceptionDiagnosis.hexString.substring(exceptionDiagnosis.hexString.length() - lineNumberHashHexString.length()));
    }

    @Test
    public void testReflectionGetStackLineHash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        Method getStackLineHashMethod = ExceptionDiagnosis.class.getDeclaredMethod("getStackLineHash", (Class[]) null);
        getStackLineHashMethod.setAccessible(true);

        int stackLineHash = (int) getStackLineHashMethod.invoke(exceptionDiagnosis);
        assertEquals(exceptionDiagnosis.stackLineHash, stackLineHash);
    }

    @Test
    public void testReflectionGetLineNumberHash() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Throwable throwable = new RuntimeException("Test message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        Method getLineNumberHashMethod = ExceptionDiagnosis.class.getDeclaredMethod("getLineNumberHash", (Class[]) null);
        getLineNumberHashMethod.setAccessible(true);

        int lineNumberHash = (int) getLineNumberHashMethod.invoke(exceptionDiagnosis);
        assertEquals(exceptionDiagnosis.lineNumberHash, lineNumberHash);
    }
}