import org.junit.Test;
import org.junit. Assert.*; // for assertions
import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisCreationWithThrowable() {
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new TwitterException("Test Exception Message"));
 Assert.assertNotNull(ed.getStackLineHash());
 Assert.assertNotNull(ed.getLineNumberHash());
 Assert.assertNotNull(ed.getHexString());
 }

 @Test
 public void testExceptionDiagnosisCreationWithThrowableAndFilter() {
 String[] filter = new String[]{"twitter4j"};
 ExceptionDiagnosis ed = new ExceptionDiagnosis(new TwitterException("Test Exception Message"), filter);
 Assert.assertNotNull(ed.getStackLineHash());
 Assert.assertNotNull(ed.getLineNumberHash());
 Assert.assertNotNull(ed.getHexString());
 }

 @Test
 public void testStackLineHashAndLineNumberHashCalculation() {
 Throwable tw = new Throwable();
 StackTraceElement[] stackTrace = tw.getStackTrace();
 String[] filter = new String[]{"twitter4j"};

 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(tw, filter);
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(tw, filter);

 Assert.assertEquals(ed1.getStackLineHash(), ed2.getStackLineHash());
 Assert.assertEquals(ed1.getLineNumberHash(), ed2.getLineNumberHash());
 }

 @Test
 public void testHexStringGeneration() {
 Throwable tw = new Throwable();
 StackTraceElement[] stackTrace = tw.getStackTrace();
 String[] filter = new String[]{"twitter4j"};

 String hexString1 = new ExceptionDiagnosis(tw, filter).asHexString();
 String hexString2 = new ExceptionDiagnosis(tw, filter).asHexString();

 Assert.assertEquals(hexString1, hexString2);
 }

 @Test
 public void testToStringMethod() {
 Throwable tw = new Throwable();
 String[] filter = new String[]{"twitter4j"};

 String toString1 = new ExceptionDiagnosis(tw, filter).toString();
 String toString2 = new ExceptionDiagnosis(tw, filter).toString();

 Assert.assertEquals(toString1, toString2);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisCreationWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisCreationWithNullFilter() {
 new ExceptionDiagnosis(new Throwable(), null);
 }

}