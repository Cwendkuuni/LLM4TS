package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345678; // expected hash code based on the stack trace
        int actualStackLineHash = exceptionDiagnosis.getStackLineHash();
        assertEquals("Unexpected stack line hash", expectedStackLineHash, actualStackLineHash);
    }
}