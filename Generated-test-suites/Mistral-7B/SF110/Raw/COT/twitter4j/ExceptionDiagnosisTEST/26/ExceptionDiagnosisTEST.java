import org.junit.Assert;
import org.junit.Test;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructorWithThrowable() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndEmptyFilter() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = new String[0];
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        Assert.assertNotNull(diagnosis.getStackLineHash());
        Assert.assertNotNull(diagnosis.getLineNumberHash());
        Assert.assertNotNull(diagnosis.asHexString());
    }

    @Test
    public void testConstructorWithThrowableAndFilter() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"java.lang"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String expectedClassName = stackTrace[0].getClassName();

        Assert.assertTrue(expectedClassName.startsWith(inclusionFilter[0]));
        Assert.assertEquals(diagnosis.getStackLineHash(), calculateHashCode(stackTrace[0]));
        Assert.assertEquals(diagnosis.getLineNumberHash(), stackTrace[0].getLineNumber());
        Assert.assertEquals(diagnosis.asHexString(), diagnosis.toHexString(diagnosis.getStackLineHash()) + "-" + diagnosis.getLineNumberHash());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullThrowable() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(null);
    }

    @Test
    public void testGetStackLineHash() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"java.lang"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int expectedHashCode = calculateHashCode(stackTrace[0]);

        Assert.assertEquals(diagnosis.getStackLineHash(), expectedHashCode);
    }

    @Test
    public void testGetLineNumberHash() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"java.lang"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        StackTraceElement stackTrace = throwable.getStackTrace()[0];

        Assert.assertEquals(diagnosis.getLineNumberHash(), stackTrace.getLineNumber());
    }

    @Test
    public void testAsHexString() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"java.lang"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        Assert.assertNotNull(diagnosis.asHexString());
        Assert.assertTrue(diagnosis.asHexString().matches("^[0-9A-F]{32}(\\s+[0-9A-F]{32})*$"));
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new IllegalArgumentException("Invalid argument 1");
        Throwable throwable2 = new IllegalArgumentException("Invalid argument 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1);

        Assert.assertTrue(diagnosis1.equals(diagnosis3));
        Assert.assertFalse(diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"java.lang"};
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable, inclusionFilter);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, inclusionFilter);

        Assert.assertEquals(diagnosis1.hashCode(), diagnosis2.hashCode());
    }

    @Test
    public void testToString() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String[] inclusionFilter = {"java.lang"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        Assert.assertEquals(diagnosis.toString(), "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}');
    }

    private int calculateHashCode(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();

        int hashCode = className.hashCode();
        hashCode = 31 * hashCode + methodName.hashCode();
        hashCode = 31 * hashCode + stackTraceElement.getLineNumber();

        return hashCode;
    }
}