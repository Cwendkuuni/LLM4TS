package jaw.entrada;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JOptionPane;
import javax.swing.filechooser.JFileChooser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    private JFileChooser escolhedor;
    private Filtro2 filtroXml;
    private Filtro filtroJaw;
    private String arquivoXML;
    private String arquivoJaw;
    private File diretorioProjetos;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        escolhedor = new JFileChooser();
        filtroXml = new Filtro2();
        filtroJaw = new Filtro();
        diretorioProjetos = new File(Main.configuracao.getCaminhoDosProjetos());
    }

    @After
    public void tearDown() {
        form = null;
        escolhedor = null;
        filtroXml = null;
        filtroJaw = null;
        arquivoXML = null;
        arquivoJaw = null;
    }

    @Test
    public void testAbrirXML() {
        arquivoXML = "teste.jaw.xml";
        File arquivoXMLFile = new File(arquivoXML);

        escolhedor.setFileFilter(filtroXml);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(arquivoXMLFile);

        new Abrir(form, arquivoXML);

        assertEquals(arquivoXML, form.getNomeArquivoSalvo());
        // Assuming form.getEntidades() is a getter method for a non-static, serializable field
        assertNotNull(form.getEntidades());
        assertTrue(form.getEntidades() instanceof Serializable);
    }

    @Test
    public void testAbrirJaw() {
        arquivoJaw = "teste.jaw";
        File arquivoJawFile = new File(arquivoJaw);

        escolhedor.setFileFilter(filtroJaw);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(arquivoJawFile);

        new Abrir(form, arquivoJaw);

        assertEquals(arquivoJaw, form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }

    @Test
    public void testAbrirNenhumArquivo() {
        escolhedor.showOpenDialog(null);

        new Abrir(form, 20);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }

    @Test
    public void testAbrirXMLInvalido() {
        arquivoXML = "teste.xml";
        File arquivoXMLFile = new File(arquivoXML);

        escolhedor.setFileFilter(filtroXml);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(arquivoXMLFile);

        new Abrir(form, 10);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertTrue(JOptionPane.showMessageDialog(null, "", "Importação XML", JOptionPane.ERROR_MESSAGE).equals("Arquivo xml corrompido ou incompatível com aplicação!"));
    }

    @Test
    public void testAbrirJawInvalido() {
        arquivoJaw = "teste";

        new Abrir(form, 20);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertTrue(JOptionPane.showMessageDialog(null, "", "Abrir Arquivo jaw", JOptionPane.ERROR_MESSAGE).equals("Arquivo jaw corrompido ou incompatível com aplicação!"));
    }

    @Test
    public void testAbrirXMLDiretorio() {
        arquivoXML = "diretorio/teste.jaw.xml";
        File arquivoXMLFile = new File(arquivoXML);
        File diretorio = arquivoXMLFile.getParentFile();

        escolhedor.setFileFilter(filtroXml);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(diretorio);

        new Abrir(form, 10);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertTrue(JOptionPane.showMessageDialog(null, "", "Importação XML", JOptionPane.ERROR_MESSAGE).equals("Arquivo xml corrompido ou incompatível com aplicação!"));
    }

    @Test
    public void testAbrirJawDiretorio() {
        arquivoJaw = "diretorio/teste.jaw";
        File arquivoJawFile = new File(arquivoJaw);
        File diretorio = arquivoJawFile.getParentFile();

        escolhedor.setFileFilter(filtroJaw);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(diretorio);

        new Abrir(form, 20);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertTrue(JOptionPane.showMessageDialog(null, "", "Abrir Arquivo jaw", JOptionPane.ERROR_MESSAGE).equals("Arquivo jaw corrompido ou incompatível com aplicação!"));
    }

    @Test
    public void testAbrirXMLFileFilter() {
        arquivoXML = "teste.jaw.xml";
        File arquivoXMLFile = new File(arquivoXML);

        escolhedor.setFileFilter(filtroXml);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(arquivoXMLFile);

        File[] files = {new File("teste.txt"), arquivoXMLFile};
        escolhedor.setFilesSelectedInputs(files);

        new Abrir(form, 10);

        assertEquals(arquivoXML, form.getNomeArquivoSalvo());
        // Assuming form.getEntidades() is a getter method for a non-static, serializable field
        assertNotNull(form.getEntidades());
        assertTrue(form.getEntidades() instanceof Serializable);
    }

    @Test
    public void testAbrirJawFileFilter() {
        arquivoJaw = "teste.jaw";
        File arquivoJawFile = new File(arquivoJaw);

        escolhedor.setFileFilter(filtroJaw);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(arquivoJawFile);

        File[] files = {new File("teste.txt"), arquivoJawFile};
        escolhedor.setFilesSelectedInputs(files);

        new Abrir(form, 20);

        assertEquals(arquivoJaw, form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }

    @Test
    public void testAbrirXMLFileFilterAndInvalidFile() {
        arquivoXML = "teste.xml";
        File arquivoXMLFile = new File(arquivoXML);

        escolhedor.setFileFilter(filtroXml);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(arquivoXMLFile);

        File[] files = {new File("teste.txt"), arquivoXMLFile};
        escolhedor.setFilesSelectedInputs(files);

        new Abrir(form, 10);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertTrue(JOptionPane.showMessageDialog(null, "", "Importação XML", JOptionPane.ERROR_MESSAGE).equals("Arquivo xml corrompido ou incompatível com aplicação!"));
    }

    @Test
    public void testAbrirJawFileFilterAndInvalidFile() {
        arquivoJaw = "teste";

        escolhedor.setFileFilter(filtroJaw);
        escolhedor.showOpenDialog(null);
        escolhedor.setSelectedFile(new File("teste.txt"));

        File[] files = {new File("teste.txt"), new File(arquivoJaw)};
        escolhedor.setFilesSelectedInputs(files);

        new Abrir(form, 20);

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
        assertTrue(JOptionPane.showMessageDialog(null, "", "Abrir Arquivo jaw", JOptionPane.ERROR_MESSAGE).equals("Arquivo jaw corrompido ou incompatível com aplicação!"));
    }
}