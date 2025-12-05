/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.*;


public class BookishClass extends StudentsClassAbstract implements Bookish, Students {

    private static final long serialVersionUID = 0L;

    private final TwoWayList<Services> visitedLeisureServices;

    /**
     * The class of Bookish students
     * @param type the type of student (bookish)
     * @param name the name of the student
     * @param country the country of the student
     * @param lodging the lodging service where the student is sleeping
     */
    BookishClass (String type, String name, String country, Services lodging) {
        super(type, name, country, lodging);
        visitedLeisureServices = new DoublyLinkedList<>();
    }

    @Override
    public void addVisitedService(Services service){
        if(visitedLeisureServices.indexOf(service) == -1)
            visitedLeisureServices.addLast(service);
    }

    @Override
    public Iterator<Services> getAllVisitedServices(){
        return visitedLeisureServices.iterator();
    }


    @Override
    public boolean hasVisitedLocations() {
        return !visitedLeisureServices.isEmpty();
    }
}
