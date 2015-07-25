package ads;


//BASIC NODE STRUCTURE IS DEFINED HERE

public class Node {
	int data;              
	Node prev;
	Node next;

	int degree;
	Node parent;
	CDLL child;
	String childCut;


	int nodeID=-1;
	int predID=-1;

	public Node()
	{
		next = null;
		prev = null;
		data = 0;

		parent=null;
		child=null;
		degree=0;
		childCut="undefined";

	}
	public Node(int id,int d)
	{
		nodeID=id;
		data = d;
		next = null;
		prev = null;

		parent=null;
		child=null;
		degree=0;
		childCut="undefined";
	}
}



