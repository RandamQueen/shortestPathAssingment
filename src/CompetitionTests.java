import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * @author Hannah Keating
 *
 */
public class CompetitionTests {
	String tinyEWDText = 
		"0 ->2 0.26\n" + 
		"0 ->4 0.38\n" + 
		"1 ->3 0.29\n" + 		
		"2 ->7 0.34\n" +
		"3 ->6 0.52\n" +
		"4 ->5 0.35\n" +
		"4 ->7 0.37\n" + 
		"5 ->7 0.35\n" +
		"5 ->4 0.35\n" +
		"6 ->2 0.4\n"  +
		"6 ->0 0.58\n" +	
		"6 ->4 0.93\n" +
		"7 ->5 0.28\n" +
		"7 ->3 0.39\n";
		

    @Test
    public void testDijkstraConstructor() throws IOException {
    		String filename = "tinyEWD.txt"; 
		int contestantSpeed = 75; 
		CompetitionDijkstra dijkstra = new CompetitionDijkstra(filename,contestantSpeed,contestantSpeed,contestantSpeed); 
    }
    
    @Test
    public void testTimeRequiredforCompetitionr() throws IOException {
    	String filename = "tinyEWD.txt"; 
		int contestantSpeed = -75; 
		int correctTestVal =-1; 
		CompetitionDijkstra dijkstra = new CompetitionDijkstra(filename,contestantSpeed,contestantSpeed,contestantSpeed); 
		int result =dijkstra.timeRequiredforCompetition(); 
		assertEquals(correctTestVal, result );  // returns -1 if speeds outside out range are passed 
		
		contestantSpeed = 75; 
		correctTestVal =-1; 
		dijkstra = new CompetitionDijkstra(filename,contestantSpeed,contestantSpeed,contestantSpeed); 
    }
    @Test
    public void testFWConstructor() throws IOException {
    	String filename = "tinyEWD.txt"; 
		int contestantSpeed = 75; 
		CompetitionFloydWarshall floydWarshall = new CompetitionFloydWarshall(filename,contestantSpeed,contestantSpeed,contestantSpeed); 
    }

    //TODO - more tests
}    
