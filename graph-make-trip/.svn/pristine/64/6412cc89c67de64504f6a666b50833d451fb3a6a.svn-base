package trip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.InputStreamReader;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.HashSet;

import graph.Graph;
import graph.Graphs;
import graph.Distancer;

/** Initial class for the 'trip' program.
 *  @author Andrew Berger
 */
public final class Main {

    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;

        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch  (FileNotFoundException e) {
                System.err.printf("error Could not open %s.%n",
                                  requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("error Could not open %s for writing.%n",
                                  outFileName);
                System.exit(1);
            }
        }

        trip(mapFileName);
    }

    /** Print a trip for the request on the standard input to the stsndard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        GraphBuilder mapParser = new GraphBuilder(mapFileName, _err);
        Scanner requests = new Scanner(new InputStreamReader(System.in));
        Graph<Place, Route> map = mapParser.buildGraph();
        List<Graph<Place, Route>.Vertex> destinations;
        try {
            destinations = parseDestinations(map, requests);
            makeTrip(map, destinations);
        } catch (MapFormatException e) {
            _err.println("error" + e.getMessage());
        }
    }

    /** Visits DESTINATIONS on MAP and prints our directions. */
    private static void makeTrip(Graph<Place, Route> map,
                                 List<Graph<Place, Route>.Vertex>
                                 destinations) {
        List<Graph<Place, Route>.Edge> directions = getPath(destinations, map);
        int step = 1;
        Graph<Place, Route>.Edge first = directions.get(0);
        Graph<Place, Route>.Vertex last = destinations.get(0);
        Graph<Place, Route>.Vertex curr;
        _out.printf("From %s:%n%n", last.getLabel().name());
        String endPoint = destinations.get(
                        destinations.size() - 1).getLabel().name();
        destinations = destinations.subList(0, destinations.size() - 1);
        String lastRoad = "";
        String lastDir = "";
        boolean init = true;
        double lastWeight = first.getLabel().weight();
        for (Graph<Place, Route>.Edge e : directions) {
            curr = last;
            last = e.getV(last);
            String currDir = "";
            String currRoad = e.getLabel().name();
            Double currWeight = e.getLabel().weight();
            if (e.getV0() == last) {
                currDir = e.getLabel().compass();
            } else {
                currDir = e.getLabel().compassReverse();
            }
            if (currRoad.equals(lastRoad) && currDir.equals(lastDir)) {
                lastWeight += e.getLabel().weight();
            } else {
                if (!init) {
                    if (destinations.contains(curr)) {
                        int ind = destinations.indexOf(last);
                        String destination =
                            destinations.get(ind).getLabel().name();
                        _out.printf("%d. Take %s %s for %.1f miles to %s.%n",
                                    step, lastRoad, lastDir, lastWeight,
                                    destination);
                    } else {
                        _out.printf("%d. Take %s %s for %.1f miles.%n",
                                step, lastRoad, lastDir, lastWeight);
                    }
                    step += 1;
                }
                init = init ? false : false;
                lastRoad = currRoad;
                lastDir = currDir;
                lastWeight = currWeight;
            }
        }
        _out.printf("%d. Take %s %s for %.1f miles to %s.%n",
                    step, lastRoad, lastDir, lastWeight,
                    endPoint);
        _out.flush();
    }

    /** Returns the shortest path between DESTINATIONS on MAP. */
    private static List<Graph<Place, Route>.Edge> getPath(
                       List<Graph<Place, Route>.Vertex> destinations,
                       Graph<Place, Route> map) {
        List<Graph<Place, Route>.Edge> directions =
            new ArrayList<Graph<Place, Route>.Edge>();
        for (int ind = 1; ind < destinations.size(); ind++) {
            directions.addAll(Graphs.shortestPath(map,
                  destinations.get(ind - 1), destinations.get(ind),
                                                  new Distancer<Place>() {
                        @Override
                        public double dist(Place p1, Place p2) {
                            double d1 = p1.coords()[0] - p2.coords()[0];
                            double d2 = p1.coords()[1] - p2.coords()[1];
                            d1 = Math.pow(d1, 2);
                            d2 = Math.pow(d2, 2);
                            double dotted = d1 + d2;
                            return Math.pow(dotted, .5);
                        }
                    }));
        }
        return directions;
    }



    /** Adds to MAP our REQUESTS, returning list of verts. */
    private static List<Graph<Place, Route>.Vertex>
    parseDestinations(Graph<Place, Route> map, Scanner requests) {
        ArrayList<Graph<Place, Route>.Vertex> destinations =
            new ArrayList<Graph<Place, Route>.Vertex>();
        List<String> requestList = new ArrayList<String>();
        HashSet<String> reqSet = new HashSet<String>();
        while (requests.hasNextLine()) {
            Scanner line = new Scanner(requests.nextLine());
            Pattern commaSpace = Pattern.compile(",\\s+");
            line.useDelimiter(commaSpace);
            while (line.hasNext()) {
                requestList.add(line.next());
            }
        }
        for (String req : requestList) {
            reqSet.add(req);
            destinations.add(null);
        }
        for (Graph<Place, Route>.Vertex v : map.vertices()) {
            String nodeName = v.getLabel().name();
            if (reqSet.contains(nodeName)) {
                int ind = requestList.indexOf(nodeName);
                destinations.set(ind, v);
            }
        }
        for (int i = 0; i < requestList.size(); i++) {
            if (destinations.get(i) == null) {
                throw new MapFormatException("Requests for nodes not in graph");
            }
        }
        return destinations;
    }



    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        _out.println("Usage: java make.Main [ -f MAKEFILE ]"
                     + "[ -D FILEINFO ] [ TARGET ... ]");
        _out.flush();
        System.exit(1);
    }

    /** The standard error output.*/
    private static PrintWriter _err = new PrintWriter(System.err);
    /** The standard output.*/
    private static PrintWriter _out = new PrintWriter(System.out);
}
