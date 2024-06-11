import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        int access = Opcodes.ACC_PUBLIC;
        String desc = "()V";
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(access, desc, mv);

        verify(mv).visitCode();
        verify(mv).visitVarInsn(ALOAD, 0);
        verify(mv).visitMethodInsn(INVOKESPECIAL, LocalVariablesSorter.class.getName(), "<init>", "(Lorg/objectweb/asm/MethodVisitor;)V");
        verify(mv).visitMaxs(0, 0);
        verify(mv).visitEnd();
    }
}