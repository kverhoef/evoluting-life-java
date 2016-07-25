package ui;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import entities.Animal;
import main.Event;
import main.EventType;
import main.FoodSupply;
import main.Population;

public class StatsPrinter implements Observer {
	
	private int totalStarved;
	private int totalConsumed;
	private int totalWandered;
	private int totalDiedOfAge;
	private int totalMatings;
	private double avgHealth;
	private FoodSupply foodSupply;
	private Population population;
	
	public StatsPrinter(FoodSupply foodSupply, Population population) {
		this.foodSupply = foodSupply;
		this.population = population;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
	
		if (event.type.equals(EventType.CONSUMED)) {
			totalConsumed++;
		}
		else if (event.type.equals(EventType.EAT)) {
//			System.out.println("Eat");
		}
		else if (event.type.equals(EventType.STARVED)) {
			totalStarved++;
		}
		else if (event.type.equals(EventType.WANDERED)) {
			totalWandered++;
		}
		else if (event.type.equals(EventType.DIED_OF_AGE)) {
			totalDiedOfAge++;
		}
		else if (event.type.equals(EventType.MATE)) {
			totalMatings++;
		}
		else if (event.type.equals(EventType.CYCLE_END)) {
			if ((int)event.value % 100 == 0) {
				collectStats();
				printStats();
				resetStats();
			}
		}
	}
	
	private void collectStats() {
		double totalHealth = 0;
		for (Animal animal : population.entities) {
			totalHealth += animal.getHealth();
	    }
		this.avgHealth = totalHealth / population.entities.size();
	}
	
	private void printStats() {
		DecimalFormat df2 = new DecimalFormat("#.##");
		DecimalFormat df3 = new DecimalFormat("#.###");
		
		System.out.println(
				"avg. health:\t" + df2.format(avgHealth) + "\t" +
				"totalMatings:\t" + totalMatings + "\t" +
				"totalConsumed:\t" + totalConsumed + "\t" +
				"totalStarved:\t" + totalStarved + "\t" +
				"totalWandered:\t" + totalWandered + "\t" +
				"totalDiedOfAge:\t" + totalDiedOfAge + "\t" +
				"best:\t" + df3.format(population.winningEntity.rank()) + "\t" 
				);
	}
	
	private void resetStats() {
		this.totalConsumed = 0;
		this.totalStarved = 0;
		this.totalWandered = 0;
		this.totalDiedOfAge = 0;
	}
}
