/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;
import dataStructures.*;
import dataStructures.exceptions.InvalidPositionException;
import dataStructures.exceptions.NoSuchElementException;
import homeaway.Exeptions.*;

import java.io.Serializable;


public class AreaClass implements Serializable, Area {

    private static final long serialVersionUID = 0L;

    private final long topLatitude;
    private final long bottomLatitude;
    private final long leftLongitude;
    private final long rightLongitude;
    private final String areaName;


    private final Map<String, Services> services;
    private final TwoWayList<Services> servicesByInsertion;
    private final TwoWayList<Students>  studentsByInsertion;
    private final List<TwoWayList<Services>> servicesByRank;
    private final SortedMap<String, Students> allStudents;
    private final Map<String,TwoWayList<Students>> studentsByCountry;
    private final Map <String, SortedList<Services>> tags;
    private final int MAX_NUM_STARS = 5;


    int updateCounter;
    int counterOfServicesInsertion;

    public AreaClass(String name, long topLatitude, long bottomLatitude, long leftLongitude, long rightLongitude){

        areaName = name;
        this.topLatitude = topLatitude;
        this.bottomLatitude = bottomLatitude;
        this.leftLongitude = leftLongitude;
        this.rightLongitude = rightLongitude;
        servicesByInsertion = new DoublyLinkedList<>();
        studentsByInsertion = new DoublyLinkedList<>();
        services = new SepChainHashTable<>();
        studentsByCountry = new SepChainHashTable<>();
        allStudents = new RedBlackSortedMap<>();
        servicesByRank = new ListInArray<>(MAX_NUM_STARS);
        servicesByRank.add(0,new DoublyLinkedList<>());
        servicesByRank.add(1,new DoublyLinkedList<>());
        servicesByRank.add(2,new DoublyLinkedList<>());
        servicesByRank.add(3,new DoublyLinkedList<>());
        servicesByRank.add(4,new DoublyLinkedList<>());
        tags = new SepChainHashTable<>();
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


        servicesByInsertion.addLast(newService);
        services.put(serviceName.toUpperCase() ,newService); // Modificado

        TwoWayList<Services> list;

        list = servicesByRank.get(1);
        if(list == null){
            list = new DoublyLinkedList<>();
            list.addLast(newService);
        }else
            list.addLast(newService);
        if(!servicesByRank.isEmpty())
            servicesByRank.remove(1);
        servicesByRank.add(1,list); // 1 the second postion 5,4,3,2,1 stars


      /* TwoWayList<Services> list = new DoublyLinkedList<>();
        list.addLast(newService);
        servicesByRank.add(1,list); // 1 the second postion 5,4,3,2,1 stars
        // servicesByRank.add(serviceName,newService); // Modificado*/

    }

    @Override
    public void addStudent(String studentType, String name, String country, String lodging) {

        if (StudentTypes.fromString(studentType) == null) {//..
            throw new InvalidStudentTypeException();
        }
        if (!lodgingExists(lodging)) {
            throw new LodgingNotExistsException(lodging);
        }
        String fullLodging = isItFull(lodging);
        if (fullLodging != null) {
            throw new LodgingIsFullException(fullLodging);
        }
        String studentExistsName = studentExists(name);
        if (studentExists(name) != null){
            throw new StudentAlreadyExistsException(studentExistsName);
        }

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


        allStudents.put(name.toUpperCase(),newStudent);
        //Modificado --------------------
        TwoWayList<Students> studentsFromCountry = studentsByCountry.get(country.toUpperCase());
        if (studentsFromCountry == null) studentsFromCountry = new DoublyLinkedList<>();
        studentsFromCountry.addLast(newStudent);
        studentsByCountry.put(country.toUpperCase(), studentsFromCountry);
    }

