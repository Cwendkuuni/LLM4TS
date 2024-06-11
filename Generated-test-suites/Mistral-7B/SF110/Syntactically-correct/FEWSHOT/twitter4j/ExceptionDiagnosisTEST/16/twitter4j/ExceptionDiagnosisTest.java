package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis.getStackLineHash());
        assertNotNull(diagnosis.getLineNumberHash());
        assertNotNull(diagnosis.getHexString());
        assertNotNull(diagnosis.getTh());

        ExceptionDiagnosis diagnosisWithFilter = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        assertNotNull(diagnosisWithFilter.getStackLineHash());
        assertNotNull(diagnosisWithFilter.getLineNumberHash());
        assertNotNull(diagnosisWithFilter.getHexString());
        assertNotNull(diagnosisWithFilter.getTh());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        assertEquals(stackTrace[0].getClassName(), diagnosis.getTh().getStackTrace()[0].getClassName());
        assertEquals(stackTrace[0].getMethodName(), diagnosis.getTh().getStackTrace()[0].getMethodName());
        assertEquals(stackTrace[0].getLineNumber(), diagnosis.getLineNumberHash());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int stackLineHash = diagnosis.getStackLineHash();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedStackLineHash = 31 * (31 * "com.twitter4j".hashCode() + "Exception".hashCode()) + stackTrace[0].getLineNumber();
        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int lineNumberHash = diagnosis.getLineNumberHash();

        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        int expectedLineNumberHash = stackTraceElement.getLineNumber();
        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String hexString = diagnosis.asHexString();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String expectedHexString = toHexString(31 * (31 * "com.twitter4j".hashCode() + "Exception".hashCode())) + "-" + toHexString(stackTrace[0].getLineNumber());
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testToString() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String toString = diagnosis.toString();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + toHexString(31 * (31 * "com.twitter4j".hashCode() + "Exception".hashCode())) + ", lineNumberHash=" + toHexString(stackTrace[0].getLineNumber()) + '}';
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new Exception("Test Exception 1");
        Throwable throwable2 = new Exception("Test Exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        assertFalse(diagnosis1.equals(diagnosis2));
        Throwable cause1 = new Exception("Cause 1");
        Throwable cause2 = new Exception("Cause 2");
        ExceptionDiagnosis diagnosisWithCause1 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        ExceptionDiagnosis diagnosisWithCause2 = new ExceptionDiagnosis(throwable2, new String[]{"com.example"});
        diagnosisWithCause1.setTh(new Exception("Test Exception", cause1));
        diagnosisWithCause2.setTh(new Exception("Test Exception", cause2));
        assertFalse(diagnosisWithCause1.equals(diagnosisWithCause2));

        ExceptionDiagnosis diagnosis1WithSameInfo = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        assertTrue(diagnosis1.equals(diagnosis1WithSameInfo));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int hashCode = diagnosis.hashCode();

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedHashCode = 31 * (31 * "com.twitter4j".hashCode() + "Exception".hashCode()) + stackTrace[0].getLineNumber();
        assertEquals(expectedHashCode, hashCode);
    }

    private String toHexString(int value) {
        final String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }
}