/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.javaperformance.money;

import java.util.Comparator;

/**
 *
 * @author rex
 */
public class MoneyComparators {
    public static final Comparator<Money> ASCENDING = new AscendingComparator();
    public static final Comparator<Money> DESCENDING = new DescendingComparator();

    private static class AscendingComparator implements Comparator<Money> {

        private AscendingComparator() {
        }

        @Override
        public int compare(Money t1, Money t2) {
            return t1.compareTo(t2);
        }
    }

    private static class DescendingComparator implements Comparator<Money> {

        private DescendingComparator() {
        }
        
        @Override
        public int compare(Money t1, Money t2) {
            return t2.compareTo(t1);
        }
    }

}
