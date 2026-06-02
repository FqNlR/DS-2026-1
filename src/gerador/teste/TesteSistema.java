package gerador.teste;

import gerador.dominio.*;
import gerador.interpretacao.ContextoDeVoz;
import gerador.interpretacao.InterpretadorDeTexto;
import gerador.mapeamento.TabelaDeMapeamento;

import java.util.List;

/**
 * Testes unitários manuais para o sistema.
 * Verifica o funcionamento correto dos TADs, do mapeamento
 * e do interpretador de texto.
 *
 * Execução: java -cp out gerador.teste.TesteSistema
 */
public class TesteSistema {

    private static int totalTestes = 0;
    private static int testesPassaram = 0;
    private static int testesFalharam = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TESTES DO GERADOR DE MÚSICA");
        System.out.println("========================================\n");

        testarTADs();
        testarMapeamentoNotas();
        testarMapeamentoPausa();
        testarMapeamentoEspaco();
        testarMapeamentoOitava();
        testarMapeamentoInstrumentos();
        testarMapeamentoBpm();
        testarMapeamentoRepetirOuPausa();
        testarMapeamentoMiBemol();
        testarInterpretadorMultiVoz();
        testarInterpretadorAtraso();
        testarInterpretadorTextoVazio();
        testarConfiguracaoMusical();
        testarVozPadroes();

