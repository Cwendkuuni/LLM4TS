package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructionWithNoFilter() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        Throwable throwable = new IOException("Test IOException");
        String filter = "java.io.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[] { filter });

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            String className = stackTrace[i].getClassName();
            if (className.startsWith(filter)) {
                int hash = className.hashCode() + stackTrace[i].getMethodName().hashCode();
                expectedHash = 31 * expectedHash + hash;
                break;
            }
        }

        assertEquals(expectedHash, diagnosis.getStackLineHash());
        assertEquals(stackTrace[stackTrace.length - 1].getLineNumber(), diagnosis.getLineNumberHash());
        assertEquals(toHexString(expectedHash) + "-" + stackTrace[stackTrace.length - 1].getLineNumber(), diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithCausedBy() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Throwable throwable = new IOException("Test IOException");
        Throwable cause = new FileNotFoundException("Test File Not Found");
        cause.initCause(throwable);
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosisCause = new ExceptionDiagnosis(cause);

        assertEquals(diagnosis.getStackLineHash(), diagnosisCause.getStackLineHash());
        assertEquals(diagnosis.getLineNumberHash(), diagnosisCause.getLineNumberHash());
        assertEquals(diagnosis.asHexString(), diagnosisCause.asHexString() + " " + diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisEquality() {
        Throwable throwable1 = new IOException("Test IOException 1");
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);

        assertEquals(diagnosis1, diagnosis3);
        assertNotEquals(diagnosis1, diagnosis2);
    }

    @Test
    public void testExceptionDiagnosisHashcode() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(new FileNotFoundException("Test File Not Found"));

        int expectedHash = diagnosis1.getStackLineHash();
        int expectedHash2 = diagnosis2.getStackLineHash();
        int expectedHash3 = diagnosis3.getStackLineHash();

        assertEquals(expectedHash, diagnosis1.hashCode());
        assertEquals(expectedHash, diagnosis2.hashCode());
        assertNotEquals(expectedHash, diagnosis3.hashCode());
    }

    @Test(expected = UndeclaredThrowableException.class)
    public void testExceptionDiagnosisToString() throws IOException {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String toString = diagnosis.toString();
    }

    private String toHexString(int value) {
        return "0000000" + Integer.toHexString(value).substring(6);
    }
}