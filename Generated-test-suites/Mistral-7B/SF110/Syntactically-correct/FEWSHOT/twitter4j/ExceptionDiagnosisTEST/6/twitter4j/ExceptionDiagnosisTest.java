package twitter4j;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
        assertEquals(throwable.getMessage(), diagnosis.th.getMessage());
        assertEquals(throwable.getClass().getName(), Arrays.asList(diagnosis.th.getStackTrace()[0].getClassName(),
                diagnosis.th.getStackTrace()[0].getMethodName()).get(0));
        assertEquals(5, diagnosis.getStackLineHash());
        assertEquals(42, diagnosis.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), "5-42");
        assertEquals(diagnosis, diagnosis);
        assertEquals(hashCode(diagnosis), diagnosis.hashCode());
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException("Test exception");
        String filter = "twitter4j.StatusCodeInt";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[] { filter });
        assertNotNull(diagnosis);
        assertEquals(throwable.getMessage(), diagnosis.th.getMessage());
        String className = Arrays.asList(diagnosis.th.getStackTrace()[0].getClassName(), diagnosis.th.getStackTrace()[0]
                .getMethodName()).get(0);
        assertTrue(className.startsWith(filter));
        assertEquals(5, diagnosis.getStackLineHash());
        assertEquals(42, diagnosis.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), "5-42");
        assertEquals(diagnosis, diagnosis);
        assertEquals(hashCode(diagnosis), diagnosis.hashCode());
    }

    @Test
    public void testExceptionDiagnosisCausedBy() {
        Throwable firstThrowable = new RuntimeException("First exception");
        Throwable secondThrowable = new RuntimeException("Second exception");
        firstThrowable.initCause(secondThrowable);
        ExceptionDiagnosis firstDiagnosis = new ExceptionDiagnosis(firstThrowable);
        ExceptionDiagnosis secondDiagnosis = new ExceptionDiagnosis(firstThrowable.getCause());
        assertEquals(firstThrowable.getMessage(), firstDiagnosis.th.getMessage());
        assertEquals(secondThrowable.getMessage(), secondDiagnosis.th.getMessage());
        assertEquals(firstDiagnosis.asHexString(), firstDiagnosis.asHexString() + " " + secondDiagnosis.asHexString());
    }

    @Test
    public void testGetters() throws IllegalAccessException, NoSuchFieldException {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        Field stackLineHashField = AccessibleObject.getField(ExceptionDiagnosis.class, "stackLineHash");
        stackLineHashField.setAccessible(true);
        Field lineNumberHashField = AccessibleObject.getField(ExceptionDiagnosis.class, "lineNumberHash");
        lineNumberHashField.setAccessible(true);
        int stackLineHash = (int) stackLineHashField.get(diagnosis);
        int lineNumberHash = (int) lineNumberHashField.get(diagnosis);
        assertEquals(stackLineHash, diagnosis.getStackLineHash());
        assertEquals(lineNumberHash, diagnosis.getLineNumberHash());
    }

    @Test
    public void testEquals() {
        Throwable firstThrowable = new RuntimeException("First exception");
        Throwable secondThrowable = new RuntimeException("Second exception");
        firstThrowable.initCause(secondThrowable);
        ExceptionDiagnosis firstDiagnosis = new ExceptionDiagnosis(firstThrowable);
        ExceptionDiagnosis secondDiagnosis = new ExceptionDiagnosis(firstThrowable.getCause());
        ExceptionDiagnosis thirdDiagnosis = new ExceptionDiagnosis(new RuntimeException("Third exception"));
        ExceptionDiagnosis fourthDiagnosis = new ExceptionDiagnosis(firstThrowable);
        assertEquals(firstDiagnosis, fourthDiagnosis);
        assertNotEquals(firstDiagnosis, secondDiagnosis);
        assertNotEquals(firstDiagnosis, thirdDiagnosis);
    }

    @Test
    public void testHashCode() {
        Throwable firstThrowable = new RuntimeException("First exception");
        Throwable secondThrowable = new RuntimeException("Second exception");
        firstThrowable.initCause(secondThrowable);
        ExceptionDiagnosis firstDiagnosis = new ExceptionDiagnosis(firstThrowable);
        ExceptionDiagnosis secondDiagnosis = new ExceptionDiagnosis(firstThrowable.getCause());
        ExceptionDiagnosis thirdDiagnosis = new ExceptionDiagnosis(new RuntimeException("Third exception"));
        assertEquals(hashCode(firstDiagnosis), hashCode(new ExceptionDiagnosis(firstThrowable)));
        assertEquals(hashCode(firstDiagnosis), hashCode(new ExceptionDiagnosis(firstThrowable.getCause())));
        assertNotEquals(hashCode(firstDiagnosis), hashCode(thirdDiagnosis));
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=5, lineNumberHash=42}", diagnosis.toString());
    }

    private static int hashCode(Serializable obj) {
        return obj.hashCode();
    }
}