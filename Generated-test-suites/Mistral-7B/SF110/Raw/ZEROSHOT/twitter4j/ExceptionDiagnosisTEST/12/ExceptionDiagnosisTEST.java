package twitter4j.test;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.FieldUtils;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.ExceptionInInitializerError;
import java.util.NoSuchElementException;
import java.util.Stack;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    private static final String CLASS_NAME_FILTER = "ExceptionDiagnosisTest";

    private static final StackTraceElement TEST_STACK_TRACE_ELEMENT = new StackTraceElement("ExceptionDiagnosisTest", "testMethod", "ExceptionDiagnosisTest.java", 1);
    private static final int TEST_STACK_LINE_HASH = TEST_STACK_TRACE_ELEMENT.getClassName().hashCode() + TEST_STACK_TRACE_ELEMENT.getMethodName().hashCode();
    private static final int TEST_LINE_NUMBER = 1;

    private ExceptionDiagnosis exceptionDiagnosis;

    @Before
    public void setUp() {
        try {
            throw new ExceptionInInitializerError("Test Exception");
        } catch (ExceptionInInitializerError e) {
            exceptionDiagnosis = new ExceptionDiagnosis(e, new String[]{CLASS_NAME_FILTER});
        }
    }

    @Test
    public void testConstructorWithThrowable() {
        assertEquals("ExceptionDiagnosis{stackLineHash=12345678, lineNumberHash=1}", exceptionDiagnosis.toString());
    }

    @Test
    public void testConstructorWithThrowableAndFilter() {
        assertEquals("ExceptionDiagnosis{stackLineHash=" + TEST_STACK_LINE_HASH + ", lineNumberHash=" + TEST_LINE_NUMBER + '}', exceptionDiagnosis.toString());
    }

    @Test
    public void testGetStackLineHash() {
        assertEquals(TEST_STACK_LINE_HASH, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        assertEquals(TEST_LINE_NUMBER, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        String expectedHexString = TEST_STACK_LINE_HASH + "-" + TEST_LINE_NUMBER;
        String actualHexString = exceptionDiagnosis.asHexString();
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis equalExceptionDiagnosis = new ExceptionDiagnosis(new ExceptionInInitializerError("Test Exception"), new String[]{CLASS_NAME_FILTER});
        assertTrue(exceptionDiagnosis.equals(equalExceptionDiagnosis));

        Object notEqualObject = new Object();
        assertFalse(exceptionDiagnosis.equals(notEqualObject));

        ExceptionDiagnosis notEqualExceptionDiagnosis = new ExceptionDiagnosis(new IOException(), new String[]{CLASS_NAME_FILTER});
        assertFalse(exceptionDiagnosis.equals(notEqualExceptionDiagnosis));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = TEST_STACK_LINE_HASH * 31 + TEST_LINE_NUMBER;
        int actualHashCode = exceptionDiagnosis.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(exceptionDiagnosis);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ExceptionDiagnosis deserializedExceptionDiagnosis = (ExceptionDiagnosis) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(exceptionDiagnosis.getStackLineHash(), deserializedExceptionDiagnosis.getStackLineHash());
        assertEquals(exceptionDiagnosis.getLineNumberHash(), deserializedExceptionDiagnosis.getLineNumberHash());
        assertEquals(exceptionDiagnosis.asHexString(), deserializedExceptionDiagnosis.asHexString());
    }

    @Test(expected = NoSuchElementException.class)
    public void testCauseWithNoCauseException() {
        ExceptionDiagnosis noCauseExceptionDiagnosis = new ExceptionDiagnosis(new NoSuchElementException(), new String[]{CLASS_NAME_FILTER});
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(noCauseExceptionDiagnosis.getCause(), new String[]{CLASS_NAME_FILTER});
    }

    @Test
    public void testCauseWithCausedException() {
        ExceptionInInitializerError causedException = new ExceptionInInitializerError("Caused Exception");
        ExceptionDiagnosis causedExceptionDiagnosis = new ExceptionDiagnosis(causedException, new String[]{CLASS_NAME_FILTER});
        ExceptionDiagnosis exceptionDiagnosisWithCause = new ExceptionDiagnosis(new ExceptionInInitializerError("Test Exception", causedException), new String[]{CLASS_NAME_FILTER});

        String expectedHexStringWithCause = TEST_STACK_LINE_HASH + "-" + TEST_LINE_NUMBER + " " + causedExceptionDiagnosis.asHexString();
        String actualHexStringWithCause = exceptionDiagnosisWithCause.asHexString();

        assertEquals(expectedHexStringWithCause, actualHexStringWithCause);
    }
}