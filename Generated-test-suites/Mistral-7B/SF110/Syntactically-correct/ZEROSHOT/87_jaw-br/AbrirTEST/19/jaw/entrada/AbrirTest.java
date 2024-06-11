package jaw.entrada.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.FileDialog;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;
    private final static String XML_VALIDO = "teste.jaw.xml";
    private final static String ARQUIVO_VALIDO = "teste.jaw";
    private final static String XML_INVALIDO = "teste.txt";
    private final static String XML_INCOMPATIVEL = "teste.jaw.bin";
    private final static String XML_INVALIDO_EXTENSAO = "teste.xml";
    private final static String XML_INVALIDO_FORMATO = "teste.xml.bin";
    private final static String DIRETORIO_VALIDO = "testes";

    @Before
    public void setup() {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, Abrir.ABRIR_XML);
    }

    @Test
    public void testAbrirXMLValido() {
        // Given
        File arquivoXML = new File(DIRETORIO_VALIDO, XML_VALIDO);
        InputStream inputStream = new FileInputStream(arquivoXML);

        // When
        abrir = new Abrir(form, XML_VALIDO);

        // Then
        assertEquals("teste.jaw.xml", abrir.getXml());
        assertTrue(form.getEntidades() != null);
    }

    @Test
    public void testAbrirXMLInvalidoExtensao() {
        // Given

        // When
        abrir = new Abrir(form, XML_INVALIDO_EXTENSAO);

        // Then
        assertNull(abrir.getXml());
        assertSame(form.getEntidades(), new Object[0]);
        assertTrue(JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompatível com aplicação!", "Importação XML", 0) != null);
    }

    @Test
    public void testAbrirXMLInvalidoFormato() {
        // Given
        File arquivoXML = new File(DIRETORIO_VALIDO, XML_INVALIDO_FORMATO);

        // When
        abrir = new Abrir(form, XML_INVALIDO_FORMATO);

        // Then
        assertNull(abrir.getXml());
        assertSame(form.getEntidades(), new Object[0]);
        assertTrue(JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompatível com aplicação!", "Importação XML", 0) != null);
    }

    @Test
    public void testAbrirXMLInvalido() {
        // Given
        File arquivoXML = new File(DIRETORIO_VALIDO, XML_INVALIDO);

        // When
        abrir = new Abrir(form, XML_INVALIDO);

        // Then
        assertNull(abrir.getXml());
        assertSame(form.getEntidades(), new Object[0]);
        assertTrue(JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompatível com aplicação!", "Importação XML", 0) != null);
    }

    @Test
    public void testAbrirArquivoValido() {
        // Given
        File arquivo = new File(DIRETORIO_VALIDO, ARQUIVO_VALIDO);

        // When
        abrir = new Abrir(form, Abrir.ABRIR, arquivo);

        // Then
        assertEquals("teste.jaw", abrir.getXml());
        assertTrue(form.getEntidades() != null);
    }

    @Test
    public void testAbrirArquivoInvalidoExtensao() {
        // Given
        File arquivo = new File(DIRETORIO_VALIDO, XML_INVALIDO_EXTENSAO);

        // When
        abrir = new Abrir(form, Abrir.ABRIR, arquivo);

        // Then
        assertNull(abrir.getXml());
        assertSame(form.getEntidades(), new Object[0]);
        assertTrue(JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompatível com aplicação!", "Abrir Arquivo jaw", 0) != null);
    }

    @Test
    public void testAbrirArquivoInvalidoFormat() {
        // Given
        File arquivo = new File(DIRETORIO_VALIDO, XML_INVALIDO_FORMATO);

        // When
        abrir = new Abrir(form, Abrir.ABRIR, arquivo);

        // Then
        assertNull(abrir.getXml());
        assertSame(form.getEntidades(), new Object[0]);
        assertTrue(JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompatível com aplicação!", "Abrir Arquivo jaw", 0) != null);
    }

    @Test
    public void testAbrirArquivoNaoSelecionado() {
        // Given
        JFileChooser escolhedor = new JFileChooser();

        // When
        int retorno = escolhedor.showOpenDialog(null);

        // Then
        assertFalse(retorno == JFileChooser.APPROVE_OPTION);

        // Given
        abrir = new Abrir(form, Abrir.ABRIR);

        // Then
        assertNull(abrir.getXml());
        assertSame(form.getEntidades(), new Object[0]);
    }

    @Test
    public void testAbrirXMLEscolhido() throws IOException {
        // Given
        File arquivoXML = new File(DIRETORIO_VALIDO, XML_VALIDO);
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setFileFilter(new Abrir.Filtro2());

        // When
        escolhedor.showOpenDialog(null);
        File arquivoSelecionado = escolhedor.getSelectedFile();
        if (arquivoSelecionado != null) {
            abrir = new Abrir(form, Abrir.ABRIR_XML, arquivoSelecionado);
        }

        // Then
        if (arquivoSelecionado != null && arquivoSelecionado.getName().endsWith(".jaw.xml")) {
            assertEquals("teste.jaw.xml", abrir.getXml());
            assertTrue(form.getEntidades() != null);
        } else {
            assertNull(abrir.getXml());
            assertSame(form.getEntidades(), new Object[0]);
        }
    }

    @Test
    public void testAbrirSerializadoEscolhido() throws IOException {
        // Given
        File arquivo = new File(DIRETORIO_VALIDO, ARQUIVO_VALIDO);
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setFileFilter(new Abrir.Filtro());

        // When
        escolhedor.showOpenDialog(null);
        File arquivoSelecionado = escolhedor.getSelectedFile();
        if (arquivoSelecionado != null) {
            abrir = new Abrir(form, Abrir.ABRIR, arquivoSelecionado);
        }

        // Then
        if (arquivoSelecionado != null && arquivoSelecionado.getName().endsWith(".jaw")) {
            assertEquals("teste.jaw", abrir.getXml());
            assertTrue(form.getEntidades() != null);
        } else {
            assertNull(abrir.getXml());
            assertSame(form.getEntidades(), new Object[0]);
        }
    }
}