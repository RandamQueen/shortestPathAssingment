import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
		processTextFile(filename);
	}

	public void processTextFile(String filename) throws IOException {
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
			if (nodeNum > 100) {
				if (fileLine.charAt(5) == ' ') // this means the second num is single digit
				{
					String tempString = fileLine.substring(6);
					fileLine = fileLine.substring(0, 4);
					fileLine += tempString;
				} else if (fileLine.charAt(4) == ' ') // this means the first num is double digit
				{
					String tempString = fileLine.substring(5);
					fileLine = fileLine.substring(0, 4);
					fileLine += tempString;
				}
				if (fileLine.charAt(1) == ' ') // this means the first num is single digit
				{
					fileLine = fileLine.substring(2);
				} else if (fileLine.charAt(0) == ' ') // this means the first num is double digit
				{
					fileLine = fileLine.substring(1);
				}
			}

			String[] strArray = fileLine.split(" ");
			String startNodeText = strArray[0];
			String endNodeText = strArray[1];
			String weightText = strArray[2];

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
		double[][] distBetweenNodeList = new double[nodeNum][nodeNum];
		for (int x = 0; x < nodeNum; x++) {
			for (int y = 0; y < nodeNum; y++) {
				distBetweenNodeList[x][y] = -1;
			}
		}
		// for (int i = 0; i < nodeNum; i++) {
		double[] sourceDistList = Dijkstra(contestGrapgh, 0);

		// for( int temp = 0; temp < nodeNum; temp++ )
		// {
		// double distFromSource = sourceDistList[temp];
		/// distBetweenNodeList[i][temp] = distFromSource;
		// }
		// }
		System.out.println("Thing finsihed");
		return timeRequired;
	}

	public double[] Dijkstra(EdgeWeightedDigrapgh contestGrapgh, int sourceNodeIndex) {
		ArrayList<String> testOrder = new ArrayList<String>();
		boolean[] marked = new boolean[nodeNum];
		double[] distTo = new double[nodeNum];
		DirectedEdge[] edgeTo = new DirectedEdge[nodeNum];
		int currentTestNode = sourceNodeIndex;
		testOrder.add(Integer.toString(currentTestNode));
		for (int temp = 0; temp < nodeNum; temp++) {
			distTo[temp] = -1;
			edgeTo[temp] = null;
			marked[temp] = false;
		}
		distTo[sourceNodeIndex] = 0;

		while (testOrder.size() > 0) {
			currentTestNode = Integer.parseInt(testOrder.get(0));
			if (!marked[currentTestNode]) {
				Bag sourceEdge = contestGrapgh.adj[currentTestNode];
				
				ArrayList<String> templist = getChildValues(sourceEdge);
				
				for (int temp = 0; temp < templist.size(); temp++) {
					testOrder.add(templist.get(temp));
				}
				
				for( int index = 0;index < sourceEdge.size(); index++)
				{ 
					DirectedEdge testEdge = sourceEdge.get(index); 
					int w = testEdge.to(); 
					double currentWeight = distTo[w]; 
					double newWeight = distTo[currentTestNode] + testEdge.weight ; 
					
					if ( currentWeight == -1) 
					{ 
						distTo[w] = newWeight; 
						edgeTo[w] = testEdge;
					}
					else if(currentWeight > newWeight  )	
					{ 
						distTo[w] = newWeight; 
						edgeTo[w] = testEdge;
					}
				}
				marked[currentTestNode] = true;
			}
			testOrder.remove(0);
		}
		return distTo;
	}

	public ArrayList<String> getChildValues(Bag sourceEdge) {
		ArrayList<String> returnStrings = new ArrayList<String>();
		for (int index = 0; index < sourceEdge.size(); index++) {
			DirectedEdge currentNode = sourceEdge.get(index);
			String childVetrice = Integer.toString(currentNode.to());
			returnStrings.add(childVetrice);
		}
		return returnStrings;
	}
 
	/*
public void relax(DirectedEdge testEdge, double[] distTo, DirectedEdge[] edgeTo) {
		int v = testEdge.from();
		int w = testEdge.to();
		if (distTo[w] > distTo[v] + testEdge.weight) {
			distTo[w] = distTo[v] + testEdge.weight;
			edgeTo[w] = testEdge;
		}
	}

	public void relax(EdgeWeightedDigrapgh graph, int source, double[] distTo, DirectedEdge[] edgeTo) {
		Bag sourceEdge = graph.adj[source];
		for (int i = 0; i < sourceEdge.size(); i++) {
			DirectedEdge testEdge = sourceEdge.get(i);
			int w = testEdge.to();
			if (distTo[w] > distTo[source] + testEdge.weight) {
				distTo[w] = distTo[source] + testEdge.weight;
				edgeTo[w] = testEdge;
			}
		}
	}
	*/ 

	public String toStringDistTo(double[] distTo) {
		DecimalFormat numberFormat = new DecimalFormat("#.########");
		String returnString = "";
		for (int index = 0; index < nodeNum; index++) {
			String currentString = "";
			currentString += "vertexNum: " + index + " Value: " + 
					numberFormat.format(distTo[index])+ "\n";
			returnString += currentString;
		}
		return returnString;
	}

	public String toStringGraph() {
		String returnString = "";
		for (int index = 0; index < nodeNum; index++) {
			Bag currentBag = contestGrapgh.adj[index];
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
		Bag[] adj;

		EdgeWeightedDigrapgh(int nodeNum, int edgeNum) {
			this.nodeNum = nodeNum;
			this.edgeNum = edgeNum;
			adj = new Bag[nodeNum];
			for (int index = 0; index < nodeNum; index++) {
				adj[index] = new Bag();
			}
		}

		void addEdge(DirectedEdge newEdge) {
			int from = newEdge.from();
			int to = newEdge.to();
			adj[from].add(newEdge);
		}
	}

	public static class Bag {
		private edgeNode startNode; // beginning of bag
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

		public void add(DirectedEdge newEdge) { // sort the Bag with ascending weights
			if (size == 0) {
				startNode = new edgeNode(newEdge, null);
				size++;
				return;
			}
			DirectedEdge firstEdge = startNode.currentNode;
			boolean nodeAdded = false;
			if (firstEdge.getWeight() > newEdge.getWeight()) { // newNode has the lowest weighting
				edgeNode oldfirst = startNode;
				startNode = new edgeNode(newEdge, oldfirst);
			} else {
				edgeNode previoudNode = startNode;
				edgeNode nextNode = startNode.getNextNode();
				while (nextNode != null && !nodeAdded) {
					DirectedEdge nextEdge = nextNode.getCurrentNode();
					if (nextEdge.getWeight() > newEdge.getWeight()) { // newNode weight is less
						edgeNode newNode = new edgeNode(newEdge, nextNode);
						previoudNode.nextNode = newNode;
						nodeAdded = true;
					} else {
						previoudNode = nextNode;
						nextNode = nextNode.getNextNode();
					}
				}
				if (nextNode == null) {
					edgeNode newNode = new edgeNode(newEdge, nextNode);
					previoudNode.nextNode = newNode;
				}
			}
			size++;
			return;
		}

		public DirectedEdge get(int index) {
			int counter = 0;
			DirectedEdge returnItem = null;
			if (index >= size) {
				return returnItem;
			}

			edgeNode currentItem = startNode;
			while (counter != index) {
				currentItem = currentItem.nextNode;
				counter++;
			}
			returnItem = currentItem.currentNode;
			return returnItem;
		}
	}

	public static class edgeNode {

		public DirectedEdge currentNode;
		public edgeNode nextNode;

		edgeNode(DirectedEdge currentNode, edgeNode nextNode) {
			this.currentNode = currentNode;
			this.nextNode = nextNode;
		}

		public DirectedEdge getCurrentNode() {
			return currentNode;
		}

		public edgeNode getNextNode() {
			return nextNode;
		}

	}

	public class DirectedEdge {
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

	public static void main(String[] args) throws IOException {
		String filename = "tinyEWD.txt";
		int contestantSpeed = 75;
		CompetitionDijkstra dijkstra = new CompetitionDijkstra(filename, contestantSpeed, contestantSpeed, contestantSpeed);
		System.out.println("Graph input\n" + dijkstra.toStringGraph());
		double[] test = dijkstra.Dijkstra(dijkstra.contestGrapgh, 0);
		System.out.println("Shortest path:\n" + dijkstra.toStringDistTo(test));
	}
}
