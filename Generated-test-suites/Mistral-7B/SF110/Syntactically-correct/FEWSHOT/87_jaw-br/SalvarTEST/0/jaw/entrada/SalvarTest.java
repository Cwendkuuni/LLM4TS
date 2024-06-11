package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private FileDialog fileDialog;
    private JFileChooser fileChooser;
    private FileFilter xmlFilter;
    private FileFilter jawFilter;
    private List<Entidade> entidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);

        fileDialog = mock(FileDialog.class);
        fileChooser = mock(JFileChooser.class);

        xmlFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
            }

            @Override
            public String getDescription() {
                return "XML JAW files";
            }
        };

        jawFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
            }

            @Override
            public String getDescription() {
                return "JAW files";
            }
        };

        when(fileChooser.getFileFilter()).thenReturn(jawFilter);
        when(fileChooser.setFileFilter(xmlFilter)).thenReturn(fileChooser);
        when(fileChooser.showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw"));
        when(fileDialog.getFilesSelected()).thenReturn(new String[]{"path/to/save/file.jaw"});
    }

    @Test
    public void testConstruction() {
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs20() {
        String expectedFilePath = "path/to/save/file.jaw";
        String expectedErrorMessage = "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !";

        doThrow(IOException.class).when(salvar).salvar(entidades, expectedFilePath);

        salvar.salvar(form, 20);

        verify(form, times(1)).showMessageDialog(expectedErrorMessage, "Salvando Arquivo", 0);
        verify(Main.janelaPrincipal, times(1)).log(anyString());
    }

    @Test
    public void testSalvarWhenTipoSalvarIs30() {
        String expectedFilePath = "path/to/save/file.jaw";
        String expectedSuccessMessage = "Arquivo salvo com sucesso!";

        when(salvar.salvar(entidades, expectedFilePath)).thenReturn(true);

        salvar.salvar(form, 30);

        verify(form, times(1)).showMessageDialog(expectedSuccessMessage, "Jaw - Salvar", 1);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndFileNameIsEmpty() {
        // This test covers the case when the user clicks the "Salvar com nome" button without selecting a file or providing a name
        salvar.salvar(form, 10);

        verify(fileChooser, never()).showSaveDialog(form);
        verify(form, never()).setNomeArquivoSalvo("");
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndFileNameIsNotEmpty() {
        when(form.getNomeArquivoSalvo()).thenReturn("name.jaw");

        salvar.salvar(form, 10);

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("name.jaw");
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndUserCancelsFileSelection() {
        when(fileChooser.getSelectedFile()).thenReturn(null);

        salvar.salvar(form, 10);

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, never()).setNomeArquivoSalvo("");
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndUserSelectsFile() {
        String expectedFilePath = "path/to/save/file.jaw";
        String expectedSuccessMessage = "Arquivo Exportado com sucesso!";

        when(fileChooser.getSelectedFile()).thenReturn(new File(expectedFilePath));

        when(fileDialog.getFilesSelected()).thenReturn(new String[]{expectedFilePath});
        when(fileDialog.setFilenameFilter(xmlFilter)).thenReturn(fileDialog);
        when(fileDialog.showDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        when(salvar.salvar(entidades, expectedFilePath)).thenReturn(true);

        salvar.salvar(form, 10);

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileDialog, times(1)).setFilenameFilter(xmlFilter);
        verify(fileDialog, times(1)).showDialog(any(Component.class));
        verify(form, times(1)).setNomeArquivoSalvo(expectedFilePath);
        verify(form, times(1)).showMessageDialog(expectedSuccessMessage, "Jaw - Salvar", 1);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndUserSelectsFileNameWithInvalidExtension() {
        String expectedFilePath = "path/to/save/file";
        String expectedErrorMessage = "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !";

        when(fileChooser.getSelectedFile()).thenReturn(new File(expectedFilePath));

        salvar.salvar(form, 10);

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, never()).setNomeArquivoSalvo("");
        verify(form, times(1)).showMessageDialog(expectedErrorMessage, "Salvando Arquivo", 0);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndUserSelectsFileNameWithValidExtension() {
        String expectedFilePath = "path/to/save/file.xml";
        String expectedSuccessMessage = "Arquivo Exportado com sucesso!";

        when(fileChooser.getSelectedFile()).thenReturn(new File(expectedFilePath.substring(0, expectedFilePath.lastIndexOf("."))));
        when(fileDialog.getFilesSelected()).thenReturn(new String[]{expectedFilePath});
        when(fileDialog.setFilenameFilter(xmlFilter)).thenReturn(fileDialog);
        when(fileDialog.showDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        when(salvar.salvar(entidades, expectedFilePath)).thenReturn(true);

        salvar.salvar(form, 10);

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileDialog, times(1)).setFilenameFilter(xmlFilter);
        verify(fileDialog, times(1)).showDialog(any(Component.class));
        verify(form, times(1)).setNomeArquivoSalvo(expectedFilePath);
        verify(form, times(1)).showMessageDialog(expectedSuccessMessage, "Jaw - Salvar", 1);
    }

}