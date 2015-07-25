# Routing_Scheme
Routing Scheme for Networks using binary tries and Dijsktra's Algorithm using Fibonacci Heap
Implemented Dijsktra's algorithm using Fibonacci Heap.
Impemented Routing Scheme for networks :
Each router has an IP address and packets are forwarded to the next hop router by longest prefix matching using a binary trie.
For each router R in the network, Dijsktra's is called to obtain shortest path from R to each destination router Y.
To construct the router table for R, for each destination Y, the shortest path from R to Y is examined and the router Z just after R on this
path is determined. This gives a set of pairs <IP address of Y, next-hop router Z>.These pairs are inserted into binary trie.
Finally, a post order traversal is done to remove subtries in which the next hop is the same for all destinations. Thus, multiple destinations having a prefix match and the same next hop will be
grouped together in the trie.

Data Structures Used : Fibonacci Heap, Binary Tries.

To execute, 
For part 1 -> $ssp file_name source_node destination_node.
Output -> The output of this part consists of two lines.
The first line has one integer, the total weight.
The second line shows the path starting with source_node and ending with destination_node
separated by a space between nodes.

For part 2, $routing file_name_1 file_name_2 source_node destination_node.
Output -> The output of this part consists of two lines.
The first line has one integer, the total weight.
The second line shows the path starting with source_node and ending with destination_node. But this
time you need to print the matched prefixes separated by a space between prefixes for each node on
the path.
