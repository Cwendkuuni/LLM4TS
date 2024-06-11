package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new TwitterException("Test Error");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(diagnosis);
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilterArray() {
        Throwable throwable = new TwitterException("Test Error");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        Assert.assertNotNull(diagnosis);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new TwitterException("Test Error");
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        int expectedStackLineHash = calculateExpectedStackLineHash(stackTrace[0]);
        int actualStackLineHash = diagnosis.getStackLineHash();
        Assert.assertEquals(expectedStackLineHash, actualStackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new TwitterException("Test Error");
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        int expectedLineNumberHash = stackTraceElement.getLineNumber();
        int actualLineNumberHash = diagnosis.getLineNumberHash();
        Assert.assertEquals(expectedLineNumberHash, actualLineNumberHash);
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new TwitterException("Test Error");
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        String expectedHexString = generateExpectedHexString(diagnosis);
        String actualHexString = diagnosis.asHexString();
        Assert.assertEquals(expectedHexString, actualHexString);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsObjectDifferentTypes() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Throwable("Test Error"));
        Object obj = new Object();
        ExceptionDiagnosis diagnosis2 = diagnosis1;
        Assert.assertNotEquals(diagnosis1, obj);
        Assert.assertNotEquals(diagnosis1, null);
        Assert.assertEquals(diagnosis1, diagnosis2);
    }

    @Test
    public void testEqualsObjectSameInstance() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Throwable("Test Error"));
        Assert.assertEquals(diagnosis1, diagnosis1);
    }

    @Test
    public void testEqualsObjectSameValues() {
        Throwable throwable1 = new TwitterException("Test Error 1");
        StackTraceElement stackTraceElement1 = throwable1.getStackTrace()[0];
        Throwable throwable2 = new TwitterException("Test Error 1");
        StackTraceElement stackTraceElement2 = throwable2.getStackTrace()[0];
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, new String[]{"test.package"});
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, new String[]{"test.package"});
        Assert.assertEquals(diagnosis1, diagnosis2);
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new TwitterException("Test Error");
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        int expectedHashCode = calculateExpectedHashCode(stackTraceElement);
        int actualHashCode1 = diagnosis1.hashCode();
        int actualHashCode2 = diagnosis2.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode1);
        Assert.assertEquals(expectedHashCode, actualHashCode2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new TwitterException("Test Error");
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"test.package"});
        String expectedToString = "ExceptionDiagnosis{stackLineHash=" + calculateExpectedStackLineHash(stackTraceElement) + ", lineNumberHash=" + stackTraceElement.getLineNumber() + '}';
        String actualToString = diagnosis.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullFilterArray() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable("Test Error"), null);
    }

    private int calculateExpectedStackLineHash(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        int hashCodeClassName = className.hashCode();
        int hashCodeMethodName = methodName.hashCode();
        return 31 * (31 * hashCodeClassName + hashCodeMethodName);
    }

    private int calculateExpectedHashCode(StackTraceElement stackTraceElement) {
        int hashCodeClassName = stackTraceElement.getClassName().hashCode();
        int hashCodeMethodName = stackTraceElement.getMethodName().hashCode();
        int lineNumber = stackTraceElement.getLineNumber();
        return 31 * (31 * hashCodeClassName + hashCodeMethodName) + lineNumber;
    }

    private String generateExpectedHexString(ExceptionDiagnosis diagnosis) {
        int stackLineHash = diagnosis.getStackLineHash();
        int lineNumberHash = diagnosis.getLineNumberHash();
        String hexString = diagnosis.asHexString();
        if (diagnosis.th.getCause() != null) {
            ExceptionDiagnosis causeDiagnosis = new ExceptionDiagnosis(diagnosis.th.getCause(), new String[]{"test.package"});
            String causeHexString = causeDiagnosis.asHexString();
            return hexString + " " + causeHexString;
        }
        return hexString;
    }
}