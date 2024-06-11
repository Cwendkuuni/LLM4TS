import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int stackLineHash = exceptionDiagnosis.getStackLineHash();
        Assert.assertNotSame(0, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
        Assert.assertNotSame(0, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String hexString = exceptionDiagnosis.asHexString();
        Assert.assertNotNull(hexString);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter);
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable, inclusionFilter);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String toString = exceptionDiagnosis.toString();
        Assert.assertNotNull(toString);
    }

    @Test
    public void testExceptionDiagnosisWithEmptyFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[0]);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisWithNullFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = null;
        try {
            ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        } catch (NullPointerException e) {
            // Expected
            Assert.assertEquals("filter is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testGetCauseWithNullThrowable() {
        Throwable throwable = null;
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNull(exceptionDiagnosis.th.getCause());
    }

    @Test
    public void testToStringWithNullThrowable() {
        Throwable throwable = null;
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String toString = exceptionDiagnosis.toString();
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", toString);
    }

    @Test
    public void testToStringWithEmptyStackTrace() {
        Throwable throwable = new RuntimeException("Test Exception");
        throwable.setStackTrace(new StackTraceElement[0]);
        String[] inclusionFilter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        String toString = exceptionDiagnosis.toString();
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", toString);
    }

}