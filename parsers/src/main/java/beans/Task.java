package beans;

import java.util.Date;

public class Task {
    private String name, note, creator;
    private Date date;

    public Task(String name, Date date, String note, String creator){
        this.name = name;
        this.date = date;
        this.note = note;
        this.creator = creator;
    }

    public Task(){}

    public String getName(){return name;}

    public Date getDate(){return date;}

    public String getNote(){return note;}

    public String getCreator(){return creator;}
}
