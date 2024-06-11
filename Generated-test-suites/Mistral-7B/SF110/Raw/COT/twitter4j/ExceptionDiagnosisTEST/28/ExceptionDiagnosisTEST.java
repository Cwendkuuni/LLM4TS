package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;
import twitter4j.Throwable; // Assuming Throwable is a standard Java class, if not replace it with the actual class used in the ExceptionDiagnosis class

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new Throwable(); // Assuming Throwable is a class that can be instantiated for testing purposes
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.getHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndStringArray() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = new String[]{"testClass1", "testClass2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        assertNotNull(exceptionDiagnosis.getStackLineHash());
        assertNotNull(exceptionDiagnosis.getLineNumberHash());
        assertNotNull(exceptionDiagnosis.getHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = new String[]{"testClass1", "testClass2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();

        assertNotSame(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = new String[]{"testClass1", "testClass2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();

        assertNotSame(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = new String[]{"testClass1", "testClass2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String hexString = exceptionDiagnosis.asHexString();

        assertNotNull(hexString);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsWithNullObject() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable(), new String[0]);
        assertFalse(exceptionDiagnosis.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new Throwable(), new String[0]);
        assertFalse(exceptionDiagnosis.equals(new Object()));
    }

    @Test
    public void testEqualsWithSameInstance() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable(), new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = exceptionDiagnosis1;
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testEqualsWithSameValues() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable(), new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable(), new String[0]);
        assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new Throwable(), new String[0]);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new Throwable(), new String[0]);
        int hashCode1 = exceptionDiagnosis1.hashCode();
        int hashCode2 = exceptionDiagnosis2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = new String[]{"testClass1", "testClass2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        String toString = exceptionDiagnosis.toString();

        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullThrowable() {
        String[] inclusionFilter = new String[0];
        new ExceptionDiagnosis(null, inclusionFilter);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullInclusionFilter() {
        Throwable throwable = new Throwable();
        new ExceptionDiagnosis(throwable, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyInclusionFilter() {
        Throwable throwable = new Throwable();
        new ExceptionDiagnosis(throwable, new String[0]);
    }

    @Test
    public void testToStringWithEmptyStack() {
        Throwable throwable = new Throwable();
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        String toString = exceptionDiagnosis.toString();

        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
        assertTrue(toString.contains("}"));
    }

    @Test
    public void testToStringWithNonEmptyStack() {
        Throwable throwable = new Throwable();
        StackTraceElement stackTraceElement = new ExceptionInInitializerError("test", new Object[0]).getStackTrace()[0];
        String[] inclusionFilter = new String[]{"testClass1", "testClass2"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        String toString = exceptionDiagnosis.toString();

        assertTrue(toString.contains("ExceptionDiagnosis{stackLineHash="));
        assertTrue(toString.contains("lineNumberHash="));
        assertTrue(toString.contains(stackTraceElement.getClassName()));
        assertTrue(toString.contains(stackTraceElement.getMethodName()));
        assertTrue(toString.contains(String.valueOf(stackTraceElement.getLineNumber())));
    }
}