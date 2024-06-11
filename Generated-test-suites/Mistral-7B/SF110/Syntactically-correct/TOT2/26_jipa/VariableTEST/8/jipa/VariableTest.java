import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import jipa.Variable;
import java.util.ArrayList;

public class VariableTest {
    @Test
    public void testGetterValue() {
        Variable variable = new Variable("testVariable");
        Assertions.assertEquals(0, variable.getValue());
    }

    @Test
    public void testSetterValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(5);
        Assertions.assertEquals(5, variable.getValue());
    }

    @Test
    public void testGetterName() {
        Variable variable = new Variable("testVariable");
        Assertions.assertEquals("testVariable", variable.getName());
    }

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        Assertions.assertTrue(Variable.addVariable("testVar1"));
        Assertions.assertFalse(Variable.addVariable("testVar1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar1");
        Variable.removeVariable("testVar1");
        Assertions.assertFalse(Variable.removeVariable("testVar1"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar1");
        Variable testVariable = new Variable("testVar1");
        testVariable.setValue(10);
        Assertions.assertEquals(10, Variable.getVariableValue("testVar1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar1");
        Variable testVariable = Variable.variableList.get(0);
        testVariable.setValue(15);
        Assertions.assertEquals(15, Variable.getVariableValue("testVar1"));
    }

    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVar1");
        Assertions.assertEquals(0, Variable.findVariable("testVar1"));
    }
}