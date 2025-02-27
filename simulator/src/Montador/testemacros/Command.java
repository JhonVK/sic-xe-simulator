package Montador.testemacros;
import java.util.LinkedList;

public class Command {
    private String command;
    private String label;
    private String comment;
    private LinkedList<Integer> arguments;
    public Command(String command, String label, String comment) {
        this.command = command;
        this.label = label;
        this.comment = comment;
        this.arguments = new LinkedList<>();
    }

    public String getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String getComment() {
        return comment;
    }

    public void setArgument(int arguments) {
        this.arguments.add(arguments);
    }

    public int getArgument(int index) {
        return this.arguments.get(index);
    }
}