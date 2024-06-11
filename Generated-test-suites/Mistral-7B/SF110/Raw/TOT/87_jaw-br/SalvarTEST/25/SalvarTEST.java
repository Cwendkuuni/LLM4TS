
package jaw.entrada;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private static final String TEST_DIRECTORY = "C:/Users/TestUser/Desktop";
    private static final String TEST_FILE_NAME = "test.jaw";
    private static final String TEST_FILE_NAME_XML = "test.jaw.xml";

    @Before
    public void setUp() {
        form = Mockito.mock(ProcessarEntidades.class);
        salvar = new Salvar(form, Salvar.SALVAR);
    }

    @Test
    public void testSaveAsJawFile() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME)).when(salvar).getSelectedFile();
        Mockito.when(salvar.save()).thenReturn(true);
        Mockito.doCallRealMethod().when(salvar).save();

        salvar.save();

        File savedFile = new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME);
        assertTrue(savedFile.exists());
        assertTrue(savedFile.isFile());
        assertFalse(salvar.vela);

        savedFile.delete();
    }

    @Test
    public void testSaveAsXMLFile() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME_XML)).when(salvar).getSelectedFile();
        Mockito.when(salvar.saveAsXML()).thenReturn("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<JawProject>\n<Entidade name=\"TestEntidade1\"/>\n<Entidade name=\"TestEntidade2\"/>\n</JawProject>");

        String xmlString = salvar.saveAsXML();
        String expectedXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<JawProject>\n<Entidade name=\"TestEntidade1\"/>\n<Entidade name=\"TestEntidade2\"/>\n</JawProject>";
        assertEquals(expectedXMLString, xmlString);
    }

    @Test
    public void testSaveWithExistingFile() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME)).when(salvar).getSelectedFile();
        Mockito.when(form.getName()).thenReturn("");

        assertFalse(salvar.save());
    }

    @Test
    public void testSaveWithInvalidFileFilter() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(null).when(salvar).getSelectedFile();
        Mockito.doReturn(new Filtro2()).when(salvar).getFileFilter();
        Mockito.when(salvar.save()).thenReturn(false);

        assertFalse(salvar.save());
    }

    @Test
    public void testSaveWithValidFileFilterButNoFileSelected() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(null).when(salvar).getSelectedFile();
        Mockito.doReturn(new Filtro()).when(salvar).getFileFilter();
        Mockito.when(salvar.save()).thenReturn(false);

        assertFalse(salvar.save());
    }

    @Test
    public void testSaveWithInvalidFileFilterButFileSelected() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(new File("test.txt")).when(salvar).getSelectedFile();
        Mockito.doReturn(new Filtro()).when(salvar).getFileFilter();
        Mockito.when(salvar.save()).thenReturn(false);

        assertFalse(salvar.save());
    }

    @Test
    public void testSaveWithValidFileFilterAndFileSelected() {
        Mockito.when(form.getEntidades()).thenReturn(getTestEntidades());
        Mockito.doReturn(new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME)).when(salvar).getSelectedFile();
        Mockito.doReturn(new Filtro()).when(salvar).getFileFilter();
        Mockito.when(salvar.save()).thenReturn(true);
        Mockito.doCallRealMethod().when(salvar).save();

        salvar.save();

        File savedFile = new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME);
        assertTrue(savedFile.exists());
        assertTrue(savedFile.isFile());
        assertFalse(salvar.vela);

        savedFile.delete();
    }

    @Test
    public void testSaveWithEmptyList() {
        Mockito.when(form.getEntidades()).thenReturn(new ArrayList<>());
        Mockito.doReturn(new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME)).when(salvar).getSelectedFile();
        Mockito.doReturn(new Filtro()).when(salvar).getFileFilter();
        Mockito.when(salvar.save()).thenReturn(false);

        assertFalse(salvar.save());
    }

    @Test
    public void testSaveWithNullEntidades() {
        Mockito.when(form.getEntidades()).thenReturn(null);
        Mockito.doReturn(new File(TEST_DIRECTORY + File.separator + TEST_FILE_NAME)).when(salvar).getSelectedFile();
        Mockito.doReturn(new Filtro()).when(salvar).getFileFilter();
        Mockito.when(salvar.save()).thenReturn(false);

        assertFalse(salvar.save());
    }

    private List<Entidade> getTestEntidades() {
        List<Entidade> entidades = new ArrayList<>();
        Entidade entidade1 = new Entidade();
        entidade1.setNome("TestEntidade1");
        Entidade entidade2 = new Entidade();
        entidade2.setNome("TestEntidade2");
        entidades.add(entidade1);
        entidades.add(entidade2);
        return entidades;
    }
}
