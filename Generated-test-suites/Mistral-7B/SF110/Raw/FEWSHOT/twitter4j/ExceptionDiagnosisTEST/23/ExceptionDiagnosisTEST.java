package twitter4j.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.ExceptionDiagnosis.Util;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithoutFilter() {
        Throwable throwable = new ExceptionInInitializerError("test");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        assertEquals(Modifier.FINAL, Modifier.isFinal(ExceptionDiagnosis.class));
        assertEquals(453958937114285988L, ExceptionDiagnosis.serialVersionUID);

        assertNull(exceptionDiagnosis.th.getCause());
        assertEquals(Util.class.getName(), exceptionDiagnosis.th.getClass().getName());
        assertEquals(-1, exceptionDiagnosis.getStackLineHash());
        assertEquals(-1, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());

        ExceptionDiagnosis clone = new ExceptionDiagnosis(exceptionDiagnosis.th);
        assertEquals(exceptionDiagnosis, clone);
        assertEquals(exceptionDiagnosis.hashCode(), clone.hashCode());
    }

    @Test
    public void testConstructorWithFilter() {
        Throwable throwable = new ExceptionInInitializerError("test");
        String[] filter = {"twitter4j.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);

        assertEquals(Modifier.FINAL, Modifier.isFinal(ExceptionDiagnosis.class));
        assertEquals(453958937114285988L, ExceptionDiagnosis.serialVersionUID);

        assertNotNull(exceptionDiagnosis.th.getCause());
        assertEquals("twitter4j.test.ExceptionDiagnosis", exceptionDiagnosis.th.getClass().getName());
        assertNotEquals(-1, exceptionDiagnosis.getStackLineHash());
        assertNotEquals(-1, exceptionDiagnosis.getLineNumberHash());
        assertNotEquals("", exceptionDiagnosis.asHexString());

        ExceptionDiagnosis clone = new ExceptionDiagnosis(exceptionDiagnosis.th);
        assertNotEquals(exceptionDiagnosis, clone);
        assertNotEquals(exceptionDiagnosis.hashCode(), clone.hashCode());

        ExceptionDiagnosis cloneWithFilter = new ExceptionDiagnosis(exceptionDiagnosis.th, filter);
        assertEquals(exceptionDiagnosis, cloneWithFilter);
        assertEquals(exceptionDiagnosis.hashCode(), cloneWithFilter.hashCode());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        int stackLineHashExpected = 31 * 31 * 51 + className.hashCode();
        int lineNumberHashExpected = stackTrace[0].getLineNumber();

        assertEquals(stackLineHashExpected, exceptionDiagnosis.getStackLineHash());
        assertEquals(lineNumberHashExpected, exceptionDiagnosis.getLineNumberHash());
        assertEquals(Util.toHexString(stackLineHashExpected) + "-" + Util.toHexString(lineNumberHashExpected), exceptionDiagnosis.asHexString());

        Throwable cause = throwable.getCause();
        if (cause != null) {
            ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(cause);
            assertEquals(Util.toHexString(stackLineHashExpected) + " " + Util.toHexString(cause.getStackTrace()[0].getLineNumber()), exceptionDiagnosis.asHexString());
            assertEquals(exceptionDiagnosis, causeExceptionDiagnosis);
            assertEquals(exceptionDiagnosis.hashCode(), causeExceptionDiagnosis.hashCode());
        }
    }

    @Test
    public void testGetters() {
        Throwable throwable = new ExceptionInInitializerError("test");
        String[] filter = {"twitter4j.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);

        assertEquals(exceptionDiagnosis.th, throwable);
        assertEquals(exceptionDiagnosis.getStackLineHash(), exceptionDiagnosis.stackLineHash);
        assertEquals(exceptionDiagnosis.getLineNumberHash(), exceptionDiagnosis.lineNumberHash);
        assertEquals(exceptionDiagnosis.asHexString(), exceptionDiagnosis.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        Throwable throwable1 = new ExceptionInInitializerError("test1");
        Throwable throwable2 = new ExceptionInInitializerError("test2");
        String[] filter1 = {"twitter4j.test1"};
        String[] filter2 = {"twitter4j.test2"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, filter1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, filter2);

        assertNotEquals(exceptionDiagnosis1, exceptionDiagnosis2);
        assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        StackTraceElement stackTrace1 = throwable1.getStackTrace()[0];
        StackTraceElement stackTrace2 = throwable2.getStackTrace()[0];

        int stackLineHash1 = Util.toHexString(stackTrace1.getClassName().hashCode()).hashCode() * 31 + stackTrace1.getMethodName().hashCode();
        int stackLineHash2 = Util.toHexString(stackTrace2.getClassName().hashCode()).hashCode() * 31 + stackTrace2.getMethodName().hashCode();
        int lineNumberHash1 = stackTrace1.getLineNumber();
        int lineNumberHash2 = stackTrace2.getLineNumber();

        ExceptionDiagnosis clone1 = new ExceptionDiagnosis(throwable1);
        assertEquals(exceptionDiagnosis1, clone1);
        assertEquals(exceptionDiagnosis1.hashCode(), clone1.hashCode());

        ExceptionDiagnosis clone2 = new ExceptionDiagnosis(throwable2);
        assertEquals(exceptionDiagnosis2, clone2);
        assertEquals(exceptionDiagnosis2.hashCode(), clone2.hashCode());

        assertEquals(exceptionDiagnosis1.hashCode(), stackLineHash1 * 31 + lineNumberHash1);
        assertEquals(exceptionDiagnosis2.hashCode(), stackLineHash2 * 31 + lineNumberHash2);

        assertEquals(exceptionDiagnosis1, new ExceptionDiagnosis(throwable1, filter1));
        assertEquals(exceptionDiagnosis2, new ExceptionDiagnosis(throwable2, filter2));

        assertEquals(exceptionDiagnosis1, new ExceptionDiagnosis(throwable1));
        assertEquals(exceptionDiagnosis2, new ExceptionDiagnosis(throwable2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, filter1);
        assertEquals(exceptionDiagnosis3, exceptionDiagnosis4);
        assertEquals(exceptionDiagnosis3.hashCode(), exceptionDiagnosis4.hashCode());

        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable2, filter2);
        assertNotEquals(exceptionDiagnosis5, exceptionDiagnosis6);
        assertNotEquals(exceptionDiagnosis5.hashCode(), exceptionDiagnosis6.hashCode());

        ExceptionDiagnosis exceptionDiagnosis7 = new ExceptionDiagnosis(throwable1, filter2);
        ExceptionDiagnosis exceptionDiagnosis8 = new ExceptionDiagnosis(throwable2, filter1);
        assertNotEquals(exceptionDiagnosis7, exceptionDiagnosis8);
        assertNotEquals(exceptionDiagnosis7.hashCode(), exceptionDiagnosis8.hashCode());

        ExceptionDiagnosis exceptionDiagnosis9 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis10 = new ExceptionDiagnosis(throwable2);
        assertNotEquals(exceptionDiagnosis9, exceptionDiagnosis10);
        assertNotEquals(exceptionDiagnosis9.hashCode(), exceptionDiagnosis10.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new ExceptionInInitializerError("test");
        String[] filter = {"twitter4j.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);

        String toString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        assertEquals(toString, exceptionDiagnosis.toString());
    }
}