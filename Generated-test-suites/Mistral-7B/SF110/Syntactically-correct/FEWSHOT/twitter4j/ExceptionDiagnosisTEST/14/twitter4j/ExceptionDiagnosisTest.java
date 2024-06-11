package twitter4j;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        assertNotNull(ed);
        assertEquals(0, ed.getStackLineHash());
        assertEquals(0, ed.getLineNumberHash());
        assertEquals("", ed.asHexString());
        assertEquals(453958937114285988L, ed.serialVersionUID);
    }

    @Test
    public void testExceptionDiagnosisWithInclusionFilter() {
        Throwable throwable = new RuntimeException();
        String className = "org.apache.commons.cli.Utils";
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[]{className});
        assertNotNull(ed);
        assertFalse(ed.getHexString().isEmpty());
        String hexString = ed.getHexString();
        int stackLineHash = ed.getStackLineHash();
        int lineNumberHash = ed.getLineNumberHash();

        int indexOfDash = hexString.indexOf("-");
        String stackTraceHex = hexString.substring(0, indexOfDash);
        String lineNumberHex = hexString.substring(indexOfDash + 1);

        int stackLineHashExpected = 0;
        int lineNumberHashExpected = 0;
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement line : stackTrace) {
            if (line.getClassName().startsWith(className)) {
                stackLineHashExpected = 31 * stackLineHashExpected + line.getClassName().hashCode() + line.getMethodName().hashCode();
                lineNumberHashExpected = 31 * lineNumberHashExpected + line.getLineNumber();
                break;
            }
        }
        assertEquals(stackLineHashExpected, stackLineHash);
        assertEquals(lineNumberHashExpected, lineNumberHash);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        String toString = ed.toString();
        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException();
        Throwable throwable2 = new RuntimeException();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(throwable2);
        assertFalse(ed1.equals(ed2));

        Throwable throwable3 = new RuntimeException();
        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(throwable3, new String[]{"org.apache.commons.cli.Utils"});
        ExceptionDiagnosis ed4 = new ExceptionDiagnosis(throwable3, new String[]{"org.apache.commons.cli.Utils"});
        assertTrue(ed3.equals(ed4));

        ExceptionDiagnosis ed5 = new ExceptionDiagnosis(ed3);
        assertTrue(ed3.equals(ed5));

        ExceptionDiagnosis ed6 = new ExceptionDiagnosis(ed3);
        ed6.lineNumberHash = ed5.lineNumberHash + 1;
        assertFalse(ed3.equals(ed6));

        ExceptionDiagnosis ed7 = new ExceptionDiagnosis(ed5);
        ed7.stackLineHash = ed5.stackLineHash + 1;
        assertFalse(ed5.equals(ed7));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(throwable);
        assertEquals(ed1.hashCode(), ed2.hashCode());

        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(throwable, new String[]{"org.apache.commons.cli.Utils"});
        ExceptionDiagnosis ed4 = new ExceptionDiagnosis(throwable, new String[]{"org.apache.commons.cli.Utils"});
        assertEquals(ed3.hashCode(), ed4.hashCode());

        ExceptionDiagnosis ed5 = new ExceptionDiagnosis(ed3);
        assertEquals(ed3.hashCode(), ed5.hashCode());

        ExceptionDiagnosis ed6 = new ExceptionDiagnosis(ed5);
        ed6.lineNumberHash = ed5.lineNumberHash + 1;
        assertNotEquals(ed5.hashCode(), ed6.hashCode());

        ExceptionDiagnosis ed7 = new ExceptionDiagnosis(ed5);
        ed7.stackLineHash = ed5.stackLineHash + 1;
        assertNotEquals(ed5.hashCode(), ed7.hashCode());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(ed);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        ExceptionDiagnosis ed2 = (ExceptionDiagnosis) objectInputStream.readObject();
        assertNotNull(ed2);
        assertEquals(ed.getStackLineHash(), ed2.getStackLineHash());
        assertEquals(ed.getLineNumberHash(), ed2.getLineNumberHash());
        assertEquals(ed.getHexString(), ed2.getHexString());
    }

    @Test
    public void testCause() {
        Throwable throwable = new RuntimeException("Test message", new IOException("Caused by IOException"));
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        assertEquals(ed.getHexString().split(" ")[0], throwable.getClass().getName() + "-" + throwable.getStackTrace()[0].getLineNumber());
        ExceptionDiagnosis causeEd = new ExceptionDiagnosis(throwable.getCause());
        assertEquals(ed.getHexString() + " " + causeEd.asHexString(), throwable.getClass().getName() + "-" + throwable.getStackTrace()[0].getLineNumber() + " " + causeEd.asHexString());
    }

    @Test(expected = UndeclaredThrowableException.class)
    public void testCauseThrowsUndeclaredThrowableException() {
        Throwable throwable = new RuntimeException("Test message", new IOException("Caused by IOException"));
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis causeEd = new ExceptionDiagnosis(throwable.getCause());
        ExceptionDiagnosis edWithCause = new ExceptionDiagnosis(throwable, new String[]{"twitter4j.ExceptionDiagnosis"});
        edWithCause.setCause(causeEd);
        ExceptionDiagnosis edFromSetCause = new ExceptionDiagnosis(edWithCause);
        assertEquals(edFromSetCause.getHexString(), ed.getHexString() + " " + causeEd.asHexString());
    }
}