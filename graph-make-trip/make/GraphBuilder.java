package make;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Comparator;
import java.util.regex.Pattern;

import java.io.PrintWriter;

import graph.DirectedGraph;
import graph.Graph;

/** A class that helps parse and build.
 *  @author Andrew Berger*/
public class GraphBuilder {
    /** A graphBuilder building from MAKEFILE and FILEINFO.
     *  Sends errors to ERR. */
    GraphBuilder(Scanner makeFile, Scanner fileInfo, PrintWriter err) {
        _make = makeFile;
        _info = fileInfo;
        _err = err;
    }


    /** Returns the graph assembled from our scanners.*/
    public DirectedGraph<Target, String> buildGraph() {
        return assembleGraph(inputParser());
    }

    /** Return list of targets with the their fields.*/
    private ArrayList<GraphBuilder.TargetBuilder> inputParser() {
        ArrayList<TargetBuilder> targets =
            new ArrayList<GraphBuilder.TargetBuilder>();
        TargetBuilder target = null;
        int place = 0;
        while (_make.hasNextLine()) {
            String line = _make.nextLine();
            if (matchesRuleFormat(line)) {
                if (target != null) {
                    targets.add(target);
                }
                String[] split = line.split("\\s+");
                target = new TargetBuilder();
                target.setName(split[0].substring(0, split[0].length() - 1));
                target.setDependencies(split);
            } else if (!line.startsWith("#")
                       && !Pattern.matches("\\s*", line)) {
                target.addLine(String.format("%s%n", line));
            }
        }
        targets.add(target);
        return targets;
    }

    /** Returns true iff LINE is a rule header. */
    private boolean matchesRuleFormat(String line) {
        Scanner r = new Scanner(line);
        boolean first = true;
        boolean matches = false;
        while (r.hasNext()) {
            String name = r.next();
            if (name.contains("=")
                || name.contains("#") || name.contains("\\")) {
                return false;
            }
            if (first) {
                if (!name.endsWith(":")) {
                    return false;
                }
                matches = true;
                first = false;
            } else if (name.contains(":")) {
                return false;
            }
        }
        return matches;
    }

    /** Adds TARGETS to our graph.
     *  Orders TARGETS by number of dependents
     *  Returns the graph.*/
    private DirectedGraph<Target, String>
    assembleGraph(ArrayList<GraphBuilder.TargetBuilder> targets) {
        DirectedGraph<Target, String> d = new DirectedGraph<Target, String>();
        Collections.sort(targets, new Comparator<GraphBuilder.TargetBuilder>() {
                public int compare(TargetBuilder t1, TargetBuilder t2) {
                    return t1.dependencies().size() - t2.dependencies().size();
                }
            });
        HashMap<String, GraphBuilder.TargetBuilder> tbuildMap =
            new HashMap<String, GraphBuilder.TargetBuilder>();
        try {
            for (TargetBuilder target : targets) {
                tbuildMap.put(target.name(), target);
                if (_verts.get(target.name()) == null) {
                    Graph<Target, String>.Vertex curr = d.add(target.build());
                    _verts.put(target.name(), curr);
                } else {
                    TargetBuilder existing = tbuildMap.get(target.name());
                    if (existing.emptyRule()) {
                        target.addDependencies(existing.dependencies());
                    } else {
                        throw new MakeFormatException(
                               "Non-empty target already exists.");
                    }
                }
            }
            for (TargetBuilder target : targets) {
                for (String dependent : target.dependencies()) {
                    if (_verts.get(dependent) != null) {
                        d.add(_verts.get(target.name()),
                              _verts.get(dependent), null);
                    } else {
                        TargetBuilder ruleless = new TargetBuilder();
                        ruleless.setName(dependent);
                        _verts.put(dependent, d.add(ruleless.build()));
                        d.add(_verts.get(target.name()),
                              _verts.get(dependent), null);
                    }
                }
            }
        } catch (MakeFormatException e) {
            reportError(e);
        }
        processInfo();
        return d;
    }

    /** Updates target times according to fileInfo. */
    private void processInfo() {
        boolean first = true;
        try {
            while (_info.hasNextLine()) {
                Scanner line = new Scanner(_info.nextLine());
                if (first) {
                    if (line.hasNextInt()) {
                        first = false;
                        _sysTime = line.nextInt();
                        if (line.hasNext()) {
                            throw new
                                MakeFormatException(
                                 "Line should only contain time");
                        }
                    } else {
                        throw new MakeFormatException("Expected time");
                    }
                } else {
                    String name = line.next();
                    Target target;
                    if (_verts.get(name) != null) {
                        target = _verts.get(name).getLabel();
                    } else {
                        throw new MakeFormatException(
                            "Name is not existing target");
                    }
                    if (line.hasNextInt()) {
                        int time = line.nextInt();
                        if (_sysTime <= time) {
                            throw new
                                MakeFormatException(
                                  "Entry in info is older than info file");
                        } else {
                            target.setTime(time);
                            target.exists();
                            if (line.hasNext()) {
                                throw new
                                    MakeFormatException("New line expected");
                            }
                        }
                    } else {
                        throw new MakeFormatException("Int expected");
                    }
                }
            }
        } catch (MakeFormatException e) {
            reportError(e);
        }
    }

    /** Framework for our target VLabels. */
    private class TargetBuilder {
        /** An empty target builder.*/
        TargetBuilder() {
            _rule = "";
        }

        /** Set dependcies to DEP, trimming off first entry.*/
        public void setDependencies(String[] dep) {
            dep = Arrays.copyOfRange(dep, 1, dep.length);
            _dependencies = new ArrayList<String>();
            for (String d : dep) {
                _dependencies.add(d);
            }
        }

        /** Add DEP to our list of dependencies. */
        public void addDependencies(ArrayList<String> dep) {
            for (String d : dep) {
                _dependencies.add(d);
            }
        }


        /** Add LINE to rule.*/
        public void addLine(String line) {
            _rule = _rule + line;
        }

        /** Set NAME.*/
        public void setName(String name) {
            _name = name;
        }

        /** Returns my name. */
        public String name() {
            return _name;
        }

        /** Returns my list of dependencies. */
        public ArrayList<String> dependencies() {
            return _dependencies;
        }

        /** Returns me as a target object. */
        public Target build() {
            return new Target(_name, _rule);
        }

        /** Returns true iff rule is empty. */
        public boolean emptyRule() {
            return _rule.isEmpty();
        }

        /** List of targetBuilder names i depend on.*/
        private ArrayList<String> _dependencies;
        /** Rules I execute.*/
        private String _rule;
        /** My name.*/
        private String _name;

    }

/** Send an error message E to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf.
     *  Also calls system.exit(1)*/
    private void reportError(Exception e) {
        if (e.getMessage() != null) {
            _err.print("Error: ");
            _err.println(e.getMessage());
        } else {
            _err.println("Error: something bad happened");
        }
        System.exit(1);
    }

    /** Returns the system time once graph has been built.*/
    public int systemTime() {
        return _sysTime;
    }

    /** Returns map to vertices. */
    public HashMap<String, Graph<Target, String>.Vertex> getVertMap() {
        return _verts;
    }

    /** The file info. */
    private Scanner _info;
    /** The make file we're parsing. */
    private Scanner _make;
    /** The standard error output.*/
    private PrintWriter _err;
    /** A map to the existing targets. */
    private HashMap<String, Graph<Target, String>.Vertex> _verts =
        new HashMap<String, Graph<Target, String>.Vertex>();
    /** The time of the youngest entry.*/
    private int _sysTime;
}
