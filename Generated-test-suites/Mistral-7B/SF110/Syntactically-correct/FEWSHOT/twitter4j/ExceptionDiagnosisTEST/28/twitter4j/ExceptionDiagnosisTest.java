package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Throwable throwable = new IOException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertNull(diagnosis.hexString);
        assertNull(diagnosis.getStackTrace());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithThrowableAndEmptyFilter() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertNotNull(diagnosis.hexString);
        assertNotNull(diagnosis.getStackTrace());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithThrowableAndNonEmptyFilter() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertNotNull(diagnosis.hexString);
        assertNotNull(diagnosis.getStackTrace());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        int stackLineHash = diagnosis.getStackLineHash();
        int expectedStackLineHash = 0; // Calculate the expected stackLineHash based on given code and filter
        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        int lineNumberHash = diagnosis.getLineNumberHash();
        int expectedLineNumberHash = 0; // Calculate the expected lineNumberHash based on given code and filter
        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        String hexString = diagnosis.asHexString();
        String expectedHexString = "XXXXXXXXXX-YYYYYYYY"; // Calculate the expected hexString based on given code and filter
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testEquals() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        Throwable throwable1 = new IOException("Test Exception 1");
        Throwable throwable2 = new IOException("Test Exception 2");
        String[] filter1 = {"com.test"};
        String[] filter2 = {"com.test"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, filter1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, filter2);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1, filter2);

        assertFalse(diagnosis1.equals(null));
        assertFalse(diagnosis1.equals(new Object()));
        assertTrue(diagnosis1.equals(diagnosis1));
        assertFalse(diagnosis1.equals(diagnosis2));
        assertFalse(diagnosis1.equals(diagnosis3));

        // Test with same exception and different filter
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"com.other"});
        assertFalse(diagnosis1.equals(diagnosis4));

        // Test with different exceptions but same filter
        ExceptionDiagnosis diagnosis5 = new ExceptionDiagnosis(new IOException("Test Exception 3"), filter1);
        assertFalse(diagnosis1.equals(diagnosis5));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable, filter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, filter);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(new IOException("Test Exception 2"), filter);
        int hashcode1 = diagnosis1.hashCode();
        int hashcode2 = diagnosis2.hashCode();
        int hashcode3 = diagnosis3.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertNotSame(hashcode1, hashcode3);
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=XXX, lineNumberHash=YYY}"; // Calculate the expected toString based on given code and filter
        assertEquals(expectedToString, diagnosis.toString());
    }

    @Test
    public void testToHexString() {
        int value = 12345678;
        String expectedHexString = "1234abcd"; // Calculate the expected hexString based on given integer value
        String hexString = ExceptionDiagnosis.Util.toHexString(value);
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testCreateExceptionDiagnosisWithCause() throws IOException {
        IOException ioException = new IOException("Test IO Exception");
        Throwable cause = new NullPointerException("Test Cause Exception");
        ioException.initCause(cause);
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(ioException, filter);
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertNotNull(diagnosis.hexString);
        assertNotNull(diagnosis.getStackTrace());
        assertNotSame(diagnosis.th, ioException); // Make sure a new ExceptionDiagnosis instance is created
        assertSame(diagnosis.th.getCause(), cause); // Make sure the cause is set correctly
    }

    @Test
    public void testCreateExceptionDiagnosisWithCauseAndFilter() throws IOException {
        IOException ioException = new IOException("Test IO Exception");
        Throwable cause = new NullPointerException("Test Cause Exception");
        ioException.initCause(cause);
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(ioException, filter);
        assertNotNull(diagnosis);
        assertNotNull(diagnosis.th);
        assertNotNull(diagnosis.hexString);
        assertNotNull(diagnosis.getStackTrace());
        assertNotSame(diagnosis.th, ioException); // Make sure a new ExceptionDiagnosis instance is created
        assertSame(diagnosis.th.getCause(), cause); // Make sure the cause is set correctly
    }
}