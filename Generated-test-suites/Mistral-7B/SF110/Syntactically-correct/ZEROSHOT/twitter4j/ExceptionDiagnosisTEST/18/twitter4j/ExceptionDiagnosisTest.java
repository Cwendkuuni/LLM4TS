package twitter4j.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new TwitterException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(diagnosis.getStackLineHash(), getStackLineHash(throwable));
        assertEquals(diagnosis.getLineNumberHash(), getLineNumberHash(throwable));
        assertEquals(diagnosis.asHexString(), diagnosis.toHexString(diagnosis.getStackLineHash()) + "-" + diagnosis.toHexString(diagnosis.getLineNumberHash()));
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        Throwable throwable = new TwitterException("Test Exception");
        String filter = "twitter4j";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[] { filter });
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement element : trace) {
            if (element.getClassName().startsWith(filter)) {
                assertEquals(element.getLineNumber(), diagnosis.getLineNumber());
                assertEquals(element.getClassName() + ":" + element.getLineNumber(), Arrays.toString(diagnosis.getLocation()));
                break;
            }
        }
    }

    @Test
    public void testExceptionDiagnosisToString() {
        Throwable throwable = new TwitterException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expected = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        assertEquals(expected, diagnosis.toString());
    }

    @Test
    public void testExceptionDiagnosisEquals() {
        Throwable throwable1 = new TwitterException("Test Exception 1");
        Throwable throwable2 = new TwitterException("Test Exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        assertFalse(diagnosis1.equals(diagnosis2));

        Throwable throwable3 = new TwitterException("Test Exception 1");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);
        assertTrue(diagnosis1.equals(diagnosis3));

        Throwable throwable4 = new TwitterException("Test Exception 1");
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable1, new String[] { "twitter4j" });
        assertTrue(diagnosis1.equals(diagnosis4));

        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(diagnosis1);
        assertTrue(diagnosis1.equals(diagnosis5));
    }

    @Test
    public void testExceptionDiagnosisHashCode() {
        Throwable throwable = new TwitterException("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);
        assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        Throwable throwable2 = new TwitterException("Test Exception 2");
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable2);
        assertNotEquals(diagnosis1.hashCode(), diagnosis3.hashCode());

        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(diagnosis1);
        assertEquals(diagnosis1.hashCode(), diagnosis4.hashCode());
    }

    @Test
    public void testExceptionDiagnosisToHexString() {
        int value = 12345678;
        String expected = "1f2a3b4c";
        assertEquals(expected, ExceptionDiagnosis.toHexString(value));
    }

    @Test
    public void testExceptionDiagnosisStreaming() throws IOException, ClassNotFoundException {
        Throwable throwable = new TwitterException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeObject(diagnosis);
        output.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream input = new ObjectInputStream(inputStream);
        ExceptionDiagnosis diagnosis2 = (ExceptionDiagnosis) input.readObject();
        input.close();

        assertEquals(diagnosis.getStackLineHash(), diagnosis2.getStackLineHash());
        assertEquals(diagnosis.getLineNumberHash(), diagnosis2.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), diagnosis2.asHexString());
    }

    private int getStackLineHash(Throwable throwable) {
        StackTraceElement[] trace = throwable.getStackTrace();
        int hash = 0;
        for (StackTraceElement element : trace) {
            String className = element.getClassName();
            String methodName = element.getMethodName();
            hash = 31 * hash + (className.hashCode() + methodName.hashCode());
        }
        return hash;
    }

    private int getLineNumberHash(Throwable throwable) {
        StackTraceElement[] trace = throwable.getStackTrace();
        int hash = 0;
        for (StackTraceElement element : trace) {
            hash = 31 * hash + element.getLineNumber();
        }
        return hash;
    }

    @Test
    public void testPrivateFieldsAccess() throws IllegalAccessException, NoSuchFieldException {
        Throwable throwable = new TwitterException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Field stackLineHashField = ExceptionDiagnosis.class.getDeclaredField("stackLineHash");
        stackLineHashField.setAccessible(true);
        int stackLineHash = stackLineHashField.getInt(diagnosis);
        assertEquals(diagnosis.getStackLineHash(), stackLineHash);

        Field lineNumberHashField = ExceptionDiagnosis.class.getDeclaredField("lineNumberHash");
        lineNumberHashField.setAccessible(true);
        int lineNumberHash = lineNumberHashField.getInt(diagnosis);
        assertEquals(diagnosis.getLineNumberHash(), lineNumberHash);

        Field hexStringField = ExceptionDiagnosis.class.getDeclaredField("hexString");
        hexStringField.setAccessible(true);
        String hexString = (String) hexStringField.get(diagnosis);
        assertEquals(diagnosis.asHexString(), hexString);
    }

}