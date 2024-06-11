package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;

import twitter4j.ExceptionDiagnosis;
import twitter4j.IOException;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosisWithNoFilter() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        int expectedStackLineHash = 12345678; // place holder for the expected hash value, to be determined through actual execution
        int expectedLineNumberHash = 98765432; // place holder for the expected hash value, to be determined through actual execution

        int actualStackLineHash = diagnosis.getStackLineHash();
        int actualLineNumberHash = diagnosis.getLineNumberHash();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
    }
    
    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new IOException("Test exception in com.example.MyClass");
        String[] inclusionFilter = {"com.example"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        int expectedStackLineHash = 11122334; // place holder for the expected hash value, to be determined through actual execution
        int expectedLineNumberHash = 55566789; // place holder for the expected hash value, to be determined through actual execution

        int actualStackLineHash = diagnosis.getStackLineHash();
        int actualLineNumberHash = diagnosis.getLineNumberHash();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
    }

    @Test
    public void testGetStackLineHashAndLineNumberHash() {
        Throwable throwable = new IOException("Test exception in com.example.MyClass");
        String[] inclusionFilter = {"com.example"};
        ExceptionDiagnosis diagnose = new ExceptionDiagnosis(throwable, inclusionFilter);

        int expectedStackLineHash = 135791193; // place holder for the expected hash value, to be determined through actual execution
        int expectedLineNumberHash = 2147483647; // place holder for the expected hash value, to be determined through actual execution
        int expectedStackLineHashWithFilter = 135791193; // place holder for the expected hash value with filter, to be determined through actual execution
        int expectedLineNumberHashWithFilter = 2147483647; // place holder for the expected hash value with filter, to be determined through actual execution

        int actualStackLineHash = diagnose.getStackLineHash();
        int actualLineNumberHash = diagnose.getLineNumberHash();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);

        StackTraceElement stackTraceElement = new Throwable(new IOException("Test exception in com.example.MyClass")).getStackTrace()[0];

        ExceptionDiagnosis diagnoseWithFilter = new ExceptionDiagnosis(new IOException("Test exception in com.example.MyClass"), new String[]{"com.example"});

        int actualStackLineHashWithFilter = diagnoseWithFilter.getStackLineHash();
        int actualLineNumberHashWithFilter = diagnoseWithFilter.getLineNumberHash();

        assertEquals(expectedStackLineHashWithFilter, actualStackLineHashWithFilter);
        assertEquals(expectedLineNumberHashWithFilter, actualLineNumberHashWithFilter);
    }
}