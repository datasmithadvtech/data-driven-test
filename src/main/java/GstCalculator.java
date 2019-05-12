import java.util.HashMap;

public class GstCalculator {
    HashMap<String, Double> gstLookUp;

    public GstCalculator(){
        gstLookUp = new HashMap<String, Double>();
        gstLookUp.put("0101", 5.0);
        gstLookUp.put("0102", 6.0);
        gstLookUp.put("0105", 18.0);
    }

    public Double calculate(String hsnCode, Double price){
        try{
            Double tax = 0.0;
            Double percentage = gstLookUp.get(hsnCode);
            tax = price * (percentage/100);
            return tax;
        }
        catch(NullPointerException npe){
            throw new HsnCodeNotFoundException(hsnCode);
        }
    }

    public static class HsnCodeNotFoundException extends RuntimeException{
        public HsnCodeNotFoundException(String hsnCode){
            super("HSN Code "+hsnCode+" is not a valid HSN Code");
        }
    }
}
