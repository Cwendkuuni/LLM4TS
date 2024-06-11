package twitter4j;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        assertEquals("TestExceptionDiagnosis{-1612176111-1109614253}", exceptionDiagnosis.asHexString());
    }
}