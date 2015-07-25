//AUTHOR - DEEPAK NAIR UFID:13361231
package ads;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

//ssp class - PART 1
public class ssp {

	public static void main(String args[])
	{
		//long startTime = System.nanoTime();
		//Reading from file and Building Adjacency List		
		String filename=args[0];
		int sourceId=Integer.parseInt(args[1]);
		int destId=Integer.parseInt(args[2]);
		String currInputStr[];
		int currInput[]=new int[3];
		//Adjacency list is a hashmap of hashmap
		HashMap <Integer,HashMap<Integer,Integer>> adjList=new HashMap <Integer,HashMap<Integer,Integer>>();  
		HashMap<Integer,Integer> temphm;

		try 
		{
			BufferedReader br=new BufferedReader(new FileReader(filename));
			String line = br.readLine(); 			//First Line that gives no. of vertices and edges
			while ((line=br.readLine())!= null) 
			{
				if(!line.equals(""))
				{      
					//from second line onwards                              
					//Creating adjacency list

					currInputStr=line.split(" ");

					for (int i=0;i<3;i++)
						currInput[i]=Integer.parseInt(currInputStr[i]);


					if (adjList.containsKey(currInput[0])) 
						(adjList.get(currInput[0])).put(currInput[1],currInput[2]);  //building adjList
					else
					{
						temphm=new HashMap<Integer,Integer>();
						temphm.put(currInput[1],currInput[2]);
						adjList.put(currInput[0],temphm);
					}

					if (adjList.containsKey(currInput[1])) 
						(adjList.get(currInput[1])).put(currInput[0],currInput[2]);  
					else
					{
						temphm=new HashMap<Integer,Integer>();
						temphm.put(currInput[0],currInput[2]);
						adjList.put(currInput[1],temphm);
					}                 

				}
			}
			br.close();
		}

		catch(Exception e)
		{
			System.out.println("File Not found exception");
		}
		//By this time adj List is ready	
		Dijkstra d=new Dijkstra();
		HashMap<Integer,Node> finalSet = d.findPath(adjList,sourceId,destId); //calling Dijkstra to get shortest distance and shortest path

		//Predecessor tracing
		LinkedList<Integer> path=new LinkedList<Integer>();
		path.addFirst(destId);
		if(finalSet.containsKey(destId))
		{
			int currNodeId=finalSet.get(destId).predID;
			while(currNodeId!=sourceId)
			{
				path.addFirst(currNodeId);
				currNodeId=finalSet.get(currNodeId).predID;

			}
			path.addFirst(sourceId);

			//Display result
			int shortestPathDist=finalSet.get(-5).data; //printing final distance (stored in a dummy node with ID '-5');
			System.out.println(shortestPathDist);			
			for (int i=0;i<path.size();i++)
				System.out.print(path.get(i)+" ");	  //printing path
		}
		//long endTime = System.nanoTime();	
		//System.out.println("Took"+(endTime-startTime));

	}
}
