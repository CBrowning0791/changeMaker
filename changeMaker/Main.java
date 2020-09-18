import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Main {

    public static void main(String[] args) {
	    changeMaker cm;
	    cm = new changeMaker();
	    cm.run();
    }
}

class changeMaker{
    BigDecimal globalDecimal; //Using to keep track of what is left to make into change.

    public void run(){
        BigDecimal inputDecimal = null;
        while(inputDecimal == null){
            enterValue();
            makeChange();
        }
    }

    private void enterValue(){
        BufferedReader lineReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        BigDecimal inputDecimal = null;
        System.out.println("Enter in value of USD to convert or enter 'EXIT' to exit:");
        try{
            input = lineReader.readLine();
            if(input.toLowerCase().equals("exit")){
                System.exit(0);
            }else{
                inputDecimal = new BigDecimal(input);
                globalDecimal = inputDecimal;
            }
        }catch (IOException e){
            System.out.println("Problem parsing entry string: " + e.getLocalizedMessage());
        }catch (NumberFormatException e){
            System.out.println("Problem with parsing entry string: Was not numeric:" + e.getLocalizedMessage());
        }
    }

    private void makeChange(){
        Integer dollars = 0;
        Integer quarters = 0;
        Integer dimes = 0;
        Integer nickels = 0;
        Integer pennies = 0;

        dollars = makeDollars();
        quarters = makeQuarters();
        dimes = makeDimes();
        nickels = makeNickels();
        globalDecimal = globalDecimal.stripTrailingZeros();
        pennies = globalDecimal.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        System.out.println("Your Change is: \n Dollar Coins: "+dollars+"\n Quarters: "+quarters+"\n Dimes: "+dimes+"\n Nickels: "+nickels+"\n Pennies: "+pennies);

    }

    private Integer makeDollars(){
        String inputString = null;
        Integer decimalPoint = null; //Set to null instead of 0 just in case. 0 is an index, null is not.
        inputString = globalDecimal.toPlainString();
        decimalPoint = inputString.indexOf(".");
        globalDecimal = globalDecimal.subtract(new BigDecimal(inputString.substring(0,decimalPoint)));
        return Integer.parseInt(inputString.substring(0,decimalPoint));
    }

    private Integer makeQuarters(){
        BigDecimal valueOfChange = globalDecimal.remainder(BigDecimal.ONE);
        BigDecimal numQuarters = valueOfChange.divide(new BigDecimal(".25"));
        globalDecimal = globalDecimal.subtract(new BigDecimal(numQuarters.intValue()*.25));
        return numQuarters.intValue();
    }

    private Integer makeDimes(){
        BigDecimal valueOfChange = globalDecimal.remainder(BigDecimal.ONE);
        BigDecimal numDimes = valueOfChange.divide(new BigDecimal(".10"));
        globalDecimal = globalDecimal.subtract(new BigDecimal(numDimes.intValue()*.10)).round(new MathContext(2, RoundingMode.CEILING)); //Odd subtraction happening here
        return numDimes.intValue();
    }

    private Integer makeNickels(){
        BigDecimal valueOfChange = globalDecimal.remainder(BigDecimal.ONE);
        BigDecimal numNickel = valueOfChange.divide(new BigDecimal(".05"));
        globalDecimal = globalDecimal.subtract(new BigDecimal(numNickel.intValue()*.05)).round(new MathContext(2, RoundingMode.CEILING));
        return numNickel.intValue();
    }
}
