package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosisWithSingleThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        int expectedStackLineHash = 12345678; // Replace with the expected hash code value for a test case
        int expectedLineNumberHash = 98765432; // Replace with the expected hash code value for a test case
        String expectedHexString = "AA-BB-CC-DD-EE-FF-GG-HH-II-JJ-KK-LL-MM-NN-OO-PP"; // Replace with the expected hex string value for a test case

        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = {"com.test.MyClass", "org.apache.commons.io.IOUtils"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        int expectedStackLineHash = 98765432; // Replace with the expected hash code value for a test case
        int expectedLineNumberHash = 12345678; // Replace with the expected hash code value for a test case
        String expectedHexString = "PP-OO-NN-MM-LL-KK-JJ-II-HH-GG-FF-EE-DD-CC-BB-AA"; // Replace with the expected hex string value for a test case

        assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
        assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithNestedThrowable() {
        Throwable throwable = new RuntimeException("Test Exception", new IOException("Test IOException"));
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        String expectedHexString = "AA-BB-CC-DD-EE-FF-GG-HH-II-JJ-KK-LL-MM-NN-OO-PP-QQ"; // Replace with the expected hex string value for a test case

        assertEquals(exceptionDiagnosis.asHexString(), expectedHexString);
    }
}