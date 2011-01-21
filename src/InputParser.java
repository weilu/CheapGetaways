import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: wei
 * Date: Jan 19, 2011
 * Time: 7:25:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class InputParser {

    private int adults;
    private int departDayOfWeek;
    private int returnDayOfWeek;
    private Date start;
    private Date end;

    public InputParser(int adults, int departDayOfWeek, int returnDayOfWeek, Date start, Date end) {
        this.adults = adults;
        this.departDayOfWeek = departDayOfWeek;
        this.returnDayOfWeek = returnDayOfWeek;
        this.start = start;
        this.end = end;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getDepartDayOfWeek() {
        return departDayOfWeek;
    }

    public void setDepartDayOfWeek(int departDayOfWeek) {
        this.departDayOfWeek = departDayOfWeek;
    }

    public int getReturnDayOfWeek() {
        return returnDayOfWeek;
    }

    public void setReturnDayOfWeek(int returnDayOfWeek) {
        this.returnDayOfWeek = returnDayOfWeek;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public ArrayList<Date> getAvailableDates(int dayOfWeek, Date start, Date end){
        ArrayList<Date> availableDates = new ArrayList<Date>();

        Calendar cal = Calendar.getInstance();
        cal.set(start.getYear() + 1900, start.getMonth(), start.getDate());

        while(cal.getTime().before(end)){
            if(cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek)
                cal.add(Calendar.DAY_OF_MONTH, 1);
            else{
                availableDates.add(cal.getTime());
                cal.add(Calendar.DAY_OF_MONTH, 7);
            }
        }

        return availableDates;
    }
}
