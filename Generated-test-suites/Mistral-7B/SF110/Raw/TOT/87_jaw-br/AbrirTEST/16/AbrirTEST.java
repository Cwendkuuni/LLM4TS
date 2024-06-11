package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.gui.JanelaPrincipal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;
    private JFileChooser fileChooser;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, "test.jaw");
        MockitoAnnotations.initMocks(this);
        fileChooser = mock(JFileChooser.class);
    }

    @Test
    public void testAbrirXMLFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw.xml"));

        abrir = new Abrir(form, fileChooser, Abrir.ABRIR_XML);

        verify(form).setNomeArquivoSalvo("test.jaw.xml");
        try {
            verify(new SerializacaoXML()).xml2Entidades(any(Reader.class));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testAbrirInvalidXMLFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.txt"));

        abrir = new Abrir(form, fileChooser, Abrir.ABRIR_XML);

        verify(form).setNomeArquivoSalvo(null);
        verify(Main.janelaPrincipal).log(anyString());
        verify(JOptionPane).showMessageDialog(eq(null), anyString(), anyString(), eq(0));
    }

    @Test
    public void testAbrirSerializadoFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));

        abrir = new Abrir(form, fileChooser, Abrir.ABRIR);

        verify(form).setNomeArquivoSalvo("test.jaw");
        try {
            verify(new Serializacao()).abrir(anyString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAbrirInvalidSerializadoFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.txt"));

        abrir = new Abrir(form, fileChooser, Abrir.ABRIR);

        verify(form).setNomeArquivoSalvo(null);
        verify(Main.janelaPrincipal).log(anyString());
        verify(JOptionPane).showMessageDialog(eq(null), anyString(), anyString(), eq(0));
    }
}