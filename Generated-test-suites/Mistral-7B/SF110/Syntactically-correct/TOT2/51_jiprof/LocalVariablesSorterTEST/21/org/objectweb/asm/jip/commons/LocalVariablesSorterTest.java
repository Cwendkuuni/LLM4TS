import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.Type;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter localVariablesSorter = mock(LocalVariablesSorter.class);
        int var = 1;
        Type type = Type.INT_TYPE;
        int expectedRemappedIndex = 1;

        when(localVariablesSorter.remap(var, type)).thenReturn(expectedRemappedIndex);

        LocalVariablesSorter.visitVarInsn(21, var); // int opcode
        verify(localVariablesSorter).visitVarInsn(21, expectedRemappedIndex);
    }
}