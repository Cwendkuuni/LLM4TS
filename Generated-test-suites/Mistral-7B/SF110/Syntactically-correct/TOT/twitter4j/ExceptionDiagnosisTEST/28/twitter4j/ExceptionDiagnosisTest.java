import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosisWithNoFilter() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHashExpected = 12345678; // replace with expected hash value from actual test
        int lineNumberHashExpected = 98765432; // replace with expected hash value from actual test
        String hexStringExpected = "expected-hex-string"; // replace with expected hex string from actual test
        assertEquals(stackLineHashExpected, diagnosis.getStackLineHash());
        assertEquals(lineNumberHashExpected, diagnosis.getLineNumberHash());
        assertEquals(hexStringExpected, diagnosis.asHexString());
    }
}