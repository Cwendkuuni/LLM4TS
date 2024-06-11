package jaw.entrada;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import static org.mockito.Mockito.*;

public class SalvarTest {
    private Salvar salvar;
    private ProcessorEntidades form;
    private JFileChooser escolhedor;

    @Before
    public void setUp() {
        this.form = mock(ProcessorEntidades.class);
        this.salvar = new Salvar(form, 20);
        this.escolhedor = mock(JFileChooser.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
    }

    @Test
    public void testSalvarConstructorValidInput() {
        // Test valid input
        List<Entidade> entidades = new ArrayList<>();
        assertNotNull(salvar.getEntidade());
        assertEquals(0, salvar.getEntidade().size());
    }

    @Test(expected = Exception.class)
    public void testSalvarConstructorInvalidInput() {
        // Test exception when form.getEntidades() throws an exception
        when(form.getEntidades()).thenThrow(new Exception());
        Salvar salvar = new Salvar(form, 20);
        fail("Constructor should have thrown an exception");
    }

    @Test
    public void testSaveFileWithValidPath() throws IOException {
        // Test saving a file with a valid path
        File selectedFile = new File("path/to/save/file.jaw");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        salvar.saveFile(escolhedor, "path/to/save/file.jaw");
        verify(salvar, times(1)).saveFile("path/to/save/file.jaw", anyList());
    }

    @Test(expected = FileNotFoundException.class)
    public void testSaveFileWithInvalidPath() throws IOException {
        // Test saving a file with an invalid path
        File selectedFile = new File("path/to/save/file");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        salvar.saveFile(escolhedor, "path/to/save/file");
        fail("File should not have been saved with an invalid path");
    }

    @Test
    public void testCancelFileChooser() throws IOException {
        // Test when the user cancels the file chooser dialog
        when(escolhedor.getSelectedFile()).thenReturn(null);
        salvar.saveFile(escolhedor, "");
        verify(form, times(0)).log("Arquivo nao foi salvo");
    }

    @Test(expected = IOException.class)
    public void testSerializationFailed() throws IOException {
        // Test when serialization fails
        doThrow(IOException.class).when(salvar).saveFile("path/to/save/file.jaw", anyList());
        salvar.saveFile(escolhedor, "path/to/save/file.jaw");
        verify(form, times(1)).log("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !");
    }
}