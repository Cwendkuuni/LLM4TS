package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;

import twitter4j.ExceptionDiagnosis;
import twitter4j.TwitterException;
import java.io.IOException;
import java.lang.reflect.UndeclaredException;

public class ExceptionDiagnosisTest {

 @Test
 public void testExceptionDiagnosisConstructionWithThrowable() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"));
 assertNotNull(exceptionDiagnosis);
 }

 @Test
 public void testExceptionDiagnosisConstructionWithThrowableAndFilterArray() {
 String[] inclusionFilter = {"twitter4j"};
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), inclusionFilter);
 assertNotNull(exceptionDiagnosis);
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionDiagnosisConstructionWithNullFilterArray() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), null);
 fail("Exception should be thrown when filter array is null");
 }

 @Test
 public void testGetStackLineHash() {
 StackTraceElement stackTraceElement = new StackTraceElement("twitter4j.ExceptionDiagnosisTest", "testGetStackLineHash", "ExceptionDiagnosisTest.java", 45);
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 int stackLineHash = exceptionDiagnosis.getStackLineHash();
 assertEquals(stackLineHash, 31 * 0x43354561L + 0x74737265L + 0x65746869L + 0x6C696262L + 0x7368616DL + 0x73746572L + 0x6573696FL + 0x6E666963L + 0x616C6964L);
 }

 @Test
 public void testGetLineNumberHash() {
 StackTraceElement stackTraceElement = new StackTraceElement("twitter4j.ExceptionDiagnosisTest", "testGetLineNumberHash", "ExceptionDiagnosisTest.java", 45);
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
 assertEquals(lineNumberHash, 45);
 }

 @Test
 public void testAsHexString() {
 StackTraceElement stackTraceElement = new StackTraceElement("twitter4j.ExceptionDiagnosisTest", "testAsHexString", "ExceptionDiagnosisTest.java", 45);
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 String hexString = exceptionDiagnosis.asHexString();
 String expectedHexString = "7368616D-73746572-3143354561696C69-6C6962627368616D-317473726561696C-6C6962627368616D-6573696F6E666963-616C6964";
 assertEquals(hexString, expectedHexString);
 }

 @Test
 public void testToString() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 String toString = exceptionDiagnosis.toString();
 String expectedToString = "ExceptionDiagnosis{stackLineHash=12345678, lineNumberHash=45}";
 assertEquals(toString, expectedToString);
 }

 @Test
 public void testEqualsObject() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 assertTrue(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testNotEqualsObjectDifferentObject() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 Object obj = new Object();
 assertFalse(exceptionDiagnosis1.equals(obj));
 }

 @Test
 public void testNotEqualsObjectDifferentStackLineHash() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"org.junit"});
 assertFalse(exceptionDiagnosis1.equals(exceptionDiagnosis2));
 }

 @Test
 public void testHashCode() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 assertEquals(exceptionDiagnosis1.hashCode(), exceptionDiagnosis2.hashCode());
 }

 @Test
 public void testHashCodeDifferentObjects() {
 ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[]{"twitter4j"});
 Object obj = new Object();
 assertNotEquals(exceptionDiagnosis1.hashCode(), obj.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionDiagnosisConstructionWithEmptyFilterArray() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new TwitterException("Test TwitterException"), new String[0]);
 fail("Empty filter array should throw IllegalArgumentException");
 }

 @Test
 public void testToStringWithCausedByException() {
 TwitterException twitterException = new TwitterException("Test TwitterException", new IOException("Test IOException"));
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(twitterException, new String[]{"twitter4j"});
 String toString = exceptionDiagnosis.toString();
 String expectedToString = "ExceptionDiagnosis{stackLineHash=12345678, lineNumberHash=45} ExceptionDiagnosis{stackLineHash=23456789, lineNumberHash=53}";
 assertEquals(toString, expectedToString);
 }

 @Test(expected = StackOverflowError.class)
 public void testStackOverflowError() {
 StackTraceElement stackTraceElement = new StackTraceElement("twitter4j.ExceptionDiagnosisTest", "testStackOverflowError", "ExceptionDiagnosisTest.java", 45);
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new StackOverflowError(), new String[]{"twitter4j"});
 fail("StackOverflowError should be thrown when testing recursive filtering");
 }

 @Test(expected = UndeclaredException.class)
 public void testUndeclaredException() {
 IOException ioException = new IOException("Test IOException");
 Thread thread = new Thread(() -> {
 try {
 throw new NullPointerException();
 } catch (NullPointerException e) {
 throw new IOException("Test IOException", e);
 }
 });
 thread.start();
 ExceptionDiagnosis exceptionDiagnosis = null;
 try {
 exceptionDiagnosis = new ExceptionDiagnosis(ioException, new String[]{"twitter4j"});
 } catch (IOException e) {
 // expected
 }
 assertNotNull(exceptionDiagnosis);
 }

}