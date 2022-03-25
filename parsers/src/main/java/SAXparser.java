import beans.Task;
import beans.User;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SAXparser {
    public static ArrayList<User> workers = new ArrayList<>();
    public static ArrayList<User> managers = new ArrayList<>();
    public static ArrayList<User> moderators = new ArrayList<>();
    public static ArrayList<Task> tasks = new ArrayList<>();

    public static void Init(){
        try {
            parse();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void parse() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler();
        parser.parse(new File("src/main/java/resource/users.xml"), handler);

        for (User worker : workers)
            System.out.printf("Имя сотрудника: %s%n", worker.getName());

        for (User manager : managers)
            System.out.printf("Имя менеджера: %s%n", manager.getName());

        for (User moderator : moderators)
            System.out.printf("Имя модератора: %s%n", moderator.getName());

        System.out.println();

        XMLHandler taskHandler = new XMLHandler();
        parser.parse(new File("src/main/java/resource/tasks.xml"), taskHandler);

        for (Task task : tasks)
            System.out.printf("Название таска: %s\n дата: %s\n заметка: %s\n создатель: %s\n", task.getName(), task.getDate(), task.getNote(), task.getCreator());

        System.out.println("SAX parser did that!\n");
    }

    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startDocument() throws SAXException {
            // Тут будет логика реакции на начало документа
        }

        @Override
        public void endDocument() throws SAXException {
            // Тут будет логика реакции на конец документа
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String name = attributes.getValue("name");
            String login = attributes.getValue("login");
            String password = attributes.getValue("password");

            switch (qName) {
                case "worker" -> workers.add(new User(name, login, password));
                case "manager" -> managers.add(new User(name, login, password));
                case "moderator" -> moderators.add(new User(name, login, password));
            }

            if (qName.equals("task")){
                String taskName = attributes.getValue("name");
                String date = attributes.getValue("date");
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                Date docDate = null;
                try {
                    docDate = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String note = attributes.getValue("note");
                if (note == null)
                    note = "there is no note left";
                String creator = attributes.getValue("creator");
                tasks.add(new Task(taskName, docDate, note, creator));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // Тут будет логика реакции на конец элемента
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            // Тут будет логика реакции на текст между элементами
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            // Тут будет логика реакции на пустое пространство внутри элементов (пробелы, переносы строчек и так далее).
        }
    }
}
