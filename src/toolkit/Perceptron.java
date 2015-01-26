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

		while (continueOn(weights, previous1, previous2)) {
			if (!previous1.isEmpty() && !previous2.isEmpty()) {
				previous1 = new ArrayList<Double>(previous2);
				previous2 = new ArrayList<Double>(weights);
			} else if (!previous1.isEmpty()) {
				previous2 = new ArrayList<Double>(weights);
			} else {
				previous1 = new ArrayList<Double>(weights);
			}

			for (int i = 0; i < features.rows(); ++i) {
				double[] input = features.row(i);
				double sum = getSum(input, weights);
				if (sum >= 0) {
					m_labels[i] = 1;
				} else {
					m_labels[i] = 0;
				}
				adjustWeights(m_labels[i], weights, input, labels.row(i));
			}

			features.shuffle(new Random(), labels);
		}
	}

	private boolean continueOn(ArrayList<Double> weights, ArrayList<Double> previous1, ArrayList<Double> previous2) {
		if(weights.equals(previous2) && weights.equals(previous1))
			return false;
		if(weights.equals(previous1))
			return false;
		
		return true;
	}

	private void adjustWeights(double output, ArrayList<Double> weights,
			double[] input, double[] target) {
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

	private double getSum(double[] inputs, ArrayList<Double> weights) {
		double total = 0;
		for (int i = 0; i < inputs.length; ++i) {
			total += weights.get(i) * inputs[i];
		}
		return total;
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {

		double out = getSum(features, weights);
		if(out>=0)
			labels[0] = 1;
		else
			labels[0] = 0;
	}

}
