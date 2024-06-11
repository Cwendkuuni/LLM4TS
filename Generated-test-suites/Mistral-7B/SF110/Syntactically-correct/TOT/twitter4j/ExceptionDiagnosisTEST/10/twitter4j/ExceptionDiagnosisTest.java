package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        // Test with valid Throwable and an empty inclusionFilter
        assertEquals(12345678, exceptionDiagnosis.getStackLineHash());
        assertEquals(98765432, exceptionDiagnosis.getLineNumberHash());

        // Test edge case with empty inclusionFilter
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis exceptionDiagnosisWithEmptyFilter = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals(12345678, exceptionDiagnosisWithEmptyFilter.getStackLineHash());
        assertEquals(98765432, exceptionDiagnosisWithEmptyFilter.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        Throwable throwable = new IOException("Test IOException");
        String[] inclusionFilter = {"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        // Test with valid Throwable and a non-empty inclusionFilter
        assertEquals(9876543210, exceptionDiagnosis.getStackLineHash());
        assertEquals(1234567890, exceptionDiagnosis.getLineNumberHash());
        assertEquals("1A1B-2C3D", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHashAndLineNumberHash() {
        // Test valid ExceptionDiagnosis instance
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertEquals(12345678, exceptionDiagnosis.getStackLineHash());
        assertEquals(98765432, exceptionDiagnosis.getLineNumberHash());

        // Test edge case with zero hash values
        ExceptionDiagnosis exceptionDiagnosisZeroHashes = new ExceptionDiagnosis(new RuntimeException("Test RuntimeException"), new String[0]);
        assertEquals(0, exceptionDiagnosisZeroHashes.getStackLineHash());
        assertEquals(0, exceptionDiagnosisZeroHashes.getLineNumberHash());
    }
}