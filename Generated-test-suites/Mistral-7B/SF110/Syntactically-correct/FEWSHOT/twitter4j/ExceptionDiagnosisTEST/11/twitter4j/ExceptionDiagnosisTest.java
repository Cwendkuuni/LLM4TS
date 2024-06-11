package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getClass().getName(), diagnosis.getClassName(0));
        assertEquals(throwable.getMessage(), diagnosis.getMethodName(0));
        assertEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumber(0));
        assertEquals(throwable.getHashCode(), diagnosis.getStackLineHash());
        assertEquals(throwable.getLineNumber(), diagnosis.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), diagnosis.toHexString(diagnosis.getStackLineHash()) + "-" + diagnosis.toHexString(diagnosis.getLineNumberHash()));
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[] { filter });
        assertEquals(filter, diagnosis.getClassName(0).substring(0, filter.length()));
        assertNull(diagnosis.getClassName(1));
        assertNull(diagnosis.getMethodName(0));
        assertEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumber(0));
        assertEquals(throwable.getHashCode(), diagnosis.getStackLineHash());
        assertEquals(throwable.getLineNumber(), diagnosis.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), diagnosis.toHexString(diagnosis.getStackLineHash()) + "-" + diagnosis.toHexString(diagnosis.getLineNumberHash()));
    }

    @Test
    public void testExceptionDiagnosisWithCause() {
        Throwable throwable = new RuntimeException("Test Exception 1", new Exception("Test Exception 2"));
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis causeDiagnosis = new ExceptionDiagnosis(throwable.getCause());
        assertEquals(throwable.getClass().getName(), diagnosis.getClassName(0));
        assertEquals(throwable.getMessage(), diagnosis.getMethodName(0));
        assertEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumber(0));
        assertEquals(throwable.getHashCode(), diagnosis.getStackLineHash());
        assertEquals(throwable.getLineNumber(), diagnosis.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), diagnosis.toHexString(diagnosis.getStackLineHash()) + " " + causeDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetClassName() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getClass().getName(), diagnosis.getClassName(0));
        assertNull(diagnosis.getClassName(1));
    }

    @Test
    public void testGetMethodName() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getStackTrace()[0].getMethodName(), diagnosis.getMethodName(0));
        assertNull(diagnosis.getMethodName(1));
    }

    @Test
    public void testGetLineNumber() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumber(0));
        assertNull(diagnosis.getLineNumber(1));
    }

    @Test
    public void testHashcodeAndEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);
        assertEquals(diagnosis1, diagnosis3);
        assertNotEquals(diagnosis1, diagnosis2);
        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        assertEquals(expected, diagnosis.toString());
    }

    @Test
    public void testSerializability() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(diagnosis);
        objStream.close();
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream objInput = new ObjectInputStream(byteInput);
        ExceptionDiagnosis deserializedDiagnosis = (ExceptionDiagnosis) objInput.readObject();
        assertEquals(diagnosis.getStackLineHash(), deserializedDiagnosis.getStackLineHash());
        assertEquals(diagnosis.getLineNumberHash(), deserializedDiagnosis.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), deserializedDiagnosis.asHexString());
    }

    @Test
    public void testToStringWithCause() {
        Throwable throwable1 = new RuntimeException("Test Exception 1", new Exception("Test Exception 2"));
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        String expected = "ExceptionDiagnosis{stackLineHash=" + diagnosis1.getStackLineHash() + ", lineNumberHash=" + diagnosis1.getLineNumberHash() + " " + diagnosis1.getClassName(1) + "{" + diagnosis1.getMethodName(1) + "}" + "}";
        assertEquals(expected, diagnosis1.toString());
    }

    @Test
    public void testToStringWithNullCause() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        assertEquals(expected, diagnosis.toString());
    }

    @Test
    public void testGetClassNameFromSerializableObject() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(diagnosis);
        objStream.close();
        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        ObjectInputStream objInput = new ObjectInputStream(byteInput);
        Serializable deserializedDiagnosis = (Serializable) objInput.readObject();
        assertEquals(diagnosis.getClassName(0), deserializedDiagnosis.getClass().getName());
    }
}