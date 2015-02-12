package toolkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Perceptron extends SupervisedLearner {

	ArrayList<Double> weights;

	@Override
	public void train(Matrix features, Matrix labels) throws Exception {
		double[] m_labels = new double[labels.rows()];
		ArrayList<Double> previous1 = new ArrayList<Double>();
		ArrayList<Double> previous2 = new ArrayList<Double>();
		weights = new ArrayList<Double>(Collections.nCopies(features.cols(), 0.0));
		double prev1_accuracy = 1;
		double prev2_accuracy = 1;
		double current_accuracy = 0;
		double epochs = 0;

		while (continueOn(previous1, previous2, prev1_accuracy, prev2_accuracy, current_accuracy)) {
				previous1 = new ArrayList<Double>(previous2);
				previous2 = new ArrayList<Double>(weights);
				prev2_accuracy = prev1_accuracy;
				prev1_accuracy = current_accuracy;

				Double missed = 0.0;
			for (int i = 0; i < features.rows(); ++i) {
				double[] input = features.row(i);
				double sum = getSum(input);
				if (sum >= 0) {
					m_labels[i] = 1;
				} else {
					m_labels[i] = 0;
				}
				if(m_labels[i]-labels.row(i)[0] != 0){
					missed++;
				}
				else{ //This is for my expreiment in number 6 and was not used for the rest of the project
					double rate = 0.01; //learning rate
					double changes = 2.0; // this is my "zero rate" and is what I varied.
					if(sum > 0){
						for (int j = 0; j < weights.size(); ++j) {
							double change = input[j] * rate * changes;
							double newValue = change + weights.get(j);
							weights.set(j, newValue);
						}
					}
					else{
						for (int j = 0; j < weights.size(); ++j) {
							double change = input[j] * rate * -changes;
							double newValue = change + weights.get(j);
							weights.set(j, newValue);
						}
					}
				}
				adjustWeights(m_labels[i], input, labels.row(i));
			}
			System.out.print(missed/features.rows()+","); // print out the miscalculation rate per epoch
			epochs++;
			try {
				current_accuracy = measureAccuracy(features, labels, new Matrix());
			} catch (Exception e) {
				e.printStackTrace();
			}
			features.shuffle(new Random(), labels);
		}
		System.out.println();
		this.printWeights();
		System.out.println("Epochs: "+epochs);
	}

	private boolean continueOn(ArrayList<Double> previous1, ArrayList<Double> previous2, double prev1_accuracy, double prev2_accuracy, double current_accuracy) {
		if(weights.equals(previous2) && weights.equals(previous1))
			return false;
		if(weights.equals(previous1))
			return false;
		if(Math.abs(current_accuracy-prev1_accuracy) < 0.0000001 && Math.abs(current_accuracy-prev2_accuracy) < 0.0000001)
			return false;
		
		return true;
	}

	private void adjustWeights(double output, double[] input, double[] target) {
		double difference = target[0] - output;
		if (difference != 0) {
			double rate = 0.01;
			for (int i = 0; i < weights.size(); ++i) {
				double change = input[i] * difference * rate;
				double newValue = change + weights.get(i);
				weights.set(i, newValue);
			}
		}
	
	

	}
	
	private void printWeights(){
		for(double d : this.weights){
			System.out.println(d);
		}
	}

	private double getSum(double[] inputs) {
		double total = 0;
		for (int i = 0; i < inputs.length; ++i) {
			total += weights.get(i) * inputs[i];
		}
		return total;
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {

		double out = getSum(features);
		if(out>=0)
			labels[0] = 1;
		else
			labels[0] = 0;
	}

}
