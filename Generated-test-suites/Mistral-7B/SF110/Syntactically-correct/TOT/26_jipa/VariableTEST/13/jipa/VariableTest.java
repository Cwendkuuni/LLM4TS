import jipa.Variable;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VariableTest {
    @Test
    public void testAddVariable() {
        // Test adding a non-existent variable
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());

        // Test adding an existing variable, should return false
        assertFalse(Variable.addVariable("var1"));

        // Test adding a variable with special character
        assertTrue(Variable.addVariable("var_1"));
        assertEquals(2, Variable.variableList.size());

        // Test adding a variable with leading &
        assertTrue(Variable.addVariable("&var1")); // Assuming addVariable handles '&' properly
        assertEquals(3, Variable.variableList.size());
    }
}