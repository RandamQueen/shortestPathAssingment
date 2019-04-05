import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * 
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * 
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestantsâ€™
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 * 
 *    ï‚· Each contestant walks at a given estimated speed.
 *    ï‚· The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Dijkstra's algorithm
 */

public class CompetitionDijkstra {
	int contestASpeed;
	int contestBSpeed;
	int contestCSpeed;
	int nodeNum;
	int edgeNum;
	EdgeWeightedDigrapgh contestGrapgh;
	double[] distTo = new double[nodeNum];
	DirectedEdge[] edgeTo = new DirectedEdge[nodeNum];

	/**
	 * @param filename:A
	 *            filename containing the details of the city road network
	 * @param sA,
	 *            sB, sC: speeds for 3 people
	 * @throws IOException
	 * 
	 */
	CompetitionDijkstra(String filename, int sA, int sB, int sC) throws IOException {
		contestASpeed = sA;
		contestBSpeed = sB;
		contestCSpeed = sC;

		File file = new File(filename);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		String fileLine = br.readLine();
		nodeNum = Integer.parseInt(fileLine);
		fileLine = br.readLine();
		edgeNum = Integer.parseInt(fileLine);

		contestGrapgh = new EdgeWeightedDigrapgh(nodeNum, edgeNum);

		fileLine = br.readLine();
		while (fileLine != null) {
			String startNodeText = fileLine.substring(0, 1);
			String endNodeText = fileLine.substring(2, 3);
			String weightText = fileLine.substring(4);

			int startNode = Integer.parseInt(startNodeText);
			int endNode = Integer.parseInt(endNodeText);
			double weight = Double.parseDouble(weightText);

			DirectedEdge newEdge = new DirectedEdge(startNode, endNode, weight);
			contestGrapgh.addEdge(newEdge);
			fileLine = br.readLine();
		}
		br.close();
	}

	/**
	 * @return int: minimum minutes that will pass before the three contestants can
	 *         meet
	 */
	public int timeRequiredforCompetition() {
		int timeRequired = -1;
		if (contestASpeed < 50 || contestASpeed > 100 || contestBSpeed < 50 || contestBSpeed > 100
				|| contestCSpeed > 100 || contestCSpeed < 50) {
			return timeRequired;
		}
		double[] locationDistance = new double[nodeNum];
		for (int i = 0; i < nodeNum; i++) {
			Dijkstra(i);
		}
		return timeRequired;
	}

	public void Dijkstra( int sourceNodeIndex) {
		for( int temp = 0; temp <nodeNum; temp++ )
		{ 
			distTo[temp] = -1; 
			edgeTo[temp] = null; 
		}
		distTo[sourceNodeIndex] = 0; 
		

	}

	public void relax(DirectedEdge testEdge) { 
	int v = testEdge.from(); 
	int w = testEdge.to(); 
		if( distTo[w] > distTo[v] + testEdge.weight )
		{ 
			distTo[w] = distTo[v] + testEdge.weight;
			edgeTo[w] = testEdge;
		}
	}
	
	public void relax(EdgeWeightedDigrapgh graph, int source ) { 
		for( int i =0; i <
		{ 
			int w = testEdge.to(); 
			if( distTo[w] > distTo[source] + testEdge.weight )
			{ 
				distTo[w] = distTo[source] + testEdge.weight;
				edgeTo[w] = testEdge;
			}
		} 
	}

	public String toString() {
		String returnString = "";

		for (int index = 0; index < nodeNum; index++) {
			Bag<DirectedEdge> currentBag = contestGrapgh.adj[index];
			int bagIndex = 0;
			DirectedEdge currentEdge = currentBag.get(bagIndex);
			while (currentEdge != null) {
				String currentString = "";
				currentString = currentEdge.from() + " ->" + currentEdge.to() + " " + currentEdge.weight + "\n";
				returnString += currentString;
				bagIndex++;
				currentEdge = currentBag.get(bagIndex);
			}
		}
		return returnString;
	}

	// Based on the files of similar name provided in the textbook
	private static class EdgeWeightedDigrapgh {
		int nodeNum;
		int edgeNum;
		Bag<DirectedEdge>[] adj;

		EdgeWeightedDigrapgh(int nodeNum, int edgeNum) {
			this.nodeNum = nodeNum;
			this.edgeNum = edgeNum;
			adj = (Bag<DirectedEdge>[]) new Bag[nodeNum];
			for (int index = 0; index < nodeNum; index++) {
				adj[index] = new Bag<DirectedEdge>();
			}
		}

		void addEdge(DirectedEdge newEdge) {
			int from = newEdge.from();
			int to = newEdge.to();
			adj[from].add(newEdge);
		}
	}

	public static class Bag<DirectedEdge> {
		private Node<DirectedEdge> startNode; // beginning of bag
		private int size; // number of elements in bag

		public Bag() {
			startNode = null;
			size = 0;
		}

		public boolean isEmpty() {
			return startNode == null;
		}

		public int size() {
			return size;
		}

		public void add(DirectedEdge newNode) {
			Node<DirectedEdge> oldfirst = startNode;
			startNode = new Node<DirectedEdge>();
			startNode.currentNode = newNode;
			startNode.nextNode = oldfirst;
			size++;
		}
	
		public DirectedEdge get(int index) {
			int counter = 0;
			DirectedEdge returnItem = null;
			if (index >= size) {
				return returnItem;
			}
			Node<DirectedEdge> currentItem = startNode;
			while (counter != index) {
				currentItem = currentItem.nextNode;
				counter++;
			}
			returnItem = currentItem.currentNode;
			return returnItem;
		}
	}

	private static class Node<DirectedEdge> {
		private DirectedEdge currentNode;
		private Node<DirectedEdge> nextNode;
	}

	private static class DirectedEdge {
		int startNode;
		int endNode;
		double weight;

		DirectedEdge(int startNode, int endNode, double weight) {
			this.startNode = startNode;
			this.endNode = endNode;
			this.weight = weight;
		}

		public int to() {
			return endNode;
		}

		public int from() {
			return startNode;
		}

		public double getWeight() {
			return weight;
		}
	}
}