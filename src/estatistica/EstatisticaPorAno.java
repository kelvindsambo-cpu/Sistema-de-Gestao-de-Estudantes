package estatistica;
import java.util.List;
import model.Estudante;
import utils.Validacao;
public class EstatisticaPorAno extends Estatistica{
    private int []ano;
    public EstatisticaPorAno(List<Estudante> estudantes){
        super(estudantes);
        this.ano = contarAno();
    }
    private int[] contarAno(){
        int [] year = new int[Validacao.ANO_MAX];
        for (Estudante e : estudantes) {
            int index = e.getAno() - 1;
            if(index >= 0 && index < year.length) {
                year[index]++;
            }
        }
        return year;
    }
    @Override
    public String toString(){
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("============= Estatistica Por Ano ==============\n");
        for(int i = 0; i < ano.length;i++){
            relatorio.append("Estudantes do "+(i+1)+".o ano: "+ano[i]+"\n");
        }
        return relatorio.toString();
    }
}
