import beans.Task;
import beans.User;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StAXparser {
    public static void Init() {
        HashMap<String, ArrayList> users = new HashMap<>(parseXMLfile("src/mai/java/resource/users.xml"));
        ArrayList<User> workers = users.get("workers");
        ArrayList<User> managers = users.get("managers");
        ArrayList<User> moderators = users.get("moderators");

        for (User worker : workers)
            System.out.printf("Имя сотрудника: %s\n", worker.getName());

        for (User manager : managers)
            System.out.printf("Имя менеджера: %s\n", manager.getName());

        for (User moderator : moderators)
            System.out.printf("Имя модератора: %s\n", moderator.getName());

        System.out.println();

        ArrayList<Task> tasks = new ArrayList<>(parseXMLfile("src/main/java/resource/tasks.xml").get("tasks"));

        for (Task task : tasks)
            System.out.printf("Название таска: %s\n дата: %s\n заметка: %s\n создатель: %s\n", task.getName(), task.getDate(), task.getNote(), task.getCreator());

        System.out.println("\nStAX parser did that!\n");
    }

    private static HashMap<String, ArrayList> parseXMLfile(String fileName){
        ArrayList<User> workersList = new ArrayList<>();
        ArrayList<User> managersList = new ArrayList<>();
        ArrayList<User> moderatorsList = new ArrayList<>();
        ArrayList<Task> taskList = new ArrayList<>();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while (reader.hasNext()) {
                XMLEvent xmlEvent = reader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    String typeOfUser = startElement.getName().getLocalPart();
                    if (typeOfUser.equals("worker") || typeOfUser.equals("manager") || typeOfUser.equals("moderator")) {
                        String name = startElement.getAttributeByName(new QName("name")).getValue();
                        String login = startElement.getAttributeByName(new QName("login")).getValue();
                        String password = startElement.getAttributeByName(new QName("password")).getValue();
                        User user = new User(name, login, password);
                        switch (startElement.getName().getLocalPart()) {
                            case "worker" -> workersList.add(user);
                            case "manager" -> managersList.add(user);
                            case "moderator" -> moderatorsList.add(user);
                        }
                    } else if (typeOfUser.equals("task")){
                        String name = startElement.getAttributeByName(new QName("name")).getValue();
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        Date docDate = null;
                        String date = startElement.getAttributeByName(new QName("date")).getValue();
                        try{
                            docDate = format.parse(date);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                        String note;
                        try {
                            note = startElement.getAttributeByName(new QName("note")).getValue();
                        } catch (NullPointerException e){
                            note = "There is no note left";
                        }
                        String creator = startElement.getAttributeByName(new QName("creator")).getValue();
                        Task task = new Task(name, docDate, note, creator);
                        taskList.add(task);
                    }
                }
            }
        }
            catch (FileNotFoundException | XMLStreamException exc) {
            exc.printStackTrace();
        }

        HashMap<String, ArrayList> elements = new HashMap<>();
        elements.put("workers", workersList);
        elements.put("managers", managersList);
        elements.put("moderators", moderatorsList);
        elements.put("tasks", taskList);
        return elements;
    }
}
