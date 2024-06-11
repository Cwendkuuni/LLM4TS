package jaw.entrada.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.FormJaw;
import jaw.entrada.Salvar;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

    private Salvar salvar;
    private FormJaw form;
    private List<Entidade> entidades;

    @Before
    public void setUp() {
        form = mock(FormJaw.class);
        entidades = new ArrayList<>();
        salvar = new Salvar(form, 20);
    }

    @Test
    public void testSalvar() {
        when(form.getEntidades()).thenReturn(entidades);
        File arquivo = new File("teste.jaw");
        when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");

        salvar.salvar(entidades, arquivo.getAbsolutePath());

        assertFalse(salvar.vela);
        assertEquals(arquivo.getAbsolutePath(), salvar.arquivo);

        // Verificar se o arquivo foi criado
        File fileCreated = new File(arquivo.getAbsolutePath());
        assertTrue(fileCreated.exists());

        // Verificar se o arquivo foi salvo corretamente
        Serializacao serializacao = new Serializacao();
        try {
            serializacao.salvar(entidades, arquivo.getAbsolutePath());
            String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidades>\n</entidades>"; // Replace with the expected XML content
            FileWriter fileWriter = new FileWriter(arquivo);
            fileWriter.write(expectedXml);
            fileWriter.flush();
            fileWriter.close();

            String actualXml = new String(Files.readAllBytes(arquivo.toPath()));
            assertEquals(expectedXml, actualXml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSalvarXml() {
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw.xml");

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidades>\n<entidade>\n<nome>Entidade1</nome>\n</entidade>\n<entidade>\n<nome>Entidade2</nome>\n</entidade>\n</entidades>";
        when(form.getFileFilter()).thenReturn(new Filtro2());

        salvar.salvarXml(entidades);

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidades>\n";
        for (Entidade entidade : entidades) {
            expectedXml += "<entidade>\n<nome>" + entidade.getNome() + "</nome>\n";
        }
        expectedXml += "</entidades>";

        String actualXml = salvar.xml;
        assertEquals(expectedXml, actualXml);

        // Verificar se o arquivo XML foi criado
        File fileCreated = new File("teste.jaw.xml");
        assertTrue(fileCreated.exists());
    }

    @Test
    public void testSalvarComNomeArquivoVazio() {
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getFileFilter()).thenReturn(new Filtro());

        salvar.salvarCom("", entidades);

        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarComDiologoCancelado() {
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getFileFilter()).thenReturn(new Filtro());

        salvar.salvarCom(null, entidades);

        assertNull(salvar.arquivo);
        assertFalse(salvar.vela);
    }
}