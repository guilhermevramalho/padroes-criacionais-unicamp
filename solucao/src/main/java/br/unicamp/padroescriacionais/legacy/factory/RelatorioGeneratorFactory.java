package br.unicamp.padroescriacionais.legacy.factory;

import br.unicamp.padroescriacionais.legacy.domain.FormatoRelatorio;
import br.unicamp.padroescriacionais.legacy.generator.RelatorioGenerator;

public interface RelatorioGeneratorFactory {

    public abstract RelatorioGenerator criarGenerator(FormatoRelatorio formato);
}
