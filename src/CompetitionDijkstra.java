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

/**
 * @author Hannah Keating
 *
 */

public class CompetitionDijkstra {
	double contestASpeed;
	double contestBSpeed;
	double contestCSpeed;
	int nodeNum;
	int edgeNum;
	EdgeWeightedDigrapgh contestGrapgh;

	/**
	 * @param filename:
	 *            A filename containing the details of the city road network
	 * @param sA,
	 *            sB, sC: speeds for 3 contestants
	 * @throws IOException
	 * 
	 *             Purpose: Constructor
	 */
	CompetitionDijkstra(String filename, int sA, int sB, int sC) throws IOException {
		contestASpeed = sA;
		contestBSpeed = sB;
		contestCSpeed = sC;
		processTextFile(filename);
	}

	/**
	 * @param filename:
	 *            A filename containing the details of the city road network
	 * @throws IOException
	 * 
	 *             Purpose: This function process the text files and creates the
	 *             contestGraph and adds the edges to this graph.
	 */

	public void processTextFile(String filename) throws IOException {
		File file = new File(filename);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String fileLine = br.readLine();

		nodeNum = Integer.parseInt(fileLine);
		fileLine = br.readLine();
		edgeNum = Integer.parseInt(fileLine);

		contestGrapgh = new EdgeWeightedDigrapgh(nodeNum);

		fileLine = br.readLine();
		while (fileLine != null) {
			if (nodeNum >= 100) {
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
			} else if (nodeNum >= 10) {
				if (fileLine.charAt(3) == ' ') // this means the second num is single digit
				{
					String tempString = fileLine.substring(4);
					fileLine = fileLine.substring(0, 2);
					fileLine += tempString;
				}
				if (fileLine.charAt(0) == ' ') // this means the first num is single digit
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
	 * 
	 *         Purpose: Calculates minimum minutes.
	 */
	public int timeRequiredforCompetition() {
		int timeRequired = -1;
		if (contestASpeed < 50 || contestASpeed > 100 || contestBSpeed < 50 || contestBSpeed > 100
				|| contestCSpeed > 100 || contestCSpeed < 50) {
			return timeRequired;
		}

		double[][] nodeDistList = new double[nodeNum][nodeNum];
		for (int x = 0; x < nodeNum; x++) {
			for (int y = 0; y < nodeNum; y++) { // sets the values to -1
				nodeDistList[x][y] = -1;
			}
		}

		for (int i = 0; i < nodeNum; i++) { // gets shortest path for all elements in nodeDistLis
			double[] sourceDistList = Dijkstra(contestGrapgh, i);
			for (int temp = 0; temp < nodeNum; temp++) {
				double distFromSource = sourceDistList[temp];
				nodeDistList[i][temp] = distFromSource;
			}
		}

		if (!isSolutionPossible(nodeDistList)) {
			return timeRequired;
		}

		double slowestContestSpeed = contestASpeed;
		if (contestBSpeed < slowestContestSpeed) {
			slowestContestSpeed = contestBSpeed;
		}
		if (contestCSpeed < slowestContestSpeed) {
			slowestContestSpeed = contestCSpeed;
		}

		double[][] slowestTimeChart = createTimeChart(nodeDistList, slowestContestSpeed);
		timeRequired = getSlowestTime(slowestTimeChart);
		return timeRequired;
	}

	/**
	 * @param contestGrapgh:
	 *            The EWG used to represent the city.
	 * @param sourceNodeIndex:
	 *            The index of the starting noe
	 * @return A Doubel array of the distance from the start node to each other node
	 * 
	 *         Purpose: Calculates the distance between a given node and the other
	 *         nodes in the graph.
	 */
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
				relaxEdge(sourceEdge, currentTestNode, distTo, edgeTo);
				marked[currentTestNode] = true;
			}
			testOrder.remove(0);
		}
		for (int index = 0; index < nodeNum; index++)// convert to metres
		{
			distTo[index] = distTo[index] * 1000;
		}
		return distTo;
	}

	/**
	 * @param sourceEdge:
	 *            The bag object that contains the edges in which the sort starts at
	 *            a certain node
	 * @return An ArrayList of Strings of the nodes that the source nodes has edges
	 *         coming out of
	 * 
	 *         Purpose: Generates a list of nodes that are connected to the source
	 *         edge. They are sorted by weighted. Using for priority
	 */

	public ArrayList<String> getChildValues(Bag sourceBag) {
		ArrayList<String> returnStrings = new ArrayList<String>();
		for (int index = 0; index < sourceBag.size(); index++) {
			DirectedEdge currentNode = sourceBag.get(index);
			String childVetrice = Integer.toString(currentNode.to());
			returnStrings.add(childVetrice);
		}
		return returnStrings;
	}

