import static org.junit.Assert.assertEquals;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithObject() {
        Object testObject = new Object();
        JCLO jclo = new JCLO(testObject);
        assertEquals(testObject, jclo.object);
    }

    // Add test cases for other constructors and methods here
}