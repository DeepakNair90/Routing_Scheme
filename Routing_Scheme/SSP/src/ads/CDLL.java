package ads;

//CIRCULAR DOUBLY LINKED LIST IS DEFINED HERE WITH FIELD START

public class CDLL {

	public Node start;
	public CDLL()
	{
		start=null;
	}

	public void insert(Node newNode) //insert specified node in the concerned circular doubly linked list
	{
		if (start == null)
		{            
			start=newNode;
			newNode.next=newNode;
			newNode.prev=newNode;
		}	
		else			
		{
			Node tempend=start.prev;
			newNode.prev=tempend;
			tempend.next=newNode;
			newNode.next=start;
			start.prev=newNode;

		}
	}


	public Node delete(Node nodeToBeDeleted)  //delete specified node from the concerned circular doubly linked list
	{
		if (start==nodeToBeDeleted && start.next==start)
		{
			start=null;
		}
		else
			if (start==nodeToBeDeleted && start.next!=start)
			{
				Node tempend=start.prev;
				Node f=start.next;
				f.prev=tempend;
				tempend.next=f;
				start=f;		
			}
			else
			{
				nodeToBeDeleted.prev.next=nodeToBeDeleted.next;
				nodeToBeDeleted.next.prev=nodeToBeDeleted.prev;
			}

		return nodeToBeDeleted;
	}		
}
