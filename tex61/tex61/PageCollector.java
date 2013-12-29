package tex61;

import java.util.List;

/** A PageAssembler that collects its lines into a designated List.
 *  @author Andrew Berger
 */
class PageCollector extends PageAssembler {

    /** A new PageCollector that stores lines in OUT. */
    PageCollector(List<String> out) {
        _out = out;
    }

    /** Add LINE to my List. */
    @Override
    void write(String line) {
        _out.add(line);
    }

    /** The field holding THIS output. */
    private List<String> _out;
}
