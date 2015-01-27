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
		double prev1_accuracy = 5;
		double prev2_accuracy = 5;
		double current_accuracy = 0;

		while (continueOn(previous1, previous2, prev1_accuracy, prev2_accuracy, current_accuracy)) {
				previous1 = new ArrayList<Double>(previous2);
				previous2 = new ArrayList<Double>(weights);
				prev2_accuracy = prev1_accuracy;
				prev1_accuracy = current_accuracy;

			for (int i = 0; i < features.rows(); ++i) {
				double[] input = features.row(i);
				double sum = getSum(input);
				if (sum >= 0) {
					m_labels[i] = 1;
				} else {
					m_labels[i] = 0;
				}
				adjustWeights(m_labels[i], input, labels.row(i));
			}
			try {
				current_accuracy = measureAccuracy(features, labels, new Matrix());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			features.shuffle(new Random(), labels);
		}
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
			double rate = 0.1;
			for (int i = 0; i < weights.size(); ++i) {
				double change = rate * difference * input[i];
				double newValue = change + weights.get(i);
				weights.set(i, newValue);
			}
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
