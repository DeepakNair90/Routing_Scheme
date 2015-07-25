//AUTHOR - DEEPAK NAIR UFID:13361231
package ads;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

//routing class - PART 2
public class routing {

	public static HashMap <Integer,String> IPmap=new HashMap <Integer,String>(); //Map storing routers and IP
	public static HashMap <Integer,TrieNode> trieMap=new HashMap <Integer,TrieNode>();  // Map storing routers and respective tries
	public static boolean flag=false;

	public static void main(String args[])
	{
		//long startTime = System.nanoTime();	
		//Start building IP mapping table
		String filenameIP=args[1];

		int vertex=0;
		String currInputipStr[]=new String[4];
		String currInputbinpiece="";
		String currInputbin="";
		String currInputbinpieceappend="";
		String zeroes="";

		try 
		{
			BufferedReader br1=new BufferedReader(new FileReader(filenameIP));
			String ipstr=br1.readLine();
			ipstr=ipstr.replace('.',' ');
			currInputipStr=ipstr.split(" ");
			for(int i=0;i<4;i++)
			{
				currInputbinpiece=Integer.toBinaryString(Integer.parseInt(currInputipStr[i]));
				int count=currInputbinpiece.length();
				zeroes="";
				for(int j=0;j<(8-count);j++)
				{
					zeroes+="0";
				}
				currInputbinpieceappend=zeroes+currInputbinpiece;
				currInputbin+=currInputbinpieceappend;
				currInputbinpieceappend="";
				currInputbinpiece="";
			}
			IPmap.put(vertex,currInputbin);        //vertex number and respective 32-bit string of IP Address
			vertex++;

			while ((ipstr = br1.readLine()) != null) 
			{
				if(!ipstr.equals(""))
				{
					currInputbin="";
					ipstr=ipstr.replace('.',' ');
					currInputipStr=ipstr.split(" ");

					for(int i=0;i<4;i++)
					{
						currInputbinpiece=Integer.toBinaryString(Integer.parseInt(currInputipStr[i]));
						int count=currInputbinpiece.length();
						zeroes="";
						for(int j=0;j<(8-count);j++)
						{
							zeroes+="0";
						}
						currInputbinpieceappend=zeroes+currInputbinpiece;
						currInputbin+=currInputbinpieceappend;
						currInputbinpieceappend="";
						currInputbinpiece="";
					}
					IPmap.put(vertex,currInputbin);
					vertex++;
				}
			}

			br1.close();

		}

		catch(Exception e)
		{
			System.out.println("IP File Not found exception");
		}

		//Reading from file and Building Adjacency List	

		String filename=args[0];
		int sourceId=Integer.parseInt(args[2]);
		int destId=Integer.parseInt(args[3]);
		String currInputStr[];
		int currInput[]=new int[3];
		//Adjacency list is a hashmap of hashmap
		HashMap <Integer,HashMap<Integer,Integer>> adjList=new HashMap <Integer,HashMap<Integer,Integer>>();  
		HashMap<Integer,Integer> temphm;

		try 
		{
			BufferedReader br=new BufferedReader(new FileReader(filename));
			String line = br.readLine(); 			//First Line that gives no. of vertices and edges
			while ((line = br.readLine())!= null) 
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

		DijkstraForRouting d=new DijkstraForRouting();

		//By this time adj List is ready	
		HashMap<Integer,Node> finalSet= d.findPath(adjList,sourceId,destId); //find shortest distance
		int shortestDist=Integer.MAX_VALUE;
		LinkedList<Integer> path=new LinkedList<Integer> ();
		int currNodeId;

		if(finalSet.containsKey(destId))
		{
			shortestDist=finalSet.get(-5).data;
			flag=true;
		}
		if(flag)                 //if path exists then continue else print nothing
		{
			ArrayList<Integer> trieTable=new ArrayList<Integer>();
			ArrayList<Integer> dests=new ArrayList<Integer>();
			ArrayList<Integer> nextHop=new ArrayList<Integer>();
			int source;
			int destcount=0;
			for (int i=0;i<vertex;i++)
			{
				trieTable.add(i);
			}

			// for each router, initialize destinations and nexthop list used for building trie
			for (int triecount=0;triecount<trieTable.size();triecount++)  
			{
				dests.clear();
				nextHop.clear();
				destcount=0;

				source=trieTable.get(triecount);

				//initializing destinations
				while(destcount<vertex)  //initialize destinations (every router other than source)
				{
					if(destcount!=source)
						dests.add(destcount);
					destcount++;

				}    			

				//initializing nextHop
				finalSet= d.findPath(adjList,source,destId);  //calling Dijkstra for each router to get shortest path to all other nodes
				path=new LinkedList<Integer>();

				for (int j=0;j<dests.size();j++)
				{
					path.addFirst(dests.get(j));
					currNodeId=finalSet.get(dests.get(j)).predID;
					while(currNodeId!=source)
					{
						path.addFirst(currNodeId);
						currNodeId=finalSet.get(currNodeId).predID;

					}
					path.addFirst(sourceId);
					nextHop.add(path.get(1));              //initialize nexthop for each destination
					path.clear();
				}

				//Building trie for source - path will be dest IPs and value at leaf will be nextHop
				TrieNode t= new TrieNode(); //root for source trie

				//build trie and put it in hashmap
				buildTrie(t,source,dests,nextHop); //this will call postorder compress and compressed trie will be put in map

			}

			//By this time trieMap is ready with compressed tries

			// Traversal Logic 
			int concernedTrieNum=sourceId;		
			String traverseStr=IPmap.get(destId);
			//System.out.println("traverseStr=>"+traverseStr);
			TrieNode concernedTrie;
			String prefixPath="";
			char toAppend;
			int i=0;

			while(concernedTrieNum!=destId)  //traverse until destination
			{
				concernedTrie=trieMap.get(concernedTrieNum);
				i=0;
				while(concernedTrie.isLeaf==false)
				{
					toAppend=traverseStr.charAt(i);  //building prefix list
					prefixPath+=toAppend;
					if(toAppend=='0')
						concernedTrie=concernedTrie.left;
					else
						concernedTrie=concernedTrie.right;
					i++;
				}
				prefixPath+=" ";
				concernedTrieNum=concernedTrie.data;
				concernedTrie=null;
			}		
			System.out.println(shortestDist);  //print shortest distance on first line
			System.out.println(prefixPath);    //print path prefixes on second line
		}
	}

	//BUILD TRIE WHICH CALLS POST ORDER COMPRESS
	public static void buildTrie(TrieNode t,int source,ArrayList<Integer> dests, ArrayList<Integer> nextHop)
	{
		int value=0;
		TrieNode p,q;
		for (int i=0;i<dests.size();i++)
		{
			//for each dests node, build trie based on ip and store value as nextHop
			String ip=IPmap.get(dests.get(i));
			int j=0;
			//extract each character and grow tree			    	         
			q=null;
			p=t;

			//Building Trie
			while(p!=null)      //till p!=null traverse and reach a point where values needs to be inserted
			{
				value=Integer.parseInt(Character.toString(ip.charAt(j)));	//traverse until fall off from trie then, start creating new nodes
				q=p;
				if(value==0)
					p=p.left;
				else
					p=p.right;
				j++;

			}
			p=new TrieNode(value);
			p.parent=q;
			if(value==0)
				q.left=p;
			else
				q.right=p;

			while(j<ip.length())             //after falling off the trie, construct new nodes
			{
				value=Integer.parseInt(Character.toString(ip.charAt(j)));
				q=p;
				if(value==0)
				{
					p=p.left;
					p=new TrieNode(value);
					p.parent=q;
					q.left=p;
				}
				else
				{
					p=p.right;
					p=new TrieNode(value);
					p.parent=q;
					q.right=p;

				}
				j++;
			}

			//Last value replace by nextHop and make it a leaf
			p.data=nextHop.get(i);
			p.isLeaf=true;
		}


		//compress here and then put in trieMap
		postOrderCompress(t); //which compresses the trie

		//insert compressed version
		trieMap.put(source,t);
	}

	//END OF BUILD TRIE

	//POST ORDER COMPRESS
	public static void postOrderCompress(TrieNode t)
	{
		if(t!=null)
		{
			postOrderCompress(t.left);
			postOrderCompress(t.right);


			//This is root.Compression logic here
			if(t.data!=-1)
			{
				if(t.left==null || t.right==null)   // If either left or right is null
				{
					if(t.left!=null && t.left.isLeaf==true)     //if left is not null and and a leaf, compress it
					{
						t.left.parent=t.parent;
						if(t==t.parent.left)
							t.parent.left=t.left;
						else
							t.parent.right=t.left;
					}
					else
					{
						if(t.right!=null && t.right.isLeaf==true)   //if right is not null and and a leaf, compress it
						{
							t.right.parent=t.parent;
							if(t==t.parent.left)
								t.parent.left=t.right;
							else
								t.parent.right=t.right;

						}
					}
				}

				else
				{
					//If both left and right are not null and both are leaves, compare.If same, compress.

					if(t.left.isLeaf==true && t.right.isLeaf==true)
					{
						if(t.left.data==t.right.data)
						{
							t.left.parent=t.parent;
							if(t==t.parent.left)
								t.parent.left=t.left;
							else
								t.parent.right=t.left;
						}

					}
				}
			}
		}
	}

	//END OF POST ORDER COMPRESS

}

//end of routing class






