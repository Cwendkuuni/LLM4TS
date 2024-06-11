package com.example.junit4; // replace with your package name if necessary

import static org.junit.Assert.*;

import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndEmptyFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(diagnosis);
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "test";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        assertNotNull(diagnosis);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        int stackLineHash = diagnosis.getStackLineHash();
        assertNotNull(stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        int lineNumberHash = diagnosis.getLineNumberHash();
        assertNotNull(lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        String hexString = diagnosis.asHexString();
        assertNotNull(hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, filter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, filter);
        assertFalse(diagnosis1.equals(diagnosis2));

        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1, filter);
        assertTrue(diagnosis1.equals(diagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable, filter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, filter);
        int hashCode1 = diagnosis1.hashCode();
        int hashCode2 = diagnosis2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        String toString = diagnosis.toString();
        assertNotNull(toString);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullDiagnosis() {
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = null;
        String toString = diagnosis.toString();
    }

    @Test
    public void testEqualsWithNullObject() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        assertFalse(diagnosis.equals(null));
    }

    @Test
    public void testEqualsWithDifferentTypeObject() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        Object obj = new Object();
        assertFalse(diagnosis.equals(obj));
    }

    @Test
    public void testHashCodeWithDifferentObjects() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        String[] filter = new String[]{"test"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, filter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, filter);
        int hashCode1 = diagnosis1.hashCode();
        int hashCode2 = diagnosis2.hashCode();
        assertNotEqual(hashCode1, hashCode2);
    }

    @Test
    public void testToStringWithEmptyFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        String toString = diagnosis.toString();
        assertNotNull(toString);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = null;
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        String toString = diagnosis.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullThrowableAndEmptyFilter() {
        String[] filter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null, filter);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullThrowableAndFilter() {
        String filter = null;
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null, filter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
    }

    @Test
    public void testToStringWithFilterStartsWithClassName() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "test.ExceptionClass";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        String toString = diagnosis.toString();
        assertTrue(toString.contains(filter));
    }

    @Test
    public void testToStringWithFilterStartsWithMethodName() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "test.ExceptionClass.methodName";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        String toString = diagnosis.toString();
        assertTrue(toString.contains(filter));
    }

    @Test
    public void testToStringWithFilterContainsBothClassNameAndMethodName() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "test.ExceptionClass.methodNameInClass";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        String toString = diagnosis.toString();
        assertTrue(toString.contains(filter));
    }

    @Test
    public void testToStringWithFilterContainsMethodNameOnly() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "methodName";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        String toString = diagnosis.toString();
        assertTrue(toString.contains(filter));
    }

    @Test
    public void testToStringWithFilterContainsClassNameOnly() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "ExceptionClass";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        String toString = diagnosis.toString();
        assertTrue(toString.contains(filter));
    }
}