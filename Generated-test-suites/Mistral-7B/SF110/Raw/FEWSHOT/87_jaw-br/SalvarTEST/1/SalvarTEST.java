package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.FileChooserAdapter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.util.ExceptionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.EntidadeFactory;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private FileDialog fileDialog;
    private JFileChooser fileChooser;
    private JTextField nomeArquivoSalvoTextField;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        fileDialog = mock(FileDialog.class);
        fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));
        nomeArquivoSalvoTextField = mock(JTextField.class);
        doReturn(nomeArquivoSalvoTextField).when(form).getNameArquivoSalvo();
        doNothing().when(form).setNomeArquivoSalvo(anyString());
        doReturn(fileChooser).when(form).getFileChooser();
    }

    @Test
    public void testSalvarJawFile() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verify(fileChooser, times(1)).setFileFilter(isA(Filtro.class));
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).setFileFilter(isA(Filtro2.class));
        verifyZeroInteractions(form);
        verify(form, times(1)).setNomeArquivoSalvo("test.jaw");
        verifyZeroInteractions(salvar.entidade);
        verify(salvar, times(1)).arquivo = "test.jaw";
        verify(salvar, times(1)).salvar(salvar.entidade, "test.jaw");
        verify(JOptionPane, times(1)).showMessageDialog(form, "Arquivo salvo com sucesso!", "Jaw - Salvar", 1);
    }

    @Test
    public void testSalvarXmlFile() {
        // Given
        when(form.getName()).thenReturn("Name");
        when(form.getNameArquivoSalvo()).thenReturn("");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verify(fileChooser, times(1)).setFileFilter(isA(Filtro2.class));
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).setFileFilter(isA(Filtro.class));
        verify(form, times(1)).setNomeArquivoSalvo("test.jaw.xml");
        verify(salvar, times(1)).arquivo = "test.jaw.xml";
        verify(salvar, times(1)).xml = anyString();
        verify(salvaXML, times(1)).entidades2XML(salvar.entidade);
        verify(new FileWriter(salvar.arquivo), times(1)).write(salvar.xml);
        verify(JOptionPane, times(1)).showMessageDialog(form, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
    }

    @Test
    public void testSalvarWithoutFileSelectOrName() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verifyZeroInteractions(JOptionPane.class);
    }

    @Test(expected = IOException.class)
    public void testSalvarException() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        doThrow(new IOException("Test IOException")).when(salvar).salvar(salvar.entidade, salvar.arquivo);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verify(JOptionPane, times(1)).showMessageDialog(null, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
        verifyZeroInteractions(Main.janelaPrincipal.log.class);
    }

    @Test
    public void testSalvarWithNullFileFilter() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verify(fileChooser, times(1)).setFileFilter(null);
        verifyZeroInteractions(JOptionPane.class);
    }

    @Test
    public void testSalvarWithFileFilterNullFile() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verify(fileChooser, times(1)).setFileFilter(isA(Filtro.class));
        verifyZeroInteractions(fileChooser.getSelectedFile());
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verifyZeroInteractions(JOptionPane.class);
    }

    @Test
    public void testSalvarNullEntidades() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verifyZeroInteractions(JOptionPane.class);
    }

    @Test
    public void testSalvarWithNullArquivo() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verifyZeroInteractions(JOptionPane.class);
    }

    @Test
    public void testSalvarNullSerializacaoException() {
        // Given
        when(form.getName()).thenReturn("Name");
        when(salvar.entidade).thenReturn(null);

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verify(JOptionPane, times(1)).showMessageDialog(null, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
        verifyZeroInteractions(Main.janelaPrincipal.log.class);
    }

    @Test
    public void testSalvarNullSerializacaoXMLException() {
        // Given
        when(form.getName()).thenReturn("Name");
        when(salvar.entidade).thenReturn(new ArrayList<Entidade>());
        when(salvar.xml).thenReturn(null);

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verify(JOptionPane, times(1)).showMessageDialog(null, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
        verifyZeroInteractions(Main.janelaPrincipal.log.class);
    }

    @Test
    public void testSalvarFileExists() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verifyZeroInteractions(salvar);
        verifyZeroInteractions(JOptionPane.class);
        verifyZeroInteractions(Main.janelaPrincipal.log.class);

        // Given
        doThrow(new IOException("File already exists")).when(salvar).salvar(salvar.entidade, salvar.arquivo);

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verify(JOptionPane, times(1)).showMessageDialog(null, "Arquivo já existe.", "Jaw - Salvar", 0);
        verifyZeroInteractions(Main.janelaPrincipal.log.class);
    }

    @Test
    public void testSalvarExceptionWithMessage() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        doThrow(new IOException("Test IOException")).when(salvar).salvar(salvar.entidade, salvar.arquivo);

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verify(JOptionPane, times(1)).showMessageDialog(null, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !\nTest IOException", "Salvando Arquivo", 0);
        verifyZeroInteractions(Main.janelaPrincipal.log.class);
    }

    @Test
    public void testSalvarExceptionWithMessageInLog() {
        // Given
        when(form.getName()).thenReturn("Name");

        // When
        doThrow(new IOException("Test IOException")).when(salvar).salvar(salvar.entidade, salvar.arquivo);

        // When
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                salvar.salvar();
            }
        });

        // Then
        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(form);
        verify(JOptionPane, times(1)).showMessageDialog(null, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !\nTest IOException", "Salvando Arquivo", 0);
        verify(Main.janelaPrincipal.log, times(1)).log("Test IOException");
    }
}

class Filtro2 extends FileFilter {
    @Override
    public boolean accept(final File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
    }

    @Override
    public String getDescription() {
        return "XML JAW files";
    }
}

class Filtro extends FileFilter {
    @Override
    public boolean accept(final File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
    }

    @Override
    public String getDescription() {
        return "JAW files";
    }
}