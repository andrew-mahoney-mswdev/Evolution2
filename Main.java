import java.util.Arrays;

public class Main {

	static int numParents = 100;
	static int numChildren = 20;
	static int generations = 50;
	static int maxAge = 0;
	static double mutationRate = 0;
	static boolean mutationGene = false;
	static boolean sex = true;
	static double pSelect = 1;
	
	//Do not change.
	static int population = numParents + (numParents * numChildren);
	
	private static void bubbleSort(Input[] inputs) {
		boolean swap;
		Input temp;
	
		do {
			swap = false;
			for (int index = 0; index < population - 1; index++) {
				if (inputs[index].getResult() > inputs[index+1].getResult()) { //Sorts lowest result first
					temp = inputs[index];
					inputs[index] = inputs[index+1];
					inputs[index+1] = temp;
					swap = true;
				}
			}
		} while (swap == true);
	}

	private static Input[] mergeArrays(Input[] parents, Input[] children) { //Merges parent and child arrays into a single array.
		Input inputPop[] = new Input[parents.length + children.length];
		
		int index = 0;
		for (int parentIndex = 0; parentIndex < numParents; parentIndex++) //For every parent
			if (parents[parentIndex].getAge() < maxAge) { //If it is young enough to stay alive
				inputPop[index] = parents[parentIndex]; //Copy to main array
				index++; //Increment index of main array
			}

		for (int childIndex = 0; childIndex < children.length && children[childIndex] != null; childIndex++, index++) //All children get copied as they are all new.
			inputPop[index] = children[childIndex];

		for (; index < population; index++)
			inputPop[index] = new Input();
		
		return inputPop;
	}
	
	private static Input evolve() {
		Input[] inputPop, children;
		int generationCount = 0;
	
		inputPop = new Input[population];
		
		//Initialise population with random values
		for (int index = 0; index < population; index++) {
			inputPop[index] = new Input();
		}

		while (generationCount < generations) {
			//Select best algorithms
			bubbleSort(inputPop);
			
			//Breed the next generation
			int parentIndex, childIndex;
			children = new Input[numParents * numChildren];
			int parentLimit = numParents;
			for (parentIndex = 0, childIndex = 0; parentIndex < parentLimit && parentIndex < population; parentIndex++) {
				if (Math.random() > pSelect) parentLimit++;
				else {
					for (int childCount = 0; childCount < numChildren; childCount++, childIndex++) {
						if (sex == false)
							children[childIndex] = new Input(inputPop[parentIndex]);
						else
							children[childIndex] = new Input(inputPop[parentIndex], inputPop[(int)(Math.random() * numParents)]);
					}
				}
			}

			for (int index = 0; index < population; index++) inputPop[index].incrementAge();
			inputPop = mergeArrays(inputPop, children);
			
			generationCount++;
		}
		
		return inputPop[0];
	}
	
	public static void main(String[] args) {
		final int numGen = 100;
		Input bestInput;
		double[] bestResult = new double[numGen];

		if (maxAge == 0) maxAge = generations; //Nothing can die.
		
		for (int count = 0; count < numGen; count++) {
			bestInput = evolve();

			System.out.print("Iteration " + count + ": ");
			System.out.print(Arrays.toString(bestInput.getValues()));
			System.out.print(" = ");
			System.out.print(bestInput.getResult());
			System.out.print(", (" + bestInput.getMutationRate());
			System.out.println(", " + bestInput.getGeneration() + ")");
			
			bestResult[count] = bestInput.getResult();
		}

		double meanAverage = 0, standardDeviation = 0;
		for (int index = 0; index < numGen; index++)
			meanAverage += bestResult[index];
		
		meanAverage /= numGen;
		
		for (int index = 0; index < numGen; index++)
			standardDeviation += Math.pow(bestResult[index] - meanAverage, 2);
		
		standardDeviation /= numGen;
		standardDeviation = Math.sqrt(standardDeviation);
		
		System.out.println("Average: " + meanAverage);
		System.out.println("Standard deviation: " + standardDeviation);
	}

}
