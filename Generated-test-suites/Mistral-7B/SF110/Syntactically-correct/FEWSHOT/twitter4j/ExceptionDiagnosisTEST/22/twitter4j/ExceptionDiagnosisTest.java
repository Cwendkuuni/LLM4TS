package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;

public class ExceptionDiagnosisTest {

    @Test
    public void testEmptyConstructor() {
        assertNull(new ExceptionDiagnosis(null));
    }

    @Test
    public void testConstructorWithThrowable() {
        final Throwable throwable = new IOException("Test IOException");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable);

        assertEquals(453958937114285988L, diagnosis.serialVersionUID);
        assertNull(diagnosis.hexString);
        assertSame(throwable, diagnosis.th);
    }

    @Test
    public void testConstructorWithThrowableAndFilterArray() {
        final Throwable throwable = new IOException("Test IOException");
        final String[] inclusionFilter = new String[]{"java.io.", "twitter4j."};
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);

        assertEquals(453958937114285988L, diagnosis.serialVersionUID);
        assertNotNull(diagnosis.hexString);
        assertSame(throwable, diagnosis.th);

        final String hexString = diagnosis.asHexString();
        final String expectedHexString = "1143635513-673470456"; // Calculated based on example stack trace element
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testGetStackLineHash() {
        final Throwable throwable = new IOException("Test IOException");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "twitter4j."});
        final int stackLineHash = diagnosis.getStackLineHash();
        final int expectedStackLineHash = 1143635513; // Calculated based on example stack trace element
        assertEquals(expectedStackLineHash, stackLineHash);
    }

    @Test
    public void testGetLineNumberHash() {
        final Throwable throwable = new IOException("Test IOException");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "twitter4j."});
        final int lineNumberHash = diagnosis.getLineNumberHash();
        final int expectedLineNumberHash = 673470456; // Calculated based on example stack trace element
        assertEquals(expectedLineNumberHash, lineNumberHash);
    }

    @Test
    public void testAsHexString() {
        final Throwable throwable = new IOException("Test IOException");
        final String[] inclusionFilter = new String[]{"java.io.", "twitter4j."};
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, inclusionFilter);
        final String hexString = diagnosis.asHexString();

        final String expectedHexString = "1143635513-673470456"; // Calculated based on example stack trace element
        assertEquals(expectedHexString, hexString);
    }

    @Test
    public void testEqualsObject() {
        final Throwable throwable1 = new IOException("Test IOException 1");
        final Throwable throwable2 = new IOException("Test IOException 2");

        final ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable1);
        final ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable2);

        assertFalse(diagnosis1.equals(diagnosis2));

        final ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable1, new String[]{"java.io.", "twitter4j."});
        final ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable1, new String[]{"java.io.", "twitter4j."});

        assertTrue(diagnosis3.equals(diagnosis4));
    }

    @Test
    public void testHashCode() {
        final Throwable throwable = new IOException("Test IOException");
        final ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(throwable);
        final ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "twitter4j."});

        assertNotEquals(diagnosis1.hashCode(), diagnosis2.hashCode());

        final ExceptionDiagnosis diagnosis3 = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "twitter4j."});
        final ExceptionDiagnosis diagnosis4 = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "twitter4j."});

        assertEquals(diagnosis3.hashCode(), diagnosis4.hashCode());
    }

    @Test
    public void testToString() {
        final Throwable throwable = new IOException("Test IOException");
        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(throwable, new String[]{"java.io.", "twitter4j."});

        final String expectedToString = "ExceptionDiagnosis{stackLineHash=1143635513, lineNumberHash=673470456}";
        assertEquals(expectedToString, diagnosis.toString());
    }

    @Test
    public void testToStringWithCause() throws IOException {
        final Throwable throwable = new IOException("Test IOException");
        final IOException cause = new IOException("Test IOException cause");
        final Throwable exception = new UndeclaredThrowableException(cause);

        final ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception, new String[]{"java.io.", "twitter4j."});

        final String expectedToString = "ExceptionDiagnosis{stackLineHash=115632039, lineNumberHash=738694216, "
                + "hexString=115632039-738694216}";
        assertEquals(expectedToString, diagnosis.toString());
    }
}