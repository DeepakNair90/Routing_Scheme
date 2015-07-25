//AUTHOR - DEEPAK NAIR UFID:13361231
package ads;
//TRIENODE CLASS WHICH DEFINES NODES OF TRIE BEING BUILT ON ROUTERS
public class TrieNode 
{
	TrieNode left;
	TrieNode right;
	int data;   
	boolean isLeaf;
	TrieNode parent;

	public TrieNode()
	{
		left = null;
		right = null;
		parent=null;
		isLeaf=false;
		data=-1;
	}

	public TrieNode(int value)
	{
		left = null;
		right = null;
		parent=null;
		isLeaf=false;
		data=value;
	}

}