import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
* Created by IntelliJ IDEA.
* User: wei
* Date: Jan 19, 2011
* Time: 7:47:20 AM
* To change this template use File | Settings | File Templates.
*/
public class InputParserTest {

    @Test
    public void testGetAvailableDates() throws Exception {  
        Calendar cal = Calendar.getInstance();
        cal.set(2011, Calendar.JANUARY, 19);
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 35);
        Date end = cal.getTime();

        InputParser p = new InputParser(2, Calendar.FRIDAY, Calendar.MONDAY, start, end);
        ArrayList<Date> availableDepart
                = p.getAvailableDates(p.getDepartDayOfWeek(), p.getStart(), p.getEnd());
        assert availableDepart.size() > 0;
        for(Date d : availableDepart){
            System.out.println(d.toString());
        }
        ArrayList<Date> availableReturn
                = p.getAvailableDates(p.getReturnDayOfWeek(), p.getStart(), p.getEnd());
        assert availableReturn.size() > 0;
        for(Date d : availableReturn){
            System.out.println(d.toString());
        }
    }
}
