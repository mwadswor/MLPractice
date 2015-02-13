package toolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BackPropLearner extends SupervisedLearner {

	private ArrayList<Node> nodes;
	private HashMap<String, Double> weights;
	private int layers;
	private double momentum;
	private double rate; 
	private int validationPos; 
	private int numouts;
	
	public BackPropLearner(int numlayers, double momentum, int numOuts) {
		this.numouts = numOuts;
		this.nodes = new ArrayList<Node>();
		this.weights = new HashMap<String,Double>();
		this.layers = numlayers;
		this.momentum = momentum;
		this.rate = .1;
		this.validationPos = 0;
	}

	@Override
	public void train(Matrix features, Matrix labels) throws Exception {
		this.validationPos = features.rows() - (features.rows()/4);
		initializeNodes(features, labels);
		initializeWeights();
		while(continueOn()){
			runEpoch(features,labels);
			runValidations(features,labels);
			features.shuffle(new Random(), labels);
		}
	}

	private void runValidations(Matrix features, Matrix labels) {
		
	}

	private void runEpoch(Matrix features, Matrix labels) {
		for(int i = 0; i < this.validationPos; ++i){
			initializeinputs(features.row(i));
			for(Node n : nodes){
				n.calculateOutput();
			}
			int labelsPos = labels.cols()-1;
			for(int j = nodes.size()-1;j > nodes.size()-labels.cols()-1; --j){
				
			}
		}
	}

	private boolean continueOn() {
		
		return true;
	}

	private void initializeWeights() {
		for(int i = 0; i < this.nodes.size(); ++i){
			for(int j = 0; j < nodes.get(i).forward.size(); ++j){
				String key = makeWeightKey(i,nodes.get(i).forward.get(j));
				Random r = new Random();
				this.weights.put(key, r.nextDouble());
			}
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
		int id = 0;
		ArrayList<Node> prev = new ArrayList<Node>();
		ArrayList<Node> curr = new ArrayList<Node>();
		ArrayList<Integer> prevID = new ArrayList<Integer>();
		ArrayList<Integer> currID = new ArrayList<Integer>();
		
		for(int i = 0; i < features.cols(); ++i){
			Node n = new Node(i,nodetype.INPUT);
			prevID.add(id);
			prev.add(n);
			++id;
		}
		this.nodes.addAll(prev);
		
		int stop = features.cols()*2;
		for(int j = 0; j < this.layers; ++j){
			for(int i = 0; i < stop; ++i){
				Node n = new Node(id, nodetype.HIDDEN);
				n.backward.addAll(prevID);
				curr.add(n);
				currID.add(id);
				++id;
			}
			for(int i = 0; i < prev.size(); ++i){
				prev.get(i).forward.addAll(currID);
			}
			this.nodes.addAll(curr);
			prev.clear();
			prev.addAll(curr);
			curr.clear();
			prevID.clear();
			prevID.addAll(currID);
			currID.clear();
			
		}
		for(int i = 0; i < this.numouts; ++i){
			Node n = new Node(i+this.nodes.size()-1,nodetype.OUTPUT);
			n.backward.addAll(prevID);
			curr.add(n);
			currID.add(id);
			++id;
		}
		for(int i = 0; i < prev.size(); ++i){
			prev.get(i).forward.addAll(currID);
		}
		this.nodes.addAll(curr);
	}

	@Override
	public void predict(double[] features, double[] labels) throws Exception {

	}

	public String makeWeightKey(int from, int to){
		StringBuilder sb = new StringBuilder();
		sb.append(from + ":" + to);
		return sb.toString();
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
			Random r = new Random();
			this.bias = r.nextDouble();
			this.type = type;
		}
		
		public void calculateOutput(){
			if(this.type != nodetype.INPUT){
				double net = this.bias;
				for(Integer n: backward){
					double weight = weights.get(makeWeightKey(n,this.position));
					net += weight*nodes.get(n).output;
				}
				this.output = 1/(1+Math.pow(Math.E, -net));
			}
		}
		
	
		
	}
	
	
	public enum nodetype{
		INPUT,
		HIDDEN,
		OUTPUT
	}
	
	
}
