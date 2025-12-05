/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;


import dataStructures.DoublyLinkedList;

import java.io.Serializable;
import java.util.LinkedList;

import dataStructures.*;

public class OutgoingClass extends StudentsClassAbstract implements Outgoing, Students{

    private final TwoWayList<Services> allVisitedServices;

    private static final long serialVersionUID = 0L;

    /**
     * The class of outgoing students
     * @param type the type of student (outgoing)
     * @param name the name of the student
     * @param country the country of the student
     * @param lodging the lodging service
     */
    OutgoingClass(String type, String name, String country, Services lodging) {
        super(type, name, country, lodging);
        allVisitedServices = new DoublyLinkedList<>();
        allVisitedServices.addLast(lodging);
    }

    @Override
    public void addVisitedService(Services service){
        if(allVisitedServices.indexOf(service) == -1)
            allVisitedServices.addLast(service);
    }

    @Override
    public Iterator<Services> getAllVisitedServices(){
        return allVisitedServices.iterator();
    }

    @Override
    public boolean hasVisitedLocation() {
        return !allVisitedServices.isEmpty();
    }
}
