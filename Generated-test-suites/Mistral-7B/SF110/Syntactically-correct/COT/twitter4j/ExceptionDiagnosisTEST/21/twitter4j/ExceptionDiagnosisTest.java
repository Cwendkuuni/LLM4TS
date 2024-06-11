package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.ThrowableWithStackTrace;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        String[] inclusionFilter = new String[]{"com.example"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"com.example"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable3, new String[]{"com.example"});
        Assert.assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));

        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);
        Assert.assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable2);
        Assert.assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }

    @Test
    public void testConstructorWithThrowableAndNullFilter() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, null);
        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }

    @Test
    public void testToStringWithLongStackTrace() {
        String className = "com.example.long.class.name";
        String methodName = "longMethodName";
        String[] stackTrace = new String[100];
        for (int i = 0; i < stackTrace.length; i++) {
            stackTrace[i] = className + "." + methodName + "(" + (i + 1) + ")";
        }
        Throwable throwable = new ThrowableWithStackTrace(new RuntimeException("Test Exception"), stackTrace);
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{"com.example"});
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}", exceptionDiagnosis.toString());
    }
}