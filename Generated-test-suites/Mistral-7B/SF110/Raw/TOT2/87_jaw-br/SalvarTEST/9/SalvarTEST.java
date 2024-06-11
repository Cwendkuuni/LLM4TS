package jaw.entrada;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.io.*;
import java.util.*;

public class SalvarTest {
    Salvar salvar;
    ProcessarEntidades form;
    List<Entidade> entidades;

    @Before
    public void setUp() {
        form = Mockito.mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        entidades = new ArrayList<>();
    }

    @Test
    public void testSalvarMethod() {
        JFileChooser escolhedor = Mockito.mock(JFileChooser.class);
        when(form.getEntidades()).thenReturn(entidades);

        // Test with valid inputs
        Mockito.when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw"));
        salvar.Salvar(form, 20, escolhedor);
        assertTrue(salvar.vela);
        assertEquals("test.jaw", salvar.arquivo);

        // Test with invalid inputs
        Mockito.when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);
        Mockito.when(form.getName()).thenReturn("");
        salvar.Salvar(form, 20, escolhedor);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarXMLMethod() throws IOException {
        when(form.getEntidades()).thenReturn(entidades);
        String expectedXml = "<entidades>\n<entidade>\n<id>1</id>\n<nome>Test</nome>\n</entidade>\n</entidades>";

        Salvar salvarInstance = Mockito.spy(salvar);
        File file = new File("test.xml");
        FileWriter fileWriter = Mockito.mock(FileWriter.class);

        when(new SerializacaoXML().entidades2XML(entidades)).thenReturn(expectedXml);
        when(fileWriter.write(expectedXml)).thenReturn(expectedXml.length());

        salvarInstance.Salvar(form, 10, file, fileWriter);
        assertTrue(salvarInstance.vela);
        assertEquals(expectedXml, salvarInstance.xml);
        verify(fileWriter, times(1)).write(expectedXml);
    }

    @Test
    public void testSalvarWithInvalidFileFilter() {
        when(form.getEntidades()).thenReturn(entidades);
        JFileChooser escolhedor = Mockito.mock(JFileChooser.class);

        Mockito.when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        Mockito.when(escolhedor.getSelectedFile()).thenReturn(new File("test.txt"));
        Mockito.when(form.getName()).thenReturn("");

        salvar.Salvar(form, 10, escolhedor);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);

        Mockito.verify(escolhedor, times(1)).setFileFilter(Mockito.isA(Filtro.class)); // Verify the correct file filter is used
    }

    @Test(expected = Exception.class)
    public void testSalvarWithInvalidFileWrite() throws IOException {
        when(form.getEntidades()).thenReturn(entidades);
        File file = new File("test.xml");
        FileWriter fileWriter = Mockito.mock(FileWriter.class);
        Mockito.when(fileWriter.write(Mockito.anyString())).thenThrow(new IOException());

        salvar.Salvar(form, 10, file, fileWriter);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarWithExceptionInGetEntities() {
        when(form.getEntidades()).thenThrow(new Exception("Test Exception"));
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }
}