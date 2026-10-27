package estatistica;

import java.util.List;
import model.Estudante;
import utils.Rounder;

public class EstatisticaPorSexo extends Estatistica{
    private int totalMasculino;
    private int totalFeminino;

    public EstatisticaPorSexo(List<Estudante> estudantes){
        super(estudantes);
        contarPorSexo();
    }

    private void contarPorSexo() {
        for (Estudante e : estudantes) {
            if (e.getSexo().equalsIgnoreCase("M")) {
                totalMasculino++;
            } else if (e.getSexo().equalsIgnoreCase("F")) {
                totalFeminino++;
            }
        }
    }

    @Override
    public String toString(){
        return "============== Estatistica Por Sexo ===========\n"+
                "Quantidade masculina: "+totalMasculino+"\n"+
                "Quantidade feminina: "+totalFeminino+"\n"+
                "Percentagem masculina: "+Rounder.formatar(percentagem(totalMasculino),2)+"%\n"+
                "Percentagem feminina: "+Rounder.formatar(percentagem(totalFeminino),2)+"%\n";

    }
}
