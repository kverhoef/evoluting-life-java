package util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Range {

	double lower = 0;
	double upper = 0;
	
	public Range(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;

         if (lower > upper){
        	 throw new Error("upper (" + upper + ") smaller than lower (" + lower + ")");
         } 
	}
	
	public double random() {
	
//		Random r = new Random();
//		double randomValue = lower + (upper - lower) * r.nextDouble();
//		return randomValue;
		
		return ThreadLocalRandom.current().nextDouble(lower, upper);
		
        //return lower + Math.random() * (upper - lower); TODO ??? klopt dit
    }
	
	public double check(double value){
		return (value > upper) ? upper : (value < lower) ? lower : value;
	}
	
	public double checkLower(double value){
		return (value < lower) ? lower : value;
	}

//	public double checkUpper(double value){
//		return (value > upper) ? upper : value;
//	}
	
	public double mutation(double mutationFraction){
		double randomFraction = new Range(-1 * mutationFraction, mutationFraction).random();
        return (upper - lower) * randomFraction; // .toFixed(2);
	}

}
