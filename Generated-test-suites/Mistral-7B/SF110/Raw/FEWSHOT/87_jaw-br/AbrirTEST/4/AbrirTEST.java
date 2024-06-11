package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, "test.jaw.xml");
    }

    @Test
    public void testAbrirXML() {
        String expectedXml = "testXml";
        when(new SerializacaoXML().xml2Entidades(any(FileReader.class))).thenReturn(expectedXml);
        abrir.abrirXML("test.jaw.xml");
        assertEquals(expectedXml, abrir.xml);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAbrirXMLWithInvalidFile() {
        abrir = new Abrir(form, "test.txt");
        abrir.abrirXML("test.txt");
    }

    @Test
    public void testAbrirSerializado() {
        String expectedXml = "testXml";
        when(new Serializacao().abrir(anyString())).thenReturn(expectedXml);
        abrir = new Abrir(form, "test.jaw");
        abrir.abrirSerializado("test.jaw");
        assertEquals(expectedXml, abrir.xml);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAbrirSerializadoWithInvalidFile() {
        abrir = new Abrir(form, "test.txt");
        abrir.abrirSerializado("test.txt");
    }

    @Test
    public void testOpenFileChooserXML() {
        File expectedFile = new File("testPath/test.jaw.xml");
        JFileChooser esperado = mock(JFileChooser.class);
        when(esperado.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(esperado.getSelectedFile()).thenReturn(expectedFile);

        abrir = new Abrir(form, 10);
        abrir.setFileChooser(esperado);
        abrir.abrirXML(null);

        verify(esperado).showOpenDialog(eq(null));
        verify(esperado).getSelectedFile();
    }

    @Test
    public void testOpenFileChooserSerializado() {
        File expectedFile = new File("testPath/test.jaw");
        JFileChooser esperado = mock(JFileChooser.class);
        when(esperado.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(esperado.getSelectedFile()).thenReturn(expectedFile);

        abrir = new Abrir(form, 20);
        abrir.setFileChooser(esperado);
        abrir.abrirSerializado(null);

        verify(esperado).showOpenDialog(eq(null));
        verify(esperado).getSelectedFile();
    }
}