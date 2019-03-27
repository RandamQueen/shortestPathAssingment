import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CompetitionTests {
	String tinyEWDText = 
		"0 ->2 0.26\n" + 
		"0 ->4 0.38\n" + 
		"1 ->3 0.29\n" + 		
		"2 ->7 0.38\n" +
		"3 ->6 0.52\n" + 
		"4 ->7 0.37\n" + 
		"4 ->5 0.35\n" +
		"5 ->7 0.35\n" +
		"5 ->4 0.35\n" + 
		"6 ->4 0.93\n"+
		"6 ->0 0.58\n" +	
		"6 ->2 0.4\n" +
		"7 ->3 0.39\n"+
		"7 ->5 0.28\n" ;

    @Test
    public void testDijkstraConstructor() throws IOException {
    		String filename = "tinyEWD.txt"; 
		int contestantSpeed = 75; 
		CompetitionDijkstra dijkstra = new CompetitionDijkstra(filename,contestantSpeed,contestantSpeed,contestantSpeed); 
		String dijkstraGrapghString  = dijkstra.toString(); 
		// add test to see if string is printing correctly 
    }

    @Test
    public void testFWConstructor() throws IOException {
    	String filename = "tinyEWD.txt"; 
		int contestantSpeed = 75; 
		CompetitionFloydWarshall floydWarshall = new CompetitionFloydWarshall(filename,contestantSpeed,contestantSpeed,contestantSpeed); 
    }

    //TODO - more tests
}    
