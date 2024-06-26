package pgdp.colony;

import static pgdp.MiniJava.*;

public class Penguin {
    //Constants
    static final int MAX_HEALTH = 50;
    private final String name;
    private final Genome genome;
    private int ageInDays;
    private int size;
    private int health;
    private int numFish;
    private Penguin child;

    //Teilaufgabe 2.1: Pinguin-Attribute
    String getName(){
         return this.name;
    }
    Genome getGenome(){
        return this.genome;
    }

    int getAge(){
        int ageInYears = ageInDays /5;
        return ageInYears;
    }

    int getSize(){
        return size;
    }

    int getHealth(){
        return health;
    }

    int getNumFish(){
        return numFish;
    }

    Penguin getChild(){
        return child;
    }

    //Teilaufgabe 2.2: Pinguin-Konstruktoren
    public Penguin(Penguin mother, Penguin father){
        name = generateName();
        genome = Genome.combine(mother.genome,father.genome);
        health =50;
        numFish =0;
        size = genome.maxSize()/2;
    }
    public Penguin(Genome genome, int ageInDays){
        this.genome = genome;
        this.ageInDays = ageInDays;
        name = generateName();
        health =50;
        numFish =0;
        size = genome.maxSize()/2;
    }

    //Teilaufgabe 2.3: Pinguin-Liebe
    public boolean canMate(){
        boolean result = false;
        if (child == null && health > 25 && ageInDays > 4){
            result = true;
        }
        return result;
    }
    public int checkSimilarity(Penguin partner){
         int result = Genome.similarity(this.genome,partner.genome);
         return result;
    }
    public Penguin mateWith(Penguin male){
        if (male.genome.isMale() == true && male.canMate()){
            this.child = new Penguin(Penguin.this,male);
            male.child = this.child;
            return child;
        }
    return null;}

    //Teilaufgabe 2.4: Pinguin-Alltag
    public int hunt(int competition){
        int x = randomInt(genome.skill()/2, genome.skill());
        int catchFish = (size/2) +(x/competition);
        numFish = numFish + catchFish;
        return numFish;
    }

    public void eat(){
         if (child != null){//Distribution of fish to the children
             child.numFish = (numFish/4);
             numFish = ((3*numFish)/4);
         }
         if (numFish<size){//Lose health situation
             int loseHealth = size-numFish;
             health = health-loseHealth;
             numFish = 0;
         }else{//Gain health situation
             if (health < MAX_HEALTH){
                 health = health + numFish-size;
                 if (health > MAX_HEALTH){
                     numFish = health-MAX_HEALTH;
                     health = MAX_HEALTH;
                 }
             }//Gain size situation
             if (size < getGenome().maxSize()){
                 size = size + numFish;
                 if (size > genome.maxSize()){
                     numFish = size - genome.maxSize();
                     size = genome.maxSize();
                 }else{
                     numFish = 0;
                 }
             }
         }
    }

    public boolean sleep(){
        boolean alive = true;
        ageInDays ++;
        if (child == null){

        }else{
            if (child.ageInDays > 4){//Child is no longer a child
                child = null;

            }
        }
        if (health <= 0 || ageInDays > genome.lifespan()){//Death
            health = 0;
            alive = false;
        }
        return alive;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name+" ");
        sb.append("("+getAge());
        if (genome.isMale()){
            sb.append("M): ");
        }else{
            sb.append("F): ");
        }
        sb.append("DNA="+genome.toString()+"; ");
        sb.append("Health="+health+"; ");
        sb.append("Size="+size+"/"+genome.maxSize()+"; ");
        sb.append("Lifespan="+genome.lifespan()+"; ");
        sb.append("Skill="+genome.skill()+"; ");
        sb.append("Num Fish="+numFish+"; ");
        sb.append("Color="+genome.color()+"; ");
        if (child == null){
            sb.append("Child=none;");
        }else{
            sb.append(child.name+" ");
            sb.append("("+child.getAge());
            if (child.genome.isMale()){
                sb.append("M): ");
            }else{
                sb.append("F): ");
            }
            sb.append("DNA="+child.genome.toString()+"; ");
            sb.append("Health="+child.health+"; ");
            sb.append("Size="+child.size+"/"+child.genome.maxSize()+"; ");
            sb.append("Lifespan="+child.genome.lifespan()+"; ");
            sb.append("Skill="+child.genome.skill()+"; ");
            sb.append("Color="+child.genome.color()+"; ");
            if (child == null){
                sb.append("Child=none;");
            }else{
                sb.append(child.name);
            }
        }
        return sb.toString();
    }

}
