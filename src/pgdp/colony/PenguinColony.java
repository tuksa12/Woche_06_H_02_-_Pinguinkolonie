package pgdp.colony;

import static pgdp.MiniJava.*;

public class PenguinColony {
	// Constants
	static final int ADULT_AGE = 1;
	static final int DAYS_PER_YEAR = 5;
	private int time = 0;
	private PenguinList population = new PenguinList();

	//Teilaufgabe 3.1: Kolonie-Attribute
	public int getTime() {
		return time;
	}

	public PenguinList getPopulation() {
		return population;
	}

	public Penguin createPenguin(Genome genome) {
		Penguin penguin = new Penguin(genome, DAYS_PER_YEAR);
		population.list.add(penguin);
		return penguin;
	}

	//Teilaufgabe 3.2: Kolonie-Simulation
	public void simulateDay() {
		time++;
		for (int i = 0; i < population.list.size(); i++) {//For loop so that every penguin hunt
			Penguin penguin = population.list.get(i);
			penguin.hunt(population.list.size());
		}
		for (int i = 0; i < population.list.size(); i++) {//For loop so that every penguin eat
			Penguin penguin = population.list.get(i);
			penguin.eat();
		}
		for (int i = 1; i <= population.list.size(); i++) {//For loop so that every penguin sleep
			Penguin penguin = population.list.get(population.list.size() - i);
			boolean alive = penguin.sleep();
			if (alive == false) {//Death scenario
				population.list.remove(penguin);
			}
		}
		//A few variables to help
		int similarity = 100;
		int smallestSimilarity = 100;
		int index = 0;
		int size = population.list.size();
		if (time % 5 == 0) {//Time for reproduce
			for (int i = 1; i <= size; i++) {
				if (population.list.get(size - i).getGenome().isMale() == false && population.list.get(size - i).canMate())//Female pick the male
					for (int j = 1; j <= size; j++) {
						if (population.list.get(size - j).getGenome().isMale() && population.list.get(size - j).canMate()) {
							similarity = population.list.get(size - i).checkSimilarity(population.list.get(size - j));
							if (similarity <= smallestSimilarity) {//Smallest similarity -> suitable mate
								smallestSimilarity = similarity;
								index = size - j;
							}
						}if (j == size && population.list.get(size - j).getChild() == null && population.list.get(index).getGenome().isMale()){
							population.list.add(population.list.get(size - i).mateWith(population.get(index)));//Creation of children
						}
					}
			}
		}
	}
}
