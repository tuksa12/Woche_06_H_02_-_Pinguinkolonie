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
    }
    public static Genome combine(Genome mother, Genome father){
        String result;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            if (randomBoolean(1000)){
                sb.append(randomInt(0,3));
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
        for (int i = genePos; i < geneLength; i++) {
            sb.append(dna.charAt(genePos));
        }
        return sb.toString();
    }
    public static int interpretGene(String gene){
        int result = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            if (gene.charAt(i) == 'A'){
                sb.append(0);
            }else if (gene.charAt(i) == 'C'){
                sb.append(1);
            }else if (gene.charAt(i) == 'G'){
                sb.append(2);
            }else if (gene.charAt(i) == 'T'){
                sb.append(3);
            }
        }
        for (int i = 0; i < LENGTH; i++) {
            result = result + (sb.codePointAt(i) * ((int) Math.pow(4,LENGTH-1-i)));
        }
        return result;
    }
    //Teilaufgabe 1.3: Gen-Interpretation
    public int lifespan(){
        int sum = 0;
        int result;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            if (dna.charAt(i) == 'A'){
                sb.append(0);
            }else if (dna.charAt(i) == 'C'){
                sb.append(1);
            }else if (dna.charAt(i) == 'G'){
                sb.append(2);
            }else if (dna.charAt(i) == 'T'){
                sb.append(3);
            }
        }
        for (int i = 0; i < 2; i++) {
            sum = sum + (sb.codePointAt(i) * ((int) Math.pow(4,LENGTH-1-i)));
        }
        result = 8 + sum;
        return result;
    }

    public int maxSize(){
        int sum = 0;
        int result;
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < 4; i++) {
            if (dna.charAt(i) == 'A'){
                sb.append(0);
            }else if (dna.charAt(i) == 'C'){
                sb.append(1);
            }else if (dna.charAt(i) == 'G'){
                sb.append(2);
            }else if (dna.charAt(i) == 'T'){
                sb.append(3);
            }
        }
        for (int i = 2; i < 4; i++) {
            sum = sum + (sb.codePointAt(i) * ((int) Math.pow(4,LENGTH-1-i)));
        }
        result = 16 + sum;
        return result;
    }

    public int skill(){
        int sum = 0;
        int result;
        StringBuilder sb = new StringBuilder();
        for (int i = 4; i < 9; i++) {
            if (dna.charAt(i) == 'A'){
                sb.append(0);
            }else if (dna.charAt(i) == 'C'){
                sb.append(1);
            }else if (dna.charAt(i) == 'G'){
                sb.append(2);
            }else if (dna.charAt(i) == 'T'){
                sb.append(3);
            }
        }
        for (int i = 4; i < 9; i++) {
            sum = sum + (sb.codePointAt(i) * ((int) Math.pow(4,LENGTH-1-i)));
        }
        result = sum;
        return result;
    }

    public byte color(){
        int sum = 0;
        byte result;
        StringBuilder sb = new StringBuilder();
        for (int i = 9; i < 12; i++) {
            if (dna.charAt(i) == 'A'){
                sb.append(0);
            }else if (dna.charAt(i) == 'C'){
                sb.append(1);
            }else if (dna.charAt(i) == 'G'){
                sb.append(2);
            }else if (dna.charAt(i) == 'T'){
                sb.append(3);
            }
        }
        for (int i = 9; i < 12; i++) {
            sum = sum + (sb.codePointAt(i) * ((int) Math.pow(4,LENGTH-1-i)));
        }
        result = (byte)sum;
        return result;
    }

    public boolean isMale(){
        boolean result;
        StringBuilder sb = new StringBuilder();
        for (int i = 12; i < 13; i++) {
            if (dna.charAt(i) == 'A'){
                sb.append(0);
            }else if (dna.charAt(i) == 'C'){
                sb.append(1);
            }else if (dna.charAt(i) == 'G'){
                sb.append(2);
            }else if (dna.charAt(i) == 'T'){
                sb.append(3);
            }
        }
        if (sb.codePointAt(0)==0 || sb.codePointAt(0)==2){
            result = true;
        }else{
            result = false;}

        return result;
    }
}
