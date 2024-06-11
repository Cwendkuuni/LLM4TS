package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbrirTest {

    @Test
    public void testAbrirXMLwithValidXMLFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        String arquivoXML = "test/resources/xmlFile.jaw.xml";
        Abrir abrir = new Abrir(form, arquivoXML);
        assertEquals("", abrir.xml);
        // Add assertions to check the state of form after xml loading
    }

    @Test
    public void testAbrirSerializadoWithValidJawFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        String arquivoJaw = "test/resources/jawFile.jaw";
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.getDirectory()).thenReturn(new File(System.getProperty("user.dir")));
        when(dialog.getFiles()).thenReturn(new String[] { arquivoJaw });
        when(dialog.show()).thenReturn(JFileChooser.APPROVE_OPTION);

        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivoJaw));

        when(form.setNomeArquivoSalvo(String.valueOf(arquivoJaw))).thenReturn(true);
        when(new Serializacao().abrir(arquivoJaw)).thenReturn(mock(Entidades.class));

        abrir.setForm(form);
        abrir.setEscolhedor(escolhedor);
        abrir.setDialog(dialog);
        abrir.actionEvent(null);

        // Add assertions to check the state of form after jaw loading
    }

    @Test
    public void testAbrirSerializadoWithInvalidJawFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        String arquivoJaw = "test/resources/invalidJawFile.jaw";
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.getDirectory()).thenReturn(new File(System.getProperty("user.dir")));
        when(dialog.getFiles()).thenReturn(new String[] { arquivoJaw });
        when(dialog.show()).thenReturn(JFileChooser.APPROVE_OPTION);

        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivoJaw));

        when(form.setNomeArquivoSalvo(String.valueOf(arquivoJaw))).thenReturn(true);
        when(new Serializacao().abrir(arquivoJaw)).thenThrow(new IOException());

        abrir.setForm(form);
        abrir.setEscolhedor(escolhedor);
        abrir.setDialog(dialog);
        abrir.actionEvent(null);

        // Add assertions to check the error message displayed
    }

    @Test
    public void testAbrirXMLwithValidXMLFileFilter() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        String arquivoXML = "test/resources/xmlFile.jaw.xml";
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.getDirectory()).thenReturn(new File(System.getProperty("user.dir")));
        when(dialog.setFileFilter(new Filtro2())).thenReturn(dialog);
        when(dialog.show()).thenReturn(JFileChooser.APPROVE_OPTION);

        when(dialog.getFile()).thenReturn(new File(arquivoXML));

        when(form.setNomeArquivoSalvo(arquivoXML)).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(new FileReader(arquivoXML))).thenReturn(mock(Entidades.class));

        abrir.setForm(form);
        abrir.setDialog(dialog);
        abrir.actionEvent(null);

        // Add assertions to check the state of form after xml loading
    }

    @Test
    public void testAbrirXMLwithInvalidXMLFileFilter() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        String arquivoXML = "test/resources/invalidXMLFile.xml";
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.getDirectory()).thenReturn(new File(System.getProperty("user.dir")));
        when(dialog.setFileFilter(new Filtro2())).thenReturn(dialog);
        when(dialog.show()).thenReturn(JFileChooser.APPROVE_OPTION);

        when(dialog.getFile()).thenReturn(new File(arquivoXML));

        when(form.setNomeArquivoSalvo(arquivoXML)).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(new FileReader(arquivoXML))).thenThrow(new IOException());

        abrir.setForm(form);
        abrir.setDialog(dialog);
        abrir.actionEvent(null);

        // Add assertions to check the error message displayed
    }

    @Test
    public void testAbrirWithNoFileChosen() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.show()).thenReturn(JFileChooser.CANCEL_OPTION);

        abrir.setDialog(dialog);
        abrir.actionEvent(null);

        // Assert that nothing happens
    }
}