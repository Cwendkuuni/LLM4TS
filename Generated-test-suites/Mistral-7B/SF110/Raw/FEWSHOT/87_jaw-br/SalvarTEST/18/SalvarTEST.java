package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.src.bean.Tipo;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private JFileChooser fileChooser;
    private File selectedFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        fileChooser = mock(JFileChooser.class);
    }

    @Test
    public void testSalvarWithXmlFileFilter() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile = new File("path/to/save/file.jaw.xml"));

        salvar.Salvar(form, 10);

        verify(fileChooser, times(1)).setFileFilter(new IsXmlFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw.xml");
        assertTrue(salvar.xml.length() > 0);

        try {
            FileWriter fileWriter = new FileWriter(salvar.arquivo);
            fileWriter.write(salvar.xml);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to write XML file");
        }

        verify(fileChooser, times(1)).setFileFilter(isA(FileFilter.class));
        verify(fileChooser, times(1)).setDialogTitle("Salvar arquivo JAW (.jaw.xml)");
        verify(fileChooser, times(1)).setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
        verify(fileChooser, times(1)).setAcceptAllFileFilterUsed(false);
        verify(fileChooser, times(1)).pack();
        verify(fileChooser, times(1)).setApproveButtonText("Salvar");
        verify(fileChooser, times(1)).setApproveButtonMnemonic(KeyEvent.VK_S);
        verify(fileChooser, times(1)).setApproveButtonToolTipText("Salvar o arquivo JAW (.jaw.xml)");
        verify(fileChooser, times(1)).setFileFilter(any(FileNameExtensionFilter.class));
    }

    @Test
    public void testSalvarWithJawFileFilter() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile = new File("path/to/save/file.jaw"));

        salvar.Salvar(form, 20);

        verify(fileChooser, times(1)).setFileFilter(new IsJawFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw");

        try {
            Serializacao serializacao = new Serializacao();
            serializacao.salvar(salvar.entidade, salvar.arquivo);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to save JAW file");
        }

        verify(fileChooser, times(1)).setFileFilter(isA(FileFilter.class));
        verify(fileChooser, times(1)).setDialogTitle("Salvar arquivo JAW (.jaw)");
        verify(fileChooser, times(1)).setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
        verify(fileChooser, times(1)).pack();
        verify(fileChooser, times(1)).setApproveButtonText("Salvar");
        verify(fileChooser, times(1)).setApproveButtonMnemonic(KeyEvent.VK_S);
        verify(fileChooser, times(1)).setApproveButtonToolTipText("Salvar o arquivo JAW (.jaw)");
        verify(fileChooser, times(1)).setFileFilter(any(FileNameExtensionFilter.class));
    }

    @Test
    public void testSalvarWithNullFileChooser() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

        salvar.Salvar(form, 20);

        verify(fileChooser, never()).setFileFilter(any(FileFilter.class));
    }

    @Test
    public void testSalvarWithEmptyFileName() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(""));

        salvar.Salvar(form, 20);

        verify(fileChooser, times(1)).setFileFilter(new IsJawFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("");
        assertFalse(salvar.arquivo.isEmpty());
    }

    @Test
    public void testSalvarWithValidFileName() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw"));

        salvar.Salvar(form, 20);

        verify(fileChooser, times(1)).setFileFilter(new IsJawFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw");
        assertTrue(salvar.arquivo.startsWith("path/to/save/file.jaw"));
    }

    @Test
    public void testSalvarWithInvalidFileName() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/invalidFile"));

        salvar.Salvar(form, 20);

        verify(fileChooser, times(1)).setFileFilter(new IsJawFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, never()).setNomeArquivoSalvo("path/to/save/invalidFile");
    }

    @Test
    public void testSalvarWithErrorWhenWritingFile() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw"));
        doThrow(IOException.class).when(salvar).salvar(salvar.entidade, salvar.arquivo);

        salvar.Salvar(form, 20);

        verify(fileChooser, times(1)).setFileFilter(new IsJawFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(JOptionPane, times(1)).showMessageDialog(form, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
        verify(Main.janelaPrincipal, times(1)).log("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !");
    }

    @Test
    public void testExportarWithXmlFileFilter() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile = new File("path/to/save/file.jaw.xml"));

        salvar.Salvar(form, 10);

        verify(fileChooser, times(1)).setFileFilter(new IsXmlFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw.xml");
        assertTrue(salvar.xml.length() > 0);

        try {
            FileWriter fileWriter = new FileWriter(salvar.arquivo);
            fileWriter.write(salvar.xml);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to write XML file");
        }

        verify(fileChooser, times(1)).setFileFilter(isA(FileFilter.class));
        verify(fileChooser, times(1)).setDialogTitle("Exportar para arquivo XML (.jaw.xml)");
        verify(fileChooser, times(1)).setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
        verify(fileChooser, times(1)).pack();
        verify(fileChooser, times(1)).setApproveButtonText("Exportar");
        verify(fileChooser, times(1)).setApproveButtonMnemonic(KeyEvent.VK_E);
        verify(fileChooser, times(1)).setApproveButtonToolTipText("Exportar as entidades para o formato XML (.jaw.xml)");
        verify(fileChooser, times(1)).setFileFilter(any(FileNameExtensionFilter.class));
    }

    @Test
    public void testExportarWithInvalidFileName() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/invalidFile.xml"));

        salvar.Salvar(form, 10);

        verify(fileChooser, times(1)).setFileFilter(new IsXmlFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, never()).setNomeArquivoSalvo("path/to/save/invalidFile.xml");
    }

    @Test
    public void testExportarWithErrorWhenWritingFile() {
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        when(form.getName()).thenReturn("");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw.xml"));
        doThrow(IOException.class).when(salvar).salvarXML(salvar.entidade, salvar.xml, salvar.arquivo);

        salvar.Salvar(form, 10);

        verify(fileChooser, times(1)).setFileFilter(new IsXmlFileFilter());
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(JOptionPane, times(1)).showMessageDialog(form, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
        verify(Main.janelaPrincipal, times(1)).log("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !");
    }

    private FileFilter isA(FileFilter fileFilter) {
        return isInstanceOf(fileFilter.getClass());
    }

    private FileFilter isInstanceOf(Class<? extends FileFilter> fileFilterClass) {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return fileFilterClass.isInstance(fileChooser.getFileFilter());
            }

            @Override
            public String getDescription() {
                return fileFilterClass.getSimpleName();
            }
        };
    }

    private static class IsXmlFileFilter extends FileNameExtensionFilter {
        public IsXmlFileFilter() {
            super("XML files (*.xml)", "xml");
        }
    }

    private static class IsJawFileFilter extends FileNameExtensionFilter {
        public IsJawFileFilter() {
            super("JAW files (*.jaw)", "jaw");
        }
    }
}