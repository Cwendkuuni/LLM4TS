package twitter4j;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisWithThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345678; // Add actual hashcode value
        int expectedLineNumberHash = 98765432; // Add actual hashcode value
        String expectedHexString = "XXYYZZ-AABBCC-XXXXXXXX"; // Add actual hexString value
        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }
}