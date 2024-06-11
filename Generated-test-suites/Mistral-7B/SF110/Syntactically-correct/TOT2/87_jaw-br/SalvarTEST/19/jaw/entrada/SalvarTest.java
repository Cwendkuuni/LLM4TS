package jaw.entrada;

import org.junit.*;
import static org.junit.Assert.*;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertNull(salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSalvarConstructorWithNullForm() {
        Salvar salvar = new Salvar(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSalvarConstructorWithInvalidTipoSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 40);
    }

    @Test
    public void testVelasetToTrueWhenSavingAFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        assertTrue(salvar.vela);
    }

    @Test
    public void testArquivoNameSetToSelectedFileName() {
        ProcessarEntidades form = new ProcessarEntidades();
        File selectedFile = new File("test.jaw");
        Salvar salvar = new Salvar(form, 20, selectedFile);
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testArquivoNameSetToFormName() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("test.jaw");
        Salvar salvar = new Salvar(form, 10);
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testArquivoNameSetToFormNameWhenSavingProject() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("test.jaw");
        Salvar salvar = new Salvar(form, 10);
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testXmlAttributeSetToCorrectValueWhenSavingXMLFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setEntidades(Collections.singletonList(new Entidade()));
        Salvar salvar = new Salvar(form, 10);
        String expectedXml = "<Entidades>";
        String actualXml = salvar.xml;
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testEntidadeListSetToListObtainedFromForm() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setEntidades(Arrays.asList(new Entidade(), new Entidade()));
        Salvar salvar = new Salvar(form, 20);
        assertEquals(2, salvar.entidade.size());
    }

    @Test
    public void testEntidadeListSetToEmptyList() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testLogErrorMessageWhenExceptionOccursObtainingEntidadesList() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setExceptions(new Exception("Test Exception"));
        Salvar salvar = new Salvar(form, 10);
        assertNotNull(salvar.xml);
    }
}