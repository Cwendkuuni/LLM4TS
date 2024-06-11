package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.Twouble; // assuming Twouble is a Throwable subclass used in the ExceptionDiagnosis constructor

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithSingleThrowable() {
        Throwable th = new Twouble("Test exception message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);

        Assert.assertNotNull(exceptionDiagnosis.getStackLineHash());
        Assert.assertNotNull(exceptionDiagnosis.getLineNumberHash());
        Assert.assertNotNull(exceptionDiagnosis.asHexString());
        Assert.assertEquals(th.getMessage(), exceptionDiagnosis.th.getMessage());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithEmptyFilter() {
        Throwable th = new Twouble("Test exception message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th);

        String hexString = exceptionDiagnosis.asHexString();
        String[] stackTraceElements = th.getStackTrace();

        // Check if the exact stack trace element is present in the hexString
        int lastDashIndex = hexString.lastIndexOf("-");
        int hexStringWithoutLastDashLength = lastDashIndex > 0 ? lastDashIndex : hexString.length();
        String hexStringWithoutLastDash = hexString.substring(0, hexStringWithoutLastDashLength);
        StackTraceElement lastStackTraceElement = stackTraceElements[stackTraceElements.length - 1];

        String expectedClassName = lastStackTraceElement.getClassName();
        String expectedMethodName = lastStackTraceElement.getMethodName();

        int stackLineHash = Integer.parseInt(hexStringWithoutLastDash.substring(0, 8), 16);
        int lineNumberHash = Integer.parseInt(hexStringWithoutLastDash.substring(8), 16);

        String classNameHash = Integer.toHexString(expectedClassName.hashCode()).substring(0, 8);
        String methodNameHash = Integer.toHexString(expectedMethodName.hashCode()).substring(0, 8);

        int expectedStackLineHash = (31 * 31 * stackLineHash.charAt(0) + 31 * stackLineHash.charAt(1) + stackLineHash.charAt(2)) * 31 + (31 * 31 * classNameHash.charAt(0) + 31 * classNameHash.charAt(1) + classNameHash.charAt(2)) * 31 + (31 * 31 * methodNameHash.charAt(0) + 31 * methodNameHash.charAt(1) + methodNameHash.charAt(2));
        int expectedLineNumberHash = 31 * 31 * lineNumberHash.charAt(0) + 31 * lineNumberHash.charAt(1) + lineNumberHash.charAt(2);

        Assert.assertEquals(Integer.toHexString(expectedStackLineHash).substring(0, 8), Integer.toHexString(exceptionDiagnosis.getStackLineHash()).substring(0, 8));
        Assert.assertEquals(Integer.toHexString(expectedLineNumberHash).substring(0, 8), Integer.toHexString(exceptionDiagnosis.getLineNumberHash()).substring(0, 8));
    }

    @Test
    public void testExceptionDiagnosisConstructorWithFilter() {
        String inclusionFilter = "com.example";
        Throwable th = new Twouble("Test exception message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, new String[]{inclusionFilter});

        String hexString = exceptionDiagnosis.asHexString();
        String[] stackTraceElements = th.getStackTrace();

        // Check if the stack trace element with the given filter is present in the hexString
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().startsWith(inclusionFilter)) {
                int lastDashIndex = hexString.lastIndexOf("-");
                int hexStringWithoutLastDashLength = lastDashIndex > 0 ? lastDashIndex : hexString.length();
                String hexStringWithoutLastDash = hexString.substring(0, hexStringWithoutLastDashLength);

                int stackLineHash = Integer.parseInt(hexStringWithoutLastDash.substring(0, 8), 16);
                int lineNumberHash = Integer.parseInt(hexStringWithoutLastDash.substring(8), 16);

                String classNameHash = Integer.toHexString(stackTraceElement.getClassName().hashCode()).substring(0, 8);
                String methodNameHash = Integer.toHexString(stackTraceElement.getMethodName().hashCode()).substring(0, 8);

                int expectedStackLineHash = (31 * 31 * stackLineHash.charAt(0) + 31 * stackLineHash.charAt(1) + stackLineHash.charAt(2)) * 31 + (31 * 31 * classNameHash.charAt(0) + 31 * classNameHash.charAt(1) + classNameHash.charAt(2)) * 31 + (31 * 31 * methodNameHash.charAt(0) + 31 * methodNameHash.charAt(1) + methodNameHash.charAt(2));
                int expectedLineNumberHash = 31 * 31 * lineNumberHash.charAt(0) + 31 * lineNumberHash.charAt(1) + lineNumberHash.charAt(2);

                Assert.assertEquals(Integer.toHexString(expectedStackLineHash).substring(0, 8), Integer.toHexString(exceptionDiagnosis.getStackLineHash()).substring(0, 8));
                Assert.assertEquals(Integer.toHexString(expectedLineNumberHash).substring(0, 8), Integer.toHexString(exceptionDiagnosis.getLineNumberHash()).substring(0, 8));
                break;
            }
        }
    }

    @Test(expected = AssertionError.class)
    public void testExceptionDiagnosisConstructorWithNullFilter() {
        Throwable th = new Twouble("Test exception message");
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(th, null);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testEqualsMethod() {
        Throwable th1 = new Twouble("Test exception message 1");
        Throwable th2 = new Twouble("Test exception message 2");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);

        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        Throwable th3 = new Twouble("Test exception message 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(th3);

        Assert.assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis3));
    }

    @Test
    public void testHashCodeMethod() {
        Throwable th1 = new Twouble("Test exception message 1");
        Throwable th2 = new Twouble("Test exception message 2");

        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(th2);

        Assert.assertFalse(exceptionDiagnosis1.hashCode() == exceptionDiagnosis2.hashCode());

        Throwable th3 = new Twouble("Test exception message 1");
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(th3);

        Assert.assertTrue(exceptionDiagnosis1.hashCode() == exceptionDiagnosis3.hashCode());
    }
}