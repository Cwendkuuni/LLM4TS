import twitter4j.ExceptionDiagnosis;
import org.junit.Assert;
import org.junit.Test;

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
        String[] filter = new String[]{"com.test"};
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, filter);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement line : stackTrace) {
            if (line.getClassName().startsWith(filter[0])) {
                int expectedStackLineHash = 31 * "hashCodeOfClassName".hashCode() + line.getMethodName().hashCode();
                int expectedLineNumberHash = line.getLineNumber();

                Assert.assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
                Assert.assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
                Assert.assertEquals(exceptionDiagnosis.asHexString(), exceptionDiagnosis.toHexString(expectedStackLineHash) + "-" + expectedLineNumberHash);
                break;
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        StackTraceElement expectedStackTraceElement = throwable.getStackTrace()[0];
        int expectedStackLineHash = 31 * expectedStackTraceElement.getClassName().hashCode() + expectedStackTraceElement.getMethodName().hashCode();

        Assert.assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        StackTraceElement expectedStackTraceElement = throwable.getStackTrace()[0];
        int expectedLineNumberHash = expectedStackTraceElement.getLineNumber();

        Assert.assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        StackTraceElement expectedStackTraceElement = throwable.getStackTrace()[0];
        String expectedHexString = exceptionDiagnosis.toHexString(31 * expectedStackTraceElement.getClassName().hashCode() + expectedStackTraceElement.getMethodName().hashCode()) + "-" + Integer.toHexString(expectedStackTraceElement.getLineNumber().intValue());

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
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);

        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test Exception 1");
        Throwable throwable2 = new RuntimeException("Test Exception 2");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);

        StackTraceElement stackTraceElement1 = throwable1.getStackTrace()[0];
        StackTraceElement stackTraceElement2 = throwable2.getStackTrace()[0];

        int expectedHashCode1 = 31 * (31 * stackTraceElement1.getClassName().hashCode() + stackTraceElement1.getMethodName().hashCode()) + stackTraceElement1.getLineNumber();
        int expectedHashCode2 = 31 * (31 * stackTraceElement2.getClassName().hashCode() + stackTraceElement2.getMethodName().hashCode()) + stackTraceElement2.getLineNumber();

        Assert.assertEquals(expectedHashCode1, exceptionDiagnosis1.hashCode());
        Assert.assertEquals(expectedHashCode2, exceptionDiagnosis2.hashCode());

        Assert.assertNotEquals(expectedHashCode1, exceptionDiagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test Exception");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);

        StackTraceElement expectedStackTraceElement = throwable.getStackTrace()[0];

        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + (31 * expectedStackTraceElement.getClassName().hashCode() + expectedStackTraceElement.getMethodName().hashCode()) + ", lineNumberHash=" + expectedStackTraceElement.getLineNumber() + '}'
                + System.lineSeparator() + "ExceptionDiagnosis{stackLineHash=..., lineNumberHash=..., class=class twitter4j.ExceptionDiagnosis}";

        Assert.assertEquals(expectedToString, exceptionDiagnosis.toString());
    }
}