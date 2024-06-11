package twitter4j.test;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.Throwable;
import java.util.Arrays;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        assertNull(new ExceptionDiagnosis(null));
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullInclusionFilter() {
        new ExceptionDiagnosis(new Throwable(), null);
    }

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        assertEquals("ExceptionDiagnosis{stackLineHash=482895375, lineNumberHash=5953}", exceptionDiagnosis.toString());
        assertEquals("482895375-5953", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisGetStackLineHash() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        assertEquals(482895375, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testExceptionDiagnosisGetLineNumberHash() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        assertEquals(5953, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisEquals() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter);

        assertNotEquals(exceptionDiagnosis1, exceptionDiagnosis2);

        Throwable throwable3 = new IOException("Test IOException 3");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, inclusionFilter);

        assertEquals(exceptionDiagnosis1, exceptionDiagnosis3);
    }

    @Test
    public void testExceptionDiagnosisHashCode() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, inclusionFilter);

        assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testExceptionDiagnosisToString() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        String expected = "ExceptionDiagnosis{stackLineHash=482895375, lineNumberHash=5953}";
        assertEquals(expected, exceptionDiagnosis.toString());
    }

    @Test
    public void testExceptionDiagnosisAsHexStringWithNullThrowable() {
        assertNull(new ExceptionDiagnosis(null).asHexString());
    }

    @Test
    public void testExceptionDiagnosisAsHexString() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        String expected = "482895375-5953";
        assertEquals(expected, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisAsHexStringWithCause() {
        Throwable throwable = new IOException("Test IOException");
        Throwable cause = new IllegalArgumentException("Test IllegalArgumentException");
        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        String expected = "482895375-5953 " + new ExceptionDiagnosis(cause, inclusionFilter).asHexString();
        assertEquals(expected, exceptionDiagnosis.asHexString());
    }

}