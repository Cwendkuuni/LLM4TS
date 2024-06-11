package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExceptionDiagnosisTest {
    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        String expectedHexString = "XXXXXXXX-YYYYYYYY-IllegalArgumentException-Invalid argument"; // Replace X's with actual hex values
        String actualHexString = diagnosis.asHexString();
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testExceptionDiagnosisWithFilter() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"mys.package.PackageClass"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String expectedHexString = "XXXXX-YYYYYYYY-mys.package.PackageClass-someMethodName-linenumber"; // Replace X's with actual hex values
        String actualHexString = diagnosis.asHexString();
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = 0xAAAABBBB; // Replace A's and B's with actual hex values
        int actualStackLineHash = diagnosis.getStackLineHash();
        assertEquals(expectedStackLineHash, actualStackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        int expectedLineNumberHash = 0xCCCCDDDD; // Replace C's and D's with actual hex values
        int actualLineNumberHash = diagnosis.getLineNumberHash();
        assertEquals(expectedLineNumberHash, actualLineNumberHash);
    }
}