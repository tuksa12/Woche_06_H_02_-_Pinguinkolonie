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
        char []dnalist = new char[LENGTH];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            int letterNumber = randomInt(0,3);
            if (letterNumber == 0){
                dnalist[i] = 'A';
            }else if (letterNumber == 1){
                dnalist[i] = 'C';
            }else if (letterNumber == 2){
                dnalist[i] = 'G';
            }else if (letterNumber == 3){
                dnalist[i] = 'T';
            }
            sb.append(dnalist[i]);
        }
        dna = sb.toString();
        return sb.toString();
    }
    public static Genome combine(Genome mother, Genome father){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            if (randomBoolean(1000)){
                sb.append(randomInt(0,3));
            }else if (randomBoolean()){
                sb.append(mother.dna.charAt(i));
            }else{
                sb.append(father.dna.charAt(i));
            }
        }Genome ji = 's';
    return this.dna.(sb.toString());}
}
