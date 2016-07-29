package genetics;

import java.util.ArrayList;
import java.util.List;

import main.Options;
import util.Range;

public class LifeGene extends Gene {

	public double oldAge;
	public double nutrition;
	public double matureAge;
	public double initialEnergyMutationRate;
	public double initialEnergy;
	
	public LifeGene(){
        this.oldAge = new Range(Options.minOldAge.get(), Options.maxOldAge.get()).random();
        this.nutrition = new Range(Options.minNutrition.get(), Options.maxNutrition.get()).random();
        this.matureAge = new Range(Options.minMatureAge.get(), Options.maxMatureAge.get()).random();
        this.initialEnergy = new Range(Options.minInitialEnergy.get(), Options.maxInitialEnergy.get()).random();
	}
	
	public LifeGene(double oldAge, double nutrition, double matureAge, double initialEnergy){
		this.oldAge = oldAge;
	    this.nutrition = nutrition;
	    this.matureAge = matureAge;
	    this.initialEnergy = initialEnergy;
	}
	
	public void mutate() {
		
        if (Math.random() <= Options.initialEnergyMutationRate.get()) {
            this.initialEnergy += new Range(Options.minInitialEnergy.get(), Options.maxInitialEnergy.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.oldAgeMutationRate.get()) {
            this.oldAge += new Range(Options.minOldAge.get(), Options.maxOldAge.get()).mutation(Options.mutationFraction.get());
        }
        
        if (Math.random() <= Options.matureAgeMutationRate.get()) {
            this.matureAge += new Range(Options.minMatureAge.get(), Options.maxMatureAge.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.nutritionMutationRate.get()) {
            this.nutrition += new Range(Options.minNutrition.get(), Options.maxNutrition.get()).mutation(Options.mutationFraction.get());
        }
    }

    public List<LifeGene> mate(LifeGene partner) {
    	return (List<LifeGene>) new Genetics().mate(this, partner);
    }
    
    public List<String> getInitiateProperties() {
		List<String> properties = new ArrayList<>();
		properties.add("oldAge");
		properties.add("nutrition");
		properties.add("matureAge");
		properties.add("initialEnergy");
		
		return properties;
	}
	
	public LifeGene initiate(List<Double> properties){
		return new LifeGene(properties.get(0), properties.get(1), properties.get(2), properties.get(3));
	}
	
}
