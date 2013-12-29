package trip;

import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

import graph.Graph;
import graph.UndirectedGraph;

/** A class for assembling graphs from map data.
 *  @author Andrew Berger */
public class GraphBuilder {
    /** A new graphBuilder on MAPFILENAME reporting errors on ERR.*/
    GraphBuilder(String mapFileName, PrintWriter err) {
        try {
            _map = new Scanner(new FileReader(new File(mapFileName)));
        } catch (FileNotFoundException e) {
            reportError(e);
        }
        Pattern spaces = Pattern.compile("\\s+");
        _map.useDelimiter(spaces);
        _err = err;
        _locations = new HashMap<String, Graph<Place, Route>.Vertex>();
    }

    /** Returns a graph built from our input.*/
    public Graph<Place, Route> buildGraph() {
        int place = 0;
        PlaceBuilder location = null;
        RouteBuilder distance = null;
        UndirectedGraph<Place, Route> g = new UndirectedGraph<Place, Route>();
        try {
            while (_map.hasNext()) {
                if (location != null) {
                    locationFramework(place, location);
                    if (place == 3) {
                        location = null;
                    }
                    place += 1;
                } else if (distance != null) {
                    distFramework(place, distance);
                    if (place == 5) {
                        distance = null;
                    }
                    place += 1;
                } else {
                    place = 0;
                    String target = _map.next();
                    switch (target) {
                    case "L":
                        location = new PlaceBuilder(g);
                        place += 1;
                        break;
                    case "R":
                        distance = new RouteBuilder(g);
                        place += 1;
                        break;
                    default:
                        throw new MapFormatException("L or R expected");
                    }
                }
            }
            if (location != null || distance != null) {
                throw new MapFormatException("EOF while processing entry");
            }
        } catch (MapFormatException e) {
            reportError(e);
        }
        return g;
    }

    /** Builds LOCATION at PLACE.*/
    private void locationFramework(int place, PlaceBuilder location) {
        switch (place) {
        case 1:
            String name = _map.next();
            if (_locations.get(name) == null) {
                location.setName(name);
            } else {
                throw new MapFormatException(name + " has already been added.");
            }
            break;
        case 2:
            if (_map.hasNextFloat()) {
                location.setX(_map.nextFloat());
            } else {
                throw new MapFormatException("Float expected");
            }
            break;
        case 3:
            if (_map.hasNextFloat()) {
                location.setY(_map.nextFloat());
                _locations.put(location.name(), location.build());
                location = null;
            } else {
                throw new MapFormatException("Float expected");
            }
            break;
        default:
            throw new MapFormatException("Something went wrong");
        }
    }

    /** Builds DISTANCE at PLACE. */
    private void distFramework(int place, RouteBuilder distance) {
        ArrayList<String> orientations = new ArrayList<String>();
        orientations.add("NS");
        orientations.add("EW");
        orientations.add("WE");
        orientations.add("SN");
        switch (place) {
        case 1:
            String from = _map.next();
            if (_locations.get(from) != null) {
                distance.setStart(from);
            } else {
                throw new MapFormatException(from
                                             + " is not an existing location");
            }
            break;
        case 2:
            String name = _map.next();
            distance.setName(name);
            break;
        case 3:
            if (_map.hasNextFloat()) {
                distance.setDistance(_map.nextFloat());
            } else {
                throw new MapFormatException("float expected");
            }
            break;
        case 4:
            String direction = _map.next();
            if (orientations.contains(direction)) {
                distance.setDirection(direction);
            } else {
                throw new MapFormatException(direction + " is malformed");
            }
            break;
        case 5:
            String to = _map.next();
            if (_locations.get(to) != null) {
                distance.setEnd(to);
                distance.build();
                distance = null;
            } else {
                throw new MapFormatException(to
                                             + " is not an existing location.");
            }
            break;
        default:

        }
    }

    /** Utility class that builds a place. */
    private class PlaceBuilder {
        /** A new location builder on G. */
        PlaceBuilder(Graph<Place, Route> g) {
            _g = g;
        }

        /** Sets _name to NAME.*/
        public void setName(String name) {
            _name = name;
        }

        /** Sets _x to X.*/
        public void setX(float x) {
            _x = x;
        }

        /** Sets _y to Y.*/
        public void setY(float y) {
            _y = y;
        }

        /** Returns a new VLABEL. */
        public Graph<Place, Route>.Vertex build() {
            Place here = new Place(_name, _x, _y);
            return _g.add(here);
        }

        /** Returns my name. */
        public String name() {
            return _name;
        }

        /** The graph we're building on. */
        private Graph<Place, Route> _g;
        /** The name of this location. */
        private String _name;
        /** The x coordinate. */
        private float _x;
        /** The y coordinate. */
        private float _y;
    }

    /** Utility class for making ELABEL.*/
    private class RouteBuilder {
        /** A new routebuilder for G. */
        RouteBuilder(Graph<Place, Route> g) {
            _g = g;
        }
        /** Set START ing vertex. */
        public void setStart(String start) {
            _from = start;
        }

        /** Set END ing vertex. */
        public void setEnd(String end) {
            _to = end;
        }

        /** Set NAME.*/
        public void setName(String name) {
            _name = name;
        }

        /** Set DIST.*/
        public void setDistance(float dist) {
            _dist = dist;
        }

        /** Set DIR.*/
        public void setDirection(String dir) {
            _direction = dir;
        }

        /** Adds to our graph.*/
        public void build() {
            Route path = new Route(_name, _direction,  _dist);
            Graph<Place, Route>.Edge r =
                _g.add(_locations.get(_from), _locations.get(_to), path);
        }

        /** Our graph. */
        private Graph<Place, Route> _g;
        /** Starting location. */
        private String _from;
        /** Ending location. */
        private String _to;
        /** Route name.*/
        private String _name;
        /** My direction. */
        private String _direction;
        /** Distance of route. */
        private float _dist;
    }


    /** Returns the vertice of that NAME or null if it doesn't exist.*/
    public Graph<Place, Route>.Vertex getVertex(String name) {
        return _locations.get(name);
    }

    /** Send an error message E to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf.
     *  Also calls system.exit(1)*/
    private void reportError(Exception e) {
        if (e.getMessage() != null) {
            _err.print("error ");
            _err.println(e.getMessage());
        } else {
            _err.println("error something bad happened");
        }
    }

    /** A scanner over our map data.*/
    private Scanner _map;
    /** The set of declared locations.*/
    private HashMap<String, Graph<Place, Route>.Vertex> _locations;
    /** The error output. */
    private PrintWriter _err;


}
