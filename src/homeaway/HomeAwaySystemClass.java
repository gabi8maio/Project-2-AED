/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.DoublyLinkedList;
import dataStructures.Iterator;
import dataStructures.TwoWayIterator;
import homeaway.Exeptions.*;
import java.io.*;

public class HomeAwaySystemClass implements HomeAwaySystem{

    private static final long serialVersionUID = 0L;

    private AreaClass loadedArea;
    private DoublyLinkedList<AreaClass> savedAreas;

    public HomeAwaySystemClass(){
        savedAreas = new DoublyLinkedList<>();
    }

    @Override
    public void addTemporaryArea(String name, long topLatitude, long bottomLatitude, long leftLongitude, long rightLongitude)
            throws BoundsAlreadyExistException, InvalidBoundsException {
        if (hasArea(name)) {
            throw new BoundsAlreadyExistException();
        }
        if (topLatitude <= bottomLatitude || rightLongitude <= leftLongitude) {
            throw new InvalidBoundsException();
        }
        saveAreaIfLoaded();
        AreaClass area = new AreaClass(name, topLatitude, bottomLatitude, leftLongitude, rightLongitude);
        loadedArea = area;
    }

    @Override
    public String saveArea() throws SystemBoundsNotDefinedException{
        if(loadedArea == null)
            throw new SystemBoundsNotDefinedException();
        String tempAreaName = loadedArea.getName();
        store(tempAreaName, loadedArea);
        savedAreas.addFirst(loadedArea);
        return tempAreaName;
    }

    @Override
    public String loadArea (String name) throws BoundsDoesNotExistException{
        loadedArea = null;
        try{
            String fileName = getFileName (name);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            loadedArea = (AreaClass) ois.readObject();
            ois.close();
            return loadedArea.getName();
        }catch (IOException | ClassNotFoundException e){
            throw new BoundsDoesNotExistException();
        }
    }

    @Override
    public boolean hasArea(String name){
        if (loadedArea != null && loadedArea.getName().equalsIgnoreCase(name)) {
            return true;
        }
        return fileExistsCaseInsensitive(getFileName(name));
    }

    @Override
    public boolean hasBounds() {
        return loadedArea != null;
    }

    @Override
    public Iterator<Services> getServiceIterator() throws NoServicesYetException {
        Iterator<Services> servicesIterator = loadedArea.getServicesIterator();
        if(!servicesIterator.hasNext()) throw new NoServicesYetException();
        return loadedArea.getServicesIterator();
    }

    @Override
    public Iterator<Services> getServicesByTagIterator(String tag) throws NoServicesWithTagException{
        Iterator <Services> tagIterator = loadedArea.getServicesByTagIterator(tag);
        if (!tagIterator.hasNext()) {
            throw new NoServicesWithTagException();
        }
        return loadedArea.getServicesByTagIterator(tag);
    }

    @Override
    public Iterator<Services> getRankedServicesIterator(int stars,String type,String studentName)
            throws InvalidStarsException, StudentDoesNotExistsException, InvalidServiceTypeException, NoTypeServicesException, NoServicesWithAverage{
        if (stars > 5 || stars < 1)
            throw new InvalidStarsException();
        String name = studentExists(studentName);
        if (name == null)
            throw new StudentDoesNotExistsException(studentName);
        TypesOfService serviceTypeEnum = TypesOfService.fromString(type);
        if (serviceTypeEnum == null) {
            throw new InvalidServiceTypeException();
        }
        if (!hasServicesOfType(type))
            throw new NoTypeServicesException(type);
        if (!isTypeWithAverage(type, stars))
            throw new NoServicesWithAverage(type);
        return loadedArea.getRankedServicesIterator(stars,type,studentName);
    }

    @Override
    public Iterator<Services> getServicesByRankingIterator() throws NoServicesInSystemException{
        Iterator<Services> rankingIterator = loadedArea.getServicesByRankingIterator();
        if (!rankingIterator.hasNext()) {
            throw new NoServicesInSystemException();
        }
        return rankingIterator;
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
        return loadedArea.getVisitedLocationsIterator(studentName);
    }

    @Override
    public void addService(String serviceType, long latitude, long longitude, double price, int value, String serviceName)
            throws InvalidServiceTypeException, InvalidLocationException, InvalidPriceMenuException, InvalidRoomPriceException, InvalidTicketPriceException, InvalidDiscountException, InvalidCapacityException, ServiceAlreadyExistsException{
        TypesOfService serviceTypeEnum = TypesOfService.fromString(serviceType);
        if (serviceTypeEnum == null)
            throw new InvalidServiceTypeException();
        if (!loadedArea.isInBounds(latitude, longitude))
            throw new InvalidLocationException();
        if (price <= 0) {
            switch (serviceTypeEnum) {
                case EATING:
                    throw new InvalidPriceMenuException();
                case LODGING:
                    throw new InvalidRoomPriceException();
                case LEISURE:
                    throw new InvalidTicketPriceException();
            }
        }

        if (serviceTypeEnum.equals(TypesOfService.LEISURE)) {
            if (value < 0 || value > 100)
                throw new InvalidDiscountException();
        } else
        if (value <= 0)
            throw new InvalidCapacityException();
        String previousServiceName = serviceNameExists(serviceName);
        if (previousServiceName != null)
            throw new ServiceAlreadyExistsException(previousServiceName);
        loadedArea.createService( serviceType,  latitude,  longitude,  price,  value,  serviceName);
    }

