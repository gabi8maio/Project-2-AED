/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.*;

public class BookishClass extends StudentsClassAbstract implements Bookish, Students {

    private static final long serialVersionUID = 0L;

    private TwoWayList<Services> visitedLeisureServices;

    BookishClass (String type, String name, String country, Services lodging) {
        super(type, name, country, lodging);
        visitedLeisureServices = new DoublyLinkedList<>();
    }

    public void addVisitedService(Services service){
        if(visitedLeisureServices.indexOf(service) == -1)
            visitedLeisureServices.addLast(service);
    }

    public Iterator<Services> getAllVisitedServices(){
        return visitedLeisureServices.iterator();
    }



    public boolean hasVisitedLocations() {
        return !visitedLeisureServices.isEmpty();
    }
}
