package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;
import twitter4j.twitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructorWithThrowable() {
        Throwable throwable = new twitterException("Test Exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndEmptyFilter() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.asHexString());
    }

    @Test
    public void testExceptionDiagnosisConstructorWithThrowableAndFilter() {
        Throwable throwable = new twitterException("Test Exception");
        String filter = "com.example.MyClass";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{filter});
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement match = null;
        for (StackTraceElement line : stackTrace) {
            if (line.getClassName().startsWith(filter)) {
                match = line;
                break;
            }
        }
        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.asHexString());
        Assert.assertEquals(match.getClassName(), diagnosis.getClassNames()[0]);
        Assert.assertEquals(match.getMethodName(), diagnosis.getMethodNames()[0]);
        Assert.assertEquals(match.getLineNumber(), diagnosis.getLineNumbers()[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisConstructorWithNullThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = new String[]{"com.example.MyClass"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(diagnosis.getStackLineHash());
        Throwable cause = new IOException("Caused by IOException");
        diagnosis.th.initCause(cause);
        ExceptionDiagnosis diagnosisCause = new ExceptionDiagnosis(diagnosis.th, inclusionFilter);
        Assert.assertEquals(diagnosis.getStackLineHash(), diagnosisCause.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = new String[]{"com.example.MyClass"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Throwable cause = new IOException("Caused by IOException");
        diagnosis.th.initCause(cause);
        ExceptionDiagnosis diagnosisCause = new ExceptionDiagnosis(diagnosis.th, inclusionFilter);
        Assert.assertEquals(diagnosis.getLineNumberHash(), diagnosisCause.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = new String[]{"com.example.MyClass"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(diagnosis.asHexString());
        Throwable cause = new IOException("Caused by IOException");
        diagnosis.th.initCause(cause);
        ExceptionDiagnosis diagnosisCause = new ExceptionDiagnosis(diagnosis.th, inclusionFilter);
        String diagnosisHex = diagnosis.asHexString();
        String diagnosisCauseHex = diagnosisCause.asHexString();
        Assert.assertEquals(diagnosis.asHexString(), diagnosisCause.asHexString());
        Assert.assertTrue(diagnosisHex.contains(diagnosis.toHexString(diagnosis.getStackLineHash())) &&
                diagnosisHex.contains(diagnosis.toHexString(diagnosis.getLineNumberHash())));
        Assert.assertTrue(diagnosisCauseHex.contains(diagnosisCause.toHexString(diagnosisCause.getStackLineHash())) &&
                diagnosisCauseHex.contains(diagnosisCause.toHexString(diagnosisCause.getLineNumberHash())));
    }

    @Test
    public void testToString() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = new String[]{"com.example.MyClass"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        Assert.assertNotNull(diagnosis.toString());
        Assert.assertTrue(diagnosis.toString().contains("ExceptionDiagnosis{stackLineHash=") &&
                diagnosis.toString().contains("lineNumberHash="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDiagnosisConstructorWithNullFilter() {
        Throwable throwable = new twitterException("Test Exception");
        String[] inclusionFilter = null;
        new ExceptionDiagnosis(throwable, inclusionFilter);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new twitterException("Test Exception 1");
        Throwable throwable2 = new twitterException("Test Exception 2");
        String[] inclusionFilter = new String[]{"com.example.MyClass"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter);
        Assert.assertFalse(diagnosis1.equals(diagnosis2));
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(diagnosis1);
        Assert.assertTrue(diagnosis1.equals(diagnosis3));
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(diagnosis1);
        Assert.assertTrue(diagnosis1.equals(diagnosis4));
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new twitterException("Test Exception 1");
        Throwable throwable2 = new twitterException("Test Exception 2");
        String[] inclusionFilter = new String[]{"com.example.MyClass"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter);
        Assert.assertFalse(diagnosis1.hashCode() == diagnosis2.hashCode());
        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(diagnosis1);
        Assert.assertEquals(diagnosis1.hashCode(), diagnosis3.hashCode());
        ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(diagnosis1);
        Assert.assertEquals(diagnosis1.hashCode(), diagnosis4.hashCode());
    }
}