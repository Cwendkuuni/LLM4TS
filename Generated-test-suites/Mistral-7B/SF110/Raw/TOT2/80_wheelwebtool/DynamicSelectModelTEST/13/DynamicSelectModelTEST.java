import org.junit.Test;
import static org.junit.Assert.*;
import wheel.util.DynamicSelectModel;
import wheel.components.Component;
import java.util.ArrayList;

public class DynamicSelectModelTest {

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel()
                .collection(new ElExpression("new int[]{{1, 2, 3, 4}}"))
                .label(new ElExpression("toString()"))
                .value(new ElExpression("ordinal()"));
        Component component = new Component();
        model.setComponent(component);

        assertEquals("1", model.getLabel(0));
        assertEquals("2", model.getLabel(1));
        assertEquals("3", model.getLabel(2));
        assertEquals("4", model.getLabel(3));
    }
}