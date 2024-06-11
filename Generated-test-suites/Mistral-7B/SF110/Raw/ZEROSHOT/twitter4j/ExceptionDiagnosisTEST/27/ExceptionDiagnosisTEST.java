package twitter4j.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstruction() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException, InvocationTargetException {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test exception message"));
        Assert.assertEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosis.toString());

        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(new TwitterException("Test exception message"), inclusionFilter);
        Assert.assertNotEquals("ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}", exceptionDiagnosisWithFilter.toString());
    }

    @Test
    public void testGetStackLineHashAndLineNumberHash() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test exception message"));
        Assert.assertEquals(0, exceptionDiagnosis.getStackLineHash());
        Assert.assertEquals(0, exceptionDiagnosis.getLineNumberHash());

        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(new TwitterException("Test exception message"), inclusionFilter);
        Assert.assertNotEqual(0, exceptionDiagnosisWithFilter.getStackLineHash());
        Assert.assertNotEqual(0, exceptionDiagnosisWithFilter.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test exception message"));
        Assert.assertEquals("", exceptionDiagnosis.asHexString());

        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(new TwitterException("Test exception message"), inclusionFilter);
        Assert.assertNotEmpty(exceptionDiagnosisWithFilter.asHexString());
    }

    @Test
    public void testToHexString() {
        int value = 12345;
        String expected = "1234bc";
        String result = new ExceptionDiagnosis().toHexString(value);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test exception message 1"));
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test exception message 2"));
        Assert.assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new TwitterException("Test exception message 1"), new String[]{"twitter4j"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(new TwitterException("Test exception message 1"), new String[]{"twitter4j"});
        Assert.assertTrue(exceptionDiagnosis3.equals(exceptionDiagnosis4));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test exception message 1"));
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test exception message 2"));
        Assert.assertNotEqual(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());

        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(new TwitterException("Test exception message 1"), new String[]{"twitter4j"});
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(new TwitterException("Test exception message 1"), new String[]{"twitter4j"});
        Assert.assertEquals(exceptionDiagnosis3.hashCode(), exceptionDiagnosis4.hashCode());
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test exception message"));
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        String result = exceptionDiagnosis.toString();
        Assert.assertEquals(expected, result);

        String[] inclusionFilter = new String[]{"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosisWithFilter = new ExceptionDiagnosis(new TwitterException("Test exception message"), inclusionFilter);
        String resultWithFilter = exceptionDiagnosisWithFilter.toString();
        String expectedWithFilter = "ExceptionDiagnosis{stackLineHash=..., lineNumberHash=...}";
        Assert.assertTrue(resultWithFilter.contains(expectedWithFilter));
    }

    @Test
    public void testGetCause() throws IOException, NoSuchMethodException, InvocationTargetException {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test exception message"));
        ExceptionDiagnosis causeExceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test cause exception message"), new String[]{"twitter4j"});
        ExceptionDiagnosis exceptionDiagnosisWithCause = new ExceptionDiagnosis(new TwitterException("Test exception message"), new String[]{"twitter4j"}, causeExceptionDiagnosis);

        Assert.assertNull(exceptionDiagnosis.getCause());
        Assert.assertEquals(exceptionDiagnosisWithCause.asHexString(), exceptionDiagnosis.asHexString() + " " + causeExceptionDiagnosis.asHexString());
    }

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws IllegalAccessException {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInclusionFilter() {
        String[] invalidFilter = null;
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test exception message"), invalidFilter);
    }

    @Test(expected = NullPointerException.class)
    public void testNullThrowable() {
        ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null);
    }
}