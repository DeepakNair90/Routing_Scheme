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

Daat Structures Used : Fibonacci Heap, Binary Tries.
