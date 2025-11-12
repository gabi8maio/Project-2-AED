/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.*;
import java.io.Serializable;

public class studentComparatorByName implements Comparator<Students>, Serializable {

    private static final long serialVersionUID = 0L;

    @Override
    public int compare(Students o1, Students o2) {
        return o1.compareTo(o2);
    }
}
