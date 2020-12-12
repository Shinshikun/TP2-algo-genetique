import java.util.Arrays;
import java.util.Random;

public class Population {

    private Individual[] individuals;
    private int genesPerPop;
    private Crosstype crosstype;
    private float mutationChance;

    /**
     * Representation of a population of pseudo-randomly generated individuals
     * @param popSize set the size of this population
     * @param genesPerPop sets the gene size of each individual in the pool
     * @param crosstype the crosstype to be used by this population
     * @param mutationChance chance for an individual to mutate at birth
     */
    public Population(int popSize, int genesPerPop, Crosstype crosstype, float mutationChance) {
        this.individuals = new Individual[popSize];
        this.genesPerPop = genesPerPop;
        this.crosstype = crosstype;
        this.mutationChance = mutationChance;
        for(int i=0; i<popSize; i++)
            this.individuals[i] = new Individual(genesPerPop);
    }

    /**
     * Representation of a population of pre-computed individuals
     * @param individuals an array of individuals
     * @param crosstype the crosstype to be used by this population
     * @param mutationChance chance for an individual to mutate at birth
     */
    public Population(Individual[] individuals, Crosstype crosstype, float mutationChance) {
        assert individuals.length > 0;
        this.individuals = individuals;
        this.genesPerPop = individuals[0].getGenes().length;
        this.crosstype = crosstype;
        this.mutationChance = mutationChance;
    }

    /**
     * Creates a new population using this generation's individuals
     * @return the newly generated population
     */
    public Population generateNewPopulation()  {
    	Individual[] offsprings = new Individual[2];
    	Individual[] newPop = new Individual[this.individuals.length];
    	
        if(this.crosstype == Crosstype.ROULETTE) {
            //ToDo generate using a ROULETTE crosstype
        	for(int i=0; i<this.individuals.length; i+=2) {
        		Random rand = new Random();
        		int rand1 = rand.nextInt(this.genesPerPop);
        		

                offsprings  = this.reproduceIndividuals(this.roulette(), this.roulette(), rand1);
                newPop[i] = offsprings[0];
                newPop[i+1] = offsprings[1];
        	}
        	
        } else {
            //ToDo generate using a TOURNOI crosstype
        	Individual[] tabGagnantTournois = tournois();
        	for(int i=0; i<tabGagnantTournois.length; i++) {
        		newPop[i] = tabGagnantTournois[i];
        	}
        	
        	for(int i = 0; i<tabGagnantTournois.length; i++) {
        		Random rand = new Random();
        		int rand1 = rand.nextInt(this.genesPerPop);
        		int rand2 = rand.nextInt(tabGagnantTournois.length);
        		int rand3 = rand.nextInt(tabGagnantTournois.length);
        		
                offsprings  = this.reproduceIndividuals(tabGagnantTournois[rand2],tabGagnantTournois[rand3], rand1);
                
                if (offsprings[0].getFitnessScore() > offsprings[1].getFitnessScore()) {
                	newPop[i+tabGagnantTournois.length] = offsprings[0];
     		   	} else {
     		   		newPop[i+tabGagnantTournois.length] = offsprings[1];
     		   	}
      
        	}
        }
        
        return new Population(newPop, this.crosstype, this.mutationChance);
    }

    /**
     * Takes 2 individuals and create 2 children using their genes
     * @param firstParent the first selected individual
     * @param secondParent the second selected individual
     * @param crosspoint index of the crosspoint
     * @return an array of 2 individuals
     */
    public Individual[] reproduceIndividuals(Individual firstParent, Individual secondParent, int crosspoint){
        Individual[] offsprings = new Individual[2];

        int[] firstChildGenes = firstParent.getGenes().clone();
        int[] secondChildGenes = firstParent.getGenes().clone();
        
//        ToDo compute the genes
        for(int i=crosspoint+1; i<genesPerPop; i++) {
        	firstChildGenes[i] = secondParent.getGenes()[i];
        }
        for(int i=0; i<crosspoint; i++) {
        	secondChildGenes[i] = secondParent.getGenes()[i];
        }
        
//        ToDo compute a possible mutation of a gene
        offsprings[0] = new Individual(firstChildGenes);
        offsprings[1] = new Individual(secondChildGenes);

        
        Random randMutation = new Random();
        Random randGeneFlip = new Random();
        if (this.mutationChance > randMutation.nextFloat()) {
        	offsprings[0].geneFlip(randGeneFlip.nextInt(this.genesPerPop));
        }
        if (this.mutationChance > randMutation.nextFloat()) {
        	offsprings[1].geneFlip(randGeneFlip.nextInt(this.genesPerPop));
        }
        
        
        
        //System.out.println("------------------------------");
        return offsprings;

    }
    
    
    public int maxNumberFind() {
    	int maxCurrentNumber = 0;
    	int maxNumber = 0;
    	int[] tabGenes = new int[4];
    	for(int i=0; i<this.individuals.length;i++) {
    		tabGenes =  this.individuals[i].getGenes();
    		for(int j=0; j<tabGenes.length; j++) {
    			maxNumber += tabGenes[j] * Math.pow(2,tabGenes.length-1-j);
    		}
    		if (maxCurrentNumber < maxNumber) {
    			maxCurrentNumber = maxNumber;
    		}
    		maxNumber = 0;
    	}
    	return maxCurrentNumber;
    }
    
    
    public Individual roulette() {
		// Code pour simuler une roulette pondérée par la valeur de fitness des individus 
		int totalFitness = 0;
		int currentFitness = 0;
		for(Individual indi: this.individuals) {
			totalFitness += indi.getFitnessScore();
		}
		
		double rnd = Math.random() * totalFitness;
		
		for(Individual indi: this.individuals) {
			if (rnd >= currentFitness && rnd <= currentFitness + indi.getFitnessScore()) {
				return indi;
			}
			currentFitness += indi.getFitnessScore();
		}
		return null;
    }
    
    public Individual[] removeIndividual(Individual[] tabIndividual, int index) {
    	Individual[] copy = new Individual[tabIndividual.length - 1];
    	for (int i = 0, j = 0; i < this.individuals.length; i++) {
    		if (i != index) {
    			copy[j++] = this.individuals[i];
    		}
    	}
    	return copy;
    }
    
   public Individual[] tournois(){
	   int taillePop = this.individuals.length;
	   Individual[] tabResultant = new Individual[taillePop/2];
	   
	   for(int i=0; i<taillePop/2; i++) {
		   int index = (int)(Math.random() * individuals.length);
		   Individual indi1 = this.individuals[index];
		   this.individuals = removeIndividual(this.individuals, index);
		   index = (int)(Math.random() * individuals.length);
		   Individual indi2 = this.individuals[index];
		   this.individuals = removeIndividual(this.individuals, index);
		   
		   if (indi1.getFitnessScore() > indi2.getFitnessScore()) {
			   tabResultant[i] = indi1;
		   } else {
			   tabResultant[i] = indi2;
		   }

	   }
	   
	   
	   return tabResultant;

   }

    @Override
    public String toString()
    {
        return "Population{" +
                "individuals=" + Arrays.toString(individuals) +
                ", genesPerPop=" + genesPerPop +
                '}';
    }
}
