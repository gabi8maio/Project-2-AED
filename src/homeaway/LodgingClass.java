/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;


public class LodgingClass extends ServicesClassAbstract implements Lodging {
    private static final long serialVersionUID = 0L;

    private final double price;

    public LodgingClass(long latitude, long longitude, double price, int value, String serviceName) {
        super(latitude,longitude,value, serviceName);
        this.price = price;
    }

    @Override
    public String getServiceType() {
        return TypesOfService.LODGING.toString();
    }
    @Override
    public double getPrice() {
        return price;
    }

}
