/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.*;

public interface Outgoing {

    /**
     * Adds a service to the data structure that keeps the visited places by a student
     * @param services - the service visited by the place
     */
    void addVisitedService(Services services);

    /**
     * An iterator of the places that the student visited
     * @return the iterator of the visited places by the student
     */
    Iterator<Services> getAllVisitedServices();

    /**
     * Checks if a certain student has already visited any place
     * @return true - if the student has already visited any location
     * @return false - if the student has not visited any location
     */
    boolean hasVisitedLocation();
}
