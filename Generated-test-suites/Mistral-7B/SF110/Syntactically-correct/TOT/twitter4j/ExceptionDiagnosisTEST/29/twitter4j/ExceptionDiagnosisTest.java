import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {
    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] filter = {"com.test"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, filter);
        int expected = 12345678; // Replace this with the expected hash value
        int actual = diagnosis.getStackLineHash();
        assertEquals(expected, actual);
    }
}