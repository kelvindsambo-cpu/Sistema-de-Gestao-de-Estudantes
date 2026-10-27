package estatistica;

import java.util.List;
import model.Estudante;
import utils.Rounder;

public class EstatisticaDeIdade extends Estatistica{
    private double idadeMedia;
    private int menorIdade;
    private int maiorIdade;
    private int[] idadeIntervalo;

    public EstatisticaDeIdade(List<Estudante> estudantes){
        super(estudantes);
        this.idadeIntervalo = new int[IdadeIntervalo.values().length];
        calcular();
    }

    private void calcular(){
        long soma = 0;
        menorIdade = Integer.MAX_VALUE;
        maiorIdade = Integer.MIN_VALUE;

        for (Estudante e : estudantes) {
            int idade = e.getIdade();
            soma += idade;
            if(idade < menorIdade){
                menorIdade = idade;
            }
            if(idade > maiorIdade){
                maiorIdade = idade;
            }
            idadeIntervalo[IdadeIntervalo.de(idade).ordinal()]++;
        }
        idadeMedia = totalEstudantes == 0 ? 0 : (double) soma / totalEstudantes;
    }

    @Override
    public String toString(){
        if (totalEstudantes == 0) {
            return "============= Estatistica de Idade =============\nSem estudantes.\n";
        }
        StringBuilder build = new StringBuilder();
        for(IdadeIntervalo l:IdadeIntervalo.values()){
            build.append("Quantidade no intervalo "+l.designacao+" : "+idadeIntervalo[l.ordinal()]+"\n");
        }
        return "============= Estatistica de Idade =============\n"+
                "Idade media: "+Rounder.formatar(idadeMedia,2)+"\n"+
                "Menor idade: "+menorIdade+"\n"+
                "Maior idade: "+maiorIdade+"\n"+
                "Quantidade por intervalo\n"+build;
    }
}

enum IdadeIntervalo{
    MENOR18("< 18"),
    ENTRE18E20("18-20"),
    ENTRE21E23("21-23"),
    ENTRE24E26("24-26"),
    MAIOR26("> 26");
    String designacao;

   IdadeIntervalo(String designacao){
        this.designacao = designacao;
    }

    static IdadeIntervalo de(int idade) {
        if (idade < 18) return MENOR18;
        if (idade <= 20) return ENTRE18E20;
        if (idade <= 23) return ENTRE21E23;
        if (idade <= 26) return ENTRE24E26;
        return MAIOR26;
    }

}