    @Override
    public Students removeStudent(String studentName) {

        String studentExistsName = studentExists(studentName);
        if (studentExists(studentName) == null){
            throw new StudentDoesNotExistsException(studentName);
        }

        Students student = findStudentElem(studentExistsName);
        String country = student.getCountry();
        Services servicesNow = student.getPlaceNow();
        Services homeService = student.getPlaceHome();

        //-------------------------------------------
        for (int i = 0; i< studentsByInsertion.size(); i++){
            if(studentsByInsertion.get(i).getName().equalsIgnoreCase(studentName)) studentsByInsertion.remove(i);
        }
        allStudents.remove(studentName.toUpperCase()); // Modificado
        TwoWayList<Students> studentsList = studentsByCountry.get(country.toUpperCase());

        Iterator<Students> it = studentsList.iterator();

        int i = 0;
        while(it.hasNext()){                    // Modificado
            Students studentFromCountry = it.next();
            if(studentFromCountry.equals(student)){
                studentsList.remove(i);
                break;
            }
            i++;
        }
        studentsByCountry.put(country.toUpperCase(),studentsList); //Modificado


        servicesNow.removeStudentsThere(student);
        homeService.removeStudentsThere(student);
        homeService.removeStudentsThereLodging();

        //services.remove(servicesNow.getServiceName().toUpperCase()); // Services Updated
        //services.remove(homeService.getServiceName().toUpperCase()); // Services Updated

        services.put(servicesNow.getServiceName().toUpperCase(),servicesNow); // Services Updated
        services.put(homeService.getServiceName().toUpperCase(),homeService); // Services Updated

        // No services Insertion tmb
        for(int j = 0; j < servicesByInsertion.size(); j++){
            if(servicesByInsertion.get(j).getServiceName().equalsIgnoreCase(servicesNow.getServiceName())){
                servicesByInsertion.remove(j);
                servicesByInsertion.add(j,servicesNow);
            }
            if(servicesByInsertion.get(j).getServiceName().equalsIgnoreCase(homeService.getServiceName())){
                servicesByInsertion.remove(j);
                servicesByInsertion.add(j,homeService);
            }
        }
        //_____________________________________
        return student;
    }

    @Override
    public Students moveStudentToLocation(String studentName, String locationName){

        String serviceName = serviceExists(locationName);
        if (serviceName == null)
            throw new LodgingNotExistsException(locationName);
        String studentNameReal = studentExists(studentName);
        if (studentExists(studentName) == null){
            throw new StudentDoesNotExistsException(studentName);
        }
        if (isStudentHome(studentName, serviceName))
            throw new StudentHomeException(studentNameReal);
        String fullLodging = isItFull(serviceName);
        if (fullLodging != null) {
            throw new LodgingIsFullException(fullLodging);
        }
        if (!isAcceptableMove(studentName, serviceName))
            throw new MoveNotAcceptableException(studentNameReal);

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

        //Modificado------------
        services.remove(oldService.getServiceName().toUpperCase());
        services.remove(service.getServiceName().toUpperCase()); // Services Updated
        allStudents.remove(student.getName().toUpperCase()); // Services Updated

        services.put(oldService.getServiceName().toUpperCase(), oldService);
        allStudents.put(student.getName().toUpperCase(),student); // Services Updated
        services.put(service.getServiceName().toUpperCase(),service); // Services Updated

        // No services Insertion tmb
        for(int j = 0; j < servicesByInsertion.size(); j++){
            if(servicesByInsertion.get(j).getServiceName().equalsIgnoreCase(service.getServiceName())){
                servicesByInsertion.remove(j);
                servicesByInsertion.add(j,service);
            }
            if(servicesByInsertion.get(j).getServiceName().equalsIgnoreCase(oldService.getServiceName())){
                servicesByInsertion.remove(j);
                servicesByInsertion.add(j,oldService);
            }
        }

        for(int j = 0; j < studentsByInsertion.size(); j++){
            if(studentsByInsertion.get(j).getName().equalsIgnoreCase(student.getName())){
                studentsByInsertion.remove(j);
                studentsByInsertion.add(j,student);
            }
        }
        //_____________________________________

        return student;
    }

