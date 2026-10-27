package estatistica;
import java.util.List;
import model.Estudante;
import utils.Rounder;

public class EstatisticaDasMedias extends Estatistica{
        private double mediaGeral;
        private Estudante maiorMedia;
        private Estudante menorMedia;
        private int[] quantidade;
        private int comMaiorMedia;
        private int comMenorMedia;

        public EstatisticaDasMedias(List<Estudante> estudantes) {
            super(estudantes);
            this.quantidade = new int[MediaIntervalo.values().length];
            calcular();
    }

    private void calcular(){
            double soma = 0;
            for (Estudante e : estudantes) {
                double media = e.getMedia();
                soma += media;

                if (menorMedia == null || media < menorMedia.getMedia()) {
                    menorMedia = e;
                }
                if (maiorMedia == null || media > maiorMedia.getMedia()) {
                    maiorMedia = e;
                }

                quantidade[MediaIntervalo.de(media).ordinal()]++;
            }
            mediaGeral = totalEstudantes == 0 ? 0 : soma / totalEstudantes;

            for (Estudante e : estudantes) {
                if (e.getMedia() == maiorMedia.getMedia()) comMaiorMedia++;
                if (e.getMedia() == menorMedia.getMedia()) comMenorMedia++;
            }
    }

    private String outros(int comMesmaMedia) {
        return comMesmaMedia > 1 ? " e mais "+(comMesmaMedia-1)+" com a mesma media" : "";
    }

    @Override
    public String toString() {
        if (totalEstudantes == 0) {
            return "============= Estatisticas das medias ==========\nSem estudantes.\n";
        }
        StringBuilder relatorio = new StringBuilder();
        for (MediaIntervalo i: MediaIntervalo.values()) {
            relatorio.append("Quantidade com media "+i.nome+": "+quantidade[i.ordinal()]+"\n");
        }
        return "============= Estatisticas das medias ==========\n"+
                "Media academica geral: "+Rounder.formatar(mediaGeral,2)+"\n"+
                "Maior media: "+maiorMedia.getMedia()+"\n"+
                "Menor media: "+menorMedia.getMedia()+"\n"+
                "Estudante com maior media: "+maiorMedia.getNome()+" ("+maiorMedia.getCodigo()+")"+
                        outros(comMaiorMedia)+"\n"+
                "Estudante com menor media: "+menorMedia.getNome()+" ("+menorMedia.getCodigo()+")"+
                        outros(comMenorMedia)+"\n"+
                "Medias por intervalo\n"+
                relatorio;

    }
}

enum MediaIntervalo{
    MENOR10("inferior a 10"),
    ENTRE10E14("entre 10 e 13,9"),
    ENTRE14E17("entre 14 e 16,9"),
    MAIOR17("igual ou superior a 17");
    String nome;
    MediaIntervalo(String nome){this.nome = nome;}

    static MediaIntervalo de(double media) {
        if (media < 10) return MENOR10;
        if (media < 14) return ENTRE10E14;
        if (media < 17) return ENTRE14E17;
        return MAIOR17;
    }
}
