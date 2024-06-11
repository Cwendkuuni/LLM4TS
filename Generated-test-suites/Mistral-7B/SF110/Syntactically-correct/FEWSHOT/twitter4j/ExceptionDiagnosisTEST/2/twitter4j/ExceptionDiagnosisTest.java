package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);

        assertEquals(diagnosis1.getStackLineHash(), diagnosis2.getStackLineHash());
        assertEquals(diagnosis1.getLineNumberHash(), diagnosis2.getLineNumberHash());
        assertEquals(diagnosis1.asHexString(), diagnosis2.asHexString());
        assertSame(diagnosis1.th, diagnosis2.th);
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "twitter4j";
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable, new String[] { filter });
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, new String[] { filter });

        assertEquals(diagnosis1.getStackLineHash(), diagnosis2.getStackLineHash());
        assertEquals(diagnosis1.getLineNumberHash(), diagnosis2.getLineNumberHash());
        assertEquals(diagnosis1.asHexString(), diagnosis2.asHexString());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().equals("twitter4j.SomeClass")) { // replace with actual class name
                int hash = stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
                int expectedStackLineHash = 31 * 0 + hash; // initial value of stackLineHash
                int expectedLineNumberHash = 31 * 0 + stackTraceElement.getLineNumber();

                assertEquals(expectedStackLineHash, diagnosis1.stackLineHash);
                assertEquals(expectedLineNumberHash, diagnosis1.lineNumberHash);
                assertEquals(diagnosis1.asHexString(), diagnosis1.asHexString().substring(0, diagnosis1.asHexString().lastIndexOf("-")) + "-" + Integer.toHexString(stackTraceElement.getLineNumber()).substring(2));

                break;
            }
        }
    }

    @Test
    public void testHashCodeAndEquals() {
        Throwable throwable1 = new RuntimeException("Throwable 1");
        Throwable throwable2 = new RuntimeException("Throwable 2");

        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
        assertNotEquals(diagnosis1, diagnosis2);

        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);
        assertEquals(diagnosis1.hashCode(), diagnosis3.hashCode());
        assertEquals(diagnosis1, diagnosis3);

        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable1, new String[] { "twitter4j" });
        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(throwable1, new String[] { "twitter4j" });
        assertEquals(diagnosis4.hashCode(), diagnosis5.hashCode());
        assertEquals(diagnosis4, diagnosis5);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        String expected = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
        assertEquals(expected, diagnosis.toString());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        byte[] serialized = serialize(diagnosis);
        ExceptionDiagnosis deserialized = deserialize(serialized);

        assertEquals(diagnosis.getStackLineHash(), deserialized.getStackLineHash());
        assertEquals(diagnosis.getLineNumberHash(), deserialized.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), deserialized.asHexString());
        assertSame(diagnosis.th, deserialized.th);
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    private <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (T) objectInputStream.readObject();
    }
}