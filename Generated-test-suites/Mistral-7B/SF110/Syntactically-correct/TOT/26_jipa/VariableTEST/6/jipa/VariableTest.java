import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    private static List<Variable> variableList;
    private Variable variable1;

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<>();
        variable1 = new Variable("testVariable");
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVariable"));
        assertEquals(1, Variable.variableList.size());
    }
}