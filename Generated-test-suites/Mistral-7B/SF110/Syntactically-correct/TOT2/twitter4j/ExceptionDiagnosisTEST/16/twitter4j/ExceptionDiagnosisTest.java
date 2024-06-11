
package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.IOException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
    }

    @Test
    public void testExceptionDiagnosisConstructionWithEmptyIncludeFilter() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithEmptyStackTrace() {
        Throwable throwable = new Throwable(); // Empty stack trace
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(exceptionDiagnosis);
        assertEquals("", exceptionDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructionWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    // Add other test cases here...

}
