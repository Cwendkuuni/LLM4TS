import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DefaultDatasetTest {
    @Test
    public void testAddAll() {
        // Create an initial dataset
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DummyInstance(0, "class1"));
        dataset.add(new DummyInstance(1, "class2"));

        // Add another collection of instances
        Collection<Instance> instancesToAdd = new ArrayList<>();
        instancesToAdd.add(new DummyInstance(2, "class1"));
        instancesToAdd.add(new DummyInstance(3, "class3"));

        // Test adding the collection
        boolean added = dataset.addAll(instancesToAdd);
        assertEquals(true, added);
        assertEquals(4, dataset.size());

        // Check that all instances were added correctly
        for (Instance instance : instancesToAdd) {
            assertEquals(instance, dataset.instance(dataset.size() - 1));
        }
    }
}

// DummyInstance class used for testing
class DummyInstance implements Instance {
    private int id;
    private Object className;

    public DummyInstance(int id, Object className) {
        this.id = id;
        this.className = className;
    }

    @Override
    public int noAttributes() {
        return 0;
    }

    @Override
    public Object classValue() {
        return className;
    }

    @Override
    public Instance copy() {
        return new DummyInstance(id, className);
    }
}