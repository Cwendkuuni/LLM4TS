import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        ClassReader cr = new ClassReader("org/objectweb/asm/jip/commons/LocalVariablesSorter");
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null, null, null) {});
        cr.accept(sorter, 0);
        assertEquals(40, sorter.mapping.length);
        assertEquals(20, sorter.newLocals.length);
        assertEquals(0, sorter.firstLocal);
        assertEquals(1, sorter.nextLocal);
    }
}