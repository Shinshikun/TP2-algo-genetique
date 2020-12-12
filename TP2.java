 /**
 * Le but du TP est de trouver la valeur maximale associée à un nombre entier constitué
 * de N bits. Par exemple, si mon entier est constitué de 4 bits, sa valeur maximale est 15. S’il
 * est constitué de 2 bits, la valeur maximale est 3, etc.
 *
 * Pour résoudre ce problème, vous allez devoir générer une population de Y individus,
 * faire se reproduire les individus entre eux à l’aide du système de la roulette afin de regénérer
 * une nouvelle population de Y individus, puis recommencer et ce jusqu’à atteindre la
 * convergence. La détection de la convergence se fait visuellement.
 *
 *  Une fois cela fait, vous devrez implémenter la sélection par tournoi et, pour les plus
 * ambitieux, implémenter la détection de la convergence et la mutation de gène. Pour rappel, la
 * mutation d’un gène correspond à un bit flip (0 -> 1 ou 1 -> 0). La convergence se calcule en
 * regardant la valeur maximale trouvée à la fin d’une génération et en la comparant à la
 * génération précédente. Si trop de générations successives possèdent la même valeur
 * maximale, c’est que la convergence a été atteinte.
 *
 * L’intégralité du code est documentée, libre à vous de vous baser dessus ou non. Le
 * choix du langage reste votre.
 */

public class TP2 {

    public static void main(String[] args) {
    	boolean end = false;
        int popSize = 50;
        int genesPerPop = 20;
        //Crosstype crosstype = Crosstype.ROULETTE;
        Crosstype crosstype = Crosstype.TOURNOI;
        float mutationChance = 0.05f;
        int numberOfEpoch = 5000;
        int numberForConvergence = 20;
        int[] tabValeurMax = new int[numberOfEpoch];
        int valMax = 0;
        Population pop = new Population(popSize, genesPerPop, crosstype, mutationChance);


        for(int epoch=0; epoch<numberOfEpoch; epoch++) {
        	if(end == true) {
        		break;
        	}
            Population newPop = pop.generateNewPopulation();
            pop = newPop;
            System.out.println("Nombre max : "+pop.maxNumberFind());
            tabValeurMax[epoch] = pop.maxNumberFind();
            valMax = tabValeurMax[epoch];
            
            if(epoch >= numberForConvergence) {
            	for(int i=numberForConvergence; i>0; i--) {
            		if(tabValeurMax[epoch-i] == tabValeurMax[epoch-i+1]) {
            			if(i == 1) {
            				System.out.println("Convergence, on stop");
            				System.out.println("Nombre trouvé en " + epoch + " générations");
            				System.out.println("");
            				end = true;
            			}
            		} else {
            			System.out.println("Pas de convergence assez grande, on continue");
            			System.out.println("");
            			break;
            		}
            	}
            }
        }
        System.out.println("Fin de la simulation.");
        System.out.println("Pour un nombre de "+genesPerPop+" bits, la valeur maximale trouvée est "+valMax);

    }
}
