import org.junit.Assert;
import org.junit.Test;
import jipa.Variable;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        List<Variable> variableList = Variable.variableList;
        Assert.assertEquals(1, variableList.size());
        Assert.assertEquals(variable, variableList.get(0));
    }

    @Test
    public void testRemoveVariable() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        Variable.removeVariable("testVariable");
        List<Variable> variableList = Variable.variableList;
        Assert.assertEquals(0, variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        variable.setValue(10);
        int value = Variable.getVariableValue("testVariable");
        Assert.assertEquals(10, value);
    }

    @Test
    public void testSetVariableValue() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 10);
        int value = Variable.getVariableValue("testVariable");
        Assert.assertEquals(10, value);
    }

    // Add tests for other methods
}