package gerador.interpretacao;

import gerador.dominio.ConfiguracaoMusical;
import gerador.dominio.Partitura;
import gerador.dominio.Pausa;
import gerador.dominio.Voz;
import gerador.mapeamento.TabelaDeMapeamento;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterpretadorDeTexto {

    private static final Pattern PADRAO_ATRASO = Pattern.compile("^\\[(\\d+)]\\s*");

    private final TabelaDeMapeamento tabelaDeMapeamento;
    private final List<String> logDeErros;

    public InterpretadorDeTexto(TabelaDeMapeamento tabelaDeMapeamento) {
        this.tabelaDeMapeamento = tabelaDeMapeamento;
        this.logDeErros = new ArrayList<>();
    }

    public Partitura interpretar(String texto, ConfiguracaoMusical configuracao) {
        logDeErros.clear();
        Partitura partitura = new Partitura(configuracao);

        if (texto == null || texto.isBlank()) {
            logDeErros.add("Texto de entrada está vazio.");
            return partitura;
        }

        String[] linhas = texto.split("\n");
        int numeroVoz = 0;

        for (String linha : linhas) {
            if (linha.trim().isEmpty()) continue;

            Voz voz = processarLinha(linha, numeroVoz, configuracao);
            partitura.adicionarVoz(voz);
            numeroVoz++;
        }

        if (partitura.getTotalVozes() == 0) {
            logDeErros.add("Nenhuma voz válida encontrada no texto.");
        }

        return partitura;
    }

    private Voz processarLinha(String linha, int numeroVoz, ConfiguracaoMusical config) {
        int atraso = 0;
        String textoParaProcessar = linha;

        Matcher matcher = PADRAO_ATRASO.matcher(linha);
        if (matcher.find()) {
            atraso = Integer.parseInt(matcher.group(1));
            textoParaProcessar = linha.substring(matcher.end());
        }

        Voz voz = new Voz(numeroVoz, atraso, config.getVolume());
        ContextoDeVoz contexto = new ContextoDeVoz(voz, config.getBpm());
        contexto.setTextoCompleto(textoParaProcessar);

        for (int i = 0; i < atraso; i++) {
            voz.adicionarEvento(new Pausa());
        }

        for (int i = 0; i < textoParaProcessar.length(); i++) {
            contexto.setPosicaoAtual(i);
            char caractere = textoParaProcessar.charAt(i);

            tabelaDeMapeamento.interpretar(caractere, contexto);

            // Atualizar posição caso a regra tenha avançado (ex: "Mb" consome 2 chars)
            i = contexto.getPosicaoAtual();
        }

        return voz;
    }

    public List<String> getLogDeErros() {
        return new ArrayList<>(logDeErros);
    }
}
