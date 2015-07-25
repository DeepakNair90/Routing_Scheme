//AUTHOR - DEEPAK NAIR UFID:13361231

package ads;
import java.util.*;
public class Fibonacci_Heap {
	public Node minPtr=null;  //Minpointer of fibheap
	public CDLL topLevel=new CDLL(); //Top Level list
	public int nodeCount=0;          //Current number of nodes in Fib Heap
	public HashMap<Integer,Node> nodeRegister=new HashMap<Integer,Node>(); //Register mapping nodeId's to nodes


	//INSERT 
	public void insert(Node nd) //Insert into Fibheap (in toplevel list)
	{
		topLevel.insert(nd);
		if(nd!=null && !(nodeRegister.containsKey(nd.nodeID))) //updating register
		{
			nodeCount++;
			nodeRegister.put(nd.nodeID, nd);
		}

		if(minPtr==null || minPtr.data>nd.data)
		{
			minPtr=nd;     //updating minPtr
		}
	}
	//END OF INSERT



	//REMOVE MIN

	public Node removeMin()  
	{
		Node toBeRemoved = minPtr;      //extracting min
		minPtr=null;
		Node temporary;
		if (toBeRemoved!=null)         //adding all it's children to toplevel list
		{

			CDLL childCDLL=toBeRemoved.child;
			if (childCDLL!=null) 
			{
				Node p=childCDLL.start;
				do
				{
					temporary=p;
					p=p.next;
					temporary=childCDLL.delete(temporary);
					toBeRemoved.degree--;
					topLevel.insert(temporary);  //toplevel insert
					temporary.childCut="undefined";	
					temporary.parent=null;
				}
				while(childCDLL.start!=null);

				toBeRemoved.child=null;	   //making child CDLL to null 
			}
			topLevel.delete(toBeRemoved); //removing min from toplevel list
		}

		//Pairwise Combine starts here

		if(topLevel.start!=null)
		{
			int count=0;
			Node p=topLevel.start;
			do
			{
				count++;
				p=p.next; 
			}
			while (p!= topLevel.start);

			if(count>1)  //executes only if there are atleast 2 nodes in fib heap
			{
				HashMap <Integer,Node> ht=new HashMap <Integer,Node>();  //Tracking table used for combining same degree nodes
				Node q=topLevel.start;
				//minPtr=q;
				Node x,y;
				Node put=q;
				int concernedDegree;

				for (int i=0;i<count;i++)
				{
					concernedDegree=q.degree;
					if (!ht.containsKey(concernedDegree))  //If not present, add in tracking table.Else, combine and add in tracking table
					{
						ht.put(concernedDegree,q);
						q=q.next;
						/*if(minPtr.data>q.data)
						minPtr=q;
						 */
						put=q;
					}
					else
					{
						while (ht.containsKey(concernedDegree))   //Combine until I get rid of same degree nodes
						{
							x=put;
							y=ht.get(concernedDegree);
							q=q.next;
							/*if(minPtr.data>q.data)
							minPtr=q;*/
							i++;

							if (x.data>y.data)   //Make smaller node, a parent of larger node while combining
							{
								//make x child of y 						
								topLevel.delete(x);

								if(y.child==null)
								{
									y.child=new CDLL();
								}

								y.child.insert(x);   //Contents of node are updated after combine step
								y.degree++;
								x.parent=y; 
								x.childCut="false";  
								ht.remove(concernedDegree);
								concernedDegree++;
								put=y;
							}
							else
							{
								//make y child of x 
								topLevel.delete(y);

								if(x.child==null)
								{
									x.child=new CDLL();
								}
								x.child.insert(y);  //Contents of node are updated after combine step
								y.parent=x;
								y.childCut="false";
								x.degree++;
								ht.remove(concernedDegree);
								concernedDegree++;
								put=x;
							} 
						}
						ht.put(concernedDegree,put);	 
						put=q;
					}


				}
			}
		}
		nodeCount--;
		nodeRegister.remove(toBeRemoved.nodeID);

		//updating minPtr after remove min
		if (topLevel.start==null)
		{
			minPtr=null;
		}
		else
		{
			minPtr=topLevel.start;
			Node j=topLevel.start.next;
			while (j!= topLevel.start)
			{
				if(minPtr.data>j.data)
				{
					minPtr=j;
				}
				j=j.next;
			}
		}

		return toBeRemoved;
	}

	//END OF REMOVE MIN


	//DECREASE KEY
	public void decreaseKey(Node node,int key)  
	{
		CDLL concernedCDLL;
		Node toBeinsertedInTop;
		Node concernedNode;
		Node p;

		node.data=key;     //Update value of the concerned node
		if(node.childCut!="undefined")  //Check for root
		{	
			if (node.data<node.parent.data) //Check for fibheap structure violation
			{
				//cut and add to top level list	
				p=node.parent;
				concernedCDLL=p.child;
				toBeinsertedInTop=concernedCDLL.delete(node);
				if (concernedCDLL.start==null)
					p.child=null;

				topLevel.insert(node);  

				//modifying node's and parent's contents
				p.degree-=1;
				node.childCut="undefined";
				node.parent=null;

				//Cascading cut
				while (p.childCut=="true")
				{

					concernedNode=p;
					p=concernedNode.parent;
					//remove and insert in toplevel
					concernedCDLL=p.child;
					toBeinsertedInTop=concernedCDLL.delete(concernedNode);

					if (concernedCDLL.start==null)
						p.child=null;

					topLevel.insert(toBeinsertedInTop);
					//modifying node's and parent's contents
					p.degree-=1;
					concernedNode.childCut="undefined";
					concernedNode.parent=null;

				}  	
				if(p.childCut=="false") //After cascading cut, if false,make it true. If undefined, leave it as it is.
					p.childCut="true";
			}
		}	

		//updating minPtr after decrease key
		if (topLevel.start==null)
		{
			minPtr=null;
		}
		else
		{
			minPtr=topLevel.start;
			Node j=topLevel.start.next;
			while (j!= topLevel.start)
			{
				if(minPtr.data>j.data)
				{
					minPtr=j;
				}
				j=j.next;
			}
		}
	}

	//END OF DECREASE KEY

}