    @Override
    public void addStudent (String studentType, String name, String country, String lodging) {
        if (StudentTypes.fromString(studentType) == null) {
            throw new InvalidStudentTypeException();
        }
        if (!lodgingExists(lodging)) {
            throw new LodgingNotExistsException(lodging);
        }
        String fullLodging = lodgingIsFull(lodging);
        if (fullLodging != null) {
            throw new LodgingIsFullException(fullLodging);
        }
        String studentExistsName = studentExists(name);
        if (studentExists(name) != null){
            throw new StudentAlreadyExistsException(studentExistsName);
        }
        loadedArea.addStudent(studentType, name, country, lodging);
    }

    @Override
    public Students getStudentLocationInfo(String studentName) throws StudentDoesNotExistsException {
        String studentExistsName = studentExists(studentName);
        if (studentExistsName == null){
            throw new StudentDoesNotExistsException(studentName);
        }
        return loadedArea.getStudentLocationInfo(studentName);
    }

    @Override
    public Students removeStudent(String studentName) throws StudentDoesNotExistsException{
        String studentExistsName = studentExists(studentName);
        if (studentExists(studentName) == null){
            throw new StudentDoesNotExistsException(studentName);
        }
        return loadedArea.removeStudent(studentExistsName);
    }

    @Override
    public Students goStudentToLocation(String studentName, String locationName) throws UnknownLocationException, StudentDoesNotExistsException, InvalidServiceException, StudentAlreadyThereException, EatingIsFullException{
        String serviceName = serviceNameExists(locationName);
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

        return loadedArea.goStudentToLocation(studentName,locationName);
    }

    @Override
    public Students moveStudentToLocation(String studentName, String locationName)
            throws LodgingNotExistsException, StudentDoesNotExistsException, StudentHomeException,LodgingIsFullException, MoveNotAcceptableException{

        String serviceName = serviceNameExists(locationName);
        if (serviceName == null)
            throw new LodgingNotExistsException(locationName);
        String studentNameReal = studentExists(studentName);
        if (studentExists(studentName) == null){
            throw new StudentDoesNotExistsException(studentName);
        }
        if (isStudentHome(studentName, serviceName))
            throw new StudentHomeException(studentNameReal);
        String fullLodging = lodgingIsFull(serviceName);
        if (fullLodging != null) {
            throw new LodgingIsFullException(fullLodging);
        }
        if (!isAcceptable(studentName, serviceName))
            throw new MoveNotAcceptableException(studentNameReal);

        return loadedArea.moveStudentToLocation(studentName,serviceName);
    }

    @Override
    public  Services findMostRelevantService(String studentName, String serviceType) throws InvalidServiceTypeException, StudentDoesNotExistsException, NoTypeServicesException{
        TypesOfService serviceTypeEnum = TypesOfService.fromString(serviceType);
        if (serviceTypeEnum == null) {
            throw new InvalidServiceTypeException();
        }
        String name = studentExists(studentName);
        if (name == null)
            throw new StudentDoesNotExistsException(studentName);
        if (!hasServicesOfType(serviceType))
            throw new NoTypeServicesException(serviceType);
        return loadedArea.findMostRelevantService(studentName, serviceType);
    }

    @Override
    public Iterator<Students> getStudentsByCountryIterator(String country) {
        return loadedArea.getStudentsByCountryIterator(country);
    }

    public Iterator<Students> getAllStudentsIterator(){
        return loadedArea.getAllStudentsIterator();
    }

    @Override
    public Iterator<Students> getStudentsIterator(String argument) throws NoStudentsException, NoStudentsFromCountryException {
        if (argument.equals("all")) {
            Iterator <Students> it = getAllStudentsIterator();
            if (!it.hasNext()) throw new NoStudentsException();
            else return it;
        } else {
            Iterator<Students> countryStudentIterator = getStudentsByCountryIterator(argument);
            if (!countryStudentIterator.hasNext()) {
                throw new NoStudentsFromCountryException();
            }else
                return countryStudentIterator;
        }
    }

    @Override
    public TwoWayIterator<Students> usersCommand(String order, String serviceName) throws NoStudentsOnServiceException, ServiceDoesNotExistException, ServiceNotControlEntryExitException{
        String service = serviceNameExists(serviceName);
        if (service == null)
            throw new ServiceDoesNotExistException(serviceName);
        if (!isThereAnyStudent(serviceName)) {
            throw new NoStudentsOnServiceException(service);
        }
        if (!isCorrectOrder(order)) {
            throw new OrderNotExistsException();
        }
        if (!isEatingOrLodgingService(serviceName))
            throw new ServiceNotControlEntryExitException(service);
        return loadedArea.getStudentsByService(service);
    }

