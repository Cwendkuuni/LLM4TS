import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
    }

    @Test(expected = NullPointerException.class)
    public void testNullThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testEmptyInclusionFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(diagnosis);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expected = String.format("ExceptionDiagnosis{stackLineHash=%d, lineNumberHash=%d}", diagnosis.getStackLineHash(), diagnosis.getLineNumberHash());
        assertEquals(expected, diagnosis.toString());
    }
}