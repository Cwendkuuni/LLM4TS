import static org.junit.Assert.assertEquals;

public class VariableTest {
    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(10);
        assertEquals(10, variable.getValue());
    }
}