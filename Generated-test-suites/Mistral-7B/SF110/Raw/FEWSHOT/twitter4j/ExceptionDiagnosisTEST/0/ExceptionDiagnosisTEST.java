package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.SerializableчуШ_;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals(0, exceptionDiagnosis.getStackLineHash());
        assertEquals(0, exceptionDiagnosis.getLineNumberHash());
        assertEquals("", exceptionDiagnosis.asHexString());

        Throwable throwableWithFilter = new IOException("Test IOException with filter");
        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);
        assertEquals(1610612745, exceptionDiagnosisWithFilter.getStackLineHash());
        assertEquals(305029895, exceptionDiagnosisWithFilter.getLineNumberHash());
        assertEquals("997d9b2f-321d2e9d", exceptionDiagnosisWithFilter.asHexString());

        Throwable throwableWithCause = new IllegalArgumentException("Test IllegalArgumentException");
        throwableWithCause.initCause(new IOException("Test IOException cause"));
        ExceptionDiagnosis exceptionDiagnosisWithCause = new ExceptionDiagnosis(throwableWithCause);
        assertEquals("997d9b2f-321d2e9d 5a82f262-31b03d5d", exceptionDiagnosisWithCause.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Throwable throwableWithFilter = new IOException("Test IOException with filter");
        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosisWithSameStack = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);

        assertEquals(exceptionDiagnosisWithFilter.getStackLineHash(), exceptionDiagnosisWithSameStack.getStackLineHash());
        assertNotEquals(exceptionDiagnosis.getStackLineHash(), exceptionDiagnosisWithFilter.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Throwable throwableWithFilter = new IOException("Test IOException with filter");
        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosisWithSameLine = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);

        assertEquals(exceptionDiagnosisWithFilter.getLineNumberHash(), exceptionDiagnosisWithSameLine.getLineNumberHash());
        assertNotEquals(exceptionDiagnosis.getLineNumberHash(), exceptionDiagnosisWithFilter.getLineNumberHash());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new ExceptionDiagnosis(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorWithFilter() {
        new ExceptionDiagnosis(new IOException("Test IOException"), null);
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());

        Throwable throwableWithFilter = new IOException("Test IOException with filter");
        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);
        assertEquals("ExceptionDiagnosis{stackLineHash=1610612745, lineNumberHash=305029895}", exceptionDiagnosisWithFilter.toString());
    }

    @Test
    public void testHashCode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Throwable throwableWithFilter = new IOException("Test IOException with filter");
        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);

        Method method = ExceptionDiagnosis.class.getDeclaredMethod("hashCode");
        method.setAccessible(true);

        assertEquals(31 * (31 * 0 + 0) + 31 * (31 * 0 + 0), method.invoke(exceptionDiagnosis).longValue());
        assertEquals(31 * (31 * 1610612745 + 305029895) + 31 * (31 * 0 + 0), method.invoke(exceptionDiagnosisWithFilter).longValue());
    }

    @Test
    public void testEquals() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Throwable throwableWithFilter = new IOException("Test IOException with filter");
        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosisWithSameValues = new ExceptionDiagnosis(throwableWithFilter, inclusionFilter);

        assertSame(exceptionDiagnosisWithFilter, exceptionDiagnosisWithSameValues);
        assertNotEquals(exceptionDiagnosis, exceptionDiagnosisWithFilter);
        assertNotEquals(exceptionDiagnosis, new Object());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

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
}