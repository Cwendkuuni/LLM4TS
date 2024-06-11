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
        String filter = "test";
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedStackLineHash = 0;
        int expectedLineNumberHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith(filter)) {
                expectedStackLineHash = 31 * expectedStackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
                expectedLineNumberHash = 31 * expectedLineNumberHash + stackTraceElement.getLineNumber();
            }
        }
        Assert.assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedStackLineHash = // Calculate expected stackLineHash based on the given stackTrace
        Assert.assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        int expectedLineNumberHash = // Calculate expected lineNumberHash based on the given stackTrace
        Assert.assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedHexString = // Calculate expected hexString based on the given stackTrace and lineNumberHash
        Assert.assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable throwable3 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        Assert.assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));

        Throwable throwable4 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable4, new String[]{"test"});
        Assert.assertFalse(exceptionDiagnosis5.equals(exceptionDiagnosis6));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        Assert.assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        Throwable throwable3 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        Assert.assertEquals(exceptionDiagnosis3.hashCode(), exceptionDiagnosis4.hashCode());

        Throwable throwable4 = new RuntimeException("Test Exception 1");
        ExceptionDiagnosis exceptionDiagnosis5 = new ExceptionDiagnosis(throwable1, new String[]{"test"});
        ExceptionDiagnosis exceptionDiagnosis6 = new ExceptionDiagnosis(throwable4, new String[]{"test"});
        Assert.assertNotEquals(exceptionDiagnosis5.hashCode(), exceptionDiagnosis6.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + exceptionDiagnosis.getStackLineHash() + ", lineNumberHash=" + exceptionDiagnosis.getLineNumberHash() + '}';
        Assert.assertEquals(expectedToString, exceptionDiagnosis.toString());
    }
}