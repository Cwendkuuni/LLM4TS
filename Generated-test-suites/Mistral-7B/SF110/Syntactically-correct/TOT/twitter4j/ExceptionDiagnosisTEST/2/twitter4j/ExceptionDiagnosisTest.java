package twitter4j.test;

import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new IOException("Test exception");
        ExceptionDiagnosis diagnostic = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 12345678;
        int expectedLineNumberHash = 98765432;
        String expectedHexString = "ABCDEF01-23456789";

        int actualStackLineHash = diagnostic.getStackLineHash();
        int actualLineNumberHash = diagnostic.getLineNumberHash();
        String actualHexString = diagnostic.asHexString();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        Throwable throwable = new IOException("Test exception");
        String[] filter = new String[]{"com.example."};
        ExceptionDiagnosis diagnostic = new ExceptionDiagnosis(throwable, filter);
        int expectedStackLineHash = 23456789;
        int expectedLineNumberHash = 87654321;
        String expectedHexString = "CDEFAB01-23456789-34567890";

        int actualStackLineHash = diagnostic.getStackLineHash();
        int actualLineNumberHash = diagnostic.getLineNumberHash();
        String actualHexString = diagnostic.asHexString();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testGetStackLineHashAndLineNumberHash() {
        ExceptionDiagnosis diagnostic = new ExceptionDiagnosis(new IOException("Test exception"));
        int expectedStackLineHash = 12345678;
        int expectedLineNumberHash = 98765432;

        int actualStackLineHash = diagnostic.getStackLineHash();
        int actualLineNumberHash = diagnostic.getLineNumberHash();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);

        diagnostic = new ExceptionDiagnosis(new IOException("Test exception"), new String[]{"com.example."});
        expectedStackLineHash = 23456789;
        expectedLineNumberHash = 87654321;

        actualStackLineHash = diagnostic.getStackLineHash();
        actualLineNumberHash = diagnostic.getLineNumberHash();

        assertEquals(expectedStackLineHash, actualStackLineHash);
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
    }

    @Test
    public void testAsHexString() {
        ExceptionDiagnosis diagnostic = new ExceptionDiagnosis(new IOException("Test exception"));
        String expectedHexString = "ABCDEF01-23456789";

        String actualHexString = diagnostic.asHexString();

        assertEquals(expectedHexString, actualHexString);

        diagnostic = new ExceptionDiagnosis(new IOException("Test exception"), new String[]{"com.example."});
        expectedHexString = "CDEFAB01-23456789-34567890";

        actualHexString = diagnostic.asHexString();

        assertEquals(expectedHexString, actualHexString);
    }
}