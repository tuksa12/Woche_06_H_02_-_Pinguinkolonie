package pgdp.colony;

import static pgdp.MiniJava.*;

public class Genome {
    // Constants
    private static final int LENGTH = 13; //length of the String dna

    //Teilaufgabe 1.1: DNA
    private final String dna;

    public Genome(String dna){
        this.dna = dna;
    }
    public String toString(){
       return(dna);
    }
    public static int similarity(Genome a, Genome b){
        int result = 0;
        for (int i = 0; i < LENGTH; i++) {
            if (a.dna.charAt(i) == b.dna.charAt(i)){
                result ++;
            }
        }
    return result;}

    //Teilaufgabe 1.2: Gene
    public Genome(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            int letterNumber = randomInt(0,3);
            if (letterNumber == 0){
                sb.append('A');
            }else if (letterNumber == 1){
                sb.append('C');
            }else if (letterNumber == 2){
                sb.append('G');
            }else if (letterNumber == 3){
                sb.append('T');
            }
        }
        this.dna = sb.toString();
    }
    public static Genome combine(Genome mother, Genome father){
        String result;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            if (randomBoolean(1000)){
                int random = randomInt(0,3);
                if (random == 0){
                    sb.append('A');
                }else if (random == 1){
                    sb.append('C');
                }else if (random == 2){
                    sb.append('G');
                }else if (random == 3){
                    sb.append('T');
                }
            }else if (randomBoolean()){
                sb.append(mother.dna.charAt(i));
            }else{
                sb.append(father.dna.charAt(i));
            }
        }result = sb.toString();
        return new Genome(result);
    }
    public String getGene(int genePos, int geneLength){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < geneLength; i++) {//mudei de i = genePos para 0
            sb.append(dna.charAt(genePos+i));}//mudei de i para genePos+i
        return sb.toString();
    }
    public static int interpretGene(String gene){
        int result = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(gene);
        int []letter = new int[gene.length()];
        for (int i = 0; i < gene.length(); i++) {
            if (sb.charAt(i) == 'A'){
                letter[i] = 0;
            }else if (sb.charAt(i) == 'C'){
                letter[i] = 1;
            }else if (sb.charAt(i) == 'G'){
                letter[i] = 2;
            }else if (sb.charAt(i) == 'T'){
                letter[i] = 3;
            }
        }
        int j = gene.length()-1;
        for (int i = 0; i < gene.length(); i++) {
            result = result + (letter[i] * ((int) Math.pow(4,j)));
            j--;
        }
        return result;
    }
    //Teilaufgabe 1.3: Gen-Interpretation
    public int lifespan(){
        int sum = 0;
        int result;
        int []list = new int[LENGTH];
        for (int i = 0; i < 2; i++) {
            if (dna.charAt(i) == 'A'){
                list[i] = 0;
            }else if (dna.charAt(i) == 'C'){
                list[i] = 1;
            }else if (dna.charAt(i) == 'G'){
                list[i] = 2;
            }else if (dna.charAt(i) == 'T'){
                list[i] = 3;
            }
        }
        int j = 1;
        for (int i = 0; i < 2; i++) {
            sum = sum + ((list[i]) * ((int) Math.pow(4,j)));
            j--;
        }
        result = 8 + sum;
        return result;
    }

    public int maxSize(){
        int sum = 0;
        int result;
        int []list = new int[LENGTH];
        for (int i = 0; i < 4; i++) {
            if (dna.charAt(i) == 'A'){
                list[i] = 0;
            }else if (dna.charAt(i) == 'C'){
                list[i] = 1;
            }else if (dna.charAt(i) == 'G'){
                list[i] = 2;
            }else if (dna.charAt(i) == 'T'){
                list[i] = 3;
            }
        }
        int j = 1;
        for (int i = 2; i < 4; i++) {
            sum = sum + ((list[i]) * ((int) Math.pow(4,j)));
            j--;
        }
        result = 16 + sum;
        return result;
    }

    public int skill(){
        int sum = 0;
        int result;
        int []list = new int[LENGTH];
        for (int i = 0; i < 9; i++) {
            if (dna.charAt(i) == 'A'){
                list[i] = 0;
            }else if (dna.charAt(i) == 'C'){
                list[i] = 1;
            }else if (dna.charAt(i) == 'G'){
                list[i] = 2;
            }else if (dna.charAt(i) == 'T'){
                list[i] = 3;
            }
        }
        int j = 4;
        for (int i = 4; i < 9; i++) {
            sum = sum + ((list[i]) * ((int) Math.pow(4,j)));
            j--;
        }
        result = sum;
        return result;
    }

    public byte color(){
        int sum = 0;
        byte result;
        int []list = new int[LENGTH];
        for (int i = 0; i < 12; i++) {
            if (dna.charAt(i) == 'A'){
                list[i] = 0;
            }else if (dna.charAt(i) == 'C'){
                list[i] = 1;
            }else if (dna.charAt(i) == 'G'){
                list[i] = 2;
            }else if (dna.charAt(i) == 'T'){
                list[i] = 3;
            }
        }
        int j = 2;
        for (int i = 9; i < 12; i++) {
            sum = sum + ((list[i]) * ((int) Math.pow(4,j)));
            j--;
        }
        result = (byte)sum;
        return result;
    }

    public boolean isMale(){
        boolean result;
        int []list = new int[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            if (dna.charAt(i) == 'A'){
                list[i] = 0;
            }else if (dna.charAt(i) == 'C'){
                list[i] = 1;
            }else if (dna.charAt(i) == 'G'){
                list[i] = 2;
            }else if (dna.charAt(i) == 'T'){
                list[i] = 3;
            }
        }
        if (list[12]==0 || list[12]==2){
            result = true;
        }else{
            result = false;}

        return result;
    }
}
