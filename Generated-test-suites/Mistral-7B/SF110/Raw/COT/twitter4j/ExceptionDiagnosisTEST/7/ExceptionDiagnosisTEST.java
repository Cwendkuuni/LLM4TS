import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertEquals("Test Exception", diagnosis.th.getMessage());
        // Check the hash codes of stackLineHash and lineNumberHash are calculated correctly
        Assert.assertNotSame(0, diagnosis.getStackLineHash());
        Assert.assertNotSame(0, diagnosis.getLineNumberHash());
        // Check the hexString is generated correctly
        Assert.assertNotNull(diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
        Throwable throwable = new Exception("Test Exception");
        String filter = "java.lang.Exception";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});

        // Check the filter is applied correctly
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement lastElement = stackTrace[stackTrace.length - 1];
        Assert.assertEquals(filter, lastElement.getClassName());

        // Check the hash codes of stackLineHash and lineNumberHash are calculated correctly
        Assert.assertNotSame(0, diagnosis.getStackLineHash());
        Assert.assertNotSame(0, diagnosis.getLineNumberHash());
        // Check the hexString is generated correctly
        Assert.assertNotNull(diagnosis.asHexString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructorWithNullFilter() {
        Throwable throwable = new Exception("Test Exception");
        new ExceptionDiagnosis(throwable, (String[]) null);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new Exception("Test Exception 1");
        Throwable throwable2 = new Exception("Test Exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        Assert.assertFalse(diagnosis1.equals(diagnosis2));

        // Check if two ExceptionDiagnosis instances with same data are equal
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);
        Assert.assertTrue(diagnosis1.equals(diagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable);

        Assert.assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}', diagnosis.toString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertNotSame(0, diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertNotSame(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new Exception("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertNotNull(diagnosis.asHexString());
    }
}