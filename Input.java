import nz.ac.vuw.kol.OptimisationFunction;

public class Input {
	private double[] values = new double[5];
	private double result;
	private double mutationRate;
	private int age = 0;
	private int generation;

	public void incrementAge() {age++;}
	
	private void calcFitness() {
		result = OptimisationFunction.unknownFunction(values);
	}
	
	private void setMutationRate() {
		mutationRate = Main.mutationRate; 
	}

	private void setMutationRate(double parentRate) {
		if (Main.mutationGene == false) mutationRate = Main.mutationRate;
		else mutationRate = parentRate + ( (Math.random() * parentRate ) - (parentRate / 2) ); //Range of new rate is 0.5-1.5 the parent rate.
	}

	Input (double[] values) { //Creates Input with fixed values.
		setMutationRate();
		for (int index = 0; index < 5; index++) {
			this.values[index] = values[index];
		}
		calcFitness();
		generation = 0;
	}
	
	Input() { //Creates random Input
		setMutationRate();
		for (int index = 0; index < 5; index++) {
			this.values[index] = (Math.random() * 255) - 128;
		}
		calcFitness();
		generation = 0;
	}
	
	Input(Input parent) { //Creates Input from one parent
		setMutationRate(parent.mutationRate);
		for (int index = 0; index < 5; index++) this.values[index] = parent.values[index];
		
		int index = (int)(Math.random() * 5); //Mutate one value only.
		this.values[index] = parent.values[index] + ( (Math.random() * mutationRate) - (mutationRate / 2) );
		calcFitness();
		generation = parent.generation + 1;
	}
	
	Input(Input parent1, Input parent2) { //Create Input from two parents
		double parentRate; //Select one of the parent's mutation rates to work with.
		if (Math.random() < 0.5) parentRate = parent1.mutationRate;
		else parentRate = parent2.mutationRate;
		setMutationRate(parentRate);
		
		for (int index = 0; index < 5; index++) { //Copy each value from either parent with equal probability.
			if (Math.random() < 0.5) this.values[index] = parent1.values[index];
			else this.values[index] = parent2.values[index];
		}
		
		int index = (int)(Math.random() * 5); //Mutate one value at random.
		this.values[index] += ( (Math.random() * mutationRate) - (mutationRate / 2) );
		calcFitness();

		if (parent1.generation > parent2.generation) generation = parent1.generation + 1; //Inherit the generation number of the oldest parent 
		else generation = parent2.generation + 1;
	}

	double getAge() {return age;}

	double[] getValues() {return values;}
	double getResult() {return result;}
	double getMutationRate() {return mutationRate;}
	double getGeneration() {return generation;}
}
