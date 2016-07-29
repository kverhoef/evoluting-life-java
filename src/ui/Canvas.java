package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import entities.Animal;
import entities.Plant;
import entities.Position;
import main.Event;
import main.EventType;
import main.FoodSupply;
import main.Population;
import sensors.Eyes;

public class Canvas extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private FoodSupply foodSupply;
	private Population population;
	private final double WEDGE_ANGLE = Math.PI * 0.25;
	
	public Canvas(FoodSupply foodSupply, Population population) {
		this.foodSupply = foodSupply;
		this.population = population;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
			this.repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, population.world.width, population.world.height);
		
		
	    for (Plant plant : foodSupply.plants) {
	    	drawPlant(plant, g);
	    }
	    
	    for (Animal animal : population.entities) {
	    	drawAnimal(animal, population.winningEntity, g);
	    	if (animal.willMate()){
	    		drawMateHalo(animal, g);
	    	}
	    	if (animal.willEat()){
	    		drawEatHalo(animal, g);
	    	}
	    }
	    
	    if (population.winningEntity != null) {
			drawFieldOfView(population.winningEntity, g);
			drawTargetLines(population.winningEntity, g);
		}
	    
  	}
	
	public void drawTargetLines(Animal animal, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        Position p = animal.position;
        g2.setStroke(new BasicStroke(1f));;
        for (int i=0; i< animal.targets.size(); i++) {
            Position t = animal.targets.get(i).position;

            g2.setColor(Color.WHITE);
            g2.drawLine(new Double(p.x).intValue(), new Double(p.y).intValue(), new Double(t.x).intValue(), new Double(t.y).intValue());
        }
    }
	
	public void drawFieldOfView (Animal animal, Graphics g) {
		if (population.winningEntity != null) {
		Graphics2D g2 = (Graphics2D) g;
		
        Position p = animal.position;
        Eyes e = animal.eyes;

        Color c=new Color(.9f,.9f,.9f,.5f );
        g2.setColor(c);
        g2.fillArc(
        		new Double(p.x - (e.viewDistance/2)).intValue(), 
        		new Double(p.y - (e.viewDistance/2)).intValue(),
        		new Double(e.viewDistance).intValue(),
        		new Double(e.viewDistance).intValue(),
        		new Double(Math.toDegrees(p.a*-1)-90).intValue(),
        		new Double(180).intValue()
        		);
        
		}
    }
	
	public void drawMateHalo(Animal animal, Graphics g) {
		double haloSize = animal.getSize() * 0.3;
		Position p = animal.position;
		double ba = p.a + Math.PI; // Find the angle 180deg of entity

        double hX = p.x + ( Math.cos( ba ) * -1 * haloSize );
        double hY = p.y + ( Math.sin( ba ) * -1 * haloSize );
//        var highlight = context.createRadialGradient( hX, hY, 0, hX, hY, haloSize );
//        highlight.addColorStop( 0, "rgba( 0, 100, 255, 1.0 )" );
//        highlight.addColorStop( 1, "rgba( 0, 100,  255, 0.0 )" );
//
//        context.fillStyle = highlight;
//        context.beginPath();
//        context.arc( hX , hY, haloSize, 0, Math.PI*2, true );
//        context.closePath();
//        context.fill();
        g.setColor(Color.RED);
        g.fillOval(
        		new Double(hX).intValue(), 
				new Double(hY).intValue(), 
				new Double(haloSize).intValue(), 
				new Double(haloSize).intValue());
    }

    public void drawEatHalo (Animal animal, Graphics g) {
        double haloSize = animal.getSize() * 0.4;
        Position p = animal.position;
        double ba = p.a + Math.PI; // Find the angle 180deg of entity

        double hX = p.x + ( Math.cos( ba ) );
        double hY = p.y + ( Math.sin( ba ) );
//        var highlight = context.createRadialGradient( hX, hY, 0, hX, hY, haloSize );
//        highlight.addColorStop( 0, "rgba( 255, 255, 255, 1.0 )" );
//        highlight.addColorStop( 1, "rgba( 255, 255, 255, 0.0 )" );

//        context.fillStyle = highlight;
//        context.beginPath();
//        context.arc( hX , hY, haloSize, 0, Math.PI*2, true );
//        context.closePath();
//        context.fill();
        g.setColor(Color.CYAN);
        g.fillOval(
        		new Double(hX).intValue(), 
				new Double(hY).intValue(), 
				new Double(haloSize).intValue(), 
				new Double(haloSize).intValue());
        
    }

	
	public void drawAnimal(Animal animal, Animal bestAnimal, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
        double entitySize = animal.getSize();
        Position p = animal.position;
        double ba = p.a + Math.PI; // Find the angle 180deg of entity

        // Find left back triangle point
        
        double lx = Math.cos( ba + ( WEDGE_ANGLE / 2 ) ) * entitySize;
        double ly = Math.sin( ba + ( WEDGE_ANGLE / 2 ) ) * entitySize;

        // Find right back triangle point
        double rx = Math.cos( ba - ( WEDGE_ANGLE / 2 ) ) * entitySize;
        double ry = Math.sin( ba - ( WEDGE_ANGLE / 2 ) ) * entitySize;

        // Find the curve control point
        double cx = Math.cos( ba ) * entitySize * 0.3;
        double cy = Math.sin( ba ) * entitySize * 0.3;

        // Color code entity based on food eaten compared to most successful
        double currentBestScore = getAbsoluteScore(bestAnimal);
        int green = (int) Math.floor(255 * (1 - (currentBestScore == 0 ? 0 : getAbsoluteScore(animal) / currentBestScore )));
        
        Color color = new Color(255, green>0?green:0, 0);
        g2.setColor(color);
        
        g2.setStroke(new BasicStroke((float) (2 + Math.floor(animal.age / (animal.getOldAge() / 5)))));
        
        // Draw the triangle
        
        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        polygon.moveTo(p.x, p.y);
        polygon.lineTo( p.x + lx, p.y + ly );
        polygon.quadTo( p.x + cx, p.y + cy, p.x + rx, p.y + ry );
        polygon.closePath();

        g2.fill(polygon);
        g2.setColor(Color.BLACK);
        g2.draw(polygon);

    }
	
	public double getAbsoluteScore(Animal animal) {
        return animal.getScore() < 0 ? 0 : animal.getScore();
    }
	
	public void drawPlant(Plant plant, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
//		g2.drawArc(35, 45, 75, 95, 0, 90);
//        context.beginPath();
		
//        context.lineWidth = 2;
        // context.lineWidth = 2 + Math.floor(plant.life.age / (plant.life.oldAge / 3));

        double r = Math.floor( 255 * (1 - plant.getNutritionN()) );
//        var color = "rgb(" + r + ",220,0)"; //187

//        if (plant.nutrition < 0) {
//        	
//        	g2.setColor(Color.RED);
////        	g2.set
////            context.strokeStyle = color;
////            context.fillStyle = "#000";
//        }
//        else {
//            context.strokeStyle = "#000";
//            context.fillStyle = color;
        	g2.setColor(Color.GREEN);
//        }

        
//        g2.drawArc(
//        		new Double(plant.position.x).intValue(), 
//        		new Double(plant.position.y).intValue(), 
//        		new Double(plant.getSize()).intValue(), 
//				new Double(plant.getSize()).intValue(), 
//				new Double(Math.PI*2).intValue(), 
//				new Double(Math.PI*2).intValue()
//        );
//        	g2.drawArc(x, y, width, height, startAngle, arcAngle);
        g2.fillOval(
        		new Double(plant.position.x).intValue(), 
				new Double(plant.position.y).intValue(), 
				new Double(plant.getSize()).intValue(), 
				new Double(plant.getSize()).intValue());
//        context.arc( plant.position.x, plant.position.x, plant.getSize(), 0, Math.PI*2, true );
        
        g2.setColor(Color.BLACK);
        g2.drawOval(
        		new Double(plant.position.x).intValue(), 
				new Double(plant.position.y).intValue(), 
				new Double(plant.getSize()).intValue(), 
				new Double(plant.getSize()).intValue());
    }
	
}
