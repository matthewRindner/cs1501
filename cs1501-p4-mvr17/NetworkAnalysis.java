import java.io.* ;
import java.util.*;

public class NetworkAnalysis{

	static EdgeWeightedDigraph graph;
	static EdgeWeightedGraph copperGraph;

	static EdgeWeightedDigraph bandwidthGraph;
	static HashMap<Integer, Integer> reindex_hashmap;	//vertex to graphs size


	public static void main(String[] args) throws Exception
	{
		//read network information from file
		BufferedReader fileInput = new BufferedReader(new FileReader(args[0]));
		Scanner input = new Scanner(System.in);
		
		int verticesNum = Integer.parseInt(fileInput.readLine());
		//int[] validVertices = new int[verticesNum*2];
		//int i = 0; 
		graph = new EdgeWeightedDigraph(verticesNum);
		copperGraph = new EdgeWeightedGraph(verticesNum);
		bandwidthGraph = new EdgeWeightedDigraph(verticesNum);

		while(fileInput.ready())
		{

			String edgeSpec_line = fileInput.readLine();
			//System.out.println("READING:" +edgeSpec_line);
			String[] attribute = edgeSpec_line.split(" ");

			int v = Integer.parseInt(attribute[0]);
			int w = Integer.parseInt(attribute[1]);
			String wireType = attribute[2];
			int bandwidth = Integer.parseInt(attribute[3]);
			int wireLength = Integer.parseInt(attribute[4]);

			if(attribute[2].equals("copper"))
			{
				double weight = wireLength/230000000.0;
				copperGraph.addEdge(new Edge(v, w, weight, wireType));
				graph.addEdge(new DirectedEdge(v, w, weight, wireType, bandwidth));
				graph.addEdge(new DirectedEdge(w, v, weight, wireType, bandwidth));
			}
			else if (attribute[2].equals("optical"))
			{
				double weight = wireLength/200000000.0;
				//System.out.println(weight);
				//copperGraph.addEdge(new DirectedEdge(-1, -1, -1, null));
				graph.addEdge(new DirectedEdge(v, w, weight, wireType, bandwidth));
				graph.addEdge(new DirectedEdge(w, v, weight, wireType, bandwidth));
			}
			bandwidthGraph.addEdge(new DirectedEdge(v, w, bandwidth, wireType, bandwidth));
			bandwidthGraph.addEdge(new DirectedEdge(w, v, bandwidth, wireType, bandwidth));

			// validVertices[i] = Integer.parseInt(attribute[0]);
			// validVertices[i+1] = Integer.parseInt(attribute[1]);

			
		}
		//System.out.println(copperGraph);
		//System.out.println(graph);
		System.out.println("All edges added successfully");

		int optionNum;

		while(true)
		{
			//implemnt UI
			System.out.println("--------------------Network Analysis Program--------------------");
			System.out.println("Enter in a number to perform each option");
			System.out.println("1. Get lowest latency path");
			System.out.println("2. Determine if graph is copper-only connect");
			System.out.println("3. Determine if graph would remain connected given any 2 vertices fail");
			System.out.println("4. Exit");

			optionNum = input.nextInt();

			if(optionNum == 1)
			{
				//prompt user for the 2 vertices
				System.out.println("Enter first vertex as an int");
				int firstVertex = Integer.parseInt(input.next());
				System.out.println("Enter second vertex as an int");
				int secondVertex = Integer.parseInt(input.next());

				getLowestlantecy(firstVertex, secondVertex);
				
			}

			else if(optionNum == 2)
			{
				System.out.println("the graph is copper-only connected: " + copperConnected());
			}
			
			else if(optionNum == 3)
			{
				System.out.println("the graph would reamin connected even if any 2 vertices fail: ");
				giveMe100();
			}

			else if(optionNum == 4) 
				{ 
					System.exit(0);
				}

			else 
			{
				System.out.println("Invalied option");
			}

		}

		//the rest of the acutual logic for the project

			//1 use dijlstra to get the shortest path from v to w for the latency
			//account for time to travel each link = cable length/cable speed
				//copper = 230 million m/s
				//fiber optics = 200 million m/s
			//output that path
		

			//2 dertermine if graph is only copper connected
				//prob use one either BFS or DFS to search though graph
					//if find one fiber optic, return false
					//if make it to the end, return true
		//or
				//make subgraph of copper only edges 
				//use the marked array to check if marked or not
		
	}

	public static boolean copperConnected()
		{
			boolean result = true;
			DepthFirstSearch dfs = new DepthFirstSearch(copperGraph, 0);

			
				for(int i = 0; i < copperGraph.V(); i++)
				{
					if(dfs.marked(i))
						continue;
					else result = false;
				}
			
			return result;
		}

	public static void getLowestlantecy(int v1, int v2)
		{
			DijkstraAllPairsSP  dijkstraGraph = new DijkstraAllPairsSP(graph);
			int min_band = 1000000;
			if(v1 != v2)
			{
					
					if(dijkstraGraph.hasPath(v1, v2))
					{
						String shortestPath = dijkstraGraph.path(v1, v2).toString();

						for(DirectedEdge e : dijkstraGraph.path(v1, v2))
						{
							min_band = Math.min(min_band, e.band());

						}
						System.out.println("the shortest path from " + v1 + " to " + v2 + " is");
						System.out.println(shortestPath);
						System.out.println("Minimum bandwidth was: " + min_band);
						//System.out.println(e.bandwidthToString());
					}
					
				
			}
			else System.out.println("The vertices must be different");
			
			
		}

	
	public static void giveMe100() //remainConnectedGiven2VertexFails()
		{
			//map integers to the size of the graph
			int size = graph.V()-1;
			reindex_hashmap = new HashMap<Integer, Integer>(size);
			EdgeWeightedDigraph subgraph = new EdgeWeightedDigraph(size);
			int num_notConnected = 0;

			for(int i = 0; i < graph.V(); i++)	//first vertex we want to remove
			{
				reindex_hashmap = new HashMap<Integer, Integer>(size);
				subgraph = new EdgeWeightedDigraph(size);
				for(int j = 0; j < graph.V(); j++)	//second vertex we want to remove
				{
					if(i != j)
					{
						//remapping the hasmap with size 0 to size
						reindex_hashmap.put(j, reindex_hashmap.size());
						//contains valid/concidered verteces will ignoring a single vertice
					}
					

				}

				for(DirectedEdge e : graph.edges())
				{
					if(i != e.to() && i != e.from()) //final check that i(vertex removed) is not connect by others thus not in the graph
					{
						int to = reindex_hashmap.get(e.to()); //returns value of edges, can we make it from v to w for each vertex in graph
						int from = reindex_hashmap.get(e.from());
						subgraph.addEdge(new DirectedEdge(to, from));	//adds verticies except one we want to ignore
					}
				}

				Biconnected check = new Biconnected(subgraph);	//articulation piont check
				//.AP(subgraph);
				if(check.hasAP()) num_notConnected++;		//if more than one, graph fails

			}
			if(num_notConnected > 0)
			{
				System.out.println("cannot reamin connected");
			}
			else System.out.println("remains connected");
	
		}
		
}