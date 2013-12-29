package make;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

import graph.Graph;
import graph.DirectedGraph;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/** Initial class for the 'make' program.
 *  @author Andrew Berger
 */
public final class Main {

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }

        make(makefileName, fileInfoName, targets);
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makeFileName, String fileInfoName,
                             List<String> targets) {
        try {
            Scanner makeFile = new Scanner(
                           new FileReader(new File(makeFileName)));
            Scanner fileInfo = new Scanner(
                           new FileReader(new File(fileInfoName)));

            GraphBuilder maker = new GraphBuilder(makeFile, fileInfo, _err);
            DirectedGraph<Target, String> makeMap = maker.buildGraph();
            int sysTime = maker.systemTime();
            CycleCheck cycler = new CycleCheck();

            Builder builder =
                new Builder(sysTime, cycler, _out);
            HashMap<String, Graph<Target, String>.Vertex> vertices =
                maker.getVertMap();
            for (String target : targets) {
                Graph<Target, String>.Vertex t = vertices.get(target);
                if (t != null) {
                    make(makeMap, builder, t);
                } else {
                    reportError("Target doesn't have a rule");
                }
            }
        } catch (FileNotFoundException e) {
            reportError(e.getMessage());
        }
    }

    /** Makes TARGET in G using BUILDER.*/
    private static void make(Graph<Target, String> G,
                             Builder builder,
                             Graph<Target, String>.Vertex target) {
        builder.depthFirstTraverse(G, target);
    }


    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        _out.println("Usage: java make.Main [ -f MAKEFILE ]"
                     + " [ -D FILEINFO ] [ TARGET ... ]");
        _out.flush();
        _err.println("error");
        _err.flush();
        System.exit(1);
    }

    /** Send an error message ERR to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf.
     *  Also calls system.exit(1)*/
    private static void reportError(String err) {
        _err.print("Error: ");
        _err.println(err);
        System.exit(1);
    }

    /** The standard error output. */
    private static PrintWriter _err = new PrintWriter(System.err);
    /** The standard output.*/
    private static PrintWriter _out = new PrintWriter(System.out);
}
