//AUTHOR - DEEPAK NAIR UFID:13361231

package ads;
import java.util.*;

//Dijkstra for part 2 which finds shortest path to all other nodes.

public class DijkstraForRouting {

	//This method will return the final set of nodes with relaxed distances and the shortest distance
	//Accepts Adjacency list , source and destination
	public HashMap<Integer,Node> findPath(HashMap <Integer,HashMap<Integer,Integer>> adjList,int sourceId,int destId)
	{
		Fibonacci_Heap f1=new Fibonacci_Heap();

		//By this time adj List is ready	
		Node nd;		
		Node ptr;
		int shortestPathDist=Integer.MAX_VALUE;

		//Dijkstra's Algorithm
		nd=new Node(sourceId,0);	
		nd.predID=-1;  //Predecessor ID of source node is kept as '-1'
		f1.insert(nd); //Insert into Fibonacci Heap

		HashMap<Integer,Node> finalSet=new HashMap<Integer,Node>();  //Set S where extracted nodes are put
		int edgeWeight;
		int distNow;
		int exDist;
		int concernedNodeID;

		while(f1.nodeCount!=0)
		{
			nd=f1.removeMin(); //nd is the node under consideration
			finalSet.put(nd.nodeID,nd);    //finalSet is hashmap mapping nodeid to nodes
			if(nd.nodeID==destId)
			{
				shortestPathDist=nd.data;
			}

			for(Map.Entry<Integer,Integer> entry : adjList.get(nd.nodeID).entrySet()) //traversing each node in adjList of nd
			{

				concernedNodeID=entry.getKey();

				ptr=f1.nodeRegister.get(concernedNodeID);

				if (ptr==null)
				{
					if(!finalSet.containsKey(concernedNodeID))
					{
						ptr=new Node(concernedNodeID,adjList.get(nd.nodeID).get(concernedNodeID)+nd.data);
						ptr.predID=nd.nodeID;
						f1.insert(ptr);	
					}
					else
					{
						continue;
					}
				}
				exDist=ptr.data;
				edgeWeight=adjList.get(nd.nodeID).get(concernedNodeID);
				distNow=nd.data+ edgeWeight;	
				if(exDist>distNow)                      //relaxing distance
				{
					f1.decreaseKey(ptr, distNow);	
					ptr.predID=nd.nodeID;

				}		
			}
		}

		//This is just to extract shortest distance from the method calling Dijkstra.
		// '-5' is a dummy node which stores shortest distance
		finalSet.put(-5,new Node(-5,shortestPathDist));
		return finalSet;

		//By this time finalSet is ready
	}		
}



