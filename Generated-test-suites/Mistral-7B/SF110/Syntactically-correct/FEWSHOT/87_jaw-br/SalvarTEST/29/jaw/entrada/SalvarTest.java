package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;

public class SalvarTest {

    @Test
    public void testSalvarWithoutFileChooser() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, 20);
        assertNull(salvar.arquivo);
        assertFalse(salvar.vela);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testSalvarWithEmptyFileName() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        Salvar salvar = new Salvar(form, 30);
        assertNull(salver.arquivo);
        assertFalse(salver.vela);
    }

    @Test
    public void testSalvarWithNullFileChooser() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, 10);
        assertNull(salvar.arquivo);
        assertFalse(salvar.vela);
    }

    @Test
    public void testSaveWithInvalidFileExtension() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.txt");
        Salvar salvar = new Salvar(form, 20);
        assertFalse(salvar.vela);
        assertEquals("test.txt", salvar.arquivo);
    }

    @Test
    public void testSaveWithValidFileExtension() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        Salvar salvar = new Salvar(form, 20);
        assertTrue(salvar.vela);
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testSaveWithNullFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        Salvar salvar = new Salvar(form, 20);
        assertFalse(salvar.vela);
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testSaveWithFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        Salvar salvar = new Salvar(form, 20);
        assertTrue(salvar.vela);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("test.jaw");
        when(form.getFileChooser().getSelectedFile()).thenReturn(selectedFile);
        salvar.Salvar(form, 20);
        assertEquals("test.jaw", salvar.arquivo);
        assertTrue(salvar.vela);
    }

    @Test
    public void testSaveWithNullSelectedFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        Salvar salvar = new Salvar(form, 20);
        assertFalse(salvar.vela);
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testSaveWithEmptyListOfEntities() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        Salvar salvar = new Salvar(form, 20);
        assertTrue(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSaveWithListOfEntities() throws IOException {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        Salvar salvar = new Salvar(form, 20);

        Entidade entidade1 = MetamodeloFactory.e("entidade1", "Tipo1", "Descr1");
        Entidade entidade2 = MetamodeloFactory.e("entidade2", "Tipo2", "Descr2");
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(entidade1);
        entidades.add(entidade2);
        when(form.getEntidades()).thenReturn(entidades);

        String expectedArquivo = "test.jaw";
        when(form.getFileChooser().showSaveDialog(eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);

        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn(expectedArquivo);
        when(form.getFileChooser().getSelectedFile()).thenReturn(selectedFile);

        salvar.Salvar(form, 20);
        assertEquals(expectedArquivo, salvar.arquivo);
        assertTrue(salvar.vela);

        FileWriter writer = mock(FileWriter.class);
        when(form.getSerializacao()).thenReturn(mock(Serializacao.class));
        when(form.getSerializacao().salvar(eq(entidades), eq(expectedArquivo))).thenReturn(true);
        when(writer.write(anyString())).thenReturn(1);
        when(writer.flush()).thenReturn(true);
        when(writer.close()).thenReturn(true);

        Serializacao serializacao = mock(Serializacao.class);
        when(form.getSerializacao()).thenReturn(serializacao);

        salvar.Salvar(form, 20);
    }

    @Test
    public void testSaveWithListOfEntitiesAndXmlFile() throws IOException {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, 10);

        Entidade entidade1 = MetamodeloFactory.e("entidade1", "Tipo1", "Descr1");
        Entidade entidade2 = MetamodeloFactory.e("entidade2", "Tipo2", "Descr2");
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(entidade1);
        entidades.add(entidade2);
        when(form.getEntidades()).thenReturn(entidades);

        String expectedArquivo = "test.jaw.xml";
        when(form.getFileChooser().showSaveDialog(eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);

        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn(expectedArquivo);
        when(form.getFileChooser().getSelectedFile()).thenReturn(selectedFile);

        SerializacaoXML serializacaoXML = mock(SerializacaoXML.class);
        when(form.getSerializacaoXML()).thenReturn(serializacaoXML);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jaw:Jaw xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:jaw=\"http://www.jaw-project.com/metamodelo/\">\n<jaw:Entidade>\n<jaw:Descr>Descr1</jaw:Descr>\n<jaw:Nome>entidade1</jaw:Nome>\n<jaw:Tipo>Tipo1</jaw:Tipo>\n</jaw:Entidade>\n<jaw:Entidade>\n<jaw:Descr>Descr2</jaw:Descr>\n<jaw:Nome>entidade2</jaw:Nome>\n<jaw:Tipo>Tipo2</jaw:Tipo>\n</jaw:Entidade>\n</jaw:Jaw>";
        when(serializacaoXML.entidades2XML(anyList())).thenReturn(xml);

        FileWriter writer = mock(FileWriter.class);
        when(writer.write(anyString())).thenReturn(xml.length());
        when(writer.flush()).thenReturn(true);
        when(writer.close()).thenReturn(true);

        salvar.Salvar(form, 10);
        assertEquals(expectedArquivo, salvar.arquivo);
        assertTrue(salvar.vela);

        FileWriter xmlWriter = mock(FileWriter.class);
        when(form.getFileWriter()).thenReturn(xmlWriter);
        when(xmlWriter.write(xml)).thenReturn(xml.length());
        when(xmlWriter.flush()).thenReturn(true);
        when(xmlWriter.close()).thenReturn(true);

        salvar.Salvar(form, 10);
    }

    @Test
    public void testSaveWithInvalidFormatException() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, 20);

        Entidade entidade1 = MetamodeloFactory.e("entidade1", "Tipo1", "Descr1");
        Entidade entidade2 = MetamodeloFactory.e("entidade2", "Tipo2", "Descr2");
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(entidade1);
        entidades.add(entidade2);
        when(form.getEntidades()).thenReturn(entidades);

        when(form.getFileChooser().showSaveDialog(eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);

        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("test.xml");
        when(form.getFileChooser().getSelectedFile()).thenReturn(selectedFile);

        SerializacaoXML serializacaoXML = mock(SerializacaoXML.class);
        when(form.getSerializacaoXML()).thenReturn(serializacaoXML);

        when(serializacaoXML.entidades2XML(anyList())).thenThrow(new java.text.ParseException());

        Salvar salvar2 = new Salvar(form, 10);
        salvar2.Salvar(form, 10);
        assertFalse(salvar2.vela);
        assertNull(salvar2.arquivo);
    }
}