	/**
	 * * @param sourceEdge: The bag object that contains the edges in which the sort
	 * starts at a certain node
	 * 
	 * @param edgeIndex:
	 *            The Index value of source node
	 * @param distTo:
	 *            A double array of the short distance from the source to all other
	 *            nodes.
	 * @param edgeTo:
	 *            A DirectedEdge array, which records the previous node traveled to
	 *            to get to the node at each index location Purpose: Relax the edge
	 *            between source and the edge at the given index
	 */
	public void relaxEdge(Bag sourceBag, int sourceIndex, double[] distTo, DirectedEdge[] edgeTo) {
		for (int index = 0; index < sourceBag.size(); index++) {
			DirectedEdge testEdge = sourceBag.get(index);
			int w = testEdge.to();
			double currentWeight = distTo[w];
			double newWeight = distTo[sourceIndex] + testEdge.weight;

			if (currentWeight == -1) {
				distTo[w] = newWeight;
				edgeTo[w] = testEdge;
			} else if (currentWeight > newWeight) {
				distTo[w] = newWeight;
				edgeTo[w] = testEdge;
			}
		}
	}

	/**
	 * * @param dist: A 2D double array of the distances in meters
	 * 
	 * @return A boolean that tells us if there is a possible path to each node from
	 *         each node
	 * 
	 *         Purpose: Determines if it's possible that each node has a path to it
	 *         from every other node in the graph. If so, returns true. If there's
	 *         no path, return false.
	 * 
	 */
	public boolean isSolutionPossible(double[][] dist) {
		boolean solutionPossible = true;
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (dist[i][j] == -1) {
					solutionPossible = false;
					return solutionPossible;
				}
			}
		}
		return solutionPossible;
	}

	/**
	 * @param dist:
	 *            A 2D double array of the distances in meters
	 * @param contestSpeed:
	 *            The speed of the contests in meters a minute
	 * @return A 2D double array of the time in minutes between nodes
	 * 
	 *         Purpose: Creates a chart of the time in minutes it takes to get to
	 *         each node
	 */
	public double[][] createTimeChart(double[][] distBetweenNodeList, double contestSpeed) {
		double[][] tempChart = distBetweenNodeList;
		double[][] timeChart = new double[nodeNum][nodeNum];
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				timeChart[i][j] = tempChart[i][j] / contestSpeed;
			}
		}
		return timeChart;
	}

	/**
	 * @param slowestContestTimeChart:
	 *            A 2D double array of the time it takes the slowest persons to walk
	 *            to any node.
	 * @return int of maximum time taken
	 * 
	 *         Purpose: Finds the largest period of time that the slowest person
	 *         will to walk to any point in the city.
	 */
	public int getSlowestTime(double[][] slowestContestTimeChart) {
		double slowestTime = slowestContestTimeChart[0][0];
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (slowestContestTimeChart[i][j] > slowestTime) {
					slowestTime = slowestContestTimeChart[i][j];
				}
			}
		}
		slowestTime++;
		int returnTimeInt = (int) slowestTime;
		return returnTimeInt;
	}

	// functioned used to process data toStrings

	/**
	 * @param dist:
	 *            A 2D array of the distances in meter from a source node to the
	 *            other nodes
	 * @return String: Detailing the distance betweena source node to the other
	 *         nodes
	 * 
	 *         Purpose: Creates a printable verion of a array, that tell us the
	 *         distance between a source node and the other nodes
	 * 
	 *         Used for testing
	 */

	public String toStringDistTo(double[] distTo) {
		DecimalFormat numberFormat = new DecimalFormat("#.########");
		String returnString = "";
		for (int index = 0; index < nodeNum; index++) {
			String currentString = "";
			currentString += "	vertexNum: " + index + " Value: " + numberFormat.format(distTo[index]) + "\n";
			returnString += currentString;
		}
		return returnString;
	}

	/**
	 * @param timeChart:
	 *            A 2D array of the time in minutes it takes to get from one node to
	 *            another.
	 * @return String: Detailing the time taken to get to node node from another
	 * 
	 *         Purpose: Creates a printable verion of the 2D array, that tell us the
	 *         time taken o get to nodes.
	 * 
	 *         Used for testing
	 */

	public String toStringTimeChart(double[][] timeChart) {
		DecimalFormat numberFormat = new DecimalFormat("#.##");
		String returnString = "";
		for (int k = 0; k < nodeNum; k++) {
			returnString += "Time in minutes to get to " + k + "\n";
			for (int l = 0; l < nodeNum; l++) {
				String timeString = "	From Source Node " + l + ":	" + numberFormat.format(timeChart[k][l]) + "\n";
				returnString += timeString;
			}
		}
		return returnString;
	}

	/**
	 * @return String: Detailing the graph
	 * 
	 *         Purpose: Creates a printable version of the graph, telling us the To,
	 *         From and weight of edges
	 * 
	 *         Used for testing
	 */

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
	// As such I didn't go through comments, unless it was a function that I added.
	private static class EdgeWeightedDigrapgh {

		Bag[] adj;

		EdgeWeightedDigrapgh(int nodeNum) {
			adj = new Bag[nodeNum];
			for (int index = 0; index < nodeNum; index++) {
				adj[index] = new Bag();
			}
		}

		void addEdge(DirectedEdge newEdge) {
			int from = newEdge.from();
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
}
