package br.unicamp.padroescriacionais.legacy;

import br.unicamp.padroescriacionais.legacy.domain.ConfiguracaoSistema;
import br.unicamp.padroescriacionais.legacy.service.ConfiguracaoService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracaoSistemaTest {

    // Testes originais (adaptados para usar criarInstanciaParaTeste)

    @Test
    void deveCriarConfiguracaoComValoresInformados() {
        ConfiguracaoSistema config = ConfiguracaoSistema.criarInstanciaParaTeste(
                "Empresa Teste", "DEV", "/tmp/test", true);

        assertEquals("Empresa Teste", config.getNomeEmpresa());
        assertEquals("DEV", config.getAmbiente());
        assertEquals("/tmp/test", config.getDiretorioExportacao());
        assertTrue(config.isDebugAtivo());
    }

    @Test
    void devePermitirAlteracaoDeAmbiente() {
        ConfiguracaoSistema config = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "DEV", "/tmp", false);
        config.setAmbiente("PROD");
        assertEquals("PROD", config.getAmbiente());
    }

    @Test
    void devePermitirAlteracaoDeDebug() {
        ConfiguracaoSistema config = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "DEV", "/tmp", false);
        config.setDebugAtivo(true);
        assertTrue(config.isDebugAtivo());
    }

    @Test
    void devePermitirAlteracaoDeDiretorio() {
        ConfiguracaoSistema config = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "DEV", "/tmp", false);
        config.setDiretorioExportacao("/novo/diretorio");
        assertEquals("/novo/diretorio", config.getDiretorioExportacao());
    }

    @Test
    void duasInstanciasIndependentesPodemTerAmbientesDiferentes() {
        ConfiguracaoSistema configDev  = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "DEV",  "/tmp",     true);
        ConfiguracaoSistema configProd = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "PROD", "/exports", false);

        assertNotEquals(configDev.getAmbiente(), configProd.getAmbiente());
        assertNotEquals(configDev.getDiretorioExportacao(), configProd.getDiretorioExportacao());
        assertNotEquals(configDev.isDebugAtivo(), configProd.isDebugAtivo());
    }

    @Test
    void alteracaoEmUmaInstanciaNaoAfetaOutra() {
        ConfiguracaoSistema config1 = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "DEV", "/tmp", false);
        ConfiguracaoSistema config2 = ConfiguracaoSistema.criarInstanciaParaTeste("Empresa", "DEV", "/tmp", false);

        config1.setAmbiente("PROD");

        assertEquals("PROD", config1.getAmbiente());
        assertEquals("DEV",  config2.getAmbiente());
    }

    @Test
    void configuracaoServiceDeveRetornarConfiguracaoNaoNula() {
        ConfiguracaoService service = new ConfiguracaoService();
        assertNotNull(service.getConfiguracao());
        assertFalse(service.getConfiguracao().getNomeEmpresa().isBlank());
    }

    // Novos testes: Singleton

    @Test
    void singletonDeveRetornarSempreAMesmaInstancia() {
        ConfiguracaoSistema instancia1 = ConfiguracaoSistema.getInstance();
        ConfiguracaoSistema instancia2 = ConfiguracaoSistema.getInstance();
        assertSame(instancia1, instancia2, "getInstance() deve retornar sempre a mesma instancia");
    }

    @Test
    void singletonDeveCompartilharEstadoEntreAcessos() {
        ConfiguracaoSistema config = ConfiguracaoSistema.getInstance();
        String ambienteOriginal = config.getAmbiente();
        config.setAmbiente("TESTE_SINGLETON");

        assertEquals("TESTE_SINGLETON", ConfiguracaoSistema.getInstance().getAmbiente());

        // Restaurar para não atrapalhar outros testes
        config.setAmbiente(ambienteOriginal);
    }

    @Test
    void configuracaoServiceDeveRetornarSingletonInstance() {
        ConfiguracaoService service1 = new ConfiguracaoService();
        ConfiguracaoService service2 = new ConfiguracaoService();

        assertSame(service1.getConfiguracao(), service2.getConfiguracao(),
                "Dois ConfiguracaoService distintos devem apontar para o mesmo singleton");
    }
}
