package pgdp.colony;

import static pgdp.MiniJava.*;

public class PenguinColony {
	// Constants
	static final int ADULT_AGE = 1;
	static final int DAYS_PER_YEAR = 5;
	private int time = 0;
	private PenguinList population = new PenguinList();

	public int getTime(){
		return time;
	}

	public PenguinList getPopulation(){
		return population;
	}
	public Penguin createPenguin(Genome genome){
		Penguin penguin = new Penguin(genome,DAYS_PER_YEAR);
		population.list.add(penguin);
	return penguin;
	}

	//Teilaufgabe 3.2: Kolonie-Simulation
	public void simulateDay(){
		time++;
		for (int i = 0; i < population.list.size(); i++) {
			Penguin penguin = population.list.get(i);
			penguin.hunt(population.list.size());
		}
		for (int i = 0; i < population.list.size(); i++) {
			Penguin penguin = population.list.get(population.list.size()-1-i);
			penguin.eat();
		}
		for (int i = 0; i < population.list.size(); i++) {
			Penguin penguin = population.list.get(i);
			penguin.sleep();
			if (penguin.sleep()== false){
				population.list.remove(penguin);
			}
		}
		int similarity = 0;
		if (time%5 == 0){
			for (int i = 0; i < population.list.size(); i++) {
				if (population.list.get(i).genome.isMale() == false && population.list.get(i).canMate())
					for (int j = 0; j < population.list.size(); j++) {
						if (population.list.get(j).genome.isMale() && population.list.get(i).canMate()){
							int biggestSimilarity = similarity;
							similarity = population.list.get(i).checkSimilarity(population.list.get(j));
							if (similarity > biggestSimilarity ){
								biggestSimilarity = similarity;
								if (j == population.list.size()-1) {
									population.list.add(population.list.get(i).mateWith(population.list.get(j)));
								}
							}
						}
					}
			}
		}
	}
}
