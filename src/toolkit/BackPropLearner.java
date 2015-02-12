package toolkit;

import java.util.ArrayList;
import java.util.HashMap;

public class BackPropLearner extends SupervisedLearner {

	private ArrayList<Node> nodes;
	private HashMap<String, Double> weights;
	private int layers;
	private double momentum;
	private double rate; 
	
	public BackPropLearner(int numlayers, double momentum) {
		this.nodes = new ArrayList<Node>();
		this.weights = new HashMap<String,Double>();
		this.layers = numlayers;
		this.momentum = momentum;
		this.rate = .1;
	}

	@Override
	public void train(Matrix features, Matrix labels) throws Exception {
		initializeNodes(features, labels);
		setPointers(features.cols());
	}
	

	private void setPointers(int start) {
		int pos = 0;
		while(pos < this.layers){
			int stop = start+(2*start);
			for(int i = start; i < stop; ++i){
				if(this.nodes.get(i).type != nodetype.HIDDEN){
					break;
				}
				if(this.layers == 1){
					
				}
				else{
					
				}
			}
			start = stop;
		}
	}

	private void initializeinputs(double[] features){
		int pos = 0;
		while(this.nodes.get(pos).type == nodetype.INPUT){
			this.nodes.get(pos).output = features[pos];
			++pos;
		}
	}

	private void initializeNodes(Matrix features, Matrix labels) {
		for(int i = 0; i < features.cols(); ++i){
			Node n = new Node(i,nodetype.INPUT);
			this.nodes.add(n);
		}
		int stop = features.cols()*2*this.layers;
		for(int i = 0; i < stop; ++i){
			Node n = new Node(i+this.nodes.size()-1,nodetype.HIDDEN);
			this.nodes.add(n);
		}
		for(int i = 0; i < labels.cols(); ++i){
			Node n = new Node(i+this.nodes.size()-1,nodetype.OUTPUT);
			this.nodes.add(n);
		}
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {

	}

	
	public class Node{
		
	
		double output;
		int position;
		ArrayList<Integer> forward;
		ArrayList<Integer> backward;
		double bias;
		nodetype type;
		
		public Node(int i, nodetype type) {
			this.output = 0;
			this.position = i;
			this.backward = new ArrayList<Integer>();
			this.forward = new ArrayList<Integer>();
			this.bias = 0;
			this.type = type;
		}
		
		public void calculateOutput(){
			if(this.type != nodetype.INPUT){
				double net = 0;
				for(Integer n: backward){
					double weight = weights.get(makeWeightLookup(n));
					net += weight*nodes.get(n).output;
				}
				this.output = 1/(1+Math.pow(Math.E, -net));
			}
		}
		
		public String makeWeightLookup(int other){
			StringBuilder sb = new StringBuilder();
			sb.append(this.position + ":" + other);
			return sb.toString();
		}
		
	}
	
	
	public enum nodetype{
		INPUT,
		HIDDEN,
		OUTPUT
	}
	
	
}
