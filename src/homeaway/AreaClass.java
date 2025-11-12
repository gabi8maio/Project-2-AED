/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;
import dataStructures.*;
import dataStructures.exceptions.InvalidPositionException;

import java.io.Serializable;



public class AreaClass implements Serializable, Area {

    private static final long serialVersionUID = 0L;

    private final long topLatitude;
    private final long bottomLatitude;
    private final long leftLongitude;
    private final long rightLongitude;
    private final String areaName;
    private final DoublyLinkedList<Services> services;
    private final SortedDoublyLinkedList<Services> servicesByRank;
    private final SortedList<Students> allStudents;
    private final DoublyLinkedList<Students> studentsByCountry;
    int updateCounter;
    int counterOfServicesInsertion;

    public AreaClass(String name, long topLatitude, long bottomLatitude, long leftLongitude, long rightLongitude){

        areaName = name;
        this.topLatitude = topLatitude;
        this.bottomLatitude = bottomLatitude;
        this.leftLongitude = leftLongitude;
        this.rightLongitude = rightLongitude;
        services = new DoublyLinkedList<>();
        studentsByCountry = new DoublyLinkedList<>();
        allStudents = new SortedDoublyLinkedList<>(new studentComparatorByName()); // Need to change
        servicesByRank = new SortedDoublyLinkedList<>(new ServiceComparatorByStars()); // Need to change
        updateCounter = 0;
        counterOfServicesInsertion =0;
    }

    public void createService(String serviceType, long latitude, long longitude, double price, int value, String serviceName) {

        Services newService = null;
        TypesOfService type = TypesOfService.fromString(serviceType);

        switch (type) {
            case LODGING:
                newService = new LodgingClass(latitude, longitude, price, value, serviceName);
                break;
            case EATING:
                newService = new EatingClass(latitude, longitude, price, value, serviceName);
                break;
            case LEISURE:
                newService = new LeisureClass(latitude, longitude, price, value, serviceName);
                break;
            case null:
                break;
        }
        assert newService != null;
        newService.updateCounterRating(updateCounter++);
        newService.setNumOfInsertion(counterOfServicesInsertion++);
        services.addLast(newService);
        servicesByRank.add(newService);
    }

    @Override
    public void addStudent(String studentType, String name, String country, String lodging) {
        Students newStudent = null;
        Services service = findServicesElem(lodging);
        StudentTypes type = StudentTypes.fromString(studentType);
        switch (type) {
            case OUTGOING -> newStudent = new OutgoingClass (studentType, name, country, service);
            case BOOKISH -> newStudent = new BookishClass(studentType, name, country, service);
            case THRIFTY -> newStudent = new ThriftyClass(studentType, name, country, service);
            case null -> {}
        }
        assert service != null;
        service.addStudentsThere(newStudent);
        service.addStudentsThereLodging();
        allStudents.add(newStudent);
        studentsByCountry.addLast(newStudent);
    }

    @Override
    public Students removeStudent(String studentName) {
        Students student = findStudentElem(studentName);
        if(student == null)
            throw new InvalidPositionException(); // Isto em principio n vai acontecer
        Services servicesNow = student.getPlaceNow();
        Services homeService = student.getPlaceHome();
        allStudents.remove(student);
        int index = studentsByCountry.indexOf(student);
        studentsByCountry.remove(index);
        servicesNow.removeStudentsThere(student);
        homeService.removeStudentsThere(student);
        homeService.removeStudentsThereLodging();
        return student;
    }

    @Override
    public Students moveStudentToLocation(String studentName, String serviceName){
        Students student = findStudentElem(studentName);
        Services service = findServicesElem(serviceName);
        assert service != null;
        assert student != null; // Deixamos?

        if (student instanceof Outgoing outgoing)
            outgoing.addVisitedService(service);

        Services oldService = student.getPlaceHome();
        oldService.removeStudentsThere(student);
        oldService.removeStudentsThereLodging();

        service.addStudentsThere(student);
        service.addStudentsThereLodging();

        student.setPlaceHome(service);
        student.setPlaceGo(service);
        return student;
    }

