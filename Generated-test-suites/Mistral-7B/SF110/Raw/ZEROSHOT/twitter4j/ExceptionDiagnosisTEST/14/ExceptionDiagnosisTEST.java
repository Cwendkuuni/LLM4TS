package twitter4j.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import twitter4j.ExceptionDiagnosis;
import twitter4j.twitterException;

public class ExceptionDiagnosisTest {

 private ExceptionDiagnosis exceptionDiagnosis;
 private ByteArrayOutputStream outputStream;
 private PrintStream printStream;

 @Before
 public void setUp() {
 outputStream = new ByteArrayOutputStream();
 printStream = new PrintStream(outputStream);
 System.setOut(printStream);
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowable() {
 Throwable throwable = new twitterException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
 assertEquals("ExceptionDiagnosis{stackLineHash=60291417, lineNumberHash=0}", ed.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionDiagnosisConstructorWithNullThrowable() {
 try {
 new ExceptionDiagnosis(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowableAndEmptyFilterArray() {
 Throwable throwable = new twitterException("Test Exception");
 String[] filterArray = new String[0];
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, filterArray);
 assertEquals("ExceptionDiagnosis{stackLineHash=60291417, lineNumberHash=0}", ed.toString());
 }

 @Test
 public void testExceptionDiagnosisConstructorWithThrowableAndFilterArray() {
 Throwable throwable = new twitterException("Test Exception");
 String[] filterArray = new String[]{"com.test"};
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable, filterArray);
 StackTraceElement[] stackTrace = throwable.getStackTrace();
 String expectedHexString = "";
 for (StackTraceElement line : stackTrace) {
 if (line.getClassName().startsWith(filterArray[0])) {
 int hash = line.getClassName().hashCode() + line.getMethodName().hashCode();
 int stackLineHash = 31 * 31 * hash + line.getLineNumber();
 expectedHexString += toHexString(stackLineHash) + "-";
 break;
 }
 }
 expectedHexString = expectedHexString.substring(0, expectedHexString.length() - 1);
 assertEquals(expectedHexString + " " + toHexString(ed.getLineNumberHash()) + " " + toHexString(ed.getStackLineHash()), ed.asHexString());
 }

 @Test
 public void testGetStackLineHash() {
 Throwable throwable = new twitterException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
 assertEquals(60291417, ed.getStackLineHash());
 }

 @Test
 public void testGetLineNumberHash() {
 Throwable throwable = new twitterException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
 assertEquals(0, ed.getLineNumberHash());
 }

 @Test
 public void testAsHexString() {
 Throwable throwable = new twitterException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
 assertEquals("", ed.asHexString());
 }

 @Test
 public void testToString() {
 Throwable throwable = new twitterException("Test Exception");
 ExceptionDiagnosis ed = new ExceptionDiagnosis(throwable);
 String expectedToString = "ExceptionDiagnosis{stackLineHash=60291417, lineNumberHash=0}";
 assertEquals(expectedToString, ed.toString());
 }

 @Test
 public void testEquals() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new twitterException("Test Exception 1"));
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new twitterException("Test Exception 2"));
 assertFalse(ed1.equals(ed2));
 }

 @Test
 public void testHashcode() {
 ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new twitterException("Test Exception 1"));
 ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new twitterException("Test Exception 2"));
 int expectedHashcode = 60291417 * 31 + 0;
 assertEquals(expectedHashcode, ed1.hashCode());
 assertEquals(ed1.hashCode(), ed1.hashCode());
 assertNotEquals(ed1.hashCode(), ed2.hashCode());
 }

 private static String toHexString(int value) {
 String str = "0000000" + Integer.toHexString(value);
 return str.substring(str.length() - 8, str.length());
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPrivateMethodToHexString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
 Class<?> clazz = ExceptionDiagnosis.class;
 Method toHexStringMethod = clazz.getDeclaredMethod("toHexString", int.class);
 toHexStringMethod.setAccessible(true);
 assertNull(toHexStringMethod.invoke(null, (Object)null));
 }
}