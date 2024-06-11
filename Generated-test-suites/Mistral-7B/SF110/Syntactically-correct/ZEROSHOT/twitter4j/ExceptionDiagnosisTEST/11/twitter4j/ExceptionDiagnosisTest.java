import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.Throwable;
import java.util.Arrays;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(exceptionDiagnosis);
        Assert.assertEquals("", exceptionDiagnosis.getHexString());
        Assert.assertNull(exceptionDiagnosis.getStackTrace());
        Assert.assertNotNull(exceptionDiagnosis.getTh());
        Assert.assertEquals(0, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(0, exceptionDiagnosis.getLineNumberHash());

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(exceptionDiagnosisWithFilter);
        Assert.assertEquals("", exceptionDiagnosisWithFilter.getHexString());
        Assert.assertNotNull(exceptionDiagnosisWithFilter.getTh());
        Assert.assertArrayNotSame(stackTrace, exceptionDiagnosisWithFilter.getStackTrace());

        int expectedStackLineHash = 0;
        int expectedLineNumberHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            expectedStackLineHash = 31 * expectedStackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
            expectedLineNumberHash = 31 * expectedLineNumberHash + stackTraceElement.getLineNumber();
        }
        Assert.assertEquals(expectedStackLineHash, exceptionDiagnosisWithFilter.getStackLineHash());
        Assert.assertEquals(expectedLineNumberHash, exceptionDiagnosisWithFilter.getLineNumberHash());
        Assert.assertEquals(exceptionDiagnosisWithFilter.getHexString(), exceptionDiagnosisWithFilter.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedStackLineHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            expectedStackLineHash = 31 * expectedStackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
        }
        Assert.assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());

        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, inclusionFilter);
        int expectedStackLineHashWithFilter = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith("java.io")) {
                expectedStackLineHashWithFilter = 31 * expectedStackLineHashWithFilter + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
            }
        }
        Assert.assertEquals(expectedStackLineHashWithFilter, exceptionDiagnosisWithFilter.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedLineNumberHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            expectedLineNumberHash = 31 * expectedLineNumberHash + stackTraceElement.getLineNumber();
        }
        Assert.assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());

        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, inclusionFilter);
        int expectedLineNumberHashWithFilter = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getClassName().startsWith("java.io")) {
                expectedLineNumberHashWithFilter += stackTraceElement.getLineNumber();
            }
        }
        Assert.assertEquals(expectedLineNumberHashWithFilter, exceptionDiagnosisWithFilter.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertEquals("", exceptionDiagnosis.getHexString());

        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, inclusionFilter);
        String expectedHexString = "";
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; --i) {
            StackTraceElement stackTraceElement = stackTrace[i];
            if (stackTraceElement.getClassName().startsWith("java.io")) {
                expectedHexString += toHexString(stackTraceElement.getClassName().hashCode()) + "-";
                expectedHexString += toHexString(stackTraceElement.getMethodName().hashCode());
                if (i > 0) {
                    expectedHexString += " ";
                }
            }
        }
        Assert.assertEquals(expectedHexString, exceptionDiagnosisWithFilter.getHexString());
    }

    @Test(expected = AssertionError.class)
    public void testEquals() {
        Throwable throwable1 = new IOException("Test IOException 1");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        Throwable throwable2 = new IOException("Test IOException 2");
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1);
        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable);
        Assert.assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        StackTraceElement stackTraceElement = new ExceptionInInitializerError("Test IOException").getStackTrace()[0];
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new ExceptionInInitializerError("Test IOException"), new String[]{"java.lang"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(new ExceptionInInitializerError("Test IOException"), new String[]{"java.lang"});
        Assert.assertEquals(exceptionDiagnosis3.hashCode(), exceptionDiagnosis4.hashCode());
        Assert.assertNotEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis3.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new IOException("Test IOException");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());

        String[] inclusionFilter = new String[]{"java.io"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(throwable, inclusionFilter);
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + toHexString(getStackLineHash(stackTrace)) + ", lineNumberHash=" + toHexString(getLineNumberHash(stackTrace)) + '}';
        Assert.assertEquals(expectedToString, exceptionDiagnosisWithFilter.toString());
    }

    private int getStackLineHash(StackTraceElement[] stackTrace) {
        int stackLineHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            stackLineHash = 31 * stackLineHash + stackTraceElement.getClassName().hashCode() + stackTraceElement.getMethodName().hashCode();
        }
        return stackLineHash;
    }

    private int getLineNumberHash(StackTraceElement[] stackTrace) {
        int lineNumberHash = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            lineNumberHash = 31 * lineNumberHash + stackTraceElement.getLineNumber();
        }
        return lineNumberHash;
    }

    private String toHexString(int value) {
        return "0000000" + Integer.toHexString(value).substring(Integer.toHexString(value).length() - 8);
    }
}