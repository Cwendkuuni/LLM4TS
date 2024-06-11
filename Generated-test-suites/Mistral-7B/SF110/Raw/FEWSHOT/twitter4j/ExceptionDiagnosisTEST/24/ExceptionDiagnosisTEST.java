package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithNullThrowable() {
        assertNull(new ExceptionDiagnosis(null));
    }

    @Test
    public void testConstructorWithEmptyInclusionFilter() {
        Throwable throwable = new ExceptionInInitializerError();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=63129448, lineNumberHash=0}", exceptionDiagnosis.toString());
        assertEquals(63129448, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testConstructorWithInclusionFilter() {
        Throwable throwable = new ExceptionInInitializerError();
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals(1, exceptionDiagnosis.getStackLines().length);
        StackTraceElement stackTraceElement = exceptionDiagnosis.getStackLines()[0];
        assertTrue(Modifier.isPublic(stackTraceElement.getModifiers()));
        assertTrue(stackTraceElement.getClassName().startsWith("twitter4j"));
    }

    @Test
    public void testGetStackLines() {
        Throwable throwable = new ExceptionInInitializerError();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(0, exceptionDiagnosis.getStackLines().length);

        String[] inclusionFilter = {"twitter4j"};
        exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals(1, exceptionDiagnosis.getStackLines().length);
    }

    @Test
    public void testHashCodeAndEquals() {
        Throwable throwable1 = new ExceptionInInitializerError();
        Throwable throwable2 = new ExceptionInInitializerError();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
        assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new ExceptionInInitializerError();
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable3);
        exceptionDiagnosis3.th = throwable1;
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());

        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis1WithFilter = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2WithFilter = new ExceptionDiagnosis(throwable2, inclusionFilter);
        assertFalse(exceptionDiagnosis1WithFilter.equals(exceptionDiagnosis2WithFilter));
        assertNotEquals(exceptionDiagnosis1WithFilter.hashCode(), exceptionDiagnosis2WithFilter.hashCode());

        ExceptionDiagnosis exceptionDiagnosis1WithSameFilter = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2WithSameFilter = new ExceptionDiagnosis(throwable1, inclusionFilter);
        assertTrue(exceptionDiagnosis1WithFilter.equals(exceptionDiagnosis2WithSameFilter));
        assertEquals(exceptionDiagnosis1WithFilter.hashCode(), exceptionDiagnosis2WithSameFilter.hashCode());
    }

    @Test
    public void testStringRepresentation() {
        Throwable throwable = new ExceptionInInitializerError();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=63129448, lineNumberHash=0}", exceptionDiagnosis.toString());

        String[] inclusionFilter = {"twitter4j"};
        exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String hexString = exceptionDiagnosis.asHexString();
        String expectedHexString = "63129448-0";
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testToStringWithCausedByException() {
        Throwable throwable = new ExceptionInInitializerError();
        Throwable causedByException = new RuntimeException();
        throwable.initCause(causedByException);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String hexString = exceptionDiagnosis.asHexString();
        String expectedHexString = "63129448-0 " + new ExceptionDiagnosis(causedByException).asHexString();
        assertEquals(expectedHexString, hexString);
    }

    private StackTraceElement[] getStackLines(ExceptionDiagnosis exceptionDiagnosis) {
        return exceptionDiagnosis.getStackTrace();
    }
}