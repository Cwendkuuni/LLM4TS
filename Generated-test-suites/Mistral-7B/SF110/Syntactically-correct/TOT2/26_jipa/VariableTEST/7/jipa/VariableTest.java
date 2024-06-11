import static org.junit.Assert.*;
import jipa.Variable;
import java.util.ArrayList;

public class VariableTest {
    @Test
    public void testVariableConstructor() {
        Variable variable = new Variable("testVariable");
        assertEquals("testVariable", variable.getName());
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(5);
        assertEquals(5, variable.getValue());
    }

    @Test
    public void testSetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(5);
        assertEquals(5, variable.getValue());
    }

    @Test
    public void testGetName() {
        Variable variable = new Variable("testVariable");
        assertEquals("testVariable", variable.getName());
    }

    @Test
    public void testAddVariable() {
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        Variable.removeVariable("testVariable1");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable variable1 = new Variable("testVariable1");
        Variable variable2 = new Variable("testVariable2");
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        Variable.setVariableValue("testVariable1", 5);
        assertEquals(5, Variable.getVariableValue("testVariable1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 5);
        assertEquals(5, Variable.getVariableValue("testVariable"));
    }

    @Test
    public void testFindVariable() {
        Variable variable1 = new Variable("testVariable1");
        Variable.addVariable("testVariable1");
        Variable variable2 = new Variable("testVariable2");
        Variable variable3 = new Variable("testVariable3");
        Variable.addVariable("testVariable2");
        Variable.addVariable("testVariable3");
        assertEquals(1, Variable.findVariable("testVariable1"));
        assertEquals(3, Variable.findVariable("testVariable3"));
    }
}