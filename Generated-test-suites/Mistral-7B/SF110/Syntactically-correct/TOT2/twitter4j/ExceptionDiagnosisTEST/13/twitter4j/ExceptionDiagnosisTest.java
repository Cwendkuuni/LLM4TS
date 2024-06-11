package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.ExceptionDiagnosis;

public class ExceptionDiagnosisTest {

@Test
public void testExceptionDiagnosisConstructorWithThrowable() {
Throwable throwable = new IOException("Test IOException");
ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
assertNotNull(diagnosis);
assertEquals("TestIOException-", diagnosis.asHexString());
}

// Test cases for other methods and edge cases go here

@Test
public void testExceptionDiagnosisToString() {
Throwable throwable = new IOException("Test IOException");
String[] inclusionFilter = {"java.lang", "twitter4j"};
ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
String expectedToString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() + ", lineNumberHash=" + diagnosis.getLineNumberHash() + '}';
String actualToString = diagnosis.toString();
assertEquals(expectedToString, actualToString);
}
}