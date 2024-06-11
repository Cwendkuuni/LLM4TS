package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
        assertEquals(throwable.getMessage(), diagnosis.th.getMessage());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        String methodName = stackTrace[0].getMethodName();
        int expectedLineNumber = stackTrace[0].getLineNumber();

        assertEquals(className, diagnosis.th.getStackTrace()[0].getClass().getName());
        assertEquals(methodName, diagnosis.th.getStackTrace()[0].getMethodName());
        assertEquals(expectedLineNumber, diagnosis.getLineNumberHash());

        String hexString = diagnosis.asHexString();
        String[] hexStringParts = hexString.split("-");
        int stackLineHash = Integer.parseInt(hexStringParts[0], 16);
        int lineNumberHash = Integer.parseInt(hexStringParts[1], 16);
        assertEquals(stackLineHash, diagnosis.getStackLineHash());
        assertEquals(lineNumberHash, diagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String filter = "com.example.";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String className = stackTrace[0].getClassName();
        String methodName = stackTrace[0].getMethodName();
        int expectedLineNumber = stackTrace[0].getLineNumber();

        assertTrue(className.startsWith(filter));
        assertEquals(className, diagnosis.th.getStackTrace()[0].getClass().getName());
        assertEquals(methodName, diagnosis.th.getStackTrace()[0].getMethodName());
        assertEquals(expectedLineNumber, diagnosis.getLineNumberHash());
    }

    @Test
    public void testHashCodeAndEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");

        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
        assertFalse(diagnosis1.equals(diagnosis2));

        Throwable causeThrowable = new RuntimeException("Cause Exception");
        Throwable causeThrowable2 = new RuntimeException("Cause Exception 2");
        ExceptionDiagnosis diagnosisWithCause1 = new ExceptionDiagnosis(throwable1, new String[]{"com.example."});
        ExceptionDiagnosis diagnosisWithCause2 = new ExceptionDiagnosis(throwable2, new String[]{"com.example."});
        ExceptionDiagnosis diagnosisWithCause3 = new ExceptionDiagnosis(causeThrowable, new String[]{"com.example."});
        ExceptionDiagnosis diagnosisWithCause4 = new ExceptionDiagnosis(causeThrowable2, new String[]{"com.example."});

        diagnosisWithCause1.th.initCause(causeThrowable);
        diagnosisWithCause2.th.initCause(causeThrowable2);

        assertEquals(diagnosisWithCause1.hashCode(), diagnosisWithCause3.hashCode());
        assertTrue(diagnosisWithCause1.equals(diagnosisWithCause3));
        assertFalse(diagnosisWithCause1.equals(diagnosisWithCause2));
        assertFalse(diagnosisWithCause2.equals(diagnosisWithCause3));
        assertFalse(diagnosisWithCause1.equals(diagnosisWithCause4));
    }
}