        System.out.println("\n========================================");
        System.out.printf("  RESULTADO: %d/%d testes passaram%n",
                testesPassaram, totalTestes);
        if (testesFalharam > 0) {
            System.out.printf("  ⚠ %d teste(s) falharam!%n", testesFalharam);
        } else {
            System.out.println("  ✔ Todos os testes passaram!");
        }
        System.out.println("========================================");
    }

    // ========================================================================
    // TESTES DOS TADs
    // ========================================================================

    private static void testarTADs() {
        System.out.println("--- TADs (Nota, Pausa, Voz, Partitura) ---");

        // Nota
        Nota nota = new Nota(60, 100, 0);
        verificar("Nota: pitch correto", nota.getPitchMidi() == 60);
        verificar("Nota: velocity correta", nota.getVelocity() == 100);
        verificar("Nota: instrumento correto", nota.getInstrumento() == 0);
        verificar("Nota: produz som", nota.produzSom());
        verificar("Nota: duração padrão", nota.getDuracaoEmTicks() == 480);

        // Nota com valores fora dos limites
        Nota notaLimite = new Nota(200, -10, 300);
        verificar("Nota: pitch limitado a 127", notaLimite.getPitchMidi() == 127);
        verificar("Nota: velocity limitada a 0", notaLimite.getVelocity() == 0);
        verificar("Nota: instrumento limitado a 127", notaLimite.getInstrumento() == 127);

        // Pausa
        Pausa pausa = new Pausa();
        verificar("Pausa: não produz som", !pausa.produzSom());
        verificar("Pausa: duração padrão", pausa.getDuracaoEmTicks() == 480);

        // Voz (Volume base de 100)
        Voz voz = new Voz(0, 0, 100);
        voz.adicionarEvento(nota);
        voz.adicionarEvento(pausa);
        verificar("Voz: 2 eventos adicionados", voz.getTotalEventos() == 2);
        verificar("Voz: lista imutável",
                voz.getEventos().getClass().getName().contains("Unmodifiable"));

        // Partitura
        ConfiguracaoMusical config = new ConfiguracaoMusical();
        Partitura partitura = new Partitura(config);
        partitura.adicionarVoz(voz);
        verificar("Partitura: 1 voz", partitura.getTotalVozes() == 1);
        verificar("Partitura: 2 eventos totais", partitura.getTotalEventos() == 2);

        System.out.println();
    }

    // ========================================================================
    // TESTES DO MAPEAMENTO
    // ========================================================================

    private static void testarMapeamentoNotas() {
        System.out.println("--- Mapeamento: Notas ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100);
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("CDEFGABH");

        String[] notas = {"C", "D", "E", "F", "G", "A", "B", "H"};
        for (int i = 0; i < notas.length; i++) {
            ctx.setPosicaoAtual(i);
            tabela.interpretar(notas[i].charAt(0), ctx);
        }

        verificar("Notas: 8 eventos gerados", voz.getTotalEventos() == 8);
        verificar("Notas: todos são Nota",
                voz.getEventos().stream().allMatch(e -> e instanceof Nota));

        System.out.println();
    }

    private static void testarMapeamentoPausa() {
        System.out.println("--- Mapeamento: Pausas (a-h minúsculas) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100);
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("abcdefgh");

        for (int i = 0; i < 8; i++) {
            ctx.setPosicaoAtual(i);
            tabela.interpretar("abcdefgh".charAt(i), ctx);
        }

        verificar("Pausas: 8 eventos gerados", voz.getTotalEventos() == 8);
        verificar("Pausas: todos são Pausa",
                voz.getEventos().stream().allMatch(e -> e instanceof Pausa));

        System.out.println();
    }

    private static void testarMapeamentoEspaco() {
        System.out.println("--- Mapeamento: Espaço (dobra volume) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100); // Volume inicial: 100
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto(" ");

        verificar("Espaço: volume inicial = 100", ctx.getVolumeAtual() == 100);
        ctx.setPosicaoAtual(0);
        tabela.interpretar(' ', ctx);
        verificar("Espaço: volume dobrado (127 max)", ctx.getVolumeAtual() == 127);

        System.out.println();
    }

    private static void testarMapeamentoOitava() {
        System.out.println("--- Mapeamento: Oitava (? e V) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100); // Oitava inicial: 6
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("?V");

        verificar("Oitava: inicial = 6", ctx.getOitavaAtual() == 6);

        ctx.setPosicaoAtual(0);
        tabela.interpretar('?', ctx);
        verificar("Oitava: após ? = 7", ctx.getOitavaAtual() == 7);

        ctx.setPosicaoAtual(1);
        tabela.interpretar('V', ctx);
        verificar("Oitava: após V = 6", ctx.getOitavaAtual() == 6);

        System.out.println();
    }

    private static void testarMapeamentoInstrumentos() {
        System.out.println("--- Mapeamento: Instrumentos (!, ;, ,, dígitos) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100); // Instrumento inicial: Cravo (6)
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("!;,24");

        ctx.setPosicaoAtual(0);
        tabela.interpretar('!', ctx);
        verificar("Instrumento: ! -> Harmonica (22)", ctx.getInstrumentoAtual() == 22);

        ctx.setPosicaoAtual(1);
        tabela.interpretar(';', ctx);
        verificar("Instrumento: ; -> Tubular Bells (15)", ctx.getInstrumentoAtual() == 15);

        ctx.setPosicaoAtual(2);
        tabela.interpretar(',', ctx);
        verificar("Instrumento: , -> Church Organ (20)", ctx.getInstrumentoAtual() == 20);

        // Dígito par: instrumento + valor
        ctx.setPosicaoAtual(3);
        tabela.interpretar('2', ctx);
        verificar("Instrumento: dígito par 2 -> 20+2=22", ctx.getInstrumentoAtual() == 22);

        // Dígito ímpar: Tubular Bells
        ctx.setPosicaoAtual(4);
        tabela.interpretar('3', ctx);
        verificar("Instrumento: dígito ímpar -> Tubular Bells (15)",
                ctx.getInstrumentoAtual() == 15);

        System.out.println();
    }

    private static void testarMapeamentoBpm() {
        System.out.println("--- Mapeamento: BPM (> e <) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100);
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("><");

        ctx.setPosicaoAtual(0);
        tabela.interpretar('>', ctx);
        verificar("BPM: > -> 130", ctx.getBpmAtual() == 130);

        ctx.setPosicaoAtual(1);
        tabela.interpretar('<', ctx);
        verificar("BPM: < -> 120", ctx.getBpmAtual() == 120);

        System.out.println();
    }

    private static void testarMapeamentoRepetirOuPausa() {
        System.out.println("--- Mapeamento: Repetir ou Pausa (vogais/else) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100);
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("CI");

        // Primeiro: uma nota C
        ctx.setPosicaoAtual(0);
        tabela.interpretar('C', ctx);
        verificar("Repetir: após C, último foi nota", ctx.ultimoFoiNota());

        // Depois: I (vogal) deve repetir a última nota
        ctx.setPosicaoAtual(1);
        tabela.interpretar('I', ctx);
        verificar("Repetir: I após nota -> 2 notas", voz.getTotalEventos() == 2);
        verificar("Repetir: ambas são Nota",
                voz.getEventos().stream().allMatch(e -> e instanceof Nota));

        // Testar pausa quando último NÃO foi nota
        Voz voz2 = new Voz(0, 0, 100);
        ContextoDeVoz ctx2 = new ContextoDeVoz(voz2, 120);
        ctx2.setTextoCompleto("aI");

        ctx2.setPosicaoAtual(0);
        tabela.interpretar('a', ctx2); // pausa
        ctx2.setPosicaoAtual(1);
        tabela.interpretar('I', ctx2); // deve ser pausa
        verificar("Repetir: I após pausa -> 2 pausas", voz2.getTotalEventos() == 2);
        verificar("Repetir: ambas são Pausa",
                voz2.getEventos().stream().allMatch(e -> e instanceof Pausa));

        System.out.println();
    }

    private static void testarMapeamentoMiBemol() {
        System.out.println("--- Mapeamento: Mi bemol (Mb) ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        Voz voz = new Voz(0, 0, 100);
        ContextoDeVoz ctx = new ContextoDeVoz(voz, 120);
        ctx.setTextoCompleto("Mb");

        ctx.setPosicaoAtual(0);
        tabela.interpretar('M', ctx);
        verificar("Mb: gerou 1 nota (Mi bemol)", voz.getTotalEventos() == 1);
        verificar("Mb: posição avançou para 1", ctx.getPosicaoAtual() == 1);

        System.out.println();
    }

    // ========================================================================
    // TESTES DO INTERPRETADOR
    // ========================================================================

    private static void testarInterpretadorMultiVoz() {
        System.out.println("--- Interpretador: Multi-voz ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        InterpretadorDeTexto interpretador = new InterpretadorDeTexto(tabela);
        ConfiguracaoMusical config = new ConfiguracaoMusical();

        String texto = "C D E F\nG A B C";
        Partitura partitura = interpretador.interpretar(texto, config);

        verificar("Multi-voz: 2 vozes criadas", partitura.getTotalVozes() == 2);
        verificar("Multi-voz: voz 0 tem eventos",
                partitura.getVozes().get(0).getTotalEventos() > 0);
        verificar("Multi-voz: voz 1 tem eventos",
                partitura.getVozes().get(1).getTotalEventos() > 0);

        System.out.println();
    }

    private static void testarInterpretadorAtraso() {
        System.out.println("--- Interpretador: Atraso [n] ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        InterpretadorDeTexto interpretador = new InterpretadorDeTexto(tabela);
        ConfiguracaoMusical config = new ConfiguracaoMusical();

        String texto = "[0] C D\n[4] G A";
        Partitura partitura = interpretador.interpretar(texto, config);

        verificar("Atraso: 2 vozes", partitura.getTotalVozes() == 2);

        Voz voz0 = partitura.getVozes().get(0);
        Voz voz1 = partitura.getVozes().get(1);

        verificar("Atraso: voz 0 atraso = 0", voz0.getAtrasoEmBeats() == 0);
        verificar("Atraso: voz 1 atraso = 4", voz1.getAtrasoEmBeats() == 4);

        // Voz 1 deve ter 4 pausas de atraso + eventos do texto
        verificar("Atraso: voz 1 tem pausas de atraso",
                voz1.getTotalEventos() > voz0.getTotalEventos());

        System.out.println();
    }

    private static void testarInterpretadorTextoVazio() {
        System.out.println("--- Interpretador: Texto vazio ---");

        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        InterpretadorDeTexto interpretador = new InterpretadorDeTexto(tabela);
        ConfiguracaoMusical config = new ConfiguracaoMusical();

        Partitura partitura = interpretador.interpretar("", config);
        verificar("Vazio: 0 vozes", partitura.getTotalVozes() == 0);

        List<String> erros = interpretador.getLogDeErros();
        verificar("Vazio: log contém aviso", !erros.isEmpty());

        System.out.println();
    }

    private static void testarConfiguracaoMusical() {
        System.out.println("--- ConfiguracaoMusical ---");

        ConfiguracaoMusical config = new ConfiguracaoMusical(120, 100, 6, 0);
        verificar("Config: BPM = 120", config.getBpm() == 120);
        verificar("Config: Volume = 100", config.getVolume() == 100);

        // Testar limites
        ConfiguracaoMusical configLimite = new ConfiguracaoMusical(999, -1, 20, 200);
        verificar("Config: BPM limitado a 300", configLimite.getBpm() == 300);
        verificar("Config: Volume limitado a 0", configLimite.getVolume() == 0);
        verificar("Config: Oitava limitada a 9", configLimite.getOitava() == 9);
        verificar("Config: Instrumento limitado a 127", configLimite.getInstrumento() == 127);

        // Testar cópia
        ConfiguracaoMusical copia = config.copiar();
        verificar("Config: cópia independente", copia != config);
        verificar("Config: cópia tem mesmos valores", copia.getBpm() == config.getBpm());

        System.out.println();
    }

    private static void testarVozPadroes() {
        System.out.println("--- Voz: Padrões por número ---");

        Voz voz0 = new Voz(0, 0, 100);
        verificar("Voz 0: oitava 6", voz0.getOitavaInicial() == 6);
        verificar("Voz 0: volume 100", voz0.getVolumeInicial() == 100);
        verificar("Voz 0: instrumento Cravo (6)", voz0.getInstrumentoInicial() == 6);

        Voz voz1 = new Voz(1, 0, 100);
        verificar("Voz 1: oitava 5", voz1.getOitavaInicial() == 5);
        verificar("Voz 1: volume 80", voz1.getVolumeInicial() == 80);
        verificar("Voz 1: instrumento Church Organ (20)", voz1.getInstrumentoInicial() == 20);

        Voz voz2 = new Voz(2, 0, 100);
        verificar("Voz 2: oitava 4", voz2.getOitavaInicial() == 4);
        verificar("Voz 2: volume 60", voz2.getVolumeInicial() == 60);
        verificar("Voz 2: instrumento Piano (0)", voz2.getInstrumentoInicial() == 0);

        Voz voz3 = new Voz(3, 0, 100);
        verificar("Voz 3: oitava 3", voz3.getOitavaInicial() == 3);
        verificar("Voz 3: volume 40", voz3.getVolumeInicial() == 40);
        verificar("Voz 3: instrumento Fagote (70)", voz3.getInstrumentoInicial() == 70);

        // Ciclo: voz 4 = voz 0
        Voz voz4 = new Voz(4, 0, 100);
        verificar("Voz 4: cicla para oitava 6", voz4.getOitavaInicial() == 6);

        System.out.println();
    }

    // ========================================================================
    // INFRAESTRUTURA DE TESTES
    // ========================================================================

    private static void verificar(String descricao, boolean condicao) {
        totalTestes++;
        if (condicao) {
            testesPassaram++;
            System.out.println("  ✔ " + descricao);
        } else {
            testesFalharam++;
            System.out.println("  ✖ FALHOU: " + descricao);
        }
    }
}
