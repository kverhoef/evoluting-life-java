package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.Animal;
import entities.Plant;
import entities.Position;
import entities.World;
import evolution.RouletteWheelSelectionByRank;
import genetics.Genome;
import util.Range;

public class Population {
	
    public World world;
	public List<Animal> entities = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI
	public Animal winningEntity;
	
	public Option populationSize = new Option(16*8);
	
	public Population(World world){
		this.world = world;
		
		// Fill our population with entities
      for (int i = 0; i < populationSize.get(); i++) {
          Genome genome = createGenome();
          Position position = createRandomPosition();
          Animal entity = new Animal(genome, position, world, this);
          addEntity(entity);
      }
	}
    
    public Genome createGenome() {
        return new Genome(14, 6);
    }
    
    public Position createRandomPosition() {
        Range range = new Range(-0.8, 0.8);
        Double x = world.width/2 + world.width/2 * range.random();
        Double y = world.height/2 + world.height/2 * range.random();
        
    	return new Position(x, y, Math.random() * Math.PI * 2);
    }

    public void run(List<Plant> plants) {

    	  Collections.sort(this.entities);

    	  List<Animal> entitiesToRemove = new ArrayList<>();
          for (int i = 0; i < this.entities.size(); i++) {
        	  Animal entity = this.entities.get(i);
        	  // TODO to make this fast this should run on seperate threads
              entity.run(plants, this.entities);

              // Check entity lifecycle and remove dead entities
              if (!entity.lives()){
            	  entitiesToRemove.add(entity);
              };

          }
          
          for (Animal entityToRemove : entitiesToRemove){
        	  this.entities.remove(entityToRemove);
          }

          // Find the best ranking entity
          winningEntity = this.entities.get(0);

          if (this.entities.size() <= populationSize.get() -2) {
        	  List<Animal> parents = selectParents();
        	  
              List<Position> positions = createRandomPositions(2);
              List<Animal> children = parents.get(0).produceChildren(parents.get(1), positions);

              for (int i=0; i<children.size(); i++) {
                  addEntity(children.get(i));
              }

          }   
    }
      
    public void addEntity(Animal newAnimal) {
	      this.entities.add( newAnimal );
	      if (Main.getInstance() != null){ // TODO fix no null check
	    	  Main.getInstance().broadcast(EventType.NEW_ANIMAL, newAnimal);
	      }
    }

      public List<Position> createRandomPositions(int count) {
    	  List<Position> positions = new ArrayList<>();

          for (int i=0; i<count; i++) {
              positions.add(createRandomPosition());
          }
          return positions;
      }

      public List<Animal> selectParents() {
	    List<Animal> parents = new ArrayList<>();
	    
	    for (int i=0; i<2; i++) {
	        Animal winningEntity = new RouletteWheelSelectionByRank().select(this.entities);
	        	parents.add(winningEntity);
	    	}
	
	    	return parents;
      }
    
    
}
