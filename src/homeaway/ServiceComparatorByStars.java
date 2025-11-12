/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.Comparator;

import java.io.Serializable;

public class ServiceComparatorByStars implements Comparator<Services>, Serializable {

    private static final long serialVersionUID = 0L;

    @Override
    public int compare(Services o1, Services o2) {
        // First compare by stars DESCENDING (higher stars first)
        int starCompare = Integer.compare(o2.getAverageStars(), o1.getAverageStars());
        if (starCompare != 0) return starCompare;

        // By LastUPdated
        return Integer.compare(o1.getLastUpdatedOrder(), o2.getLastUpdatedOrder());
    }
}
