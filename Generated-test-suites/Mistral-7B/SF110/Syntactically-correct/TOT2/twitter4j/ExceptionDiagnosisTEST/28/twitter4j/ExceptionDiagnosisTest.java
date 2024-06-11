import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ExceptionDiagnosisTest {
    @Test
    public void testEmptyConstructor() {
        ExceptionDiagnosis ed = new ExceptionDiagnosis();
        assertNotNull(ed);
    }
}