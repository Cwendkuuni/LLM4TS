package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExceptionDiagnosisTest {

    private final Class<?> clazz;
    private final String methodName;
    private final int lineNumber;
    private final int expectedStackLineHash;
    private final int expectedLineNumberHash;
    private final String expectedHexString;

    public ExceptionDiagnosisTest(Class<?> clazz, String methodName, int lineNumber) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.lineNumber = lineNumber;

        // Calculate expected hash codes based on the given class and line number
        int stackLineHash = 0;
        int lineNumberHash = 0;

        try {
            Class<?> currentClass = clazz;
            while (!currentClass.equals(Object.class)) {
                stackLineHash = 31 * stackLineHash + currentClass.getName().hashCode();
                for (Method method : currentClass.getDeclaredMethods()) {
                    if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(methodName)) {
                        lineNumberHash = 31 * lineNumberHash + lineNumber;
                        break;
                    }
                }
                currentClass = currentClass.getSuperclass();
            }

            this.expectedStackLineHash = stackLineHash;
            this.expectedLineNumberHash = lineNumberHash;
            this.expectedHexString = this.toHexString(this.expectedStackLineHash) + "-" + this.toHexString(this.expectedLineNumberHash);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Parameters(name = "{0}#{1}.{2}")
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();

        // Add test cases for various classes and methods within the twitter4j package
        data.add(new Object[] { TwitterFactory.class, "getSingletonInstance", 1234 });
        data.add(new Object[] { Twitter.class, "destroy", 5678 });
        data.add(new Object[] { Status.class, "getUserMentionEntities", 9012 });

        // Add test cases for other classes and methods, if needed

        return data;
    }

    @Test
    public void testExceptionDiagnosisConstruction() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertNotNull(diagnosis);
        assertEquals(this.expectedHexString, diagnosis.asHexString());
        assertEquals(this.expectedStackLineHash, diagnosis.getStackLineHash());
        assertEquals(this.expectedLineNumberHash, diagnosis.getLineNumberHash());
    }

    @Test
    public void testExceptionDiagnosisConstructionWithFilter() {
        Throwable throwable = new RuntimeException("Test exception");
        String[] inclusionFilter = new String[] {"twitter4j.TwitterFactory"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(this.expectedHexString, diagnosis.asHexString());
        assertEquals(this.expectedStackLineHash, diagnosis.getStackLineHash());
        assertEquals(this.expectedLineNumberHash, diagnosis.getLineNumberHash());

        Throwable cause = throwable.getCause();
        if (cause != null) {
            ExceptionDiagnosis causeDiagnosis = new ExceptionDiagnosis(cause, inclusionFilter);
            assertEquals(diagnosis.getHexString() + " " + causeDiagnosis.getHexString(), diagnosis.asHexString());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionDiagnosisNullThrowable() {
        new ExceptionDiagnosis(null);
    }

    @Test
    public void testEquals() {
        Throwable throwable1 = new RuntimeException("Test exception 1");
        Throwable throwable2 = new RuntimeException("Test exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        assertNotSame(diagnosis1, diagnosis2);

        Throwable cause1 = throwable1.getCause();
        Throwable cause2 = throwable2.getCause();
        if (cause1 != null && cause2 != null) {
            ExceptionDiagnosis causeDiagnosis1 = new ExceptionDiagnosis(cause1);
            ExceptionDiagnosis causeDiagnosis2 = new ExceptionDiagnosis(cause2);
            ExceptionDiagnosis diagnosisWithCauses1 = new ExceptionDiagnosis(throwable1, new String[0]);
            ExceptionDiagnosis diagnosisWithCauses2 = new ExceptionDiagnosis(throwable2, new String[0]);

            assertNotSame(diagnosis1, diagnosisWithCauses1);
            assertNotSame(diagnosis1, diagnosisWithCauses2);
            assertSame(diagnosisWithCauses1, diagnosisWithCauses2);

            assertNotSame(diagnosis1, diagnosis2);
            assertNotSame(diagnosisWithCauses1, diagnosis2);
            assertSame(diagnosisWithCauses1, diagnosisWithCauses2);

            assertSame(diagnosis1, causeDiagnosis1);
            assertSame(diagnosisWithCauses1, diagnosisWithCauses1);
            assertSame(diagnosis1, diagnosisWithCauses1);
            assertSame(diagnosis1, diagnosis1);

            assertNotSame(diagnosis2, diagnosisWithCauses1);
            assertNotSame(diagnosis2, diagnosisWithCauses2);
            assertSame(diagnosisWithCauses2, diagnosisWithCauses2);

            assertNotSame(causeDiagnosis1, diagnosisWithCauses1);
            assertSame(causeDiagnosis1, causeDiagnosis1);
            assertSame(causeDiagnosis1, causeDiagnosis1);

            assertNotSame(causeDiagnosis1, causeDiagnosis2);
            assertSame(causeDiagnosis1, causeDiagnosis1);
            assertNotSame(causeDiagnosis1, causeDiagnosis2);
        } else {
            ExceptionDiagnosis diagnosis1WithEmptyCause = new ExceptionDiagnosis(throwable1, new String[0]);
            ExceptionDiagnosis diagnosis2WithEmptyCause = new ExceptionDiagnosis(throwable2, new String[0]);

            assertNotSame(diagnosis1, diagnosis2);
            assertNotSame(diagnosis1WithEmptyCause, diagnosis2WithEmptyCause);
            assertSame(diagnosis1WithEmptyCause, diagnosis1WithEmptyCause);
            assertSame(diagnosis2WithEmptyCause, diagnosis2WithEmptyCause);
        }
    }

    @Test
    public void testHashCode() {
        Throwable throwable1 = new RuntimeException("Test exception 1");
        Throwable throwable2 = new RuntimeException("Test exception 2");
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        int stackLineHash1 = this.expectedStackLineHash;
        int lineNumberHash1 = this.expectedLineNumberHash;
        int stackLineHash2 = this.expectedStackLineHash;
        int lineNumberHash2 = this.expectedLineNumberHash;

        int hashCode1 = diagnosis1.hashCode();
        int hashCode2 = diagnosis2.hashCode();

        assertEquals(stackLineHash1, diagnosis1.getStackLineHash());
        assertEquals(lineNumberHash1, diagnosis1.getLineNumberHash());
        assertEquals(stackLineHash1, hashCode1);
        assertEquals(lineNumberHash1, hashCode1 >>> 16);

        assertEquals(stackLineHash2, diagnosis2.getStackLineHash());
        assertEquals(lineNumberHash2, diagnosis2.getLineNumberHash());
        assertEquals(stackLineHash2, hashCode2);
        assertEquals(lineNumberHash2, hashCode2 >>> 16);

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        Throwable throwable = new RuntimeException("Test exception");
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);
        assertEquals("ExceptionDiagnosis{stackLineHash=" + this.expectedStackLineHash + ", lineNumberHash=" + this.expectedLineNumberHash + '}', diagnosis.toString());
    }

    private String toHexString(final int value) {
        return "0000000" + Integer.toHexString(value).substring(6);
    }
}