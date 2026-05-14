package br.unicamp.padroescriacionais.legacy;

import br.unicamp.padroescriacionais.legacy.domain.FormatoRelatorio;
import br.unicamp.padroescriacionais.legacy.domain.Relatorio;
import br.unicamp.padroescriacionais.legacy.domain.TipoRelatorio;
import br.unicamp.padroescriacionais.legacy.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioServiceTest {

    private RelatorioService service;

    @BeforeEach
    void setUp() {
        service = new RelatorioService();
    }

    // Testes originais

    @Test
    void deveCriarRelatorioDeVendas() {
        Relatorio relatorio = service.criarRelatorio(TipoRelatorio.VENDAS);

        assertNotNull(relatorio);
        assertEquals(TipoRelatorio.VENDAS, relatorio.getTipo());
        assertNotNull(relatorio.getTitulo());
        assertFalse(relatorio.getTitulo().isBlank());
        assertNotNull(relatorio.getConteudo());
        assertFalse(relatorio.getConteudo().isBlank());
        assertNotNull(relatorio.getDataGeracao());
    }

    @Test
    void deveCriarRelatorioDeEstoque() {
        Relatorio relatorio = service.criarRelatorio(TipoRelatorio.ESTOQUE);
        assertNotNull(relatorio);
        assertEquals(TipoRelatorio.ESTOQUE, relatorio.getTipo());
        assertFalse(relatorio.getTitulo().isBlank());
    }

    @Test
    void deveCriarRelatorioDeClientes() {
        Relatorio relatorio = service.criarRelatorio(TipoRelatorio.CLIENTES);
        assertNotNull(relatorio);
        assertEquals(TipoRelatorio.CLIENTES, relatorio.getTipo());
        assertFalse(relatorio.getTitulo().isBlank());
    }

    @Test
    void todosTiposDevemProduizirRelatorioValido() {
        for (TipoRelatorio tipo : TipoRelatorio.values()) {
            Relatorio relatorio = service.criarRelatorio(tipo);
            assertNotNull(relatorio, "Relatorio nulo para tipo: " + tipo);
            assertFalse(relatorio.getTitulo().isBlank(), "Titulo vazio para tipo: " + tipo);
            assertFalse(relatorio.getConteudo().isBlank(), "Conteudo vazio para tipo: " + tipo);
        }
    }

    @Test
    void deveGerarConteudoPdfNaoVazio() {
        String resultado = service.gerarRelatorio(TipoRelatorio.VENDAS, FormatoRelatorio.PDF);
        assertNotNull(resultado);
        assertFalse(resultado.isBlank());
        assertTrue(resultado.contains("PDF"), "Saida PDF deve conter a palavra PDF");
    }

    @Test
    void deveGerarConteudoCsvComVirgulas() {
        String resultado = service.gerarRelatorio(TipoRelatorio.ESTOQUE, FormatoRelatorio.CSV);
        assertNotNull(resultado);
        assertFalse(resultado.isBlank());
        assertTrue(resultado.contains(","), "Saida CSV deve conter virgulas");
    }

    @Test
    void deveGerarConteudoJsonComChaves() {
        String resultado = service.gerarRelatorio(TipoRelatorio.CLIENTES, FormatoRelatorio.JSON);
        assertNotNull(resultado);
        assertFalse(resultado.isBlank());
        assertTrue(resultado.contains("{"), "Saida JSON deve conter '{'");
        assertTrue(resultado.contains("}"), "Saida JSON deve conter '}'");
    }

    @Test
    void todosFormatosDevemProduizirConteudoValido() {
        for (FormatoRelatorio formato : FormatoRelatorio.values()) {
            String resultado = service.gerarRelatorio(TipoRelatorio.VENDAS, formato);
            assertNotNull(resultado, "Resultado nulo para formato: " + formato);
            assertFalse(resultado.isBlank(), "Resultado vazio para formato: " + formato);
        }
    }

    @Test
    void conteudoDeveConterTituloDoRelatorio() {
        String resultado = service.gerarRelatorio(TipoRelatorio.VENDAS, FormatoRelatorio.PDF);
        assertTrue(resultado.contains("Vendas"), "Saida PDF deve mencionar o titulo do relatorio");
    }

    // Novos testes: formatos XML e HTML 

    @Test
    void deveGerarConteudoXmlComTagsCorresponentes() {
        String resultado = service.gerarRelatorio(TipoRelatorio.VENDAS, FormatoRelatorio.XML);
        assertNotNull(resultado);
        assertFalse(resultado.isBlank());
        assertTrue(resultado.contains("<?xml"), "XML deve comecar com declaracao XML");
        assertTrue(resultado.contains("<relatorio>"), "XML deve conter tag <relatorio>");
        assertTrue(resultado.contains("<titulo>"), "XML deve conter tag <titulo>");
        assertTrue(resultado.contains("</relatorio>"), "XML deve fechar tag raiz");
    }

    @Test
    void deveGerarConteudoHtmlValido() {
        String resultado = service.gerarRelatorio(TipoRelatorio.ESTOQUE, FormatoRelatorio.HTML);
        assertNotNull(resultado);
        assertFalse(resultado.isBlank());
        assertTrue(resultado.contains("<!DOCTYPE html>"), "HTML deve ter DOCTYPE");
        assertTrue(resultado.contains("<html"), "HTML deve conter tag <html");
        assertTrue(resultado.contains("</html>"), "HTML deve fechar tag </html>");
        assertTrue(resultado.contains("<h1>"), "HTML deve ter cabecalho h1");
    }

    @Test
    void xmlDeveConterTipoDoRelatorio() {
        String resultado = service.gerarRelatorio(TipoRelatorio.CLIENTES, FormatoRelatorio.XML);
        assertTrue(resultado.contains("CLIENTES"), "XML deve conter o tipo do relatorio");
    }

    @Test
    void htmlDeveConterTituloDoRelatorio() {
        String resultado = service.gerarRelatorio(TipoRelatorio.VENDAS, FormatoRelatorio.HTML);
        assertTrue(resultado.contains("Vendas"), "HTML deve mencionar o titulo do relatorio");
    }

    // Novos testes: comportamento da fábrica

    @Test
    void servicoComFabricaPersonalizadaDeveGerarRelatorio() {
        // Verifica que o construtor com injecao de fabrica funciona corretamente
        RelatorioService serviceComFactory = new RelatorioService(
                formato -> switch (formato) {
                    case PDF  -> relatorio -> "[CUSTOM-PDF] " + relatorio.getTitulo();
                    default   -> relatorio -> "[CUSTOM] " + relatorio.getTitulo();
                }
        );

        String resultado = serviceComFactory.gerarRelatorio(TipoRelatorio.VENDAS, FormatoRelatorio.PDF);
        assertTrue(resultado.startsWith("[CUSTOM-PDF]"), "Fabrica personalizada deve ser usada");
    }
}
