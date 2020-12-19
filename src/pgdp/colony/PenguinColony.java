package pgdp.colony;

import static pgdp.MiniJava.*;

public class PenguinColony {
	// Constants
	static final int ADULT_AGE = 1;
	static final int DAYS_PER_YEAR = 5;
	private int time = 0;
	private PenguinList population = new PenguinList();

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
		for (int i = 0; i < population.list.size(); i++) {
			Penguin penguin = population.list.get(i);
			penguin.hunt(population.list.size());
		}
		for (int i = 0; i < population.list.size(); i++) {
			Penguin penguin = population.list.get(i);
			penguin.eat();
		}
		for (int i = 1; i <= population.list.size(); i++) {
			Penguin penguin = population.list.get(population.list.size() - i);
			penguin.sleep();
			if (penguin.sleep() == false) {
				population.list.remove(penguin);
			}
		}
		int similarity = 100;
		int biggestSimilarity = 100;
		int index = 0;
		int size = population.list.size();
		if (time % 5 == 0) {
			for (int i = 1; i <= population.list.size(); i++) {
				if (population.list.get(population.list.size() - i).getGenome().isMale() == false && population.list.get(population.list.size() - i).canMate())
					for (int j = 1; j <= population.list.size(); j++) {
						if (population.list.get(population.list.size() - j).getGenome().isMale() && population.list.get(population.list.size() - j).canMate()) {
							similarity = population.list.get(population.list.size() - i).checkSimilarity(population.list.get(population.list.size() - j));
							if (similarity < biggestSimilarity) {
								biggestSimilarity = similarity;
								index = population.list.size() - j;

							}
						}if (j == size){
							population.list.add(population.list.get(population.list.size() - i).mateWith(population.get(index)));
						}
						}

			}

		}


	}
}