    @Override
    public Students goStudentToLocation(String studentName, String locationName){

        String serviceName = serviceExists(locationName);
        if (serviceName == null)
            throw new UnknownLocationException(locationName);
        String studentExistsName = studentExists(studentName);
        if (studentExistsName == null){
            throw new StudentDoesNotExistsException(studentName);
        }
        if (!isEatingOrLeisureService(locationName))
            throw new InvalidServiceException(locationName);
        if (isStudentAtLocation(studentName, locationName))
            throw new StudentAlreadyThereException();
        if (isEatingServiceFull(locationName))
            throw new EatingIsFullException();

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

        //Modificado------------
        services.remove(previousService.getServiceName().toUpperCase());
        services.remove(newService.getServiceName().toUpperCase()); // Services Updated
        allStudents.remove(student.getName().toUpperCase()); // Services Updated

        services.put(previousService.getServiceName().toUpperCase(), previousService);
        allStudents.put(student.getName().toUpperCase(),student); // Services Updated
        services.put(newService.getServiceName().toUpperCase(),newService); // Services Updated

        // No services Insertion tmb
        for(int j = 0; j < servicesByInsertion.size(); j++){
            if(servicesByInsertion.get(j).getServiceName().equalsIgnoreCase(previousService.getServiceName())){
                servicesByInsertion.remove(j);
                servicesByInsertion.add(j,previousService);
            }
            if(servicesByInsertion.get(j).getServiceName().equalsIgnoreCase(newService.getServiceName())){
                servicesByInsertion.remove(j);
                servicesByInsertion.add(j,newService);
            }
        }

        for(int j = 0; j < studentsByInsertion.size(); j++){
            if(studentsByInsertion.get(j).getName().equalsIgnoreCase(student.getName())){
                studentsByInsertion.remove(j);
                studentsByInsertion.add(j,student);
            }
        }
        //_____________________________________

        return student;

    }
    @Override
    public void starCommand(int rating, String serviceName, String tag) {

        //Throw
        if(rating < 1 || rating > 5)
            throw new InvalidEvaluationException();
        if(serviceExists(serviceName) == null)
            throw  new ServiceDoesNotExistException(serviceName);

        Services service = findServicesElem(serviceName);
        assert service != null;

        int ratingResult = service.addRating(rating, tag, updateCounter++);

        TwoWayList<Services> targetList = null;
        Services serviceToUpdate = service; // Default to the original service

        if (ratingResult != -1) {
            // Remove service from current position
            for (int i = 0; i < servicesByRank.size(); i++) {
                TwoWayList<Services> list = servicesByRank.get(i);
                if (list != null && list.indexOf(service) != -1) {
                    list.remove(list.indexOf(service));
                    break;
                }
            }
            // Add to correct position based on new rating
            int newIndex = 5 - service.getAverageStars();

            servicesByRank.get(newIndex).addLast(service);
            targetList = servicesByRank.get(newIndex);

            int newPosition = targetList.indexOf(service);
            if (newPosition != -1)
                serviceToUpdate = targetList.get(newPosition);
        }
        if (tag != null)
            updateTags(tag, serviceToUpdate);
    }

    private void updateTags(String tag, Services newService) {
        if (tag == null || tag.trim().isEmpty() || newService == null) return;


        String[] words = tag.split("[\\s,\\-_.;]+");

        for (String word : words) {
            String cleanWord = word.trim().toUpperCase();

                SortedList<Services> servicesList = tags.get(cleanWord);

                if (servicesList == null) {
                    servicesList = new SortedDoublyLinkedList<>(new ServiceComparatorByInsertion());
                    tags.put(cleanWord, servicesList);
                }

                // check if already exists on the list
                if (!serviceExistsInList(servicesList, newService))
                    servicesList.add(newService);
        }
    }

