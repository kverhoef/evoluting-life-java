package genetics;

import java.util.ArrayList;
import java.util.List;

import main.Options;
import util.Range;

public class AxonGene extends Gene {

	public double strength;
	public double strengthening;
	public double weakening;

	public AxonGene(double strength, double strengthening, double weakening){
		this.strength = strength;
	    this.strengthening = strengthening;
	    this.weakening = weakening;
	}
	
	public AxonGene(){
		this.strength = new Range(-1 * Options.maxStrength.get(), Options.maxStrength.get()).random();
        this.strengthening = new Range(Options.minStrengthening.get(), Options.maxStrengthening.get()).random();
        this.weakening = new Range(Options.minWeakening.get(), Options.maxWeakening.get()).random();
	}
    
    public void mutate() {

        if (Math.random() <= Options.strengthMutationRate.get()) {
          Range range = new Range(0, Options.maxStrength.get());
          this.strength += range.mutation(Options.mutationFraction.get());
          this.strength = range.check(this.strength);
        }

        if (Math.random() <= Options.strengtheningMutationRate.get()) {
        	Range range = new Range(Options.minStrengthening.get(), Options.maxStrengthening.get());
            this.strengthening += range.mutation(Options.mutationFraction.get());
            this.strengthening = range.check(this.strengthening);
        }

        if (Math.random() <= Options.weakeningMutationRate.get()) {
        	Range range = new Range(Options.minWeakening.get(), Options.maxWeakening.get());
            this.weakening += range.mutation(Options.mutationFraction.get());
            this.weakening = range.check(this.weakening);
        }
    }

    public List<AxonGene> mate(AxonGene partner) {
    	return (List<AxonGene>) new Genetics().mate(this, partner);
    }
    
    public List<String> getInitiateProperties() {
		List<String> properties = new ArrayList<>();
		properties.add("strength");
		properties.add("strengthening");
		properties.add("weakening");
		return properties;
	}
	
	public AxonGene initiate(List<Double> properties){
		return new AxonGene(properties.get(0), properties.get(1), properties.get(2));
	}
	
	
}
