import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestantsâ€™
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
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
	 * @param filename:
	 *            A filename containing the details of the city road network
	 * @param sA,
	 *            sB, sC: speeds for 3 contestants
	 * 
	 *            CompetitionDijkstra (String filename, int sA, int sB, int sC) –
	 *            constructor for this class should take the four parameters as
	 *            specified in the input, and create and populate the most
	 *            appropriate data structure in which to hold the city road network
	 *            in this example.
	 * 
	 * 
	 */
	CompetitionDijkstra(String filename, int sA, int sB, int sC) {
		contestASpeed = sA;
		contestBSpeed = sB;
		contestCSpeed = sC;
		
		Scanner inputScanner = new Scanner(filename);
		nodeNum = inputScanner.nextInt();
		edgeNum = inputScanner.nextInt();
		contestGrapgh = new EdgeWeightedDigrapgh(nodeNum,edgeNum);
		
		while (inputScanner.hasNext()) {
			int startNode = inputScanner.nextInt();
			int endNode = inputScanner.nextInt();
			double weight = inputScanner.nextDouble();
			DirectedEdge newEdge = new DirectedEdge(startNode, endNode, weight);
			contestGrapgh.addEdge(newEdge);
		}
		inputScanner.close();
	}

	/**
	 * @return int: minimum minutes that will pass before the three contestants can
	 *         meet
	 */
	public int timeRequiredforCompetition() {

		// TO DO
		return -1;
	}

	
	private static class EdgeWeightedDigrapgh {
		int nodeNum;
		int edgeNum;
		private Bag<DirectedEdge>[] adj;
		private int[] indegree;

		EdgeWeightedDigrapgh(int nodeNum, int edgeNum) {
			this.nodeNum = nodeNum;
			this.edgeNum =edgeNum; 
			this.indegree = new int[nodeNum];
			 adj = (Bag<DirectedEdge>[]) new Bag[nodeNum];
			 for (int index = 0; index < nodeNum; index++)
			 {
				 adj[index] = new Bag<DirectedEdge>(); 
			 }         
		}

		void addEdge(DirectedEdge newEdge) 
		{
			int from = newEdge.from();
	        int to = newEdge.to();
	        adj[from].add(newEdge);
	        indegree[to]++;
		}

	}

	private static class Node<Item> {
		private Item currentNode;
		private Node<Item> nextNode;
	}

	public static class Bag<Item> implements Iterable<Item> {
		private Node<Item> startNode; // beginning of bag
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

		public void add(Item item) {
			Node<Item> oldfirst = startNode;
			startNode = new Node<Item>();
			startNode.currentNode = item;
			startNode.nextNode = oldfirst;
			size++;
		}

		public Iterator<Item> iterator() {
			return new ListIterator(startNode);
		}
	
		private class ListIterator implements Iterator<Item> {
			private Node<Item> current;

			public ListIterator(Node<Item> startNode) {
				current = startNode;
			}

			public boolean hasNext() {
				if( current != null) { 
					return true; 
				}
				return false; 
			}

			public Item next() {
				if (hasNext()) {
					Item item = current.currentNode;
					current = current.nextNode;
					return item;
				} 
				else 
				{
					return null;
				}
			}
		}
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
	}

}