    private boolean serviceExistsInList(SortedList<Services> list, Services service) {
        Iterator<Services> it = list.iterator();
        while (it.hasNext()) {
            Services current = it.next();
            if (current.equals(service)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Iterator<Services> getServicesIterator() throws NoServicesYetException{
        if (servicesByInsertion.isEmpty())
            throw new NoServicesYetException();
        return servicesByInsertion.iterator();
    }

    public Iterator<Map.Entry<String,Students>> getAllStudentsIterator(){
        return allStudents.iterator();
    }
    @Override
    public Iterator<Students> getStudentsByCountryIterator(String country){

        if(studentsByCountry.isEmpty()) return null;
        if(studentsByCountry.get(country.toUpperCase()) == null) return null;
        TwoWayList<Students> list = studentsByCountry.get(country.toUpperCase());
        return list.iterator();
    }
    @Override
    public Iterator<Services> getServicesByTagIterator(String tag){

        if(tags == null) return null;
        if(tags.isEmpty()) return null;
        if(tags.get(tag.toUpperCase()) == null) return null;
        SortedList<Services> list = tags.get(tag.toUpperCase());

        return list.iterator();
    }
    @Override
    public Iterator<Services> getRankedServicesIterator (int stars,String type,String studentName){

        //Throws
        if (stars > 5 || stars < 1)
            throw new InvalidStarsException();
        String name = studentExists(studentName);
        if (name == null)
            throw new StudentDoesNotExistsException(studentName);
        TypesOfService serviceTypeEnum = TypesOfService.fromString(type);
        if (serviceTypeEnum == null) {
            throw new InvalidServiceTypeException();
        }
        if (!hasServiceOfType(type))
            throw new NoTypeServicesException(type);
        if (!isTypeWithAverage(type, stars))
            throw new NoServicesWithAverage(type);

        Students student = findStudentElem(studentName);
        assert student != null;
        Services studentLocation = student.getPlaceNow();
        long minDistance = Long.MAX_VALUE;
        ListInArray <Services> tempList = new ListInArray<>(20);
        Iterator<TwoWayList<Services>> iterator = servicesByRank.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            TwoWayList<Services> list = iterator.next();
            Iterator<Services> it = list.iterator();
            while(it.hasNext()){
                Services service = it.next();
            if (service.getServiceType().equalsIgnoreCase(type) && service.getAverageStars() == stars)
                if ( calculateManhattanDistance(studentLocation, service) < minDistance) {
                    minDistance = calculateManhattanDistance(studentLocation, service); //atualiza a nova distância minima
                    tempList = new ListInArray<>(20);
                    tempList.addLast(service); //adiciona o serviço que é mais perto
                } else if (calculateManhattanDistance(studentLocation, service) == minDistance) {
                    tempList.addLast(service); //se a distância for = adiciona no final da lista temporária
                }
            i++;
            }
        }
        return tempList.iterator();
    }
    @Override
    public TwoWayIterator<Students> getStudentsByService(String order, String serviceName){

        String service = serviceExists(serviceName);
        if (service == null)
            throw new ServiceDoesNotExistException(serviceName);
        if (!isThereAnyStudents(serviceName)) {
            throw new NoStudentsOnServiceException(service);
        }
        if (!isCorrectOrder(order)) {
            throw new OrderNotExistsException();
        }
        if (!isEatingOrLodgingService(serviceName))
            throw new ServiceNotControlEntryExitException(service);

        Services serviceObj = findServicesElem(serviceName);
        assert serviceObj != null;
        return serviceObj.getStudentsThere();

    }
    @Override
    public Iterator<TwoWayList<Services>> getServicesByRankingIterator(){
        return servicesByRank.iterator();
    }
    @Override
    public Iterator<Services> getVisitedLocationsIterator(String studentName){

        String name = studentExists(studentName);
        if (name == null)
            throw new StudentDoesNotExistsException(studentName);
        if(isThrifty(studentName))
            throw new StudentIsThriftyException(name);
        if (!hasVisitedLocation(name))
            throw new NoVisitedLocationsException(name);

        Students student = findStudentElem(studentName);
        if(student instanceof Outgoing outgoing) return outgoing.getAllVisitedServices();
        if(student instanceof Bookish bookish) return bookish.getAllVisitedServices();
        return null;
    }

    @Override
    public String serviceExists(String serviceName) {
        if(services.isEmpty()) return null;
        if(services.get(serviceName.toUpperCase()) == null) return null;
        return services.get(serviceName.toUpperCase()).getServiceName();
    }
    @Override
    public boolean lodgingExists(String serviceName) {

        Services service = services.get(serviceName.toUpperCase());
        return service != null && service.getServiceType().equalsIgnoreCase(TypesOfService.LODGING.toString());
    }
    @Override
    public boolean isThereAnyStudents (String serviceName){
        Services service = findServicesElem(serviceName);
        return service != null && service.isThereAnyStudents();
    }
    @Override
    public boolean isStudentAtLocation(String studentName,String locationName){
        Students student = allStudents.get(studentName.toUpperCase());
        String locationOfStudent = student.getPlaceNow().getServiceName();
        return locationOfStudent.equalsIgnoreCase(locationName);
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

        Services service = services.get(serviceName.toUpperCase());
        return service.getServiceType().equalsIgnoreCase(TypesOfService.EATING.toString())
                && service.isFull() != null;
    }

    public String studentExists(String name) {
        if(allStudents.isEmpty()) return null;
        if(allStudents.get(name.toUpperCase()) == null) return null;
        return allStudents.get(name.toUpperCase()).getName();
    }
    @Override
    public String isItFull(String name) {
        return services.get(name.toUpperCase()).isFull();
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

        Iterator <Map.Entry<String,Services>> it = services.iterator();
        while (it.hasNext()) {
            Services services1 = it.next().value();
            if (services1.getServiceType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isTypeWithAverage(String type, int n){
        Iterator<TwoWayList<Services>> iterator = servicesByRank.iterator();
        int i = 0;
        while (iterator.hasNext()) { // Modificado
            TwoWayList<Services> list = iterator.next();
            Iterator<Services> it = list.iterator();
            while(it.hasNext()){
                Services service = it.next();
            if (service.getServiceType().equalsIgnoreCase(type) && service.getAverageStars() == n) return true;
            i++;
            }
        }
        return false;
    }
    @Override
    public String getName() {
        return areaName;
    }
    @Override
    public Students getStudentLocationInfo(String studentName){

        String studentExistsName = studentExists(studentName);
        if (studentExistsName == null){
            throw new StudentDoesNotExistsException(studentName);
        }
       return allStudents.get(studentName.toUpperCase()); // Ignore case needs to be implemented
    }

    @Override
    public Services findMostRelevantService(String studentName, String serviceType){

        //Throw
        TypesOfService serviceTypeEnum = TypesOfService.fromString(serviceType);
        if (serviceTypeEnum == null)
            throw new InvalidServiceTypeException();
        String name = studentExists(studentName);
        if (name == null)
            throw new StudentDoesNotExistsException(studentName);
        if (!hasServiceOfType(serviceType))
            throw new NoTypeServicesException(serviceType);

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
        return allStudents.get(name.toUpperCase());
    }
    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param name - The name of the Service
     * @return - returns the Service Object by the given name of the Service
     */
    private Services findServicesElem(String name){
        if(services.isEmpty()) return null;
        if(services.get(name.toUpperCase()) == null) return null;

       return services.get(name.toUpperCase());
    }

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param serviceType - The service type
     * @return - returns the cheapest Service on the area
     */
    private Services findCheapestService(String serviceType) {
        Services cheapest = null;
        Iterator<Services> it = servicesByInsertion.iterator();

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
        Iterator<Services> it = servicesByInsertion.iterator(); // Nodificdo

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

    private boolean isCorrectOrder(String order){
        return order.equals(">") || order.equals("<");
    }
}