    @Override
    public Students goStudentToLocation(String studentName, String serviceName){
        Students student = findStudentElem(studentName);
        Services newService = findServicesElem(serviceName);

        assert student != null; // Deixamos??
        assert newService != null;

        if (student instanceof Bookish bookish && newService instanceof Leisure) bookish.addVisitedService(newService);
        else if (student instanceof Outgoing outgoing) outgoing.addVisitedService(newService);


        Services previousService = student.getPlaceNow();
        previousService.removeStudentsThere(student);    // Remove from previous Service
        newService.addStudentsThere(student);               // Add on new Service
        student.setPlaceGo(newService);
        return student;

    }
    @Override
    public void starCommand(int rating, String serviceName,String tag){

        Services service = findServicesElem(serviceName);
        assert service != null;

        servicesByRank.remove(service);
        service.addRating(rating, tag, updateCounter++);

        servicesByRank.add(service);
    }
    @Override
    public Iterator<Services> getServicesIterator() {
        return services.iterator();
    }

    public Iterator<Students> getAllStudentsIterator(){
        return allStudents.iterator();
    }
    @Override
    public Iterator<Students> getStudentsByCountryIterator(String country){
        ListInArray<Students> tempList = new ListInArray<>(studentsByCountry.size());
        Iterator<Students> iterator = studentsByCountry.iterator();

        while (iterator.hasNext()) {
            Students student = iterator.next();
            if (student.getCountry().equalsIgnoreCase(country)) tempList.addLast(student);
        }
        return tempList.iterator();
    }
    @Override
    public Iterator<Services> getServicesByTagIterator(String tag){
        Iterator<Services> it = services.iterator();
        DoublyLinkedList<Services> iteratorWithServices = new DoublyLinkedList<>();
        while (it.hasNext()) {
            Services s = it.next();
            Iterator<String> it2 = s.getTags();
            while (it2.hasNext()){
                String tagService = it2.next();
                if(tagService.toUpperCase().matches(".*\\b" + tag.toUpperCase() + "\\b.*")) {
                    iteratorWithServices.addLast(s);
                    break;
                }
            }
        }

        return iteratorWithServices.iterator();
    }
    @Override
    public Iterator<Services> getRankedServicesIterator (int stars,String type,String studentName){
        Students student = findStudentElem(studentName);
        assert student != null;
        Services studentLocation = student.getPlaceNow();
        long minDistance = Long.MAX_VALUE;
        ListInArray <Services> tempList = new ListInArray<>(20);
        Iterator<Services> iterator = servicesByRank.iterator();
        while (iterator.hasNext()) {
            Services service = iterator.next();
            if (service.getServiceType().equalsIgnoreCase(type) && service.getAverageStars() == stars)
                if ( calculateManhattanDistance(studentLocation, service) < minDistance) {
                    minDistance = calculateManhattanDistance(studentLocation, service); //atualiza a nova distância minima
                    tempList = new ListInArray<>(20);
                    tempList.addLast(service); //adiciona o serviço que é mais perto
                } else if (calculateManhattanDistance(studentLocation, service) == minDistance) {
                    tempList.addLast(service); //se a distância for = adiciona no final da lista temporária
                }
        }
        return tempList.iterator();
    }
    @Override
    public TwoWayIterator<Students> getStudentsByService(String serviceName){
        Services service = findServicesElem(serviceName);
        assert service != null;
        return service.getStudentsThere();

    }
    @Override
    public Iterator<Services> getServicesByRankingIterator(){
        return servicesByRank.iterator();
    }
    @Override
    public Iterator<Services> getVisitedLocationsIterator(String studentName){
        Students student = findStudentElem(studentName);
        if(student instanceof Outgoing outgoing) return outgoing.getAllVisitedServices();
        if(student instanceof Bookish bookish) return bookish.getAllVisitedServices();
        return null;
    }

