package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getClass().getName(), ed.th.getClass().getName());
        assertNotNull(ed.getStackLineHash());
        assertNotNull(ed.getLineNumberHash());
        assertNotNull(ed.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new IOException("Test exception");
        String filter = "java.io.";
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[] { filter });
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            String className = stackTrace[i].getClassName();
            if (className.startsWith(filter)) {
                int expectedHash = 31 * "java.io.IOException".hashCode() + stackTrace[i].getMethodName().hashCode();
                int stackLineHash = ed.getStackLineHash();
                int lineNumberHash = ed.getLineNumberHash();
                int hash = 31 * stackLineHash + lineNumberHash;
                assertEquals(Integer.toHexString(expectedHash).substring(0, 8), ed.toHexString(stackLineHash));
                assertEquals(Integer.toHexString(expectedHash).substring(8, 16), ed.toHexString(lineNumberHash));
                assertEquals(Integer.toHexString(hash).substring(0, 16), ed.asHexString());
                break;
            }
        }
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(ed);
        byte[] bytes = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        ExceptionDiagnosis edDeserialized = (ExceptionDiagnosis) ois.readObject();
        assertEquals(ed.getStackLineHash(), edDeserialized.getStackLineHash());
        assertEquals(ed.getLineNumberHash(), edDeserialized.getLineNumberHash());
        assertEquals(ed.asHexString(), edDeserialized.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new ExceptionDiagnosis(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFilter() {
        Throwable throwable = new IOException("Test exception");
        new ExceptionDiagnosis(throwable, null);
    }

    @Test
    public void testGetters() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getStackTrace(), ed.th.getStackTrace());
        assertNotNull(ed.getStackLineHash());
        assertNotNull(ed.getLineNumberHash());
        assertNotNull(ed.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new IOException("Test exception 1");
        Throwable throwable2 = new IOException("Test exception 2");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(throwable2);
        assertFalse(ed1.equals(ed2));

        Throwable throwable3 = new IOException("Test exception 3");
        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(throwable1, new String[] { "java.io." });
        ExceptionDiagnosis ed4 = new ExceptionDiagnosis(throwable3, new String[] { "java.io." });
        assertTrue(ed3.equals(ed4));

        ExceptionDiagnosis ed5 = new ExceptionDiagnosis(ed3);
        assertTrue(ed3.equals(ed5));
        assertTrue(ed5.equals(ed3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(throwable);
        assertEquals(ed1.hashCode(), ed2.hashCode());

        int expectedHash = 31 * "java.io.IOException".hashCode() + "getStackTrace".hashCode();
        StackTraceElement stackTraceElement = new ExceptionInInitializerError("Test exception").getStackTrace()[0];
        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(new ExceptionInInitializerError("Test exception"), new String[] { stackTraceElement.getClassName() });
        int stackLineHash = ed3.getStackLineHash();
        int lineNumberHash = ed3.getLineNumberHash();
        int hash = 31 * stackLineHash + lineNumberHash;
        int ed3HashCode = ed3.hashCode();
        assertEquals(Integer.toHexString(expectedHash).substring(0, 8), ed3.toHexString(stackLineHash));
        assertEquals(Integer.toHexString(expectedHash).substring(8, 16), ed3.toHexString(lineNumberHash));
        assertEquals(Integer.toHexString(hash).substring(0, 16), Integer.toHexString(ed3HashCode));
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + '}';
        assertEquals(expected, ed.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFilterLength() {
        Throwable throwable = new IOException("Test exception");
        String filter = "java.io.";
        new ExceptionDiagnosis(throwable, new String[] { filter, "" });
    }

    @Test
    public void testToStringWithCause() {
        Throwable throwable = new IOException("Test exception");
        IOException cause = new IOException("Test cause exception");
        throwable.initCause(cause);
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + " " + cause.getClass().getName() + "@" + cause.toString() + '}';
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithNullCause() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + '}';
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithFilter() {
        Throwable throwable = new IOException("Test exception");
        String filter = "java.io.";
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[] { filter });
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + "}";
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithFilterAndCause() {
        Throwable throwable = new IOException("Test exception");
        IOException cause = new IOException("Test cause exception");
        throwable.initCause(cause);
        String filter = "java.io.";
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[] { filter });
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + " " + cause.getClass().getName() + "@" + cause.toString() + "}";
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithFilterAndNullCause() {
        Throwable throwable = new IOException("Test exception");
        String filter = "java.io.";
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[] { filter });
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + "}";
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithEmptyFilter() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[0]);
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + '}';
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithFilterAndEmptyCause() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[] { "java.io." });
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + "}";
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithFilterAndMultipleCauses() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Throwable throwable = new IOException("Test exception");
        IOException cause1 = new IOException("Test cause exception 1");
        IOException cause2 = new IOException("Test cause exception 2");
        cause1.initCause(cause2);
        throwable.initCause(cause1);
        String filter = "java.io.";
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[] { filter });
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + " " + cause2.getClass().getName() + "@" + cause2.toString() + " " + cause1.getClass().getName() + "@" + cause1.toString() + "}";
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithoutFilter() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + "java.io.IOException".hashCode() + ", lineNumberHash=" + "getStackTrace".hashCode() + '}';
        assertEquals(expected, ed.toString());
    }

    @Test
    public void testToStringWithEmptyStack() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, new String[0]);
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        assertEquals(expected, ed.toString());
    }
}