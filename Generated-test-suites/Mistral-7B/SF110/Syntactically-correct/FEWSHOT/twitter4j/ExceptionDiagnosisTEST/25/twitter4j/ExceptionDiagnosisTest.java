package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisWithNullThrowable() {
        assertNull(new ExceptionDiagnosis(null));
    }

    @Test
    public void testExceptionDiagnosisWithEmptyInclusionFilter() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable);
        assertEquals("", ed.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithSingleClassInclusionFilter() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable, new String[]{"javax.net.ssl"});
        String expectedHexString = "1b02c15e3b64b761-3b692133"; // hexString of the StackTraceElement for javax.net.ssl.SSLSocketImpl.connect

        String actualHexString = ed.asHexString();
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testExceptionDiagnosisWithMultipleClassInclusionFilter() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable, new String[]{"javax.net.ssl", "java.io"});
        String expectedHexString = "1b028929363b2d33696d616e65742d326f6e64656e636f6465642d656e636f6465642d38363239343633393b64b761-3b692133-6578652d343233333139393939"; // hexString of the concatenated StackTraceElements for javax.net.ssl.SSLSocketImpl.connect and java.io.BufferedInputStream.close

        String actualHexString = ed.asHexString();
        assertEquals(expectedHexString, actualHexString);
    }

    @Test
    public void testExceptionDiagnosisWithNoMatchingClassInclusionFilter() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable, new String[]{"org.apache.commons.cli"});
        assertEquals("", ed.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithRecursiveThrowable() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        Throwable nestedThrowable = new IOException("Nested IOException");
        testThrowable.initCause(nestedThrowable);
        ExceptionDiagnosis e1 = new ExceptionDiagnosis(testThrowable);
        String firstHexString = e1.asHexString();

        ExceptionDiagnosis e2 = new ExceptionDiagnosis(nestedThrowable);
        String secondHexString = e2.asHexString();

        String expectedCombinedHexString = firstHexString + " " + secondHexString;
        String actualCombinedHexString = e1.asHexString();

        assertEquals(expectedCombinedHexString, actualCombinedHexString);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable);
        int expectedStackLineHash = 1313131; // a random value for illustration purposes
        int actualStackLineHash = ed.getStackLineHash();

        assertEquals(expectedStackLineHash, actualStackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable);
        int expectedLineNumberHash = 1313131; // a random value for illustration purposes
        int actualLineNumberHash = ed.getLineNumberHash();

        assertEquals(expectedLineNumberHash, actualLineNumberHash);
    }

    @Test
    public void testEquals() {
        Throwable testThrowable1 = new RuntimeException("Test Exception 1");
        Throwable testThrowable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(testThrowable1);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(testThrowable2);

        assertFalse(ed1.equals(ed2));

        Throwable testThrowable3 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(testThrowable1);

        assertTrue(ed1.equals(ed3));
    }

    @Test
    public void testHashCode() {
        Throwable testThrowable1 = new RuntimeException("Test Exception 1");
        Throwable testThrowable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(testThrowable1);
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(testThrowable2);

        int expectedHashCode1 = 1313131; // a random value for illustration purposes
        int actualHashCode1 = ed1.hashCode();

        int expectedHashCode2 = 1314131; // a different random value for illustration purposes
        int actualHashCode2 = ed2.hashCode();

        assertNotEquals(expectedHashCode1, actualHashCode2);

        int expectedHashCode3 = expectedHashCode1;
        int actualHashCode3 = ed1.hashCode();

        assertEquals(expectedHashCode3, actualHashCode3);
    }

    @Test
    public void testToString() {
        Throwable testThrowable = new RuntimeException("Test Exception");
        ExceptionDiagnosis ed = new ExceptionDiagnosis(testThrowable);
        String expectedString = "ExceptionDiagnosis{stackLineHash=1313131, lineNumberHash=1313131}";
        String actualString = ed.toString();

        assertEquals(expectedString, actualString);
    }
}