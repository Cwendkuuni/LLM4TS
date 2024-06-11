package twitter4j.test;

 import static org.junit.Assert.*;
 import twitter4j.ExceptionDiagnosis;
 import twitter4j.ExceptionDiagnosis.*; // for importing static hashCode, getClassName, getLineNumber, getMethodName

 import java.io.IOException;
 import java.lang.reflect.UndeclaredThrowableException;
 import java.util.Arrays;

 public class ExceptionDiagnosisTest {

 @org.junit.Test
 public void testExceptionDiagnosisConstructor() {
 ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(new IOException());
 assertNotNull(exceptionDiagnosis);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testExceptionDiagnosisConstructorWithNullThrowable() {
 new ExceptionDiagnosis(null);
 }

 @org.junit.Test
 public void testGetStackLineHash() {
 final Throwable throwable = new IOException();
 final StackTraceElement[] stackTrace = throwable.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
 final int expectedStackLineHash = arrangement(stackTrace, inclusionFilter);
 assertEquals(expectedStackLineHash, exceptionDiagnosis.getStackLineHash());
 }

 @org.junit.Test
 public void testGetLineNumberHash() {
 final Throwable throwable = new IOException();
 final StackTraceElement[] stackTrace = throwable.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
 final int expectedLineNumberHash = arrangementLineNumber(stackTrace, inclusionFilter);
 assertEquals(expectedLineNumberHash, exceptionDiagnosis.getLineNumberHash());
 }

 @org.junit.Test
 public void testAsHexString() {
 final Throwable throwable = new IOException();
 final StackTraceElement[] stackTrace = throwable.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
 final String expectedHexString = arrangementHexString(stackTrace, inclusionFilter);
 assertEquals(expectedHexString, exceptionDiagnosis.asHexString());
 }

 @org.junit.Test
 public void testEquals() {
 final Throwable throwable1 = new IOException();
 final Throwable throwable2 = new IOException();
 final StackTraceElement[] stackTrace1 = throwable1.getStackTrace();
 final StackTraceElement[] stackTrace2 = throwable2.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter);
 final ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter);
 final boolean expectedEquals = stackTrace1.equals(stackTrace2);
 final boolean actualEquals = exceptionDiagnosis1.equals(exceptionDiagnosis2);
 assertTrue(expectedEquals == actualEquals);
 }

 @org.junit.Test
 public void testHashCode() {
 final Throwable throwable1 = new IOException();
 final Throwable throwable2 = new IOException();
 final StackTraceElement[] stackTrace1 = throwable1.getStackTrace();
 final StackTraceElement[] stackTrace2 = throwable2.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1, inclusionFilter);
 final ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2, inclusionFilter);
 final int expectedHashCode1 = arrangementHashCode(stackTrace1, inclusionFilter);
 final int expectedHashCode2 = arrangementHashCode(stackTrace2, inclusionFilter);
 final int actualHashCode1 = exceptionDiagnosis1.hashCode();
 final int actualHashCode2 = exceptionDiagnosis2.hashCode();
 assertEquals(expectedHashCode1, actualHashCode1);
 assertEquals(expectedHashCode2, actualHashCode2);
 assertEquals(expectedHashCode1, actualHashCode2);
 }

 @org.junit.Test
 public void testToString() {
 final Throwable throwable = new IOException();
 final StackTraceElement[] stackTrace = throwable.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
 final String expectedToString = "ExceptionDiagnosis{stackLineHash=" + arrangementHashCode(stackTrace, inclusionFilter) + ", lineNumberHash=" + arrangementLineNumber(stackTrace, inclusionFilter) + '}';
 assertEquals(expectedToString, exceptionDiagnosis.toString());
 }

 @org.junit.Test
 public void testToStringWithCause() {
 final IOException ioException = new IOException();
 final FileNotFoundException fileNotFoundException = new FileNotFoundException("File not found");
 ioException.initCause(fileNotFoundException);
 final StackTraceElement[] stackTrace = ioException.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(ioException, inclusionFilter);
 final String expectedToString = "ExceptionDiagnosis{stackLineHash=" + arrangementHashCode(stackTrace, inclusionFilter) + ", lineNumberHash=" + arrangementLineNumber(stackTrace, inclusionFilter) + " " + new ExceptionDiagnosis(fileNotFoundException, inclusionFilter).asHexString() + '}';
 assertEquals(expectedToString, exceptionDiagnosis.toString());
 }

 @org.junit.Test
 public void testToStringWithNullCause() {
 final IOException ioException = new IOException();
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(ioException, new String[0]);
 assertEquals("ExceptionDiagnosis{stackLineHash=, lineNumberHash=}", exceptionDiagnosis.toString());
 }

 @org.junit.Test
 public void testEqualsWithNullObject() {
 final Throwable throwable = new IOException();
 final StackTraceElement[] stackTrace = throwable.getStackTrace();
 final String[] inclusionFilter = new String[]{"java.io"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
 assertFalse(exceptionDiagnosis.equals(null));
 }

 @org.junit.Test
 public void testHashCodeWithNullObject() {
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null, new String[0]);
 assertEquals(-1, exceptionDiagnosis.hashCode());
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testToStringWithNullStackTrace() {
 final IOException ioException = new IOException();
 ioException.setStackTrace(null);
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(ioException, new String[0]);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testToStringWithEmptyInclusionFilter() {
 final IOException ioException = new IOException();
 final String[] inclusionFilter = {};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(ioException, inclusionFilter);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testToStringWithInvalidInclusionFilter() {
 final IOException ioException = new IOException();
 final String[] inclusionFilter = {"InvalidFilter"};
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(ioException, inclusionFilter);
 }

 @org.junit.Test
 public void testAsHexStringWithNullThrowable() {
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(null, new String[0]);
 assertEquals("", exceptionDiagnosis.asHexString());
 }

 private static int arrangement(StackTraceElement[] stackTrace, String[] inclusionFilter) {
 int stackLineHash = 0;
 for (final StackTraceElement line : stackTrace) {
 for (final String filter : inclusionFilter) {
 if (line.getClassName().startsWith(filter)) {
 final int hash = line.getClassName().hashCode() + line.getMethodName().hashCode();
 stackLineHash = 31 * stackLineHash + hash;
 break;
 }
 }
 }
 return stackLineHash;
 }

 private static int arrangementLineNumber(StackTraceElement[] stackTrace, String[] inclusionFilter) {
 int lineNumberHash = 0;
 for (final StackTraceElement line : stackTrace) {
 for (final String filter : inclusionFilter) {
 if (line.getClassName().startsWith(filter)) {
 lineNumberHash = 31 * lineNumberHash + line.getLineNumber();
 break;
 }
 }
 }
 return lineNumberHash;
 }

 private static int arrangementHashCode(StackTraceElement[] stackTrace, String[] inclusionFilter) {
 return arrangement(stackTrace, inclusionFilter);
 }

 private static String arrangementHexString(StackTraceElement[] stackTrace, String[] inclusionFilter) {
 StringBuilder hexStringBuilder = new StringBuilder();
 final int stackLineHash = arrangement(stackTrace, inclusionFilter);
 hexStringBuilder.append(toHexString(stackLineHash));
 hexStringBuilder.append("-");
 final int lineNumberHash = arrangementLineNumber(stackTrace, inclusionFilter);
 hexStringBuilder.append(toHexString(lineNumberHash));
 if (stackTrace.length > 0) {
 final ExceptionDiagnosis exceptionDiagnosis = new ExceptionDiagnosis(stackTrace[0].getStackTrace()[0], inclusionFilter);
 hexStringBuilder.append(" ").append(exceptionDiagnosis.asHexString());
 }
 return hexStringBuilder.toString();
 }

 private static String toHexString(final int value) {
 final String str = "0000000" + Integer.toHexString(value);
 return str.substring(str.length() - 8, str.length());
 }
 }