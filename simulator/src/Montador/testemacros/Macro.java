package Montador.testemacros;
import java.util.LinkedList;

public class Macro {
    private String macroName;
    private String label;
    private String comment;
    private LinkedList<String> arguments;
    private LinkedList<Command> commands;

    public Macro(String macroName, String label, String comment) {
        this.macroName = macroName;
        this.label = label;
        this.comment = comment;
        this.arguments = new LinkedList<>();
        this.commands = new LinkedList<>();
    }

    public String getMacroName() {
        return macroName;
    }

    public Command getCommand(int index){
        return this.commands.get(index);
    }

    public String getCommandName(int qual){
        return this.commands.get(qual).getCommand();
    }

    public String getCommentName(int qual){
        return this.commands.get(qual).getComment();
    }

    public String getLabelName(int qual){
        return this.commands.get(qual).getLabel();
    }

    public int getCommandArg(int qual, int index){
        return this.commands.get(qual).getArgument(index);
    }

    public int commandSize(){
        return this.commands.size();
    }

    public LinkedList<String> getArguments() {
        return arguments;
    }

    public void setArguments(LinkedList<String> arguments) {
        this.arguments=arguments;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }
}