    @Override
    public String serviceExists(String serviceName) {

        Iterator<Services> it = services.iterator();
        while (it.hasNext()) {
            Services s = it.next();
            if((s.getServiceName().equalsIgnoreCase(serviceName))) return s.getServiceName();
        }
        return null;
    }
    @Override
    public boolean lodgingExists(String serviceName) {
        Iterator<Services> it = services.iterator();
        while (it.hasNext()) {
            Services s = it.next();
            if (s.getServiceName().equalsIgnoreCase(serviceName) && s.getServiceType().equalsIgnoreCase(TypesOfService.LODGING.toString())) return true;
        }
        return false;
    }
    @Override
    public boolean isThereAnyStudents (String serviceName){
        Services service = findServicesElem(serviceName);
        return service != null && service.isThereAnyStudents();
    }
    @Override
    public boolean isStudentAtLocation(String studentName,String locationName){
        Iterator<Students> it = allStudents.iterator();
        while (it.hasNext()) {
            Students s = it.next();
            if((s.getName().equalsIgnoreCase(studentName))&&s.getPlaceNow().getServiceName().equalsIgnoreCase(locationName)) return true;
        }
        return false;
    }
    @Override
    public boolean isStudentHome(String studentName, String locationName) {
        Students student = findStudentElem(studentName);
        if (student == null) {
            return false;
        }
        return student.getPlaceHome().getServiceName().equals(locationName);
    }
    @Override
    public boolean isEatingServiceFull(String serviceName){
        Iterator<Services> it = services.iterator();
        while (it.hasNext()) {
            Services s = it.next();
            if((s.getServiceName().equalsIgnoreCase(serviceName)) && s.isFull() != null ) return true;
        }
        return false;
    }

