/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.DoublyLinkedList;
import dataStructures.Iterator;
import dataStructures.TwoWayIterator;
import dataStructures.TwoWayList;

public abstract class ServicesClassAbstract implements Services, ServicesChange{

    private final long latitude;
    private final long longitude;
    private final String serviceName;
    private final int value;
    private int lastUpdateCounter;
    private int averageStars;
    private int totalStars;
    private int rating;
    int studentsThereLodging;
    int numOfServiceInsertion; // The number when was inserted;

    public TwoWayList<Students> studentsThere;
    private final TwoWayList<String> tags;

    private static final long serialVersionUID = 0L;

    public ServicesClassAbstract(long latitude, long longitude, int value, String serviceName) {
        this.serviceName = serviceName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.value = value;
        tags = new DoublyLinkedList<>();
        studentsThere = new DoublyLinkedList<>();
        studentsThereLodging = 0;
        averageStars = 4;
        rating = 1;
        totalStars = 4;

    }
    @Override
    public int getValue(){
        return value;
    }
    @Override
    public void addStudentsThere(Students newStudent){
        studentsThere.addLast(newStudent);
    }
    @Override
    public void removeStudentsThere(Students student){
        int position = studentsThere.indexOf(student);
        if (position != -1) {
            studentsThere.remove(position);
        }
    }
    @Override
    public void addStudentsThereLodging(){
        studentsThereLodging++;
    }
    @Override
    public void removeStudentsThereLodging(){
        studentsThereLodging--;
    }
    @Override
    public String isFull(){
        if(getServiceType().equalsIgnoreCase(TypesOfService.LODGING.toString()))
            if(getValue() == studentsThereLodging) return getServiceName();

        if(getValue() == studentsThere.size()) return getServiceName();
        return null;
    }

    @Override
    public boolean isThereAnyStudents() {
        return !studentsThere.isEmpty();
    }

    @Override
    public int compareTo(Services o) {
        return 0;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public long getLatitude() {
        return latitude;
    }

    @Override
    public long getLongitude() {
        return longitude;
    }

    @Override
    public int getLastUpdatedOrder() {
        return lastUpdateCounter;
    }

    @Override
    public Iterator<String> getTags(){
        return tags.iterator();
    }

    @Override
    public TwoWayIterator<Students> getStudentsThere(){
        return studentsThere.twoWayiterator();
    }

    @Override
    public void updateCounterRating(int counter) {
        lastUpdateCounter = counter;
    }
    @Override
    public void setNumOfInsertion(int counter){
        numOfServiceInsertion = counter;
    }
    @Override
    public void addRating(int stars,String tag, int counter) {
        int oldAverage = averageStars;
        totalStars += stars;
        rating++;
        averageStars = (totalStars + rating / 2) / rating;
        int newAverage = averageStars;
        tags.addLast(tag);
        if(oldAverage != newAverage) lastUpdateCounter = counter;
    }
    @Override
    public int getAverageStars() {
        return averageStars;
    }
}