    @Override
    public void starCommand(int rating,String serviceName,String tag) throws InvalidEvaluationException, ServiceDoesNotExistException{
        if(rating < 1 || rating > 5)
            throw new InvalidEvaluationException();
        if(serviceNameExists(serviceName) == null)
            throw  new ServiceDoesNotExistException(serviceName);
        loadedArea.starCommand(rating,serviceName,tag);
    }

    @Override
    public String serviceNameExists(String name) {
        return loadedArea.serviceExists(name);
    }

    @Override
    public String studentExists (String name){
        return loadedArea.studentExists(name);
    }

    @Override
    public boolean lodgingExists (String name){
        return loadedArea.lodgingExists(name);
    }

    @Override
    public String lodgingIsFull(String name){
        return loadedArea.isItFull(name);
    }

    @Override
    public boolean isServiceMoreExpensiveForThrifty(String studentName, String serviceName){
        return loadedArea.isServiceMoreExpensiveForThrifty(studentName, serviceName);
    }

    /**
     * Method to save the area if it's been loaded on the system
     */
    private void saveAreaIfLoaded(){
        if(loadedArea != null && !fileExistsCaseInsensitive(loadedArea.getName())){
            saveArea();
        }
    }

    /**
     * Method to store the area into a .ser file
     * @param fileName - The name that will be the file
     * @param area - The object that will be stored
     */
    private void store(String fileName, AreaClass area){
        try{
            String nameFile = this.getFileName (fileName);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nameFile));
            oos.writeObject(area);
            oos.flush();
            oos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to get the name with extensions and underscores (So the read can be correct)
     * @param fileName - The file name
     * @return - returns the name of the file with everything done
     */
    private String getFileName (String fileName){
        return fileName.toLowerCase().replace(" ", "_") + ".ser";
    }

    /**
     * @param name - Name of the file to see
     * @return - returns true if it finds the file
     */
    private boolean fileExistsCaseInsensitive(String name) {
        File directory = new File(".");

        if (!directory.exists() || !directory.isDirectory())
            return false;
        String[] files = directory.list();
        if (files == null)
            return false;
        String targetName = name.toLowerCase();
        for (String file : files)
            if (file.toLowerCase().equals(targetName))
                return true;
        return false;
    }

    /**
     * @param serviceName - The service Name
     * @return - returns true if is an Eating or Leisure service
     */
    private boolean isEatingOrLeisureService(String serviceName){
        return loadedArea.isEatingOrLeisureService(serviceName);
    }

    /**
     * @param studentName - The student's name
     * @param locationName - The location Service Name
     * @return - returns true if the student is at the certain location given
     */
    private boolean isStudentAtLocation(String studentName,String locationName){
        return loadedArea.isStudentAtLocation(studentName,locationName);
    }

    /**
     * @param locationName - The name of the service
     * @return - returns true if the Eating Service is full
     */
    private boolean isEatingServiceFull(String locationName){
        return loadedArea.isEatingServiceFull(locationName);
    }

    /**
     * @param name - Student's name
     * @return - returns true if the given student has visited any location
     */
    private boolean hasVisitedLocation(String name) {
        return loadedArea.hasVisitedLocation(name);
    }

    /**
     * @param studentName - The Student's name
     * @return - returns true if the Student is a type Thrifty
     */
    private boolean isThrifty(String studentName) {
        return loadedArea.isThrifty(studentName);
    }

    /**
     *
     * @param type - Type of the service
     * @param n - The certain average
     * @return - returns true if the certain type of service has a certain average
     */
    private boolean isTypeWithAverage (String type, int n){
        return loadedArea.isTypeWithAverage(type, n);
    }

    /**
     * @param type - The type to check
     * @return - returns true if there is a service with a certain type
     */
    private boolean hasServicesOfType(String type) {
        return loadedArea.hasServiceOfType(type);
    }

    /**
     *
     * @param studentName - Student name
     * @param locationName - Service lodging name
     * @return - returns true if the Student is thrifty and the lodging is cheaper than the one that is currently in
     */
    private boolean isAcceptable(String studentName, String locationName) {
        return loadedArea.isAcceptableMove(studentName, locationName);
    }

    /**
     * @param studentName - Student name
     * @param locationName - Service name
     * @return - returns true if the student given is at home
     */
    private boolean isStudentHome(String studentName, String locationName) {
        return loadedArea.isStudentHome(studentName, locationName);
    }

    /**
     *
     * @param order - the char of < or >
     * @return - returns true if the order is the correct char
     */
    private boolean isCorrectOrder(String order){
        return order.equals(">") || order.equals("<");
    }

    /**
     *
     * @param serviceName - The service name
     * @return - returns true if there are any student on the area
     */
    private boolean isThereAnyStudent(String serviceName) {
        return loadedArea.isThereAnyStudents(serviceName);
    }

    /**
     * @param serviceName - The service name
     * @return - returns true if it is an Eating or Lodging service, the one given
     */
    private boolean isEatingOrLodgingService(String serviceName) {
        return loadedArea.isEatingOrLodgingService(serviceName);
    }
}