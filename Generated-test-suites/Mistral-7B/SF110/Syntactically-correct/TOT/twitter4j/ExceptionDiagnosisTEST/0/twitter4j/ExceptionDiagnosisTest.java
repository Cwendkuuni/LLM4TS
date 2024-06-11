package twitter4j.test;

import org.junit.*;
import static org.junit.Assert.*;;

public class ExceptionDiagnosisTest {
	@BeforeClass
	public static void setUpBeforeClass() {
		// Set up before all tests
	}

	@AfterClass
	public static void tearDownAfterClass() {
		// Tear down after all tests
	}

	@Before
	public void setUp() {
		// Set up before each test
	}

	@After
	public void tearDown() {
		// Tear down after each test
	}

	@Test
	public void testExceptionDiagnosis_SingleThrowable() {
		Throwable throwable = new RuntimeException("Test exception");
		ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
		assertEquals(throwable.getClass().getName().hashCode() + throwable.getStackTrace()[0].getMethodName().hashCode(), diagnosis.getStackLineHash());
		assertEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumberHash());
		assertEquals("Test-ExceptionDiagnosis-" + toHexString(throwable.getClass().getName().hashCode()) + "-" + toHexString(throwable.getStackTrace()[0].getLineNumber()), diagnosis.asHexString());
	}

	@Test
	public void testExceptionDiagnosis_WithFilter() {
		Throwable throwable = new RuntimeException("Test exception");
		ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"test"});
		assertEquals(throwable.getClass().getName().hashCode() + throwable.getStackTrace()[1].getMethodName().hashCode(), diagnosis.getStackLineHash());
		assertEquals(throwable.getStackTrace()[1].getLineNumber(), diagnosis.getLineNumberHash());
		assertEquals("Test-ExceptionDiagnosis-" + toHexString(throwable.getClass().getName().hashCode()) + "-" + toHexString(throwable.getStackTrace()[1].getLineNumber()), diagnosis.asHexString());
	}

	@Test
	public void testGetStackLineHashAndLineNumberHash() {
		Throwable throwable = new RuntimeException("Test exception");
		ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
		assertEquals(throwable.getClass().getName().hashCode() + throwable.getStackTrace()[0].getMethodName().hashCode(), diagnosis.getStackLineHash());
		assertEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumberHash());
	}

	@Test(expected = RuntimeException.class)
	public void testGetStackLineHashAndLineNumberHash_WithIncorrectException() {
		Throwable throwable = new RuntimeException("Test exception");
		ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new FileNotFoundException("Test file not found"));
		assertNotEquals(throwable.getClass().getName().hashCode() + throwable.getStackTrace()[0].getMethodName().hashCode(), diagnosis.getStackLineHash());
		assertNotEquals(throwable.getStackTrace()[0].getLineNumber(), diagnosis.getLineNumberHash());
	}

	private static String toHexString(int value) {
		String str = Integer.toHexString(value);
		return "0000000" + str.substring(str.length() - 8);
	}
}