import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.addVariable("var1")); // should return false as var1 already exists
    }
}