package pageranking;

import java.util.Comparator;

public class PageComparator implements Comparator<PageNode> {
    @Override
    public int compare(PageNode lhs, PageNode rhs) {
        return Double.compare(rhs.getPageRank(), lhs.getPageRank());
    }
}
