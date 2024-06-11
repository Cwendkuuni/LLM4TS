package jaw.entrada;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import jaw.*;

public class SalvarTest {

    @BeforeClass
    public static void setupClass() {
        // Set up any static initializations or mock objects here
    }

    @AfterClass
    public static void tearDownClass() {
        // Clean up any static objects or mocks here
    }

    @Before
    public void setup() {
        // Set up any test-specific initializations here
    }

    @After
    public void tearDown() {
        // Clean up any test-specific objects here
    }

    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(new Entidade());
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals(entidades, salvar.getEntidade());
    }

    @Test
    public void testShowSaveDialog() {
        ProcessorEntidades form = mock(ProcessorEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        doReturn(fileChooser).when(salvar).getFileChooser();
        Component parent = mock(Component.class);
        salvar.showSaveDialog(parent);
        verify(fileChooser, times(1)).showSaveDialog(parent);
        verify(salvar, never()).saveFile(anyString(), anyList());
    }

    @Test
    public void testSaveInvalidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(new Entidade());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        File file = new File("test.txt");
        FileFilter filter = new Salvar.Filtro();
        assertFalse(filter.accept(file));
        try {
            salvar.saveFile("test.txt", entidades);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testSaveValidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(new Entidade());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        File file = new File("test.jaw.xml");
        FileFilter filter = new Salvar.FiltroXML();
        assertTrue(filter.accept(file));
        try {
            salvar.saveFile(file.getAbsolutePath(), entidades);
            // Verify that the file was saved correctly
            File savedFile = new File(file.getAbsolutePath());
            assertTrue(savedFile.exists());
            String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Test</nome>\n<propriedade1>Test1</propriedade1>\n<propriedade2>Test2</propriedade2>\n</entidade>\n</entidades>";
            String actualXml = new String(Files.readAllBytes(savedFile.toPath()));
            assertEquals(expectedXml, actualXml);
        } catch (IOException | FileNotFoundException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}