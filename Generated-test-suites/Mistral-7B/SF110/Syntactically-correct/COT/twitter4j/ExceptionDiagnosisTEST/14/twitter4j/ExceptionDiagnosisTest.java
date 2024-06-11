import org.junit.Assert;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @org.junit.Test
    public void testExceptionDiagnosisConstructionWithThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.getHexString());
        Assert.assertEquals(throwable, exceptionDiagnosis.getTh());
    }

    @org.junit.Test
    public void testExceptionDiagnosisConstructionWithThrowableAndFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.getHexString());
        Assert.assertEquals(throwable, exceptionDiagnosis.getTh());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructionWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @org.junit.Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        Assert.assertNotSame(0, stackLineHash);
    }

    @org.junit.Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        Assert.assertNotSame(0, lineNumberHash);
    }

    @org.junit.Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String hexString = exceptionDiagnosis.asHexString();
        Assert.assertNotNull(hexString);
    }

    @org.junit.Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        String[] inclusionFilter1 = new String[]{"com.test"};
        String[] inclusionFilter2 = new String[]{"com.test2"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter2);
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, inclusionFilter1);
        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @org.junit.Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String toString = exceptionDiagnosis.toString();
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}', toString);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testToStringWithNullExceptionDiagnosis() {
        ExceptionDiagnosis exceptionDiagnosis = null;
        String toString = exceptionDiagnosis.toString();
    }

    @org.junit.Test
    public void testToHexString() {
        int value = 12345678;
        String toHexString = ExceptionDiagnosis.toHexString(value);
        Assert.assertEquals("78563412", toHexString);
    }
}