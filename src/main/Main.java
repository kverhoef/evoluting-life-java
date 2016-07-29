package main;

import java.util.Observable;

import entities.World;

public class Main extends Observable {
	
	public FoodSupply foodSupply;
	public Population population;
	public World world;
	
	private static Main singleton = new Main();
	
	public static Main getInstance() {
      return singleton;
	}

	private Main() {

		this.world = new World();
        this.foodSupply = new FoodSupply(world);
        this.population = new Population(world);

	}
	
	private int iteration = 0;
	
	public void startMainLoop(){
		
		long sleepTime = (long) Options.mainLoopSleep.get();
		
		while (true){
			mainLoop();
			if (sleepTime > 0){
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void mainLoop(){

          // Keep track of our iteration count
          iteration++;
          
          // Run a tick of foodSupply life cycle
          foodSupply.run();

          // Run a tick of population life cycle
          population.run(foodSupply.plants);

          broadcast(EventType.CYCLE_END, iteration);
	}
	
	public void broadcast(EventType eventType, Object value) {
		setChanged();
        super.notifyObservers(new Event(eventType, value));
	}
	
}