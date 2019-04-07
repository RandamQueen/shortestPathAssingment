import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

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
 * This class implements the competition using Floyd-Warshall algorithm
 */

public class CompetitionFloydWarshall {
	double contestASpeed;
	double contestBSpeed;
	double contestCSpeed;
	int nodeNum;
	int edgeNum; 
	public double[][] distanceTable; 
	double inf = Double.POSITIVE_INFINITY;
	
    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
     * @throws IOException 
     */
    CompetitionFloydWarshall (String filename, int sA, int sB, int sC) throws IOException{
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
		
		distanceTable = new double[nodeNum][nodeNum]; 
		
		for( int i = 0; i <nodeNum; i++) // initialise toble to inf 
		{ 
			for( int j = 0; j <nodeNum; j++)
			{ 
				distanceTable[i][j] = inf; 
			}
		}
		
		for( int index = 0; index <nodeNum; index++) // sets [index][index] to zero 
		{ 
			distanceTable[index][index] = 0; 
		}
		
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
			}
			else if (nodeNum >= 10) {
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
			weight = weight* 1000;

			distanceTable[startNode][endNode] = weight; 
			fileLine = br.readLine();
		}
		br.close();
	}
    
    /**@return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition(){
    	int timeRequired = -1;
		if (contestASpeed < 50 || contestASpeed > 100 || contestBSpeed < 50 || contestBSpeed > 100
				|| contestCSpeed > 100 || contestCSpeed < 50) {
			return timeRequired;
		}
		
		distanceTable =floydWarshall( distanceTable); 
		
		if( !checkSolutionsisPossible(distanceTable ))
		{ 
			return timeRequired; 
		}
		
		double slowestContestSpeed =contestASpeed; 
		if( contestBSpeed < slowestContestSpeed)
		{ 
			slowestContestSpeed = contestBSpeed; 
		}
		if( contestCSpeed < slowestContestSpeed)
		{ 
			slowestContestSpeed = contestCSpeed; 
		}
		

		double[][] slowestContestTimeChart = createTimeChart(distanceTable,slowestContestSpeed); 
		timeRequired = getSlowestTime(slowestContestTimeChart ); 	
        return timeRequired;
    }
    
	public double[][] floydWarshall(double[][] dist) {
		for (int k = 0; k < nodeNum; k++) {
			for (int i = 0; i < nodeNum; i++) {
				for (int j = 0; j < nodeNum; j++) {
					if (dist[i][j] > dist[i][k] + dist[k][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
					}
				}
			}
		}
		return dist; 
    }
    
    public double [][] createTimeChart(double[][] distBetweenNodeList, double contestSpeed  )
	{ 
		double[][] tempChart =distBetweenNodeList; 
		double[][] timeChart = new double[nodeNum][nodeNum] ; 
		for( int i =0; i < nodeNum; i++) 
		{ 
			for( int j =0; j < nodeNum; j++) 
			{ 
				timeChart[i][j] = tempChart[i][j] / contestSpeed; 
			}
		}
		return timeChart; 
	}

	public int getSlowestTime (  double[][] slowestContestTimeChart)
	{ 
		double slowestTime = slowestContestTimeChart[0][0]; 
		for( int i =0 ; i < nodeNum; i ++)
		{ 
			for( int j =0; j < nodeNum; j ++)
			{ 
				if(slowestContestTimeChart[i][j] > slowestTime  )
				{ 
					slowestTime = slowestContestTimeChart[i][j]; 
				}
			}
		}
		slowestTime++ ;
		int returnTimeInt = (int)  slowestTime; 
		return returnTimeInt; 
	}
    
	
	public boolean checkSolutionsisPossible( double[][] distList)
	{ 
		boolean solutionPossible = true; 
		for( int i = 0; i <nodeNum; i++ )
		{ 
			for( int j =0; j < nodeNum; j++ )
			{ 
				if( distList[i][j]  == inf) 
				{ 
					solutionPossible = false; 
					return solutionPossible; 
				}
			}
		}
		return solutionPossible; 
	}
    
  
	public String toStringDistanceChart(double[][] distList )
	{ 
		DecimalFormat numberFormat = new DecimalFormat("#.##");
		String returnString = "";
		for( int i =0; i < nodeNum; i++ )
		{ 
			returnString +=  "From source node " + i + "\n";
			for( int j= 0; j < nodeNum;j++ ) { 
				String timeString = 
						"	To get to  " + j + ":	" + numberFormat.format(distList[i][j] ) + "\n"; 
				returnString+= timeString; 
			}
		}
		return returnString; 
	}
   
	public static void main(String[] args) throws IOException {
		String filename = "1000EWD.txt";
		CompetitionFloydWarshall floydWarshall = new CompetitionFloydWarshall(filename, 50, 50,50);
		int timeTaken = floydWarshall.timeRequiredforCompetition();
		System.out.print( "Time Taken: " + timeTaken);
	}
}