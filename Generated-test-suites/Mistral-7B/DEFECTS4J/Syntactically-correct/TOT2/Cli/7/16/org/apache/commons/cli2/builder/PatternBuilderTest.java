import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {

    @Test
    public void testCreateOption() {
        PatternBuilder pb = new PatternBuilder();

        pb.withPattern("a:"); // single required option 'a'
        Option optionA = pb.create();
        assertNotNull(optionA);

        assertEquals("a", optionA.getLongOpt());
        assertEquals(true, optionA.isRequired());
        assertNull(optionA.getArgName());

        pb.reset();

        pb.withPattern("b:@com.example.MyClass"); // option 'b' with valid class argument
        Option optionB = pb.create();
        assertNotNull(optionB);

        assertEquals("b", optionB.getLongOpt());
        assertEquals(true, optionB.isRequired());
        assertNotNull(optionB.getArgName());
        assertEquals("com.example.MyClass", optionB.getArgName().getName());

        pb.reset();

        pb.withPattern("c:#yyyy-MM-dd"); // option 'c' with date validation
        Option optionC = pb.create();
        assertNotNull(optionC);

        assertEquals("c", optionC.getLongOpt());
        assertEquals(true, optionC.isRequired());
        assertNull(optionC.getArgName());
    }
}