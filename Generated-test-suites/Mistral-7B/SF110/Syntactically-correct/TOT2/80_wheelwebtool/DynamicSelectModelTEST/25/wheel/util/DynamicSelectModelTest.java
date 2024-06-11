import org.junit.Test;
import static org.junit.Assert.*;
import wheel.util.DynamicSelectModel;
import java.util.ArrayList;

public class DynamicSelectModelTest {
    @Test
    public void testGetOptionCount() {
        DynamicSelectModel ds = new DynamicSelectModel(new ElExpression("new int[5]"), new ElExpression("toString"), new ElExpression("toString"));
        ArrayList objects = new ArrayList();
        objects.add(new Integer(1));
        objects.add(new Integer(2));
        objects.add(new Integer(3));
        objects.add(new Integer(4));
        objects.add(new Integer(5));
        ds.setObjects(objects);
        assertEquals(5, ds.getOptionCount());
    }
}