package dados;

import hash.TabelaHash;
import model.Estudante;

public class Dados {

    private final TabelaHash tabela;
    private int invalidos;
    private int duplicados; // válidos, mas com código já existente
    private int inseridos;
    private int lidas;

    public Dados() {
        this.tabela = new TabelaHash();
        this.invalidos = 0;
        this.duplicados = 0;
        this.inseridos = 0;
        this.lidas = 0;
    }

    public void inserir(Estudante estudante) {
        this.tabela.inserir(estudante);
        this.inseridos++;
    }

    public void incrementarInvalidos() {
        this.invalidos++;
    }

    public TabelaHash getTabelaHash() {
        return this.tabela;
    }

    public int getInvalidos() {
        return this.invalidos;
    }

    public void incrementarDuplicados() {
        this.duplicados++;
    }

    public int getDuplicados() {
        return this.duplicados;
    }

    public void incrementarLidas() {
        this.lidas++;
    }

    public int getLidas() {
        return this.lidas;
    }

    // Válidos = passaram a validação; inclui os duplicados, que não foram inseridos.
    public int getValidos() {
        return this.inseridos + this.duplicados;
    }

    public int getInseridos() {
        return this.inseridos;
    }

}
