package homeaway;

import dataStructures.Comparator;

import java.io.Serializable;

public class ServiceComparatorByInsertion implements Comparator<Services>, Serializable {

    @Override
    public int compare(Services o1, Services o2)  {
        // First compare by stars DESCENDING (higher stars first)
        return Integer.compare(o1.getNumOfInsertion(), o2.getNumOfInsertion());
    }
}

