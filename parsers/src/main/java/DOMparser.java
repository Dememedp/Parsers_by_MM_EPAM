import beans.Task;
import beans.User;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DOMparser {
    public static ArrayList<User> workers = new ArrayList<>();
    public static ArrayList<User> managers = new ArrayList<>();
    public static ArrayList<User> moderators = new ArrayList<>();
    public static ArrayList<Task> tasks = new ArrayList<>();

    public static void parse() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documentUser = builder.parse(new File("src/main/java/resource/users.xml"));
        Document documentTask = builder.parse(new File("src/main/java/resource/tasks.xml"));

        NodeList workerElements = documentUser.getDocumentElement().getElementsByTagName("worker");
        NodeList managerElements = documentUser.getDocumentElement().getElementsByTagName("manager");
        NodeList moderatorElements = documentUser.getDocumentElement().getElementsByTagName("moderator");
        NodeList taskElements = documentTask.getDocumentElement().getElementsByTagName("task");

        for (int i = 0; i< workerElements.getLength(); i++){
            Node worker = workerElements.item(i);
            NamedNodeMap attributes = worker.getAttributes();
            workers.add(new User(attributes.getNamedItem("name").getNodeValue(),
                    attributes.getNamedItem("login").getNodeValue(),
                    attributes.getNamedItem("password").getNodeValue()));
        }

        for (User user : workers)
            System.out.println(String.format("Информация о сотруднике: имя - %s", user.getName()));

        for (int i = 0; i< managerElements.getLength(); i++){
            Node manager = managerElements.item(i);
            NamedNodeMap attributes = manager.getAttributes();
            managers.add(new User(attributes.getNamedItem("name").getNodeValue(),
                    attributes.getNamedItem("login").getNodeValue(),
                    attributes.getNamedItem("password").getNodeValue()));
        }

        for (User user : managers)
            System.out.println(String.format("Информация о менеджере: имя - %s", user.getName()));

        for (int i = 0; i< moderatorElements.getLength(); i++){
            Node moderator = moderatorElements.item(i);
            NamedNodeMap attributes = moderator.getAttributes();
            moderators.add(new User(attributes.getNamedItem("name").getNodeValue(),
                    attributes.getNamedItem("login").getNodeValue(),
                    attributes.getNamedItem("password").getNodeValue()));
        }

        for (User user : moderators)
            System.out.println(String.format("Информация о модераторе: имя - %s", user.getName()));

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date docDate = null;

        for (int i = 0; i< taskElements.getLength(); i++){
            Node task = taskElements.item(i);
            NamedNodeMap attributes = task.getAttributes();
            try {
                docDate = format.parse(attributes.getNamedItem("date").getNodeValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String note;
            try {
                note = attributes.getNamedItem("note").getNodeValue();
            } catch (NullPointerException e){
                note = "There is no note left";
            }
            tasks.add(new Task(attributes.getNamedItem("name").getNodeValue(),
                    docDate,
                    note,
                    attributes.getNamedItem("creator").getNodeValue()));
        }

        for (Task task : tasks)
            System.out.printf("Информации о таске: название - %s, дата - %s, заметка - %s, автор - %s\n", task.getName(), task.getDate(), task.getNote(), task.getCreator());

        System.out.println("\nDOM parser did that!\n");
    }

    public static void Init(){
        try {
            parse();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
