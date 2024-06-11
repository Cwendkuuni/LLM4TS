package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testEmptyConstructor() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
        assertNull(exceptionDiagnosis.getTh());
        assertNull(exceptionDiagnosis.getHexString());
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis.getTh());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotEquals(0, exceptionDiagnosis.getStackLineHash());
        assertNotEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testConstructorWithThrowableAndEmptyFilter() {
        Throwable throwable = new RuntimeException();
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis.getTh());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotEquals(0, exceptionDiagnosis.getStackLineHash());
        assertNotEquals(0, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testConstructorWithThrowableAndNonEmptyFilter() {
        Throwable throwable = new RuntimeException();
        String[] inclusionFilter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(exceptionDiagnosis.getTh());
        assertNotNull(exceptionDiagnosis.getHexString());
        assertNotEquals(0, exceptionDiagnosis.getStackLineHash());
        assertNotEquals(0, exceptionDiagnosis.getLineNumberHash());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith("com.example")) {
                int hash = stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
                int expectedStackLineHash = 31 * 31 * this.stackLineHash(stackTraceElement) + hash;
                int actualStackLineHash = exceptionDiagnosis.getStackLineHash();
                assertEquals("Unexpected stack line hash", expectedStackLineHash, actualStackLineHash);

                int expectedLineNumberHash = 31 * 31 * stackTraceElement.getLineNumber() + this.lineNumberHash(stackTraceElement);
                int actualLineNumberHash = exceptionDiagnosis.getLineNumberHash();
                assertEquals("Unexpected line number hash", expectedLineNumberHash, actualLineNumberHash);
            }
        }
    }

    @Test
    public void testGetStackLineHash() {
        StackTraceElement stackTraceElement = new StackTraceElement("com.example", "method", "file:/path/to/file", 1, 10);
        int stackLineHash = this.stackLineHash(stackTraceElement);
        int expectedStackLineHash = 31 * 31 * stackLineHash(stackTraceElement.getClassName()) + stackLineHash(stackTraceElement.getMethodName());
        int actualStackLineHash = new ExceptionDiagnosis(null, new String[]{"com.example"}).getStackLineHash();
        assertEquals("Unexpected stack line hash", expectedStackLineHash, actualStackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        StackTraceElement stackTraceElement = new StackTraceElement("com.example", "method", "file:/path/to/file", 1, 10);
        int lineNumberHash = this.lineNumberHash(stackTraceElement);
        int expectedLineNumberHash = 31 * stackTraceElement.getLineNumber() + lineNumberHash("file:/path/to/file");
        int actualLineNumberHash = new ExceptionDiagnosis(null, new String[]{"com.example"}).getLineNumberHash();
        assertEquals("Unexpected line number hash", expectedLineNumberHash, actualLineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException();
        String[] inclusionFilter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String expectedHexString = this.toHexString(exceptionDiagnosis.getStackLineHash()) + "-" + this.toHexString(exceptionDiagnosis.getLineNumberHash());
        if (throwable.getCause() != null) {
            ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(throwable.getCause(), inclusionFilter);
            expectedHexString += " " + causeExceptionDiagnosis.asHexString();
        }
        String actualHexString = exceptionDiagnosis.asHexString();
        assertEquals("Unexpected hex string", expectedHexString, actualHexString);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException();
        String[] inclusionFilter = {"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + this.toHexString(exceptionDiagnosis.getStackLineHash()) + ", lineNumberHash=" + this.toHexString(exceptionDiagnosis.getLineNumberHash()) + "}";
        if (throwable.getCause() != null) {
            ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(throwable.getCause(), inclusionFilter);
            expectedString += " ExceptionDiagnosis{stackLineHash=" + this.toHexString(causeExceptionDiagnosis.getStackLineHash()) + ", lineNumberHash=" + this.toHexString(causeExceptionDiagnosis.getLineNumberHash()) + "}";
        }
        String actualString = exceptionDiagnosis.toString();
        assertEquals("Unexpected string representation", expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException();
        Throwable throwable2 = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, new String[0]);
        assertFalse("Unexpected equality", exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertFalse("Unexpected equality", exceptionDiagnosis1.equals(exceptionDiagnosis3));

        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertFalse("Unexpected equality", exceptionDiagnosis3.equals(exceptionDiagnosis4));

        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertTrue("Unexpected inequality", exceptionDiagnosis5.equals(exceptionDiagnosis6));

        ExceptionDiagnosis exceptionDiagnosis7 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        ExceptionDiagnosis exceptionDiagnosis8 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        Throwable causeThrowable1 = new RuntimeException();
        ExceptionDiagnosis causeExceptionDiagnosis1 = new ExceptionDiagnosis(causeThrowable1, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis9 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        exceptionDiagnosis9.setTh(causeThrowable1);
        assertTrue("Unexpected inequality", exceptionDiagnosis7.equals(exceptionDiagnosis9));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, new String[0]);
        assertNotEquals("Unexpected hash codes", exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        assertNotEquals("Unexpected hash codes", exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());

        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        assertEquals("Unexpected hash codes", exceptionDiagnosis4.hashCode(), exceptionDiagnosis5.hashCode());

        Throwable causeThrowable = new RuntimeException();
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(causeThrowable, new String[0]);
        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        exceptionDiagnosis6.setTh(causeThrowable);
        assertEquals("Unexpected hash codes", exceptionDiagnosis4.hashCode(), exceptionDiagnosis6.hashCode());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(exceptionDiagnosis);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ExceptionDiagnosis deserializedExceptionDiagnosis = (ExceptionDiagnosis) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals("Unexpected deserialized object", exceptionDiagnosis, deserializedExceptionDiagnosis);
    }

    private int stackLineHash(StackTraceElement stackTraceElement) {
        int hash = stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
        return 31 * hash;
    }

    private int lineNumberHash(StackTraceElement stackTraceElement) {
        return 31 * stackTraceElement.getLineNumber() + "file:/path/to/file".hashCode();
    }

    private String toHexString(int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
}