    public String studentExists(String name) {
        Iterator<Students> it = allStudents.iterator();
        while (it.hasNext()) {
            Students s = it.next();
            if (s.getName().equalsIgnoreCase(name))
                return s.getName();
        }
        return null;
    }
    @Override
    public String isItFull(String name) {
        Iterator<Services> iterator = services.iterator();
        while(iterator.hasNext()) {
            Services service = iterator.next();
            if(service.getServiceName().equals(name))
                return service.isFull();
        }
        return null;
    }
    @Override
    public boolean isInBounds (long latitude, long longitude) {
        return latitude >= this.bottomLatitude && latitude <= this.topLatitude &&
                longitude >= this.leftLongitude && longitude <= this.rightLongitude;
    }
    @Override
    public boolean isEatingOrLeisureService(String serviceName) {
        Services service = findServicesElem(serviceName);
        return service instanceof Leisure || service instanceof Eating;
    }
    @Override
    public boolean isEatingOrLodgingService(String serviceName) {
        Services service = findServicesElem(serviceName);
        return service instanceof Eating || service instanceof Lodging;
    }
    @Override
    public boolean isServiceMoreExpensiveForThrifty(String studentName, String serviceName){
        Students student = findStudentElem(studentName);
        Services newService = findServicesElem(serviceName);
        if(student instanceof Thrifty thrifty){
            return thrifty.isMoreExpensiveThanCheapest(newService);
        }
        return false;
    }
    @Override
    public boolean isAcceptableMove(String studentName, String locationName) {

        Students student = findStudentElem(studentName);
        Services service = findServicesElem(locationName);

        if(!(student instanceof Thrifty))
            return true;
        return service != null && getPrice(student.getPlaceHome()) > getPrice(service);
    }
    @Override
    public boolean isThrifty(String studentName) {
        Students student = findStudentElem(studentName);
        return student instanceof Thrifty;
    }
    @Override
    public boolean hasVisitedLocation(String name) {
        Students student = findStudentElem(name);
        if (student instanceof Bookish bookish) {
            return bookish.hasVisitedLocations();
        }
        if (student instanceof Outgoing outgoing){
            return outgoing.hasVisitedLocation();
        }
        return false;
    }
    @Override
    public boolean hasServiceOfType(String type) {
        Iterator <Services> it = services.iterator();
        while (it.hasNext()) {
            Services service = it.next();
            if (service.getServiceType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isTypeWithAverage(String type, int n){
        Iterator<Services> iterator = servicesByRank.iterator();
        while (iterator.hasNext()) {
            Services service = iterator.next();
            if (service.getServiceType().equalsIgnoreCase(type) && service.getAverageStars() == n) return true;
        }
        return false;
    }
    @Override
    public String getName() {
        return areaName;
    }
    @Override
    public Students getStudentLocationInfo(String studentName){
        Iterator<Students> it = allStudents.iterator();
        while (it.hasNext()) {
            Students s = it.next();
            if((s.getName().equalsIgnoreCase(studentName))) return s;
        }
        return null;
    }

    @Override
    public Services findMostRelevantService(String studentName, String serviceType){
        Students student = findStudentElem(studentName);
        Services relevantService;
        assert student != null;

        if (student.getType().equalsIgnoreCase(StudentTypes.THRIFTY.toString()))
            relevantService = findCheapestService(serviceType);
        else
            relevantService = findBestRatedService(serviceType);
        return relevantService;
    }

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param name The name of the Student
     * @return - returns the Student Object by the given name of the Student
     */
    private Students findStudentElem(String name){
        Iterator<Students> it = allStudents.iterator();
        while (it.hasNext()) {
            Students s = it.next();
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }
    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param name - The name of the Service
     * @return - returns the Service Object by the given name of the Service
     */
    private Services findServicesElem(String name){
        Iterator<Services> it = services.iterator();
        while (it.hasNext()) {
            Services s = it.next();
            if (s.getServiceName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param serviceType - The service type
     * @return - returns the cheapest Service on the area
     */
    private Services findCheapestService(String serviceType) {
        Services cheapest = null;
        Iterator<Services> it = getServicesIterator();
        while (it.hasNext()) {
            Services service = it.next();
                if (service.getServiceType().equalsIgnoreCase(serviceType)) {
                    if (cheapest == null || getPrice(service) < getPrice(cheapest)) {
                        cheapest = service;
                    }
                }
        }
        return cheapest;
    }

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * It will check which is the best rated Service on the Area
     * @param serviceType - The Service Type to check
     * @return - returns the best Rated Service on the Area
     */
    private Services findBestRatedService(String serviceType) {
        Services bestRated = null;
        Iterator<Services> it = getServicesIterator();
        while (it.hasNext()) {
            Services service = it.next();
            if (service.getServiceType().equalsIgnoreCase(serviceType)) {
                if (bestRated == null || service.getAverageStars() > bestRated.getAverageStars()) {
                    bestRated = service;
                } else if (service.getAverageStars() == bestRated.getAverageStars()) {
                    // Em caso de empate: mais tempo com esta média (lastUpdatedOrder mais antigo)
                    if (service.getLastUpdatedOrder() < bestRated.getLastUpdatedOrder()) {
                        bestRated = service;
                    }
                }
            }
        }
        return bestRated;
    }

    /**
     * Calculates by ManhattanDistance
     * @param s1 - Object Service one
     * @param s2 - Object Service two
     * @return - The distance between two Services
     */
    private long calculateManhattanDistance(Services s1, Services s2) {
        return Math.abs(s1.getLatitude() - s2.getLatitude()) +
                Math.abs(s1.getLongitude() - s2.getLongitude());
    }

    /**
     * Gets the price of a certain Service
     * @param service - The Object Service
     * @return - returns teh price of a Service
     */
    private double getPrice (Services service) {
        double price = 0;
        if (service instanceof Leisure) {
            price = ((Leisure)service).getPrice();
        } else if (service instanceof Lodging) {
            price = ((Lodging)service).getPrice();
        } else if (service instanceof Eating) {
            price = ((Eating)service).getPrice();
        }
        return price;
    }
}
