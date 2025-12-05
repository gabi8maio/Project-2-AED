/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

public class EatingClass extends ServicesClassAbstract implements Eating {

    private static final long serialVersionUID = 0L;

    private final double price;

    /**
     * The class of eating service
     * @param latitude its latitude
     * @param longitude its longitude
     * @param price the price of the menu
     * @param value the value (discount)
     * @param serviceName the name of the service
     */
    public EatingClass(long latitude, long longitude, double price, int value, String serviceName){
        super(latitude,longitude,value, serviceName);
        this.price = price;

    }

    @Override
    public String getServiceType() {
        return TypesOfService.EATING.toString();
    }

    @Override
    public double getPrice() {
        return price;
    }

}
