import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosisConstructorNoFilter() {
        Throwable throwable = new RuntimeException();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345678; // This value will change with the actual stack trace
        int expectedLineNumberHash = 98765432; // This value will change with the actual line number
        String expectedHexString = "ABCDEF01-ABCDEF02-XXX"; // Replace XX with actual hex values
        ExceptionDiagnosis expectedDiagnosis = new ExceptionDiagnosis(new RuntimeException(), new String[0]);
        assertEquals(expectedDiagnosis.getStackLineHash(), diagnosis.getStackLineHash());
        assertEquals(expectedDiagnosis.getLineNumberHash(), diagnosis.getLineNumberHash());
        assertEquals(expectedHexString, diagnose.asHexString());
    }
}