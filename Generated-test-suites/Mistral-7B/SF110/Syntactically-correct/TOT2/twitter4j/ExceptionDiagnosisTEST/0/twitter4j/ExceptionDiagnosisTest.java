import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new IOException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("TestExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }

    @Test
    public void testConstructorWithEmptyFilter() {
        Throwable throwable = new IOException("Test Exception");
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertEquals("TestExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }
}