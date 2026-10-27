package estatistica;

import hash.TabelaHash;
import utils.Rounder;

public class EstatisticaHashTable {
    private TabelaHash tabela;

    public EstatisticaHashTable(TabelaHash tabela){
        this.tabela = tabela;
    }

    @Override
    public String toString(){
        return "========== ESTATISTICAS DA HASH TABLE ==========\n"+
                "Capacidade inicial : "+tabela.getCapacidadeInicial()+"\n"+
                "Capacidade final : "+tabela.getCapacidade()+"\n"+
                "Elementos armazenados : "+tabela.getTamanho()+"\n"+
                "Factor de carga : "+ Rounder.formatar(tabela.getFactorCarga(),4)+"\n"+
                "Colisoes : "+tabela.getTotalColisoes()+"\n"+
                "Sondagens realizadas : "+tabela.getTotalSondagens()+"\n"+
                "Media sondagens/insercao : "+Rounder.formatar(tabela.getMediaSondagens(),2)+"\n"+
                "Maior sondagem : "+tabela.getMaiorSondagem()+"\n"+
                "Redimensionamentos : "+tabela.getNumRedimensionamentos()+"\n"+
                "Limpezas de REMOVIDO : "+tabela.getNumLimpezas()+"\n"+
                "Posicoes REMOVIDO actuais : "+tabela.getRemovidos()+"\n";
    }
}
